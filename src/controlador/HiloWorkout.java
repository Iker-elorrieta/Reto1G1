package controlador;

import java.util.Date;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import modelo.Ejercicio;
import modelo.Serie;
import modelo.Workout;
import modelo.UsuWorkout;
import modelo.Usuario;
import vista.PantallaEjercicio;
import vista.Workouts;

public class HiloWorkout extends Thread {

	public enum Estado {
		PARADO, PREPARACION, SERIE, DESCANSO, ESPERANDO_SIGUIENTE, FIN
	}

	// Vista a actualizar y workout en curso
	private final PantallaEjercicio vista;
	private final Workout entrenamiento;
	private final Usuario usuario;
	private final Workouts vistaWorkouts;

	// Estados de ejecución
	private volatile boolean enEjecucion = false; // indica si hay cronos activos
	private volatile boolean enPausa = false; // indica si están pausados
	private volatile boolean detenido = false; // indica fin/cancelación
	private final Object bloqueoPausa = new Object(); // monitor para pausar/reanudar

	// Índices de progreso
	private int idxEjercicio = 0;
	private int idxSerie = 0;
	private Estado estado = Estado.PARADO;

	// Marcas de tiempo (en ms) para cronos ascendentes
	private long inicioWorkoutMs; // inicio del workout
	private long inicioEjercicioMs; // inicio del ejercicio actual
	private long pausaAcumWorkoutMs = 0L; // ms acumulados en pausa (workout)
	private long pausaAcumEjercicioMs = 0L; // ms acumulados en pausa (ejercicio)
	private long inicioPausaMs = 0L; // marca de inicio de una pausa actual

	private HiloCronometros hiloCronometros;
	private final ControladorInicio controlador;


	public HiloWorkout(PantallaEjercicio vista, Workout workout, Usuario usuario, Workouts vistaWorkouts,
			ControladorInicio controlador) {
		this.vista = vista;
		this.entrenamiento = workout;
		this.usuario = usuario;
		this.vistaWorkouts = vistaWorkouts;
		this.controlador = controlador;
	}

	/**
	 * Arranca los hilos de cronometraje y de fases. - Resetea acumulados de pausa y
	 * marca tiempos de inicio. - Lanza HiloCronometros para workout/ejercicio. -
	 * Lanza este hilo para gestionar fases.
	 */
	@Override
	public synchronized void start() {
		if (enEjecucion)
			return; // ya en marcha
		detenido = false;
		enEjecucion = true;
		enPausa = false;
		pausaAcumWorkoutMs = 0L;
		pausaAcumEjercicioMs = 0L;
		inicioWorkoutMs = System.currentTimeMillis();
		inicioEjercicioMs = inicioWorkoutMs;

		hiloCronometros = new HiloCronometros(this, vista);
		hiloCronometros.start();

		if (vista.getLblCronometroPreparacion() != null)
			vista.getLblCronometroPreparacion().setVisible(false);
		if (vista.getLblCronometroDescanso() != null)
			vista.getLblCronometroDescanso().setVisible(false);
		super.start();
	}

	/** Cuerpo del hilo principal: ejecuta el bucle de fases. */
	@Override
	public void run() {
		while (!detenido && enEjecucion) {
			// Deshabilitar "Siguiente" durante el ciclo
			habilitarSiguiente(false);

			Ejercicio ej = entrenamiento.getEjercicios().get(idxEjercicio);
			Serie serie = ej.getSeries().get(idxSerie);

			// Preparación (5s)
			estado = Estado.PREPARACION;
			// Mostrar label de preparación sólo mientras se usa
			actualizarVisibilidad(vista.getLblCronometroPreparacion(), true);
			hiloCronometros.cuentaAtrasSegundos(5, (seg) -> actualizarLabel(vista.getLblCronometroPreparacion(),
					"Empieza en..." + formatearPreparacion(seg * 1000L)));
			// Ocultar tras terminar
			actualizarVisibilidad(vista.getLblCronometroPreparacion(), false);
			if (estaDetenido())
				return;

			// Serie
			estado = Estado.SERIE;
			// Mostrar label de la serie si existe
			if (serie.getCronometro() != null)
				actualizarVisibilidad(serie.getCronometro(), true);
			hiloCronometros.cuentaAtrasSegundos(serie.getTiempo(), (seg) -> {
				if (serie.getCronometro() != null)
					actualizarLabel(serie.getCronometro(), formatear(seg * 1000L));
			});
			if (estaDetenido())
				return;

			// Descanso
			estado = Estado.DESCANSO;
			// Mostrar label de descanso sólo mientras se usa
			actualizarVisibilidad(vista.getLblCronometroDescanso(), true);
			hiloCronometros.cuentaAtrasSegundos(ej.getTiempoDescanso(),
					(seg) -> actualizarLabel(vista.getLblCronometroDescanso(), "Descanso: " + formatear(seg * 1000L)));
			// Ocultar tras terminar
			actualizarVisibilidad(vista.getLblCronometroDescanso(), false);
			if (estaDetenido())
				return;

			// Si hemos terminado el último descanso de la última serie del último ejercicio,
			// finalizar automáticamente sin esperar al botón "Siguiente".
			boolean esUltimaSerie = idxSerie == ej.getSeries().size() - 1;
			boolean esUltimoEjercicio = idxEjercicio == entrenamiento.getEjercicios().size() - 1;
			if (esUltimoEjercicio && esUltimaSerie) {
				// Marcar como completado el último ejercicio (idx pasa a tamaño total)
				int totalEj = entrenamiento.getEjercicios().size();
				if (idxEjercicio < totalEj) {
					idxEjercicio = Math.min(idxEjercicio + 1, totalEj);
				}
				estado = Estado.FIN;
				enEjecucion = false;
				terminarWorkout();
				return;
			}

			// Ciclo finalizado -> esperar "Siguiente"
			estado = Estado.ESPERANDO_SIGUIENTE;
			habilitarSiguiente(true);
			while (!detenido && enEjecucion && estado == Estado.ESPERANDO_SIGUIENTE) {
				if (enPausa) {
					esperarDespausaBloqueante();
					continue;
				}
				dormir(100);
			}
		}

	}

	/** Indica si el gestor está corriendo (cronos activos). */
	public boolean isRunning() {
		return enEjecucion;
	}

	/** Indica si el gestor está en pausa. */
	public boolean isPaused() {
		return enPausa;
	}

	/** Indica si está detenido. */
	public boolean isDetenido() {
		return detenido;
	}

	/** Devuelve ms transcurridos del workout (descontando pausas). */
	public long workoutMs() {
		long ahora = System.currentTimeMillis();
		return Math.max(0, ahora - inicioWorkoutMs - pausaAcumWorkoutMs);
	}

	/** Devuelve ms transcurridos del ejercicio actual (descontando pausas). */
	public long ejercicioMs() {
		long ahora = System.currentTimeMillis();
		return Math.max(0, ahora - inicioEjercicioMs - pausaAcumEjercicioMs);
	}

	/** Espera bloqueante durante la pausa. */
	public void esperarDespausaBloqueante() {
		synchronized (bloqueoPausa) {
			while (enPausa && !detenido) {
				try {
					bloqueoPausa.wait();
				} catch (InterruptedException ignored) {
				}
			}
		}
	}

	/** Pausa todos los cronos. */
	public void pause() {
		if (!enEjecucion || enPausa)
			return;
		enPausa = true;
		inicioPausaMs = System.currentTimeMillis();
	}

	/** Reanuda todos los cronos y corrige acumulados. */
	public void reanudar() {
		if (!enEjecucion || !enPausa)
			return;
		long delta = System.currentTimeMillis() - inicioPausaMs;
		enPausa = false;
		pausaAcumWorkoutMs += delta;
		pausaAcumEjercicioMs += delta;
		synchronized (bloqueoPausa) {
			bloqueoPausa.notifyAll();
		}
	}

	/** Detiene completamente la ejecución y despierta hilos bloqueados. */
	public void stopCronos() {
		detenido = true;
		enEjecucion = false;
		enPausa = false;
		synchronized (bloqueoPausa) {
			bloqueoPausa.notifyAll();
		}
	}

	/** Avanza manualmente a la siguiente serie/ejercicio si procede. */
	public void siguiente() {
		if (!enEjecucion)
			return;
		if (estado != Estado.ESPERANDO_SIGUIENTE)
			return;
		iniciarSiguiente();
	}

	/** Calcula y arranca la siguiente serie/ejercicio o finaliza. */
	private void iniciarSiguiente() {
		if (detenido || !enEjecucion)
			return;
		habilitarSiguiente(false);

		Ejercicio ej = entrenamiento.getEjercicios().get(idxEjercicio);
		idxSerie++;
		if (idxSerie >= ej.getSeries().size()) {
			idxEjercicio++;
			idxSerie = 0;
			if (idxEjercicio >= entrenamiento.getEjercicios().size()) {
				estado = Estado.FIN;
				enEjecucion = false;
				// Gestionar finalización internamente
				this.terminarWorkout();
				return;
			}
			cambiarEjercicio();
		}
		estado = Estado.PREPARACION;
	}

	/** Resetea crono de ejercicio y actualiza UI con el nuevo ejercicio. */
	private void cambiarEjercicio() {
		long ahora = System.currentTimeMillis();
		inicioEjercicioMs = ahora;
		pausaAcumEjercicioMs = 0L;

		Ejercicio ej = entrenamiento.getEjercicios().get(idxEjercicio);
		vista.getLblNombreEjercicio().setText(ej.getNombre());
		vista.getLblEjercicioDescripcion().setText(ej.getDescripcion());
		vista.getLblCronometroEjercicio().setText("00:00");
		vista.getLblCronometroPreparacion().setText("");
		vista.getLblCronometroDescanso().setText("");
		try {
			vista.mostrarPanelSeries(idxEjercicio);
		} catch (Exception ignore) {
		}

	}

	/** Duerme el hilo actual en ms. */
	private void dormir(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException ignored) {
		}
	}

	/** True si se detuvo o no está en ejecución. */
	private boolean estaDetenido() {
		return detenido || !enEjecucion;
	}

	/** Actualiza el texto de un JLabel. */
	private void actualizarLabel(JComponent comp, String texto) {
		if (comp instanceof JLabel lbl)
			lbl.setText(texto);
	}

	/** Cambia la visibilidad. */
	private void actualizarVisibilidad(JComponent comp, boolean visible) {
		comp.setVisible(visible);
	}

	/** Habilita o deshabilita el botón "Siguiente". */
	private void habilitarSiguiente(boolean enable) {
		vista.getBtnSiguiente().setEnabled(enable);
		vista.getBtnSiguiente().setVisible(true);
	}

	/** Formatea ms a mm:ss. */
	private String formatear(long millis) {
		long total = Math.max(0, millis) / 1000;
		long mm = total / 60;
		long ss = total % 60;
		return String.format("%02d:%02d", mm, ss);
	}

	private String formatearPreparacion(long millis) {
		long total = Math.max(0, millis) / 1000;
		long ss = total % 60;
		return String.format("%2d", ss);
	}

	/** Devuelve el número de ejercicios completados (índice actual). */
	public int getEjerciciosCompletados() {
		return Math.max(0, idxEjercicio);
	}

	/**
	 * Termina el workout: para cronos, guarda el historial, evalúa nivel y muestra
	 * diálogos. Ejecuta persistencia y evaluación en segundo plano y actualiza la
	 * UI en el EDT.
	 */
	private void terminarWorkout() {
		// Parar cronos y marcar detenido
		stopCronos();

		long totalMs = workoutMs();
		int completados = getEjerciciosCompletados();
		int totalEj = (entrenamiento != null && entrenamiento.getEjercicios() != null)
				? entrenamiento.getEjercicios().size()
				: 0;
		int porcentaje = totalEj == 0 ? 0 : (int) Math.round((completados * 100.0) / totalEj);
		long tiempoPrevistoMs = (entrenamiento != null) ? entrenamiento.getTiempoPrevistoSegundos() * 1000L : 0L;

		String msg = String.format(
				"Resumen workout:\nTiempo total: %s\nEjercicios completados: %d/%d (%d%%)\nTiempo estimado: %s",
				formatear(totalMs), completados, totalEj, porcentaje, formatear(tiempoPrevistoMs));

        String[] frases = new String[] {
            "¡Gran trabajo! Cada día más fuerte.",
            "Has dado un paso más hacia tus objetivos.",
            "Constancia y esfuerzo: así se construye el progreso."
        };
        String motivacion = frases[(int) (Math.random() * frases.length)];
		// Mostrar resumen y cambiar a vistaWorkouts en el EDT
			try {
            JOptionPane.showMessageDialog(vista, msg + "\n\n" + motivacion, "Resumen Workout", JOptionPane.INFORMATION_MESSAGE);
				vista.setVisible(false);
				if (vistaWorkouts != null) {
					vistaWorkouts.setVisible(true);
				}
			} catch (Exception ignore) {}
	
	
			try {
				if (usuario != null) {
					UsuWorkout uw = new UsuWorkout(entrenamiento, completados, (int) (totalMs / 1000L), new Date());
					try {
						uw.mAnadirHistorialUsuario(usuario, controlador.tieneConexionInternet());
					} catch (Exception e) {
						e.printStackTrace();
					}
					synchronized (usuario) {
						usuario.getWorkouts().add(uw);
					}

					boolean promovido = usuario.mEvaluarNivel(controlador.tieneConexionInternet());
					if (promovido) {
						controlador.mostrarDatosUsuario();
						controlador.mCargarWorkouts();

						JOptionPane.showMessageDialog(vistaWorkouts,
								"¡Enhorabuena! Has subido al nivel " + usuario.getNivel(), "Promoción",
								JOptionPane.INFORMATION_MESSAGE);
					}
				}
			} finally {
				try {
					if (controlador != null) controlador.terminarWorkout();
				} catch (Exception ignore) {
				}
			}
	

	}
	public void finalizarInmediato() {
		terminarWorkout();
	}
}
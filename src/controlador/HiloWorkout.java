package controlador;

import javax.swing.SwingUtilities;

import modelo.Ejercicio;
import modelo.Serie;
import modelo.Workout;
import vista.PantallaEjercicio;

/**
 * Gestiona los cronómetros y el flujo Preparación -> Serie -> Descanso
 * con soporte de Pausar/Reanudar y avance manual a la siguiente serie.
 *
 * No usa javax.swing.Timer; se basa en hilos y System.currentTimeMillis.
 */
public class HiloWorkout extends Thread {

	public enum Estado { IDLE, PREPARACION, SERIE, DESCANSO, ESPERANDO_SIGUIENTE, FIN }

	// Vista a actualizar y modelo del entrenamiento en curso
	private final PantallaEjercicio vista;
	private final Workout entrenamiento;

	// Estados de ejecución
	private volatile boolean enEjecucion = false; // indica si hay cronos activos
	private volatile boolean enPausa = false;     // indica si están pausados
	private volatile boolean detenido = false;    // indica fin/cancelación
	private final Object bloqueoPausa = new Object(); // monitor para pausar/reanudar

	// Índices de progreso
	private int idxEjercicio = 0;
	private int idxSerie = 0;
	private Estado estado = Estado.IDLE;

	// Marcas de tiempo (en ms) para cronos ascendentes
	private long inicioWorkoutMs;            // inicio del workout
	private long inicioEjercicioMs;          // inicio del ejercicio actual
	private long pausaAcumWorkoutMs = 0L;    // ms acumulados en pausa (workout)
	private long pausaAcumEjercicioMs = 0L;  // ms acumulados en pausa (ejercicio)
	private long inicioPausaMs = 0L;         // marca de inicio de una pausa actual

	// Hilo auxiliar que actualiza los cronos ascendentes en UI
	private HiloCronometros hiloCronometros;

	/**
	 * Constructor del gestor de cronos.
	 * @param vista pantalla a la que se le actualizan los labels
	 * @param workout entrenamiento a ejecutar (lista de ejercicios y series)
	 */
	public HiloWorkout(PantallaEjercicio vista, Workout workout) {
		this.vista = vista;
		this.entrenamiento = workout;
	}

	/** Indica si el gestor está corriendo (cronos activos). */
	public boolean isRunning() { return enEjecucion; }
	/** Indica si el gestor está en pausa. */
	public boolean isPaused() { return enPausa; }
	/** Indica si está detenido (para hilos auxiliares). */
	public boolean isDetenido() { return detenido; }

	/** Devuelve ms transcurridos del workout (descontando pausas). */
	public long getElapsedWorkoutMs() {
		long ahora = System.currentTimeMillis();
		return Math.max(0, ahora - inicioWorkoutMs - pausaAcumWorkoutMs);
	}
	/** Devuelve ms transcurridos del ejercicio actual (descontando pausas). */
	public long getElapsedEjercicioMs() {
		long ahora = System.currentTimeMillis();
		return Math.max(0, ahora - inicioEjercicioMs - pausaAcumEjercicioMs);
	}
	/** Espera bloqueante para hilos auxiliares durante la pausa. */
	public void esperarDespausaBloqueante() {
		synchronized (bloqueoPausa) {
			while (enPausa && !detenido) {
				try { bloqueoPausa.wait(); } catch (InterruptedException ignored) {}
			}
		}
	}

	/**
	 * Arranca los hilos de cronometraje y de fases.
	 * - Resetea acumulados de pausa y marca tiempos de inicio.
	 * - Lanza HiloCronometros para workout/ejercicio.
	 * - Lanza este hilo para gestionar fases.
	 */
	@Override
	public synchronized void start() {
		if (enEjecucion) return; // ya en marcha
		detenido = false;
		enEjecucion = true;
		enPausa = false;
		pausaAcumWorkoutMs = 0L;
		pausaAcumEjercicioMs = 0L;
		inicioWorkoutMs = System.currentTimeMillis();
		inicioEjercicioMs = inicioWorkoutMs;

		// Hilo de actualización de cronos ascendentes
		hiloCronometros = new HiloCronometros(this, vista);
		hiloCronometros.start();

		// Ejecutar fases en este hilo
		super.start();
	}

	/** Cuerpo del hilo principal: ejecuta el bucle de fases. */
	@Override
	public void run() { bucleSerie(); }

	/** Pausa todos los cronos. */
	public void pause() {
		if (!enEjecucion || enPausa) return;
		enPausa = true;
		inicioPausaMs = System.currentTimeMillis();
	}

	/** Reanuda todos los cronos y corrige acumulados. */
	public void reanudar() {
		if (!enEjecucion || !enPausa) return;
		long delta = System.currentTimeMillis() - inicioPausaMs;
		enPausa = false;
		pausaAcumWorkoutMs += delta;
		pausaAcumEjercicioMs += delta;
		synchronized (bloqueoPausa) { bloqueoPausa.notifyAll(); }
	}

	/** Detiene completamente la ejecución y despierta hilos bloqueados. */
	public void stopCronos() {
		detenido = true;
		enEjecucion = false;
		enPausa = false;
		synchronized (bloqueoPausa) { bloqueoPausa.notifyAll(); }
	}

	/** Avanza manualmente a la siguiente serie/ejercicio si procede. */
	public void siguiente() {
		if (!enEjecucion) return;
		if (estado != Estado.ESPERANDO_SIGUIENTE) return;
		iniciarSiguiente();
	}

	/** Bucle de fases: Preparación -> Serie -> Descanso, luego espera "Siguiente". */
	private void bucleSerie() {
		while (!detenido && enEjecucion) {
			// Deshabilitar "Siguiente" durante el ciclo
			habilitarSiguiente(false);

			Ejercicio ej = entrenamiento.getEjercicios().get(idxEjercicio);
			Serie serie = ej.getSeries().get(idxSerie);

			// Preparación (5s)
			estado = Estado.PREPARACION;
			hiloCronometros.cuentaAtrasSegundos(5, (seg) -> actualizarLabelSeguro(vista.getLblCronometroPreparacion(), formatear(seg*1000L)));
			if (estaDetenido()) return;

			// Serie
			estado = Estado.SERIE;
			hiloCronometros.cuentaAtrasSegundos(serie.getTiempo(), (seg) -> {
				if (serie.getCronometro() != null) actualizarLabelSeguro(serie.getCronometro(), formatear(seg*1000L));
			});
			if (estaDetenido()) return;

			// Descanso
			estado = Estado.DESCANSO;
			hiloCronometros.cuentaAtrasSegundos(ej.getTiempoDescanso(), (seg) -> actualizarLabelSeguro(vista.getLblCronometroDescanso(), formatear(seg*1000L)));
			if (estaDetenido()) return;

			// Ciclo finalizado -> esperar "Siguiente"
			estado = Estado.ESPERANDO_SIGUIENTE;
			habilitarSiguiente(true);
			while (!detenido && enEjecucion && estado == Estado.ESPERANDO_SIGUIENTE) {
				if (enPausa) { esperarDespausaBloqueante(); continue; }
				dormir(100);
			}
		}
	}

	/** Calcula y arranca la siguiente serie/ejercicio o finaliza. */
	private void iniciarSiguiente() {
		if (detenido || !enEjecucion) return;
		habilitarSiguiente(false);

		Ejercicio ej = entrenamiento.getEjercicios().get(idxEjercicio);
		idxSerie++;
		if (idxSerie >= ej.getSeries().size()) {
			idxEjercicio++;
			idxSerie = 0;
			if (idxEjercicio >= entrenamiento.getEjercicios().size()) {
				estado = Estado.FIN;
				enEjecucion = false;
				imprimirTiempoTotal();
				return;
			}
			resetCronoEjercicioYUi();
		}
		estado = Estado.PREPARACION;
	}

	/** Resetea crono de ejercicio y actualiza UI con el nuevo ejercicio. */
	private void resetCronoEjercicioYUi() {
		long ahora = System.currentTimeMillis();
		inicioEjercicioMs = ahora;
		pausaAcumEjercicioMs = 0L;

		Ejercicio ej = entrenamiento.getEjercicios().get(idxEjercicio);
		SwingUtilities.invokeLater(() -> {
			vista.getLblNombreEjercicio().setText(ej.getNombre());
			vista.getLblEjercicioDescripcion().setText(ej.getDescripcion());
			vista.getLblCronometroEjercicio().setText("00:00");
			vista.getLblCronometroPreparacion().setText("00:05");
			vista.getLblCronometroDescanso().setText(formatear(ej.getTiempoDescanso() * 1000L));
			try { vista.mostrarPanelSeries(idxEjercicio); } catch (Exception ignore) {}
		});
	}

	/** Duerme el hilo actual en ms. */
	private void dormir(long ms) { try { Thread.sleep(ms); } catch (InterruptedException ignored) {} }
	/** True si se detuvo o no está en ejecución. */
	private boolean estaDetenido() { return detenido || !enEjecucion; }
	/** Actualiza de forma segura el texto de un JLabel. */
	private void actualizarLabelSeguro(javax.swing.JComponent comp, String texto) {
		SwingUtilities.invokeLater(() -> {
			if (comp instanceof javax.swing.JLabel lbl) lbl.setText(texto);
		});
	}
	/** Habilita o deshabilita el botón "Siguiente". */
	private void habilitarSiguiente(boolean enable) {
		SwingUtilities.invokeLater(() -> {
			vista.getBtnSiguiente().setEnabled(enable);
			vista.getBtnSiguiente().setVisible(true);
		});
	}
	/** Formatea ms a mm:ss. */
	private String formatear(long millis) {
		long total = Math.max(0, millis) / 1000;
		long mm = total / 60;
		long ss = total % 60;
		return String.format("%02d:%02d", mm, ss);
	}
	/** Imprime el tiempo total del workout. */
	private void imprimirTiempoTotal() {
		long totalMs = System.currentTimeMillis() - inicioWorkoutMs - pausaAcumWorkoutMs;
		System.out.println("Tiempo total workout: " + formatear(totalMs));
	}
}
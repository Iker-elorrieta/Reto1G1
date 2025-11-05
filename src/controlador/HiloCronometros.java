package controlador;

import java.util.function.IntConsumer;

import vista.PantallaEjercicio;

public class HiloCronometros extends Thread {

	// Referencia al gestor principal de la sesión de workout que controla
	// el estado (running, paused, detenido) y provee los tiempos.
	private final HiloWorkout hiloWorkout;
	// Referencia a la vista para actualizar los cronómetros en pantalla.
	private final PantallaEjercicio vista;

	public HiloCronometros(HiloWorkout manager, PantallaEjercicio vista) {
		super("CronosAscendentes");
		this.hiloWorkout = manager;
		this.vista = vista;
		// Marcamos el hilo como daemon para que no impida que la JVM termine
		// si solo quedan hilos daemon en ejecución.
		setDaemon(true);
	}

	@Override
	public void run() {
		// Bucle principal que actualiza dos cronómetros en la vista mientras
		// el workout esté en ejecución y no se haya detenido.
		while (hiloWorkout.isRunning() && !hiloWorkout.isDetenido()) {
			// Si está en pausa, esperamos de forma bloqueante hasta que se reanude.
			if (hiloWorkout.isPaused()) {
				hiloWorkout.esperarDespausaBloqueante();
				continue; // Volvemos a comprobar el estado tras la reanudación.
			}

			// Obtenemos los tiempos transcurridos (en milisegundos) desde el
			// inicio del workout y del ejercicio actual.
			long transWorkout = hiloWorkout.workoutMs();
			long transEjercicio = hiloWorkout.ejercicioMs();
			String txtWorkout = formatear(transWorkout);
			String txtEjercicio = formatear(transEjercicio);

			// Actualizamos las etiquetas de la vista con los tiempos formateados.
			vista.getLblCronometroWorkout().setText(txtWorkout);
			vista.getLblCronometroEjercicio().setText(txtEjercicio);

			// Pequeña pausa para evitar refrescos demasiado frecuentes.
			try {
				Thread.sleep(200);
			} catch (InterruptedException ignored) {
			}
		}
	}

	/**
	 * Cuenta atrás en segundos, llamando a onTick con los segundos restantes.
	 * Respeta el estado de pausa y detención del hiloWorkout.
	 *
	 * Notas de comportamiento:
	 * - Si el workout se detiene o deja de estar en running, el método retorna.
	 * - Mientras esté en pausa, se acumula el tiempo de pausa para no contabilizarlo
	 *   dentro de la cuenta atrás.
	 *
	 * @param segundos número de segundos de la cuenta atrás (se protege contra
	 *                  valores negativos convirtiéndolos a 0)
	 * @param onTick   consumidor que recibe los segundos restantes en cada iteración
	 */
	public void cuentaAtrasSegundos(int segundos, IntConsumer onTick) {
		int total = Math.max(0, segundos);
		long inicio = System.currentTimeMillis();
		long pausaAcum = 0L; // milisegundos acumulados en pausas
		long inicioPausaLocal = 0L;

		for (;;) {
			// Si el gestor indica detención o que no está en ejecución, salimos.
			if (hiloWorkout.isDetenido() || !hiloWorkout.isRunning())
				return;
			// Si hay pausa, registramos cuándo empieza y bloqueamos hasta la reanudación.
			if (hiloWorkout.isPaused()) {
				inicioPausaLocal = System.currentTimeMillis();
				hiloWorkout.esperarDespausaBloqueante();
				// Acumulamos el tiempo que ha durado la pausa local para descontarlo
				// del tiempo transcurrido de la cuenta atrás.
				pausaAcum += System.currentTimeMillis() - inicioPausaLocal;
			}
			long ahora = System.currentTimeMillis();
			long trans = (ahora - inicio - pausaAcum);
			// Calculamos segundos restantes (no negativos).
			int restante = (int) Math.max(0, total - (trans / 1000));
			// Notificamos al consumidor externo con los segundos restantes.
			onTick.accept(restante);
			if (restante <= 0)
				break; // Cuenta atrás finalizada
			try {
				Thread.sleep(200); // frecuencia de actualización
			} catch (InterruptedException ignored) {
			}
		}
	}

	// Formatea milisegundos a "MM:SS" para mostrar en la interfaz.
	private String formatear(long millis) {
		long total = Math.max(0, millis) / 1000;
		long mm = total / 60;
		long ss = total % 60;
		return String.format("%02d:%02d", mm, ss);
	}
}
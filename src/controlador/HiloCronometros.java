package controlador;

import javax.swing.SwingUtilities;
import java.util.function.IntConsumer;

import vista.PantallaEjercicio;

/**
 * Hilo dedicado a actualizar los cronómetros ascendentes de workout y ejercicio.
 * Además expone utilidades como la cuenta atrás respetando pausa/detención.
 */
public class HiloCronometros extends Thread {

	private final HiloWorkout hiloWorkout;
	private final PantallaEjercicio vista;

	public HiloCronometros(HiloWorkout manager, PantallaEjercicio vista) {
		super("CronosAscendentes");
		this.hiloWorkout = manager;
		this.vista = vista;
		setDaemon(true);
	}

	@Override
	public void run() {
		while (hiloWorkout.isRunning() && !hiloWorkout.isDetenido()) {
			if (hiloWorkout.isPaused()) {
				hiloWorkout.esperarDespausaBloqueante();
				continue;
			}

			long transWorkout = hiloWorkout.getElapsedWorkoutMs();
			long transEjercicio = hiloWorkout.getElapsedEjercicioMs();
			String txtWorkout = formatear(transWorkout);
			String txtEjercicio = formatear(transEjercicio);

			SwingUtilities.invokeLater(() -> {
				vista.getLblCronometroWorkout().setText(txtWorkout);
				vista.getLblCronometroEjercicio().setText(txtEjercicio);
			});

			try { Thread.sleep(200); } catch (InterruptedException ignored) {}
		}
	}

	/**
	 * Cuenta atrás en segundos, llamando a onTick con los segundos restantes.
	 * Respeta el estado de pausa y detención del hiloWorkout.
	 */
	public void cuentaAtrasSegundos(int segundos, IntConsumer onTick) {
		int total = Math.max(0, segundos);
		long inicio = System.currentTimeMillis();
		long pausaAcum = 0L;
		long inicioPausaLocal = 0L;

		for (;;) {
			if (hiloWorkout.isDetenido() || !hiloWorkout.isRunning()) return;
			if (hiloWorkout.isPaused()) {
				inicioPausaLocal = System.currentTimeMillis();
				hiloWorkout.esperarDespausaBloqueante();
				pausaAcum += System.currentTimeMillis() - inicioPausaLocal;
			}
			long ahora = System.currentTimeMillis();
			long trans = (ahora - inicio - pausaAcum);
			int restante = (int) Math.max(0, total - (trans / 1000));
			onTick.accept(restante);
			if (restante <= 0) break;
			try { Thread.sleep(200); } catch (InterruptedException ignored) {}
		}
	}

	private String formatear(long millis) {
		long total = Math.max(0, millis) / 1000;
		long mm = total / 60;
		long ss = total % 60;
		return String.format("%02d:%02d", mm, ss);
	}
}

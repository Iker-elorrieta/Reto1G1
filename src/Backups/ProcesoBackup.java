package Backups;


import modelo.Usuario;
import modelo.Workout;

public class ProcesoBackup {
	public static void main(String[] args) {
		// Llamar al hilo de backup
		try {
			Backup backup = new Backup(Workout.mObtenerTodosWorkouts(true), Usuario.fbObtenerTodosUsuarios(true));
			// Guardar datos binarios
			backup.backupBinario();
			// Guardar datos XML
			backup.historialXML();
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);

		}
	}

}

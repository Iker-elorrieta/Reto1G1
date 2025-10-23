package Backups;

public class ProcesoBackup {

	public static void main(String[] args) {
		// Llamar al hilo de backup
		try {
			HiloBackup hiloBackup = new HiloBackup();
			hiloBackup.start();
			hiloBackup.join();
			System.exit(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}

package backups;

public class ProcesoBackup {

	public static void main(String[] args) {
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

package Backups;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.swing.JLabel;

public class HiloBackup extends Thread {

	JLabel label;

	public HiloBackup(JLabel label) {
		this.label = label;
	}

	@Override
	public void run() {
		try {
			// En proceso...
			label.setText("Backup en proceso...");
			label.setForeground(Color.BLUE);
			
			// Inicio proceso
			ProcessBuilder pb = new ProcessBuilder("java", "-jar", "backups.jar");
			pb.directory(new File("."));
			Process proces = pb.start();
			pb.redirectErrorStream(true);
			int exitCode = proces.waitFor();

			// Finalizado
			label.setForeground(Color.GREEN);
			label.setText("Backup finalizado correctamente (" + exitCode + ")");
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			label.setText("Backup finalizado con error.");
			label.setForeground(Color.RED);

		}

	}

}
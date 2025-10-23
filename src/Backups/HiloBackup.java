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
			ProcessBuilder pb = new ProcessBuilder("java", "Backups.ProcesoBackup");
			pb.directory(new File("target/classes"));
			Process proces = pb.start();
			int exitCode = proces.waitFor();
			label.setText("Backup finalizado correctamente. ("+ exitCode+")");
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			label.setText("Backup finalizado con error.");
			label.setForeground(Color.RED);	

		}

    }


}
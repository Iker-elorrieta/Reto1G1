package modelo.modeloBackup;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import modelo.*;
import java.util.List;



public class BackupManager {

	public static void guardarBackupWorkouts(List<Workout> workouts, String rutaArchivo) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(rutaArchivo))) {
            oos.writeObject(workouts);
        }
    }

    public static void guardarBackupUsuarios(List<Usuario> usuarios, String rutaArchivo) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(rutaArchivo))) {
            oos.writeObject(usuarios);
        }
    }
}

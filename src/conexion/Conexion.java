package conexion;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;

public class Conexion {
	
	private static String nombreJSON = "GymSquad.json";
	private static String IdDeProyecto = "dam2-r1-g1";
	
	public static Firestore conectar() throws IOException {
		FileInputStream serviceAccount;
		Firestore firestore = null;
		try {
			serviceAccount = new FileInputStream(nombreJSON);
		

		FirestoreOptions firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder()
				.setProjectId(IdDeProyecto).setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();
		firestore = firestoreOptions.getService();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return firestore;
	}

}

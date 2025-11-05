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
	
	/**
	 * Conexta a Firestore 
	 */
	public static Firestore conectar() throws IOException {
		FileInputStream serviceAccount;
		Firestore firestore = null;
		try {
			// Abre el fichero JSON con las credenciales de la cuenta de servicio
			serviceAccount = new FileInputStream(nombreJSON);
		
		// Construye las opciones de Firestore con el ID de proyecto y las credenciales leídas
		FirestoreOptions firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder()
				.setProjectId(IdDeProyecto).setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();
		// Obtiene el cliente de Firestore con la configuración anterior
		firestore = firestoreOptions.getService();
		} catch (FileNotFoundException e) {
			// Si no se encuentra el fichero de credenciales se captura la excepción.
			// Aquí solo se imprime la traza; se devuelve null para indicar que la conexión no se realizó.
			e.printStackTrace();
		}
		
		return firestore;
	}

}

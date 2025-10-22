package vista;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import modelo.UsuWorkout;
import modelo.Usuario;
import modelo.Workout;
import java.io.File;

public class HiloBackup extends Thread {

	private List<Workout> workouts;
	private List<Usuario> usuarios;

	public HiloBackup() {
	}

	@Override
	public void run() {
		try {
			workouts = Workout.mObtenerTodosWorkouts();
			usuarios = Usuario.mObtenerTodosUsuarios();

			// Guardar datos binarios
			backupBinario();

			// Guardar datos XML
			historialXML();
			System.out.println("Backups y XML generados correctamente.");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void backupBinario() throws Exception {

		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("backups/usuarios.dat"))) {
			oos.writeObject(usuarios);
		}
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("backups/workouts.dat"))) {
			oos.writeObject(workouts);
		}
	}

	private void historialXML() throws Exception {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();

		// Historial
		Element historialElement = doc.createElement("historial");
		doc.appendChild(historialElement);
		for (Usuario u : usuarios) {
			for (UsuWorkout uw : u.getWorkouts()) {
				Element workoutElement = doc.createElement("workout");
				workoutElement.setAttribute("id_workout", uw.getWorkout().getIdWorkout());
				workoutElement.setAttribute("id_usuario", u.getIdUsuario());

				// Fecha
				Element fecha = doc.createElement("fecha");
				fecha.appendChild(doc.createTextNode(uw.getFecha().toString()));
				workoutElement.appendChild(fecha);

				// Tiempo total
				Element tiempo = doc.createElement("tiempo_total");
				tiempo.appendChild(doc.createTextNode(String.valueOf(uw.getTiempoTotal())));
				workoutElement.appendChild(tiempo);

				// Ejercicios completados
				Element ejercicios = doc.createElement("ejercicios_completados");
				ejercicios.appendChild(doc.createTextNode(String.valueOf(uw.getEjerciciosCompletados())));
				workoutElement.appendChild(ejercicios);

				historialElement.appendChild(workoutElement);
			}

			// Guardar archivo XML
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("backups/historial.xml"));
			transformer.transform(source, result);

		}

	}
}
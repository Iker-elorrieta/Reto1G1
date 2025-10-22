package vista;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import modelo.Ejercicio;
import modelo.UsuWorkout;
import modelo.Usuario;
import modelo.Workout;
import java.io.File;

public class HiloBackup extends Thread {

	private Usuario usuario;
	private List<Workout> workouts;

	public HiloBackup(Usuario usuario) {
		this.usuario = usuario;

	}

	@Override
	public void run() {
		try {

			// Guardar datos binarios
			guardarBackup(usuario);

			// Guardar datos XML
			usuarioXML(usuario);
			System.out.println("Backups y XML generados correctamente.");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void guardarBackup(Usuario u) throws Exception {

		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("backups/usuario.dat"))) {
			oos.writeObject(u);
		}
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("backups/workouts.dat"))) {
			oos.writeObject(workouts);
		}
	}

	private void usuarioXML(Usuario u) throws Exception {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();

		// Historial
		Element historialElement = doc.createElement("historial");
		doc.appendChild(historialElement);

		u.mCargarHistorialWorkouts();
		for (UsuWorkout uw : u.getWorkouts()) {
			Element workoutElement = doc.createElement("workout");
			workoutElement.setAttribute("id", uw.getWorkout().getIdWorkout());

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

		System.out.println("XML generado para usuario: " + u.getNombre());
	}
}
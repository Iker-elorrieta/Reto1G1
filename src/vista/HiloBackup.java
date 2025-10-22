package vista;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
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


public class HiloBackup extends Thread{
	
	private List<Usuario> usuarios;
    private List<Workout> workouts;
    
    public HiloBackup(List<Usuario> usuarios, List<Workout> workouts) {
		this.usuarios = usuarios;
		this.workouts = workouts;
	}
	
	@Override
	public void run() {
		try {
			for (Usuario u : usuarios) {
				// Guardar datos binarios
				guardarBackup(u);
				
				// Guardar datos XML
				generarXML(u);
				System.out.println("Backups y XML generados correctamente.");
			}
		} catch (Exception e) {
            e.printStackTrace();
        }

	}

	private void guardarBackup(Usuario u) throws Exception {
		try (ObjectOutputStream oos = new ObjectOutputStream(
		        new FileOutputStream("usuarios_backup.dat"))) {
		    oos.writeObject(usuarios); 
		}

		 
		 try (ObjectOutputStream oos = new ObjectOutputStream(
			        new FileOutputStream("workouts_backup.dat"))) {
			    oos.writeObject(workouts);
			}
		 System.out.println("Backup guardado para usuario: " + u.getNombre());
	}

	private void generarXML(Usuario u) throws Exception {
	    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	    Document doc = docBuilder.newDocument();

	    // Elemento raíz <usuario>
	    Element root = doc.createElement("usuario");
	    root.setAttribute("id", u.getIdUsuario());
	    root.setAttribute("nombre", u.getNombre());
	    doc.appendChild(root);

	    // Historial
	    Element historialElement = doc.createElement("historial");
	    root.appendChild(historialElement);

	    // Iterar sobre los workouts del usuario
	    for (UsuWorkout uw : u.getWorkouts()) {
	        Element workoutElement = doc.createElement("workout");
	        workoutElement.setAttribute("id", uw.getWorkout().getIdWorkout());  // Acceder al workout
	        workoutElement.setAttribute("nombre", uw.getWorkout().getNombre());  // Acceder al nombre del workout

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
	    StreamResult result = new StreamResult(new File("historial_" + u.getIdUsuario() + ".xml"));
	    transformer.transform(source, result);

	    System.out.println("XML generado para usuario: " + u.getNombre());
    }
}
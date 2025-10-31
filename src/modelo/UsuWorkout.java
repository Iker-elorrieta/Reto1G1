package modelo;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;

import conexion.Conexion;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class UsuWorkout implements Serializable {

	/************** Atributos **************/
	private static final long serialVersionUID = 1L;
	private Workout workout; // Relación con el workout
	private int ejerciciosCompletados; // Cuántos ejercicios completó el usuario
	private int tiempoTotal; // Tiempo total en segundos
	private Date fecha; // Fecha en la que se completó el workout

	private static String collectionName = "workouts";
	private static String fieldWorkout = "id_workout";
	private static String fieldEjerciciosCompletados = "ejercicioscompletados";
	private static String fieldTiempoTotal = "tiempo_total";
	private static String fieldFecha = "fecha";

	/************** Constructores **************/
	public UsuWorkout(Workout workout, int ejerciciosCompletados, int tiempoTotal, Date fecha) {
		this.workout = workout;
		this.ejerciciosCompletados = ejerciciosCompletados;
		this.tiempoTotal = tiempoTotal;
		this.fecha = fecha;
	}

	public UsuWorkout() {

	}

	/************** Getters y Setters **************/
	public Workout getWorkout() {
		return workout;
	}

	public int getEjerciciosCompletados() {
		return ejerciciosCompletados;
	}

	public int getTiempoTotal() {
		return tiempoTotal;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setWorkout(Workout workout) {
		this.workout = workout;
	}

	public void setEjerciciosCompletados(int ejerciciosCompletados) {
		this.ejerciciosCompletados = ejerciciosCompletados;
	}

	public void setTiempoTotal(int tiempoTotal) {
		this.tiempoTotal = tiempoTotal;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	/************** Métodos **************/
	public static ArrayList<UsuWorkout> xmlCargarHistorialWorkouts(Usuario usuario) throws Exception {
		ArrayList<UsuWorkout> lista = new ArrayList<>();
		File f = new File("backups/historial.xml");
		DocumentBuilder b = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = b.parse(f);
		doc.getDocumentElement().normalize();

		NodeList nodes = doc.getElementsByTagName("workout");
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

		for (int i = 0; i < nodes.getLength(); i++) {
			Element el = (Element) nodes.item(i);

			UsuWorkout uw = new UsuWorkout();
			Workout w = new Workout();
			w.setIdWorkout(el.getAttribute("id_workout"));
			w.mObtenerWorkout(usuario.getNivel(), null, false);
			uw.setWorkout(w);

			NodeList nf = el.getElementsByTagName("fecha");
			String fechaStr = nf.item(0).getTextContent();
			try {
				uw.setFecha(sdf.parse(fechaStr));
			} catch (Exception ex) {
			}

			int tiempo = 0, comps = 0;
			NodeList nt = el.getElementsByTagName("tiempo_total");
			try {
				tiempo = Integer.parseInt(nt.item(0).getTextContent());
			} catch (Exception ignore) {
			}
			NodeList ne = el.getElementsByTagName("ejercicios_completados");
			try {
				comps = Integer.parseInt(ne.item(0).getTextContent());
			} catch (Exception ignore) {
			}

			uw.setTiempoTotal(tiempo);
			uw.setEjerciciosCompletados(comps);
			lista.add(uw);
		}
		return lista;
	}

	public static ArrayList<UsuWorkout> mCargarHistorialWorkouts(Usuario usuario, boolean conexion) throws Exception {
		if (conexion) {
			return fbCargarHistorialWorkouts(usuario);
		} else {
			return xmlCargarHistorialWorkouts(usuario);
		}
	}

	public static ArrayList<UsuWorkout> fbCargarHistorialWorkouts(Usuario usuario) throws Exception {
		Firestore conexion = Conexion.conectar();
		var query = conexion.collection("usuarios").document(usuario.getIdUsuario()).collection(collectionName).get()
				.get();
		ArrayList<UsuWorkout> listaWorkouts = new ArrayList<>();
		for (var doc : query.getDocuments()) {
			UsuWorkout uw = new UsuWorkout();
			Workout w = new Workout();
			DocumentReference refWorkout = (DocumentReference) doc.getData().get(fieldWorkout);
			w.setIdWorkout(refWorkout.getId());
			w.mObtenerWorkout(usuario.getNivel(), conexion,true);
			uw.setWorkout(w);
			uw.setEjerciciosCompletados(doc.getLong(fieldEjerciciosCompletados).intValue());
			uw.setTiempoTotal(doc.getLong(fieldTiempoTotal).intValue());
			uw.setFecha(doc.getDate(fieldFecha));
			listaWorkouts.add(uw);
		}
		conexion.close();
		return listaWorkouts;
	}

	public void mAnadirHistorialUsuario(Usuario usuario, boolean conexion) throws Exception {
		if (conexion) {
			fbAnadirHistorialUsuario(usuario);
		} else {
			xmlAnadirHistorialUsuario(usuario);
		}
	}

	public void xmlAnadirHistorialUsuario(Usuario usuario) throws Exception {

		File xmlFile = new File("backups/historial.xml");

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc;
		Element root;

		doc = builder.parse(xmlFile);
		doc.getDocumentElement().normalize();
		root = doc.getDocumentElement();
		if (root == null || !"historial".equals(root.getNodeName())) {
			doc = builder.newDocument();
			root = doc.createElement("historial");
			doc.appendChild(root);
		}

		String idUsuario = usuario.getIdUsuario();
		String idWorkout = this.getWorkout().getIdWorkout();
		String fechaStr = String.valueOf(this.getFecha());

		Element workoutElement = doc.createElement("workout");
		workoutElement.setAttribute("id_workout", idWorkout);
		workoutElement.setAttribute("id_usuario", idUsuario);

		Element fecha = doc.createElement("fecha");
		fecha.appendChild(doc.createTextNode(fechaStr));
		workoutElement.appendChild(fecha);

		Element tiempo = doc.createElement("tiempo_total");
		tiempo.appendChild(doc.createTextNode(String.valueOf(this.getTiempoTotal())));
		workoutElement.appendChild(tiempo);

		Element ejercicios = doc.createElement("ejercicios_completados");
		ejercicios.appendChild(doc.createTextNode(String.valueOf(this.getEjerciciosCompletados())));
		workoutElement.appendChild(ejercicios);

		root.appendChild(workoutElement);

		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(xmlFile);
		transformer.transform(source, result);
	}

	public void fbAnadirHistorialUsuario(Usuario usuario) throws Exception {
		if (usuario == null || usuario.getIdUsuario() == null) {
			throw new IllegalArgumentException("Usuario inválido para añadir historial");
		}
		Firestore conexion = Conexion.conectar();
		try {
			DocumentReference workoutRef = conexion.collection(collectionName).document(workout.getIdWorkout());
			Map<String, Object> datos = new HashMap<>();
			datos.put(fieldWorkout, workoutRef);
			datos.put(fieldEjerciciosCompletados, ejerciciosCompletados);
			datos.put(fieldTiempoTotal, tiempoTotal);
			datos.put(fieldFecha, fecha);
			conexion.collection("usuarios").document(usuario.getIdUsuario()).collection(collectionName).document()
					.set(datos).get();
		} finally {
			conexion.close();
		}
	}
}

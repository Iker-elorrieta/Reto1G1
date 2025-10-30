package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;

import conexion.Conexion;
import java.util.HashMap;
import java.util.Map;

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
	public static ArrayList<UsuWorkout> mCargarHistorialWorkouts(Usuario usuario) throws Exception {
		Firestore conexion = Conexion.conectar();
		var query = conexion.collection("usuarios").document(usuario.getIdUsuario()).collection(collectionName).get().get();
		ArrayList<UsuWorkout> listaWorkouts = new ArrayList<>();
		for (var doc : query.getDocuments()) {
			UsuWorkout uw = new UsuWorkout();
			Workout w = new Workout();
			DocumentReference refWorkout = (DocumentReference) doc.getData().get(fieldWorkout);
			w.setIdWorkout(refWorkout.getId());
			w.mObtenerWorkout(usuario.getNivel(),conexion);
			uw.setWorkout(w);
			uw.setEjerciciosCompletados(doc.getLong(fieldEjerciciosCompletados).intValue());
			uw.setTiempoTotal(doc.getLong(fieldTiempoTotal).intValue());
			uw.setFecha(doc.getDate(fieldFecha));
			listaWorkouts.add(uw);
		}
		conexion.close();
		return listaWorkouts;
	}


	public void mAnadirHistorialUsuario(Usuario usuario) throws Exception {
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
			conexion.collection("usuarios").document(usuario.getIdUsuario()).collection(collectionName)
				.document().set(datos).get();
		} finally {
			conexion.close();
		}
	}
}

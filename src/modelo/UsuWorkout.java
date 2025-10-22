package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;

import conexion.Conexion;

public class UsuWorkout implements Serializable {

	private Workout workout; // Relación con el workout
	private int ejerciciosCompletados; // Cuántos ejercicios completó el usuario
	private int tiempoTotal; // Tiempo total en segundos
	private Date fecha; // Fecha en la que se completó el workout

	// Constructor de la clase
	public UsuWorkout(Workout workout, int ejerciciosCompletados, int tiempoTotal, Date fecha) {
		this.workout = workout;
		this.ejerciciosCompletados = ejerciciosCompletados;
		this.tiempoTotal = tiempoTotal;
		this.fecha = fecha;
	}

	public UsuWorkout() {
	}

	// Getters
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

	// Setters
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

	public static ArrayList<UsuWorkout> mCargarHistorialWorkouts(Usuario usuario) throws Exception {
		Firestore conexion = Conexion.conectar();
		var query = conexion.collection("usuarios").document(usuario.getIdUsuario()).collection("workouts").get().get();
		ArrayList<UsuWorkout> listaWorkouts = new ArrayList<>();
		for (var doc : query.getDocuments()) {
			UsuWorkout uw = new UsuWorkout();
			Workout w = new Workout();
			DocumentReference refWorkout = (DocumentReference) doc.getData().get("id_workout");
			w.setIdWorkout(refWorkout.getId());
			w.mObtenerWorkout(usuario.getNivel(),conexion);
			uw.setWorkout(w);
			uw.setEjerciciosCompletados(doc.getLong("ejercicioscompletados").intValue());
			uw.setTiempoTotal(doc.getLong("tiempo_total").intValue());
			uw.setFecha(doc.getDate("fecha"));
			listaWorkouts.add(uw);
		}
		conexion.close();
		return listaWorkouts;
	}
}

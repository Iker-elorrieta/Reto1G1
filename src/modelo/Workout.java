package modelo;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import conexion.Conexion;

public class Workout implements Serializable {

	/************** Atributos **************/
	private String IdWorkout;
	private String nombre;
	private String descripcion;
	private int nivel;
	private String video;
	private List<Ejercicio> ejercicios = new ArrayList<>();

	private static String collectionName = "workouts";
	private static String fieldNombre = "nombre";
	private static String fieldDescripcion = "descripcion";
	private static String fieldNivel = "nivel";
	private static String fieldVideo = "video";

	/************** Constructores **************/

	public Workout() {

	}

	public Workout(String pNombre, String pDescripcion, int pNivel, String pVideo) {
		this.nombre = pNombre;
		this.descripcion = pDescripcion;
		this.nivel = pNivel;
		this.video = pVideo;
	}

	/************** Getters y Setters **************/

	public String getIdWorkout() {
		return IdWorkout;
	}

	public void setIdWorkout(String idWorkout) {
		IdWorkout = idWorkout;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public List<Ejercicio> getEjercicios() {
		return ejercicios;
	}

	public void setEjercicios(List<Ejercicio> ejercicios) {
		this.ejercicios = ejercicios;
	}

	/************** Metodo CRUD **************/

	// ********** READ **********
	public void mObtenerWorkout(int nivel, Firestore conexion) {
		boolean cerrarConexion = false;
		try {
			if (conexion == null) {
				cerrarConexion = true;
				conexion = Conexion.conectar();
			}

			DocumentSnapshot workout = conexion.collection(collectionName).document(String.valueOf(getIdWorkout()))
					.get().get();
			if (workout.getLong(fieldNivel).intValue() < nivel) {
				return;
			}
			setIdWorkout(workout.getId());
			setNombre(workout.getString(fieldNombre));
			setDescripcion(workout.getString(fieldDescripcion));
			setNivel(workout.getLong(fieldNivel).intValue());
			setVideo(workout.getString(fieldVideo));
			if (cerrarConexion) {
				conexion.close();
			}
		} catch (InterruptedException | ExecutionException e) {
			System.out.println("Error: Clase Workout, metodo mObtenerWorkout");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static ArrayList<Workout> mObtenerWorkouts(int nivel) {
		Firestore conexion = null;

		ArrayList<Workout> listaDeWorkouts = new ArrayList<Workout>();

		try {
			conexion = Conexion.conectar();

			ApiFuture<QuerySnapshot> query = conexion.collection(collectionName)
					.whereLessThanOrEqualTo(fieldNivel, nivel).get();
			QuerySnapshot querySnapshot = query.get();
			List<QueryDocumentSnapshot> workouts = querySnapshot.getDocuments();
			for (QueryDocumentSnapshot workout : workouts) {

				Workout w = new Workout();
				w.setIdWorkout(workout.getId());
				w.setNombre(workout.getString(fieldNombre));
				w.setDescripcion(workout.getString(fieldDescripcion));
				w.setNivel(workout.getLong(fieldNivel).intValue());
				w.setVideo(workout.getString(fieldVideo));

				listaDeWorkouts.add(w);
			}
			conexion.close();

		} catch (InterruptedException | ExecutionException e) {
			System.out.println("Error: Clase Workout, metodo mObtenerWorkout con arrayList");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return listaDeWorkouts;
	}

	public static ArrayList<Workout> mObtenerTodosWorkouts() {
		Firestore conexion = null;
		ArrayList<Workout> listaDeWorkouts = new ArrayList<Workout>();
		try {
			conexion = Conexion.conectar();

			ApiFuture<QuerySnapshot> query = conexion.collection(collectionName).get();
			QuerySnapshot querySnapshot = query.get();
			List<QueryDocumentSnapshot> workouts = querySnapshot.getDocuments();
			for (QueryDocumentSnapshot workout : workouts) {

				Workout w = new Workout();
				w.setIdWorkout(workout.getId());
				w.setNombre(workout.getString(fieldNombre));
				w.setDescripcion(workout.getString(fieldDescripcion));
				w.setNivel(workout.getLong(fieldNivel).intValue());
				w.setVideo(workout.getString(fieldVideo));
				listaDeWorkouts.add(w);
			}
			conexion.close();

		} catch (InterruptedException | ExecutionException e) {
			System.out.println("Error: Clase Workout, metodo mObtenerWorkout con arrayList");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Workout w : listaDeWorkouts) {
			w.ejercicios = Ejercicio.mObtenerEjerciciosWorkout(w);
		}
		return listaDeWorkouts;
	}

}

package modelo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query.Direction;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import conexion.Conexion;

public class Workout implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
		this.ejercicios = new ArrayList<>();
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

	public int getTiempoPrevistoSegundos() {
		int tiempoPrevisto = 0;
		if (ejercicios == null)
			return tiempoPrevisto;
		final int PREPARACION_POR_SERIE = 5; // segundos
		for (Ejercicio e : ejercicios) {
			if (e == null || e.getSeries() == null)
				continue;
			for (Serie s : e.getSeries()) {
				if (s == null)
					continue;
				tiempoPrevisto += s.getTiempo();
				tiempoPrevisto += e.getTiempoDescanso();
				tiempoPrevisto += PREPARACION_POR_SERIE;
			}
		}
		return tiempoPrevisto;
	}

	/**
	 * Calcula el porcentaje completado de un workout.
	 * Si uw es null o no hay ejercicios, devuelve 0.
	 */
	public int calcularPorcentajeCompletado(UsuWorkout uw) {
	    if (uw == null || this.ejercicios == null || this.ejercicios.isEmpty())
	        return 0;

	    int total = this.ejercicios.size();
	    int completados = uw.getEjerciciosCompletados();

	    return Math.max(0, Math.min(100,
	        (int) Math.round((completados * 100.0) / total)
	    ));
	}

	/************** Metodo CRUD **************/

	public void mObtenerWorkout(int nivel, Firestore conexion, boolean conexionInternet) {
		if (conexionInternet) {
			fbObtenerWorkout(nivel, conexion);
		} else {
			ArrayList<Workout> workouts = datLeerTodosWorkouts();
			for (Workout w : workouts) {
				if (w.getIdWorkout().equals(this.getIdWorkout()) && w.getNivel() <= nivel) {
					this.setNombre(w.getNombre());
					this.setDescripcion(w.getDescripcion());
					this.setNivel(w.getNivel());
					this.setVideo(w.getVideo());
					this.setEjercicios(w.getEjercicios());
					return;
				}
			}
		}
	}

	public void fbObtenerWorkout(int nivel, Firestore conexion) {
		boolean cerrarConexion = false;
		try {
			if (conexion == null) {
				cerrarConexion = true;
				conexion = Conexion.conectar();
			}

			DocumentSnapshot workout = conexion.collection(collectionName).document(String.valueOf(getIdWorkout()))
					.get().get();
			if (workout.getLong(fieldNivel).intValue() > nivel) {
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
		setEjercicios(Ejercicio.fbObtenerEjerciciosWorkout(this));
		return;

	}

	public static ArrayList<Workout> datLeerTodosWorkouts() {
		File file = new File("backups/workouts.dat");
		ArrayList<Workout> lista = new ArrayList<>();
		if (!file.isFile()) {
			return lista;
		}
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
			Object obj = ois.readObject();
			if (obj instanceof List<?>) {
				for (Object o : (List<?>) obj) {
					if (o instanceof Workout) {
						Workout w = (Workout) o;
						lista.add(w);
					}
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Error leyendo backups/workouts.dat");
			e.printStackTrace();
		}
		return lista;
	}

	public static ArrayList<Workout> mObtenerWorkouts(int nivel, boolean conexionInternet) {
		if (conexionInternet) {
			return fbObtenerWorkouts(nivel);
		} else {
			ArrayList<Workout> workouts = datLeerTodosWorkouts();
			ArrayList<Workout> workoutsNivel = new ArrayList<>();
			for (Workout w : workouts) {
				if (w.getNivel() <= nivel) {
					workoutsNivel.add(w);
				}
			}
			return workoutsNivel;
		}
	}

	public static ArrayList<Workout> fbObtenerWorkouts(int nivel) {

		Firestore conexion = null;

		ArrayList<Workout> listaDeWorkouts = new ArrayList<Workout>();

		try {
			conexion = Conexion.conectar();
			ApiFuture<QuerySnapshot> query = conexion.collection(collectionName)
					.whereLessThanOrEqualTo(fieldNivel, nivel).orderBy(fieldNivel, Direction.DESCENDING).get();
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

	public static ArrayList<Workout> mObtenerTodosWorkouts(boolean conexionWifi) {
		if (conexionWifi) {
			return fbObtenerTodosWorkouts();
		} else {
			return datLeerTodosWorkouts();
		}
	}

	public static ArrayList<Workout> fbObtenerTodosWorkouts() {
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
			w.ejercicios = Ejercicio.fbObtenerEjerciciosWorkout(w);
		}
		return listaDeWorkouts;
	}

}

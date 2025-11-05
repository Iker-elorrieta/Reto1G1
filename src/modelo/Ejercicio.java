package modelo;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import conexion.Conexion;

public class Ejercicio implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/************** Atributos **************/
	private String idEjercicio;
    private String nombre;
    private String descripcion;
    private int tiempoDescanso; // segundos
    private List<Serie> series = new ArrayList<>();
    private boolean actual;
    
    private static String collectionWorkouts = "workouts";
    private static String collectionEjercicios = "ejercicios";
    private static String fieldNombre = "nombre";
    private static String fieldDescripcion = "descripcion";
    private static String fieldTiempoDescanso = "tiempo_descanso";
	/************** Constructores **************/
    
    public Ejercicio() {
    	
    }

    public Ejercicio(String idEjercicio, String nombre, String descripcion, int tiempoDescanso) {
        this.idEjercicio = idEjercicio;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tiempoDescanso = tiempoDescanso;
        this.actual = false;
    }
    
    /************** Getters y Setters **************/
	public String getIdEjercicio() {
		return idEjercicio;
	}

	public void setIdEjercicio(String idEjercicio) {
		this.idEjercicio = idEjercicio;
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

	public int getTiempoDescanso() {
		return tiempoDescanso;
	}

	public void setTiempoDescanso(int tiempoDescanso) {
		this.tiempoDescanso = tiempoDescanso;
	}

	public List<Serie> getSeries() {
		return series;
	}

	public void setSeries(List<Serie> series) {
		this.series = series;
	}
	
    
	public boolean isActual() {
		return actual;
	}

	public void setActual(boolean actual) {
		this.actual = actual;
	}
	
	//Recupera desde Firestore todos los ejercicios de un workout dado.
	public static ArrayList<Ejercicio> fbObtenerEjerciciosWorkout(Workout workout) {
		Firestore conexion = null;

		ArrayList<Ejercicio> listaDeEjercicios = new ArrayList<Ejercicio>();

		try {
			conexion = Conexion.conectar();

			ApiFuture<QuerySnapshot> query = conexion.collection(collectionWorkouts).document(workout.getIdWorkout()).collection(collectionEjercicios).get();

			// Esperar el resultado de la consulta (bloqueante) y obtener los documentos
			QuerySnapshot querySnapshot = query.get();
			List<QueryDocumentSnapshot> ejercicios = querySnapshot.getDocuments();
			for (QueryDocumentSnapshot ejercicio : ejercicios) {

				// Construir objeto Ejercicio a partir de los datos del documento
				Ejercicio w = new Ejercicio();
				w.setIdEjercicio(ejercicio.getId());
				w.setNombre(ejercicio.getString(fieldNombre));
				w.setDescripcion(ejercicio.getString(fieldDescripcion));
				// El campo tiempo_descanso se almacena en Firestore como número (Long), lo convertimos a int
				w.setTiempoDescanso(ejercicio.getLong(fieldTiempoDescanso).intValue());
										
				// Añadir a la lista local
				listaDeEjercicios.add(w);
			}
			conexion.close();

		} catch (InterruptedException | ExecutionException e) {
			System.out.println("Error: Clase Workout, metodo mObtenerWorkout con arrayList");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// Captura genérica para no romper la aplicación; en producción
			// podría lanzarse o gestionarse de otra forma.
			e.printStackTrace();
		}
		// Por cada ejercicio obtenido, cargar sus series asociadas.
		for (Ejercicio ej : listaDeEjercicios) {
			ej.series = Serie.fbObtenerSeriesEjercicio(workout,ej);
		}

		return listaDeEjercicios;
	}
    

}

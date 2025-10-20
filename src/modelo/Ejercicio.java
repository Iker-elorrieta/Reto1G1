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
	
	/************** Atributos **************/
	private String idEjercicio;
    private String nombre;
    private String descripcion;
    private int tiempoDescanso; // segundos
    private List<Serie> series = new ArrayList<>();
    
	/************** Constructores **************/
    
    public Ejercicio() {
    	
    }

    public Ejercicio(String idEjercicio, String nombre, String descripcion, int tiempoDescanso) {
        this.idEjercicio = idEjercicio;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tiempoDescanso = tiempoDescanso;
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
    
	public static ArrayList<Ejercicio> mObtenerEjerciciosWorkout(Workout workout) {
		Firestore conexion = null;

		ArrayList<Ejercicio> listaDeEjercicios = new ArrayList<Ejercicio>();

		try {
			conexion = Conexion.conectar();

			ApiFuture<QuerySnapshot> query = conexion.collection("workouts").document(workout.getIdWorkout()).collection("ejercicios").get();

			QuerySnapshot querySnapshot = query.get();
			List<QueryDocumentSnapshot> ejercicios = querySnapshot.getDocuments();
			for (QueryDocumentSnapshot ejercicio : ejercicios) {

				Ejercicio w = new Ejercicio();
				w.setIdEjercicio(ejercicio.getId());
				w.setNombre(ejercicio.getString("nombre"));
				w.setDescripcion(ejercicio.getString("descripcion"));
				w.setTiempoDescanso(ejercicio.getLong("tiempo_descanso").intValue());
				
				
				
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return listaDeEjercicios;
	}
    

}

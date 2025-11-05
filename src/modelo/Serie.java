package modelo;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JLabel;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import conexion.Conexion;

public class Serie implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/************** Atributos **************/
	private String IdSerie;
    private String nombre;
    private String foto;
    private int tiempo; 
    private boolean actual;
    private JLabel cronometro;
    private static String collectionWorkouts = "workouts";
    private static String collectionEjercicios = "ejercicios";
    private static String collectionName = "series";
    private static String fieldNombre = "nombre";
    private static String fieldFoto = "foto";
    private static String fieldTiempo = "tiempo";
    

    
    /************** Constructores **************/
    public Serie() {
    	
    }

    public Serie(String pIdSerie, String pNombre, String pFoto, int pTiempo) {
        this.IdSerie = pIdSerie;
        this.nombre = pNombre;
        this.foto = pFoto;
        this.tiempo = pTiempo;
        this.actual = false;
    }

    /************** Getters y Setters **************/
	public String getIdSerie() {
		return IdSerie;
	}

	public void setIdSerie(String idSerie) {
		IdSerie = idSerie;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public int getTiempo() {
		return tiempo;
	}

	public void setTiempo(int tiempo) {
		this.tiempo = tiempo;
	}
	
	
	
	public boolean isActual() {
		return actual;
	}

	public void setActual(boolean actual) {
		this.actual = actual;
	}
	
	

	public JLabel getCronometro() {
		return cronometro;
	}

	public void setCronometro(JLabel cronometro) {
		this.cronometro = cronometro;
	}

	public static List<Serie> fbObtenerSeriesEjercicio(Workout workout,Ejercicio ejercicio) {
		Firestore conexion = null;

		ArrayList<Serie> listaSeries = new ArrayList<Serie>();

		try {
			conexion = Conexion.conectar();

			ApiFuture<QuerySnapshot> query = conexion.collection(collectionWorkouts).document(workout.getIdWorkout()).collection(collectionEjercicios).document(ejercicio.getIdEjercicio()).collection(collectionName).get();

			QuerySnapshot querySnapshot = query.get();
			List<QueryDocumentSnapshot> series = querySnapshot.getDocuments();
			for (QueryDocumentSnapshot serie : series) {

				Serie s = new Serie();
				s.setIdSerie(serie.getId());
				s.setNombre(serie.getString(fieldNombre));
				s.setFoto(serie.getString(fieldFoto));
				s.setTiempo(serie.getLong(fieldTiempo).intValue());		
				listaSeries.add(s);
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


		return listaSeries;
	}
    
    
}

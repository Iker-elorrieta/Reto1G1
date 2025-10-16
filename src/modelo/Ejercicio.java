package modelo;

import java.util.ArrayList;
import java.util.List;

public class Ejercicio {
	
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
    
    

}

package modelo;

import java.util.Date;

public class UsuWorkout {
	
	/************** Atributos **************/
	private String idHistorial;
    private String idWorkout;
    private int tiempoTotal; 
    private Date fecha;
    private double ejerciciosCompletados;
    
    /************** Constructores **************/
    
    public UsuWorkout() {
    	
    }

    public UsuWorkout(String idHistorial, String idWorkout, int tiempoTotal, Date fecha, double ejerciciosCompletados) {
        this.idHistorial = idHistorial;
        this.idWorkout = idWorkout;
        this.tiempoTotal = tiempoTotal;
        this.fecha = fecha;
        this.ejerciciosCompletados = ejerciciosCompletados;
    }
    
    /************** Getters y Setters **************/
	public String getIdHistorial() {
		return idHistorial;
	}

	public void setIdHistorial(String idHistorial) {
		this.idHistorial = idHistorial;
	}

	public String getIdWorkout() {
		return idWorkout;
	}

	public void setIdWorkout(String idWorkout) {
		this.idWorkout = idWorkout;
	}

	public int getTiempoTotal() {
		return tiempoTotal;
	}

	public void setTiempoTotal(int tiempoTotal) {
		this.tiempoTotal = tiempoTotal;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public double getEjerciciosCompletados() {
		return ejerciciosCompletados;
	}

	public void setEjerciciosCompletados(double ejerciciosCompletados) {
		this.ejerciciosCompletados = ejerciciosCompletados;
	}

}

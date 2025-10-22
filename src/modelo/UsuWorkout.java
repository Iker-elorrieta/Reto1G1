package modelo;

import java.io.Serializable;
import java.util.Date;

public class UsuWorkout implements Serializable {

    private Workout workout;  // Relación con el workout
    private int ejerciciosCompletados;  // Cuántos ejercicios completó el usuario
    private int tiempoTotal;  // Tiempo total en segundos
    private Date fecha;  // Fecha en la que se completó el workout

    // Constructor de la clase
    public UsuWorkout(Workout workout, int ejerciciosCompletados, int tiempoTotal, Date fecha) {
        this.workout = workout;
        this.ejerciciosCompletados = ejerciciosCompletados;
        this.tiempoTotal = tiempoTotal;
        this.fecha = fecha;
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
}

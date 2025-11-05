package Gym_Daji;

import controlador.ControladorInicio;
import vista.Inicio;

/**
 * Clase principal de la aplicación Gym_Daji.
 *
 * Se encarga únicamente de arrancar la aplicación creando la ventana
 * principal (vista) y su controlador asociado.
 */
public class Gym_Daji {

    public static void main(String[] args) {
        try {
            Inicio ventanaPrincipal = new Inicio();
            ventanaPrincipal.setVisible(true);
            new ControladorInicio(ventanaPrincipal);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
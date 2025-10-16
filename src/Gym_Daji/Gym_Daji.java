package Gym_Daji;

import controlador.ControladorInicio;
import vista.Inicio;

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

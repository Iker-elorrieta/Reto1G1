package vista;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.lang.model.element.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import modelo.Ejercicio;
import modelo.Usuario;
import modelo.Workout;
import java.io.File;

public class HiloBackup extends Thread{
	
	private List<Usuario> usuarios;
    private List<Workout> workouts;
    
    public HiloBackup(List<Usuario> usuarios, List<Workout> workouts) {
		this.usuarios = usuarios;
		this.workouts = workouts;
	}
	
	@Override
	public void run() {
		try {
			for (Usuario u : usuarios) {
				// Guardar datos binarios
				guardarBackup(u);
				
				// Guardar datos XML
				//generarXML(u);
			}
		} catch (Exception e) {
            e.printStackTrace();
        }

	}

	private void guardarBackup(Usuario u) throws Exception {
		 try (ObjectOutputStream oos = new ObjectOutputStream(
	                new FileOutputStream(u.getNombre() + "_backup.dat"))) {
	            oos.writeObject(u);
		 }
	}

	

}

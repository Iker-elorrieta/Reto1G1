package modelo.modeloBackup;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

import modelo.Ejercicio;
import modelo.Workout;

import java.io.File;
import java.util.List;

public class XMLHistorico {

    public static void generarXML(List<Workout> workouts, String rutaArchivo) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        Document doc = dBuilder.newDocument();
        Element rootElement = doc.createElement("workouts");
        doc.appendChild(rootElement);

        for (Workout w : workouts) {
            Element workoutElement = doc.createElement("workout");
            workoutElement.setAttribute("nombre", w.getNombre());

            for (Ejercicio e : w.getEjercicios()) {
                Element ejercicioElement = doc.createElement("ejercicio");
                ejercicioElement.setAttribute("nombre", e.getNombre());
                ejercicioElement.setAttribute("descripcion", e.getDescripcion());
                ejercicioElement.setAttribute("tiempo de descanso", String.valueOf(e.getTiempoDescanso()));
                ejercicioElement.setAttribute("series", String.valueOf(e.getSeries()));
                
                workoutElement.appendChild(ejercicioElement);
            }
            rootElement.appendChild(workoutElement);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(rutaArchivo));
        transformer.transform(source, result);

    }
}

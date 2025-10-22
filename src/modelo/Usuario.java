package modelo;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.ExecutionException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;

import conexion.Conexion;

public class Usuario implements Serializable{

	/************** Atributos **************/

	private String IdUsuario;
	private String nombre;
	private String apellidos;
	private String email;
	private String contrasena;
	private Date fec_nac;
	private int nivel;
	private String tipo;
	private List<UsuWorkout> workouts = new ArrayList<>();

	private static String collectionName = "usuarios";
	private static String fieldNombre = "nombre";
	private static String fieldApellidos = "apellidos";
	private static String fieldEmail = "email";
	private static String fieldContrasena = "contrasena";
	private static String fieldFecNac = "fec_nac";
	private static String fieldNivel = "nivel";
	private static String fieldTipo = "tipo";

	/************** Constructores **************/

	public Usuario() {

	}

	public Usuario(String pNombre, String pApellidos, String pEmail, String pContrasena, Date pFec_nac, int pNivel,
			String pTipo) {
		this.nombre = pNombre;
		this.apellidos = pApellidos;
		this.email = pEmail;
		this.contrasena = pContrasena;
		this.fec_nac = pFec_nac;
		this.nivel = pNivel;
		this.tipo = pTipo;
	}

	/************** Getters y Setters **************/

	public String getIdUsuario() {
		return IdUsuario;
	}

	public void setIdUsuario(String IdUsuario) {
		this.IdUsuario = IdUsuario;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public Date getFec_nac() {
		return fec_nac;
	}

	public void setFec_nac(Date fec_nac) {
		this.fec_nac = fec_nac;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public List<UsuWorkout> getWorkouts() {
		return workouts;
	}

	public void setWorkouts(List<UsuWorkout> workouts) {
		this.workouts = workouts;
	}

	/************** Metodo CRUD **************/

	// ********** READ **********
	public Usuario mObtenerUsuario(int idUsu) {
		Firestore conexion = null;

		try {
			conexion = Conexion.conectar();

			DocumentSnapshot usuario = conexion.collection(collectionName).document(String.valueOf(idUsu)).get().get();

			setIdUsuario(usuario.getId());
			setNombre(usuario.getString(fieldNombre));
			setApellidos(usuario.getString(fieldApellidos));
			setEmail(usuario.getString(fieldEmail));
			setContrasena(usuario.getString(fieldContrasena));
			setFec_nac(usuario.getDate(fieldFecNac));
			setNivel(usuario.getLong(fieldNivel).intValue());
			setTipo(usuario.getString(fieldTipo));

		} catch (InterruptedException | ExecutionException e) {
			System.out.println("Error: Clase Usuario, metodo mObtenerUsuario");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return this;
	}

	public boolean mExisteUsuario(String email) throws Exception {
		Firestore conexion = Conexion.conectar();
		var query = conexion.collection(collectionName).whereEqualTo(fieldEmail, email).get().get();
		if (!query.isEmpty()) {
			conexion.close();
			return true;
		} else {
			conexion.close();
			return false;
		}
	}

	// ********** CREATE **********
	public void mAnadirUsuario() throws Exception {

		Firestore conexion = Conexion.conectar();

		// Obtener todos los IDs existentes
		var documentos = conexion.collection(collectionName).get().get();

		int maxId = 0;
		for (var doc : documentos) {
			try {
				int idNum = Integer.parseInt(doc.getId());
				if (idNum > maxId) {
					maxId = idNum;
				}
			} catch (NumberFormatException e) {
				// Ignorar si algún documento tiene un ID no numérico
			}
		}

		int nuevoId = maxId + 1;
		Map<String, Object> nuevoUsuario = new HashMap<>();
		nuevoUsuario.put(fieldNombre, nombre);
		nuevoUsuario.put(fieldApellidos, apellidos);
		nuevoUsuario.put(fieldEmail, email);
		nuevoUsuario.put(fieldContrasena, contrasena);
		nuevoUsuario.put(fieldFecNac, fec_nac);
		nuevoUsuario.put(fieldNivel, nivel);
		nuevoUsuario.put(fieldTipo, tipo);

		DocumentReference UsuarioRef = conexion.collection(collectionName).document(String.valueOf(nuevoId));
		UsuarioRef.set(nuevoUsuario).get();

		setIdUsuario(String.valueOf(nuevoId));

		conexion.close();
	}

	// ********** UPDATE **********
	public void mActualizarUsuario() throws Exception {
		Firestore conexion = null;

		conexion = Conexion.conectar();
		Map<String, Object> usuarioActualizado = new HashMap<>();
		usuarioActualizado.put(fieldNombre, nombre);
		usuarioActualizado.put(fieldApellidos, apellidos);
		usuarioActualizado.put(fieldEmail, email);
		usuarioActualizado.put(fieldContrasena, contrasena);
		usuarioActualizado.put(fieldFecNac, fec_nac);
		usuarioActualizado.put(fieldNivel, nivel);
		usuarioActualizado.put(fieldTipo, tipo);

		DocumentReference usuarioRef = conexion.collection(collectionName).document(IdUsuario);
		usuarioRef.update(usuarioActualizado);
		conexion.close();

	}

	/************** LOGIN / VALIDAR USUARIO **************/
	public boolean validarLogin(String email, String contrasena) throws Exception {
		Firestore conexion = Conexion.conectar();
		var query = conexion.collection(collectionName).whereEqualTo(fieldEmail, email).get().get();
		if (!query.isEmpty()) {
			var doc = query.getDocuments().get(0);
			String passBD = doc.getString(fieldContrasena);
			if (passBD.equals(contrasena)) {
				setIdUsuario(doc.getId());
				setNombre(doc.getString(fieldNombre));
				setApellidos(doc.getString(fieldApellidos));
				setEmail(doc.getString(fieldEmail));
				setContrasena(passBD);
				setFec_nac(doc.getDate(fieldFecNac));
				setNivel(doc.getLong(fieldNivel).intValue());
				setTipo(doc.getString(fieldTipo));
				conexion.close();
				return true;
			}
		}
		conexion.close();

		return false;
	}
	
	public void mCargarHistorialWorkouts() throws Exception {
		this.workouts = UsuWorkout.mCargarHistorialWorkouts(this);
	}


}

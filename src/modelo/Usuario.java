package modelo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.concurrent.ExecutionException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;

import conexion.Conexion;
import java.util.stream.Collectors;

public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;

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
		this.workouts = new ArrayList<>();
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

	public ArrayList<Usuario> datObtenerTodos() {
		File file = new File("backups/usuarios.dat");
		ArrayList<Usuario> lista = new ArrayList<>();
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
			Object obj = ois.readObject();
			if (obj instanceof List<?>) {
				for (Object o : (List<?>) obj) {
					if (o instanceof Usuario) {
						lista.add((Usuario) o);
					}
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Error leyendo backups/usuarios.dat");
			e.printStackTrace();
		}
		return lista;
	}

	public Usuario mObtenerUsuario(int idUsu, boolean conexionInternet) {
		if (conexionInternet) {
			return fbObtenerUsuario(idUsu);
		} else {
			ArrayList<Usuario> usuarios = datObtenerTodos();
			for (Usuario u : usuarios) {
				if (u.getIdUsuario().equals(idUsu + "")) {
					return u;
				}
			}
			return null;
		}
	}

	// ********** READ **********
	public Usuario fbObtenerUsuario(int idUsu) {
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

	public boolean mExisteUsuario(String email, boolean conexionInternet) throws Exception {
		if (conexionInternet) {
			return fbExisteUsuario(email);
		} else {
			ArrayList<Usuario> usuarios = datObtenerTodos();
			for (Usuario u : usuarios) {
				if (u.getEmail().equals(email)) {
					return true;
				}
			}
			return false;
		}
	}

	public boolean fbExisteUsuario(String email) throws Exception {
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

	public void mAnadirUsuario(boolean conexionInternet) throws Exception {
		if (conexionInternet) {
			fbAnadirUsuario();
		} else {
			ArrayList<Usuario> usuarios = datObtenerTodos();
			int maxId = 0;
			for (Usuario u : usuarios) {
				try {
					int idNum = Integer.parseInt(u.getIdUsuario());
					if (idNum > maxId) {
						maxId = idNum;
					}
				} catch (NumberFormatException e) {
					// Ignorar si algún usuario tiene un ID no numérico
				}
			}
			int nuevoId = maxId + 1;
			setIdUsuario(String.valueOf(nuevoId));
			usuarios.add(this);
			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("backups/usuarios.dat"))) {
				oos.writeObject(usuarios);
			}
		}
	}

	// ********** CREATE **********
	public void fbAnadirUsuario() throws Exception {

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
	public void mActualizarUsuario(boolean conexion) throws Exception {
		if (conexion) {
			fbActualizarUsuario();
		} else {
			ArrayList<Usuario> usuarios = datObtenerTodos();
			for (int i = 0; i < usuarios.size(); i++) {
				if (usuarios.get(i).getIdUsuario().equals(this.getIdUsuario())) {
					usuarios.set(i, this);
					break;
				}
			}
			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("backups/usuarios.dat"))) {
				oos.writeObject(usuarios);
			} catch (IOException e) {
				System.out.println("Error escribiendo backups/usuarios.dat");
				e.printStackTrace();
			}
		}
	}

	public void fbActualizarUsuario() throws Exception {
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
	
	public boolean validarLogin(String email, String contrasena, boolean conexionInternet) throws Exception {
		if (conexionInternet) {
			return fbvalidarLogin(email, contrasena);
		} else {
			ArrayList<Usuario> usuarios = datObtenerTodos();
			for (Usuario u : usuarios) {
				if (u.getEmail().equals(email) && u.getContrasena().equals(contrasena)) {
					setIdUsuario(u.getIdUsuario());
					setNombre(u.getNombre());
					setApellidos(u.getApellidos());
					setEmail(u.getEmail());
					setContrasena(u.getContrasena());
					setFec_nac(u.getFec_nac());
					setNivel(u.getNivel());
					setTipo(u.getTipo());
					return true;
				}
			}
			return false;
		}
	}
	public boolean fbvalidarLogin(String email, String contrasena) throws Exception {
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

	public void mCargarHistorialWorkouts(boolean conexion) throws Exception {
		this.workouts = UsuWorkout.mCargarHistorialWorkouts(this,conexion);
		this.workouts.sort(Comparator.comparing(UsuWorkout::getFecha).reversed());
		
	}

	public static List<Usuario> mObtenerTodosUsuarios(boolean conexionInternet) {
		if (conexionInternet) {
			return fbObtenerTodosUsuarios(true);
		} else {
			ArrayList<Usuario> usuarios = new ArrayList<>();
			File file = new File("backups/usuarios.dat");
			try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
				Object obj = ois.readObject();
				if (obj instanceof List<?>) {
					for (Object o : (List<?>) obj) {
						if (o instanceof Usuario) {
							usuarios.add((Usuario) o);
						}
					}
				}
			} catch (IOException | ClassNotFoundException e) {
				System.out.println("Error leyendo backups/usuarios.dat");
			 e.printStackTrace();
			}
			return usuarios;
		}
	}
	public static List<Usuario> fbObtenerTodosUsuarios(boolean conexionInternet) {
		List<Usuario> listaUsuarios = new ArrayList<>();
		Firestore conexion = null;
		try {
			conexion = Conexion.conectar();

			var query = conexion.collection(collectionName).get().get();
			var documentos = query.getDocuments();

			for (var doc : documentos) {
				Usuario u = new Usuario();
				u.setIdUsuario(doc.getId());
				u.setNombre(doc.getString(fieldNombre));
				u.setApellidos(doc.getString(fieldApellidos));
				u.setEmail(doc.getString(fieldEmail));
				u.setContrasena(doc.getString(fieldContrasena));
				u.setFec_nac(doc.getDate(fieldFecNac));
				u.setNivel(doc.getLong(fieldNivel).intValue());
				u.setTipo(doc.getString(fieldTipo));
				listaUsuarios.add(u);
			}

			conexion.close();
		} catch (InterruptedException | ExecutionException e) {
			System.out.println("Error: Clase Usuario, metodo mObtenerTodosUsuarios");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (Usuario u : listaUsuarios) {
			try {
				u.mCargarHistorialWorkouts(conexionInternet);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return listaUsuarios;
	}

	public boolean mEvaluarNivel(boolean conexion) {
		try {
			ArrayList<Workout> todos = Workout.mObtenerTodosWorkouts(conexion);
			if (todos == null || todos.isEmpty())
				return false;
			int nivelActual = this.nivel;
			List<Workout> nivelWorkouts = todos.stream().filter(w -> w.getNivel() == nivelActual)
					.collect(Collectors.toList());
			if (nivelWorkouts.isEmpty())
				return false; // no hay workouts para este nivel

			// Asegurarse de tener el historial
			try {
				this.mCargarHistorialWorkouts(conexion);
			} catch (Exception ignore) {
			}

			// Para cada workout del nivel, comprobar si existe en el historial del usuario
			// con el 100% de ejercicios completados
			for (Workout w : nivelWorkouts) {
				boolean completadoAl100 = false;
				if (this.workouts != null) {
					for (UsuWorkout uw : this.workouts) {
						if (uw.getWorkout() != null && w.getIdWorkout() != null
								&& w.getIdWorkout().equals(uw.getWorkout().getIdWorkout())) {
							int pct = w.calcularPorcentajeCompletado(uw);
							if (pct >= 100) {
								completadoAl100 = true;
								break;
							}
						}
					}
				}
				if (!completadoAl100) {
					return false; // falta al menos un workout del nivel completado al 100%
				}
			}

			// Si llegamos aquí, el usuario completó todos los workouts del nivel al 100%
			if (this.nivel < 5) {
				this.nivel = this.nivel + 1;
				try {
					this.mActualizarUsuario(conexion);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}

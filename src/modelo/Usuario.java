package modelo;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import conexion.Conexion;

public class Usuario {
	
	/************** Atributos **************/
	
	private String IdUsuario;
	private String nombre;
	private String apellidos;
	private String email;
	private String contrasena;
	private Date fec_nac;
	private int nivel;
	private String tipo;
	
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

	public Usuario(String pNombre, String pApellidos, String pEmail, String pContrasena, Date pFec_nac, int pNivel, String pTipo) {
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
	
	public ArrayList<Usuario> mObtenerUsuario() {
		Firestore conexion = null;

		ArrayList<Usuario> listaDeUsarios = new ArrayList<Usuario>();

		try {
			conexion = Conexion.conectar();

			ApiFuture<QuerySnapshot> query = conexion.collection(collectionName).get();

			QuerySnapshot querySnapshot = query.get();
			List<QueryDocumentSnapshot> usuarios = querySnapshot.getDocuments();
			for (QueryDocumentSnapshot usuario : usuarios) {

				Usuario usu = new Usuario();
				usu.setIdUsuario(usuario.getId());				
				usu.setNombre(usuario.getString(fieldNombre));
				usu.setApellidos(usuario.getString(fieldApellidos));
				usu.setEmail(usuario.getString(fieldEmail));
				usu.setContrasena(usuario.getString(fieldContrasena));
				usu.setFec_nac(usuario.getDate(fieldFecNac));
				usu.setNivel(usuario.getLong(fieldNivel).intValue());
				usu.setTipo(usuario.getString(fieldTipo));
				
				
				listaDeUsarios.add(usu);
			}
			conexion.close();

		} catch (InterruptedException | ExecutionException e) {
			System.out.println("Error: Clase Usuario, metodo mObtenerUsuario con arrayList");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return listaDeUsarios;
	}	
	
	
	// ********** CREATE **********
	public boolean mAnadirUsuario() {
		Firestore conexion = null;

		try {
			conexion = Conexion.conectar();
			Map<String, Object> nuevoUsuario = new HashMap<>();
			nuevoUsuario.put(fieldNombre, nombre);
			nuevoUsuario.put(fieldApellidos, apellidos);
			nuevoUsuario.put(fieldEmail, email);
			nuevoUsuario.put(fieldContrasena, contrasena);
			nuevoUsuario.put(fieldFecNac, fec_nac);
			nuevoUsuario.put(fieldNivel, nivel);
			nuevoUsuario.put(fieldTipo, tipo);
			

			DocumentReference UsuarioRef = conexion.collection(collectionName).document();
			UsuarioRef.set(nuevoUsuario);
			conexion.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	
	// ********** UPDATE **********
	public boolean mActualizarUsuario() {
	    Firestore conexion = null;

	    try {
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
	        return true;

	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return false;
	}
	

	// ********** DELETE **********
	public boolean mEliminarUsuario() {
	    Firestore conexion = null;

	    try {
	        conexion = Conexion.conectar();
	        DocumentReference usuarioRef = conexion.collection(collectionName).document(IdUsuario);
	        usuarioRef.delete();
	        conexion.close();
	        return true;

	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return false;
	}
	
}

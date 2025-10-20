package controlador;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.toedter.calendar.JDateChooser;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import modelo.Ejercicio;
import modelo.Usuario;
import modelo.Workout;
import modelo.modeloBackup.BackupManager;
import modelo.modeloBackup.XMLHistorico;
import vista.Inicio;
import vista.Workouts;

public class ControladorInicio extends MouseAdapter implements ActionListener, ListSelectionListener {

	private Inicio vistaInicio;
	private Workouts vistaWorkouts;

	private ArrayList<Workout> workouts;
	private Usuario usuario;

	// Constructor
	public ControladorInicio(Inicio vistaInicio) {
		this.vistaInicio = vistaInicio;
		vistaWorkouts = new Workouts();
		usuario = new Usuario();
		inicializarControlador();
	}

	private void inicializarControlador() {

		// Inicio
		vistaInicio.getContentPane().addMouseListener(this);
		vistaInicio.getPanelLogoGrande().addMouseListener(this);
		// Panel Login
		vistaInicio.getPanelLogin().getBtnIniciarSesion().setActionCommand("INICIAR_SESION");
		vistaInicio.getPanelLogin().getBtnIniciarSesion().addActionListener(this);
		vistaInicio.getPanelLogin().getLblRegistrar().addMouseListener(this);

		// Panel Registro
		vistaInicio.getPanelRegistro().getBtnRegistrar().setActionCommand("REGISTRAR");
		vistaInicio.getPanelRegistro().getBtnRegistrar().addActionListener(this);

		vistaInicio.getPanelRegistro().getBtnAtras().setActionCommand("ATRAS");
		vistaInicio.getPanelRegistro().getBtnAtras().addActionListener(this);

		// Workouts
		vistaWorkouts.getBtnDesconectar().setActionCommand("DESCONECTAR");
		vistaWorkouts.getBtnDesconectar().addActionListener(this);

		vistaWorkouts.getTableWorkouts().getSelectionModel().addListSelectionListener(this);
		vistaWorkouts.getComboBox().setActionCommand("FILTRAR_NIVEL");
		vistaWorkouts.getComboBox().addActionListener(this);
		vistaWorkouts.getTableWorkouts().addMouseListener(this);
				
		
		vistaWorkouts.getBtnEditarPerfil().setActionCommand("EDITAR_PERFIL");
		vistaWorkouts.getBtnEditarPerfil().addActionListener(this);

		vVaciarLogin();
		vVaciarRegistro();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();

		switch (cmd) {
		case "MOSTRAR_LOGIN":
			mMostrarPanelLogin();
			break;
		case "INICIAR_SESION":
			mIniciarSesion();
			break;
		case "REGISTRAR":
			mRegistro();
			break;

		case "ATRAS":
			if(vistaInicio.getPanelRegistro().isModoEdicion()) {
				vistaInicio.setVisible(false);
			}else {
				vVaciarLogin();
				vistaInicio.getPanelRegistro().setVisible(false);
				vistaInicio.getPanelLogin().setVisible(true);
			}
			break;
		case "MOSTRAR_REGISTRO":
			vVaciarRegistro();
			vistaInicio.getPanelLogin().setVisible(false);
			vistaInicio.getPanelRegistro().setVisible(true);
			break;
		case "DESCONECTAR":
			mDesconectar();
			break;
		case "FILTRAR_NIVEL":
			mFiltrarNiveles();
			break;
		case "WORKOUT_SELECCIONADO":
			mCargarEjercicios(mWorkoutSeleccionado());
			break;
		case "EDITAR_PERFIL":
		    mEditarPerfil();
		    break;
		default:
			break;
		}

	}

	@Override
	public void valueChanged(ListSelectionEvent event) {
		// Mover un ListSelectionListener a actionPerformed
		if (event.getSource() == vistaWorkouts.getTableWorkouts().getSelectionModel()) {
			actionPerformed(new ActionEvent(vistaWorkouts.getTableWorkouts(), ActionEvent.ACTION_PERFORMED,
					"WORKOUT_SELECCIONADO"));
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == vistaInicio.getPanelLogin().getLblRegistrar()) {
			actionPerformed(new ActionEvent(vistaInicio.getPanelLogin().getLblRegistrar(), ActionEvent.ACTION_PERFORMED,
					"MOSTRAR_REGISTRO"));
		} else if (e.getSource() == vistaWorkouts.getTableWorkouts()) {
			mAbrirVideo(e);
		} else if (e.getSource() == vistaInicio.getPanelLogoGrande() || e.getSource() == vistaInicio.getContentPane()) {
			mMostrarPanelLogin();
		}
	}

	public void mMostrarPanelLogin() {
		if (vistaInicio.getPanelLogoGrande().isVisible()) {
			vistaInicio.getPanelLogin().setVisible(true);
			vistaInicio.getPanelRegistro().setVisible(false);
			vistaInicio.getPanelLogoGrande().setVisible(false);
			vistaInicio.getPanelLogoPequeno().setVisible(true);
		}
	}

	public void mIniciarSesion() {
		JTextField txtEmail = vistaInicio.getPanelLogin().getTextFieldEmail();
		JTextField txtContasena = vistaInicio.getPanelLogin().getTextFieldContrasena();

		String email = txtEmail.getText().trim();
		String contrasena = txtContasena.getText().trim();

		boolean emailPlaceholder = (boolean) txtEmail.getClientProperty("placeholder");
		boolean contrasenaPlaceholder = (boolean) txtContasena.getClientProperty("placeholder");

		if (email.isEmpty() || emailPlaceholder) {
			vistaInicio.placeholder("Campo obligatorio", Color.RED, txtEmail);
		}
		if (contrasena.isEmpty() || contrasenaPlaceholder) {
			vistaInicio.placeholder("Campo obligatorio", Color.RED, txtContasena);
		}
		if (!email.isEmpty() && !emailPlaceholder && !emailValido(email)) {
			vistaInicio.placeholder("Correo electrónico no válido", Color.RED, txtEmail);
		}
		if (email.isEmpty() || contrasena.isEmpty() || emailPlaceholder || contrasenaPlaceholder
				|| !emailValido(email)) {
			return;
		}

		// si esta bien
		try {
			if (usuario.validarLogin(email, contrasena)) {
				mCargarWorkouts();
				vistaInicio.setVisible(false);
				vistaWorkouts.setVisible(true);
				
				try {
			        // Crear lista con el usuario actual
			        List<Usuario> listaUsuarios = new ArrayList<>();
			        listaUsuarios.add(usuario);

			        // Generar backups y XML
			        BackupManager.guardarBackupUsuarios(listaUsuarios, "usuarios.dat");
			        BackupManager.guardarBackupWorkouts(workouts, "workouts.dat");
			        XMLHistorico.generarXML(workouts, "historico.xml");

			        // DANIII ESTO ESTA AQUI POR EL MOMENTO pa comprobar que los archivos se han creado
			        boolean usuariosOk = false;
			        boolean workoutsOk = false;
			        boolean xmlOk = false;

			        usuariosOk = new java.io.File("usuarios.dat").exists();
			        workoutsOk = new java.io.File("workouts.dat").exists();
			        xmlOk = new java.io.File("historico.xml").exists();

			        if (usuariosOk && workoutsOk && xmlOk) {
			            JOptionPane.showMessageDialog(vistaWorkouts, "Backups y XML generados correctamente.");
			        } else {
			            JOptionPane.showMessageDialog(vistaWorkouts, "Error al generar backups.");
			        }
			    } catch (Exception ex) {
			        System.out.println("Error al generar backups: " + ex.getMessage());
			        ex.printStackTrace();
			    }
			} else {
				vistaInicio.getPanelLogin().getLblError().setForeground(Color.RED);
				vistaInicio.getPanelLogin().getLblError().setText("Correo electrónico o contraseña incorrectos");
			}
		} catch (Exception e) {
			vistaInicio.getPanelLogin().getLblError().setForeground(Color.RED);
			vistaInicio.getPanelLogin().getLblError().setText("Error al conectar con la base de datos");
		}

	}

	// java
	public void mRegistro() {
		JTextField txtNombre = vistaInicio.getPanelRegistro().getTxtNombre();
		JTextField txtApellidos = vistaInicio.getPanelRegistro().getTxtApellidos();
		JTextField txtEmail = vistaInicio.getPanelRegistro().getTxtEmail();
		JTextField txtContrasena = vistaInicio.getPanelRegistro().getTxtContrasena();
		JDateChooser dateChooser = vistaInicio.getPanelRegistro().getDateChooser();
		JTextField dateField = (JTextField) dateChooser.getDateEditor().getUiComponent();

		String nombre = txtNombre.getText().trim();
		String apellidos = txtApellidos.getText().trim();
		String email = txtEmail.getText().trim();
		String password = txtContrasena.getText().trim();
		Date fechaNacimiento = dateChooser.getDate();

		boolean nombrePH = Boolean.TRUE.equals(txtNombre.getClientProperty("placeholder"));
		boolean apellidosPH = Boolean.TRUE.equals(txtApellidos.getClientProperty("placeholder"));
		boolean emailPH = Boolean.TRUE.equals(txtEmail.getClientProperty("placeholder"));
		boolean passwordPH = Boolean.TRUE.equals(txtContrasena.getClientProperty("placeholder"));
		boolean fechaPH = Boolean.TRUE.equals(dateField.getClientProperty("placeholder"));

		if (nombre.isEmpty() || nombrePH) {
			vistaInicio.placeholder("Campo obligatorio", Color.RED, txtNombre);
		}
		if (apellidos.isEmpty() || apellidosPH) {
			vistaInicio.placeholder("Campo obligatorio", Color.RED, txtApellidos);
		}
		if (email.isEmpty() || emailPH) {
			vistaInicio.placeholder("Campo obligatorio", Color.RED, txtEmail);
		}
		if (password.isEmpty() || passwordPH) {
			vistaInicio.placeholder("Campo obligatorio", Color.RED, txtContrasena);
		}
		if (fechaNacimiento == null || fechaPH) {
			vistaInicio.placeholder("Campo obligatorio", Color.RED, dateField);
		}
		if (!email.isEmpty() && !emailPH && !emailValido(email)) {
			vistaInicio.placeholder("Correo electrónico no válido", Color.RED, txtEmail);
		}

		if (nombre.isEmpty() || apellidos.isEmpty() || email.isEmpty() || password.isEmpty() || fechaNacimiento == null
				|| nombrePH || apellidosPH || emailPH || passwordPH || fechaPH || !emailValido(email)) {
			return;
		}
		try {
			if (vistaInicio.getPanelRegistro().isModoEdicion()) {
			    usuario.setNombre(nombre);
			    usuario.setApellidos(apellidos);
			    usuario.setContrasena(password);
			    usuario.setFec_nac(fechaNacimiento);
			    usuario.mActualizarUsuario(); // suponiendo que tienes este método

			    vistaInicio.getPanelRegistro().setVisible(false);
			    vistaWorkouts.setVisible(true);
			    return;
			}
			if (usuario.mExisteUsuario(email)) {
				vistaInicio.getPanelRegistro().getLblError().setForeground(Color.RED);
				vistaInicio.getPanelRegistro().getLblError().setText("El correo electrónico ya está registrado");
				return;
			}

			Usuario nuevoUsuario = new Usuario(nombre, apellidos, email, password, fechaNacimiento, 1, "cliente");

			nuevoUsuario.mAnadirUsuario();
			vistaInicio.getPanelRegistro().setVisible(false);
			vistaInicio.getPanelLogin().setVisible(true);
			vVaciarLogin();
			vistaInicio.getPanelLogin().getLblError().setForeground(Color.BLACK);
			vistaInicio.getPanelLogin().getLblError().setText("Registro exitoso. Por favor, inicia sesión.");
		} catch (Exception e) {
			vistaInicio.getPanelRegistro().getLblError().setForeground(Color.RED);
			vistaInicio.getPanelRegistro().getLblError().setText("Error al registrar el usuario");
			e.printStackTrace();
		}

	}

	public void mCargarWorkouts() {
		vistaWorkouts.getModeloWorkouts().setRowCount(0);
		workouts = Workout.mObtenerWorkout(usuario.getNivel());
		mRellenarTablaWorkouts(0);

		vistaWorkouts.getModeloComboBox().removeAllElements();
		vistaWorkouts.getModeloComboBox().addElement("Todos los niveles");
		for (int i = 1; i <= usuario.getNivel(); i++) {
			vistaWorkouts.getModeloComboBox().addElement("Nivel " + i);
		}
	}

	private void mRellenarTablaWorkouts(int filtroNivel) {
		if (workouts == null)
			return;
		vistaWorkouts.getModeloWorkouts().setRowCount(0);
		for (int i = 0; i < workouts.size(); i++) {
			Workout w = workouts.get(i);
			if (filtroNivel == 0 || w.getNivel() == filtroNivel) {
				String[] fila = new String[6];
				fila[0] = w.getIdWorkout();
				fila[1] = w.getNivel() + "";
				fila[2] = w.getNombre();
				fila[3] = w.getDescripcion();
				fila[4] = w.getVideo();
				fila[5] = "Ver vídeo";
				vistaWorkouts.getModeloWorkouts().addRow(fila);

			}
		}
		mMostrarOcultarEjercicios(false);
	}

	public void mFiltrarNiveles() {
		String sel = (String) vistaWorkouts.getComboBox().getSelectedItem();
		int filtro = 0;
		if (sel != null && sel.startsWith("Nivel ")) {
			try {
				// Es "Nivel X"
				filtro = Integer.parseInt(sel.substring(6));
			} catch (NumberFormatException ex) {
				// Es "Todos los niveles"
				filtro = 0;
			}
		}
		mRellenarTablaWorkouts(filtro);
	}

	public void mAbrirVideo(MouseEvent e) {
		int colView = vistaWorkouts.getTableWorkouts().columnAtPoint(e.getPoint());
		int rowView = vistaWorkouts.getTableWorkouts().rowAtPoint(e.getPoint());
		if (colView == -1 || rowView == -1)
			return;

		int colModel = vistaWorkouts.getTableWorkouts().convertColumnIndexToModel(colView);
		if (colModel == 5) {
			int rowModel = vistaWorkouts.getTableWorkouts().convertRowIndexToModel(rowView);
			Object urlCell = vistaWorkouts.getModeloWorkouts().getValueAt(rowModel, 4);
			if (urlCell == null)
				return;
			String url = urlCell.toString().trim();
			if (url.isEmpty())
				return;

			try {
				Desktop.getDesktop().browse(new URI(url));

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public void mCargarEjercicios(Workout workout) {
		if (workout == null)
			return;
		vistaWorkouts.getModeloEjercicios().setRowCount(0);
		ArrayList<Ejercicio> ejerciciosArray = Ejercicio.mObtenerEjerciciosWorkout(workout);
		workout.setEjercicios(ejerciciosArray);
		for (int i = 0; i < ejerciciosArray.size(); i++) {
			String[] fila = new String[6];
			fila[0] = ejerciciosArray.get(i).getIdEjercicio();
			fila[1] = ejerciciosArray.get(i).getNombre();
			fila[2] = ejerciciosArray.get(i).getDescripcion();
			fila[3] = String.format("%02d:%02d", ejerciciosArray.get(i).getTiempoDescanso() / 60,
					ejerciciosArray.get(i).getTiempoDescanso() % 60);
			vistaWorkouts.getModeloEjercicios().addRow(fila);
		}
		mMostrarOcultarEjercicios(true);
	}

	public void mMostrarOcultarEjercicios(boolean mostrar) {
		vistaWorkouts.getTableEjercicios().setVisible(mostrar);
		vistaWorkouts.getLblEventos().setVisible(mostrar);
		vistaWorkouts.getScrollPaneEventos().setVisible(mostrar);
		vistaWorkouts.getBtnEmpezarWorkout().setVisible(mostrar);
	}

	public Workout mWorkoutSeleccionado() {
		if (vistaWorkouts.getTableWorkouts().getSelectedRow() != -1) {
			String IDSeleccionado = vistaWorkouts.getTableWorkouts()
					.getValueAt(vistaWorkouts.getTableWorkouts().getSelectedRow(), 0).toString();
			for (int i = 0; i < workouts.size(); i++) {
				if (workouts.get(i).getIdWorkout().equals(IDSeleccionado)) {
					return workouts.get(i);
				}
			}
		}
		return null;
	}

	public void mDesconectar() {
		vistaWorkouts.setVisible(false);
		vistaInicio.setVisible(true);
		vistaInicio.getPanelLogin().setVisible(true);
		vistaInicio.getPanelRegistro().setVisible(false);
		vVaciarLogin();
		usuario = new Usuario();
	}

	private boolean emailValido(String email) {
		if (email == null)
			return false;
		if (email.contains(" "))
			return false;
		if (email.indexOf('@') != email.lastIndexOf('@'))
			return false;

		int at = email.indexOf('@');
		if (at <= 0 || at == email.length() - 1)
			return false;

		String local = email.substring(0, at);
		String domain = email.substring(at + 1);

		if (local.length() == 0 || domain.length() == 0)
			return false;
		if (local.startsWith(".") || local.endsWith("."))
			return false;
		if (domain.startsWith(".") || domain.endsWith("."))
			return false;
		if (email.contains(".."))
			return false;
		if (!domain.contains("."))
			return false;

		for (char c : email.toCharArray()) {
			if (c <= 32 || c == 127)
				return false;
		}

		return true;
	}
	public void mEditarPerfil() {
	    vistaInicio.getPanelRegistro().setModoEdicion(true, usuario);
	    vistaInicio.getPanelLogin().setVisible(false);
	    vistaInicio.getPanelRegistro().setVisible(true);
	    vistaInicio.setVisible(true);
	}
	public void vVaciarLogin() {
		JTextField txtEmail = vistaInicio.getPanelLogin().getTextFieldEmail();
		JTextField txtContrasena = vistaInicio.getPanelLogin().getTextFieldContrasena();

		txtEmail.setText("");
		txtContrasena.setText("");

		vistaInicio.placeholder("Introduce tu correo electrónico", Color.GRAY, txtEmail);
		vistaInicio.placeholder("********", Color.GRAY, txtContrasena);
		vistaInicio.getPanelLogin().getLblError().setText("");

	}

	public void vVaciarRegistro() {
		JTextField txtNombre = vistaInicio.getPanelRegistro().getTxtNombre();
		JTextField txtApellidos = vistaInicio.getPanelRegistro().getTxtApellidos();
		JTextField txtEmail = vistaInicio.getPanelRegistro().getTxtEmail();
		JTextField txtContrasena = vistaInicio.getPanelRegistro().getTxtContrasena();
		JDateChooser dateChooser = vistaInicio.getPanelRegistro().getDateChooser();
		JTextField dateField = (JTextField) dateChooser.getDateEditor().getUiComponent();

		txtNombre.setText("");
		txtApellidos.setText("");
		txtEmail.setText("");
		txtContrasena.setText("");
		dateChooser.setDate(null);

		vistaInicio.placeholder("Introduce tu nombre", Color.GRAY, txtNombre);
		vistaInicio.placeholder("Introduce tus apellidos", Color.GRAY, txtApellidos);
		vistaInicio.placeholder("Introduce tu correo electrónico", Color.GRAY, txtEmail);
		vistaInicio.placeholder("********", Color.GRAY, txtContrasena);
		vistaInicio.placeholder("Selecciona tu fecha de nacimiento", Color.GRAY, dateField);

		vistaInicio.getPanelRegistro().getLblError().setText("");

	}

}

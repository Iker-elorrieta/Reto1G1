package controlador;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.text.SimpleDateFormat;
import java.io.IOException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import Backups.HiloBackup;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import modelo.Ejercicio;
import modelo.Usuario;
import modelo.Workout;
import modelo.UsuWorkout;
import vista.HistoricoWorkouts;
import vista.Inicio;
import vista.PantallaEjercicio;
import vista.Workouts;

/**
 * Controlador principal de la aplicación: gestiona la interacción entre las
 * vistas (Inicio, Workouts, Ejercicio, Historial) y el modelo (Usuario,
 * Workout, Ejercicio).
 *
 * Se encarga de registrar listeners, manejar acciones de usuario, controlar el
 * flujo de inicio de sesión/registro, iniciar workouts y monitorizar la
 * conexión a internet.
 */
public class ControladorInicio extends MouseAdapter implements ActionListener, ListSelectionListener {

	private Inicio vistaInicio;
	private Workouts vistaWorkouts;

	private ArrayList<Workout> workouts;
	private Usuario usuario;
	private HistoricoWorkouts vistaHistorico;
	private PantallaEjercicio vistaEjercicio;

	private volatile boolean conexionInternet = false;
	private ScheduledExecutorService monitorConexion;

	private HiloWorkout hiloWorkout;
	private Workout workoutEnCurso;

	public ControladorInicio(Inicio vistaInicio) {
		this.vistaInicio = vistaInicio;
		vistaWorkouts = new Workouts();
		usuario = new Usuario();
		vistaHistorico = new HistoricoWorkouts();
		vistaEjercicio = new PantallaEjercicio();
		inicializarControlador();
	}


	/**
	 * Inicializa y registra todos los listeners y valores iniciales de los paneles.
	 * Aquí se asocian comandos de acción y manejadores a botones y componentes.
	 */
	private void inicializarControlador() {

		// Inicio: detectar click sobre la ventana para mostrar login
		vistaInicio.getContentPane().addMouseListener(this);
		vistaInicio.getPanelLogoGrande().addMouseListener(this);

		// Panel Login: configurar botón y enlace a registro
		vistaInicio.getPanelLogin().getBtnIniciarSesion().setActionCommand("INICIAR_SESION");
		vistaInicio.getPanelLogin().getBtnIniciarSesion().addActionListener(this);
		vistaInicio.getPanelLogin().getLblRegistrar().addMouseListener(this);

		// Panel Registro: botones de registrar / atrás
		vistaInicio.getPanelRegistro().getBtnRegistrar().setActionCommand("REGISTRAR");
		vistaInicio.getPanelRegistro().getBtnRegistrar().addActionListener(this);

		vistaInicio.getPanelRegistro().getBtnAtras().setActionCommand("ATRAS");
		vistaInicio.getPanelRegistro().getBtnAtras().addActionListener(this);

		// Workouts: desconectar, selección de tabla, filtrado por nivel, edición perfil
		vistaWorkouts.getBtnDesconectar().setActionCommand("DESCONECTAR");
		vistaWorkouts.getBtnDesconectar().addActionListener(this);

		vistaWorkouts.getTableWorkouts().getSelectionModel().addListSelectionListener(this);
		vistaWorkouts.getComboBox().setActionCommand("FILTRAR_NIVEL");
		vistaWorkouts.getComboBox().addActionListener(this);
		vistaWorkouts.getTableWorkouts().addMouseListener(this);

		vistaWorkouts.getBtnEditarPerfil().setActionCommand("EDITAR_PERFIL");
		vistaWorkouts.getBtnEditarPerfil().addActionListener(this);

		vistaWorkouts.getBtnEmpezarWorkout().setActionCommand("ABRIR_PANTALLA_EJERCICIO");
		vistaWorkouts.getBtnEmpezarWorkout().addActionListener(this);

		// Vista Ejercicio: gestionar botones de salir, empezar/pausar y siguiente
		vistaEjercicio.getBtnSalir().setActionCommand("SALIR_EJERCICIO");
		vistaEjercicio.getBtnSalir().addActionListener(this);

		vistaEjercicio.getBtnEmpezar().setActionCommand("EMPEZAR_PAUSAR_REANUDAR");
		vistaEjercicio.getBtnEmpezar().addActionListener(this);

		vistaEjercicio.getBtnSiguiente().setActionCommand("SIGUIENTE_EJERCICIO");
		vistaEjercicio.getBtnSiguiente().addActionListener(this);

		// Historial de Workouts
		vistaWorkouts.getBtnHistoricoWorkouts().setActionCommand("HISTORICO_WORKOUTS");
		vistaWorkouts.getBtnHistoricoWorkouts().addActionListener(this);

		vistaHistorico.getBtnAtras().setActionCommand("ATRAS_HISTORIALWORKOUTS");
		vistaHistorico.getBtnAtras().addActionListener(this);

		// Inicializar placeholders en formularios
		vVaciarLogin();
		vVaciarRegistro();

		// Iniciar el monitor de conexión a internet en background
		iniciarMonitorConexionInternet();
	}


	/**
	 * Gestiona las acciones disparadas por los componentes (botones, combos).
	 * Redirige a métodos específicos según el comando de acción.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		final String cmd = e.getActionCommand();

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
			if (usuario.getEmail() != null) {
				vistaInicio.setVisible(false);
			} else {
				// Volver al panel de login desde registro
				vVaciarLogin();
				vistaInicio.getPanelRegistro().setVisible(false);
				vistaInicio.getPanelLogin().setVisible(true);
			}
			break;
		case "ATRAS_HISTORIALWORKOUTS":
			// Volver de historial a lista de workouts
			vistaHistorico.setVisible(false);
			vistaWorkouts.setVisible(true);
			break;
		case "MOSTRAR_REGISTRO":
			vSetModoRegistro();
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
			// Cargar ejercicios del workout seleccionado en la vista
			mCargarEjercicios(mWorkoutSeleccionado());
			break;
		case "EDITAR_PERFIL":
			mEditarPerfil();
			break;
		case "HISTORICO_WORKOUTS":
			try {
				// Cargar historial desde modelo
				usuario.mCargarHistorialWorkouts(conexionInternet);
				mRellenarTablaHistorico();
				vistaHistorico.setVisible(true);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			break;

		case "ABRIR_PANTALLA_EJERCICIO":
			abrirPantallaEjercicio();
			break;

		case "EMPEZAR_PAUSAR_REANUDAR":
			manejarEmpezarPausarReanudar();
			break;

		case "SIGUIENTE_EJERCICIO":
			if (hiloWorkout != null)
				hiloWorkout.siguiente();
			break;

		case "SALIR_EJERCICIO":
			// Controla la finalización del workout según estado del hilo
			if (hiloWorkout != null) {
				// Si no ha empezado, salir directo sin mostrar resumen
				if (!hiloWorkout.isRunning()) {
					vistaEjercicio.setVisible(false);
					vistaWorkouts.setVisible(true);
					hiloWorkout = null;
				} else {
					// Si está en curso, finalizar de forma inmediata mostrando el resumen
					hiloWorkout.finalizarInmediato();
				}
			} else {
				vistaEjercicio.setVisible(false);
				vistaWorkouts.setVisible(true);
			}
			break;

		default:
			break;
		}

	}

	@Override
	public void valueChanged(ListSelectionEvent event) {
		// Detecta selección en la tabla de workouts y lanza la acción correspondiente
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
			// Si se hace click en la tabla, puede abrir el vídeo correspondiente
			mAbrirVideo(e);
		} else if (e.getSource() == vistaInicio.getPanelLogoGrande() || e.getSource() == vistaInicio.getContentPane()) {
			// Click en logo muestra login
			mMostrarPanelLogin();
		}
	}

	/**
	 * Muestra el panel de login cuando se hace clic Ajusta la visibilidad de los
	 * paneles relacionados.
	 */
	public void mMostrarPanelLogin() {
		if (vistaInicio.getPanelLogoGrande().isVisible()) {
			vistaInicio.getPanelLogin().setVisible(true);
			vistaInicio.getPanelRegistro().setVisible(false);
			vistaInicio.getPanelLogoGrande().setVisible(false);
			vistaInicio.getPanelLogoPequeno().setVisible(true);
		}
	}

	/**
	 * Valida campos de login, intenta autenticar al usuario y carga la vista de
	 * workouts si la autenticación es correcta.
	 *
	 * - Utiliza placeholders para marcar campos vacíos.
	 * - Llama a usuario.validarLogin que puede usar la conexión a internet.
	 */
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
			if (usuario.validarLogin(email, contrasena, conexionInternet)) {
				// Cargar workouts y datos del usuario en la vista
				mCargarWorkouts();
				mostrarDatosUsuario();
				vistaInicio.setVisible(false);
				vistaWorkouts.setVisible(true);
				iniciarBackup();
			} else {
				// Mostrar error si credenciales incorrectas
				vistaInicio.getPanelLogin().getLblError().setForeground(Color.RED);
				vistaInicio.getPanelLogin().getLblError().setText("Correo electrónico o contraseña incorrectos");
			}
		} catch (Exception e) {
			// Error de conexión/BD
			vistaInicio.getPanelLogin().getLblError().setForeground(Color.RED);
			vistaInicio.getPanelLogin().getLblError().setText("Error al conectar con la base de datos");
		}

	}

	/**
	 * Valida y procesa el registro o edición de usuario. Maneja placeholders y
	 * muestra errores en la vista en caso de fallo.
	 *
	 * - Si ya existe un usuario en memoria, actúa como "editar perfil".
	 * - Si no, registra un nuevo usuario (validando que el email no exista).
	 */
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

		// Validaciones y marcadores visuales
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
			// Si hay usuario activo, actualizar perfil
			if (usuario.getEmail() != null) {
				usuario.setNombre(nombre);
				usuario.setApellidos(apellidos);
				usuario.setContrasena(password);
				usuario.setFec_nac(fechaNacimiento);
				usuario.mActualizarUsuario(conexionInternet);

				vistaInicio.setVisible(false);
				vistaWorkouts.setVisible(true);
				return;
			}
			// Comprobar si ya existe el email
			if (usuario.mExisteUsuario(email, conexionInternet)) {
				vistaInicio.getPanelRegistro().getLblError().setForeground(Color.RED);
				vistaInicio.getPanelRegistro().getLblError().setText("El correo electrónico ya está registrado");
				return;
			}
			
			// Crear y persistir nuevo usuario
			Usuario nuevoUsuario = new Usuario(nombre, apellidos, email, password, fechaNacimiento, 0, "cliente");

			nuevoUsuario.mAnadirUsuario(conexionInternet);
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

	/**
	 * Carga la lista de workouts disponibles para el nivel del usuario y prepara el
	 * combobox de filtrado.
	 * - Obtiene los workouts desde el modelo (puede usar conexión remota o local).
	 */
	public void mCargarWorkouts() {
		vistaWorkouts.getModeloWorkouts().setRowCount(0);
		workouts = Workout.mObtenerWorkouts(usuario.getNivel(), conexionInternet);
		mRellenarTablaWorkouts(0);

		// Preparar combobox con "Todos" + niveles disponibles hasta el del usuario
		vistaWorkouts.getModeloComboBox().removeAllElements();
		vistaWorkouts.getModeloComboBox().addElement("Todos los niveles");
		for (int i = 0; i <= usuario.getNivel(); i++) {
			vistaWorkouts.getModeloComboBox().addElement("Nivel " + i);
		}
	}

	/**
	 * Rellena la tabla de workouts aplicando un filtro por nivel (0 = todos).
	 */
	private void mRellenarTablaWorkouts(int filtroNivel) {
		if (workouts == null)
			return;
		vistaWorkouts.getModeloWorkouts().setRowCount(0);
		for (int i = 0; i < workouts.size(); i++) {
			Workout w = workouts.get(i);
			if (filtroNivel == -1 || w.getNivel() == filtroNivel) {
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
		// Ocultar la sección de ejercicios hasta que se seleccione un workout
		mMostrarOcultarEjercicios(false);
	}

	/**
	 * Rellena la tabla del historial de workouts del usuario, calculando tiempos y
	 * porcentajes.
	 *
	 * - Formatea fechas y tiempos para presentación en la tabla.
	 */
	private void mRellenarTablaHistorico() {
		DefaultTableModel modelo = vistaHistorico.getModeloEjercicios();
		modelo.setRowCount(0);

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		for (UsuWorkout uw : usuario.getWorkouts()) {
			Workout w = uw.getWorkout();

			String nombre = w.getNombre();
			int tiempoTotal = uw.getTiempoTotal();
			
			String nivelStr = "Desconocido";
			if (w != null) {
				int nv = w.getNivel();
				switch (nv) {
				case 0:
					nivelStr = "Principiante";
					break;
				case 1:
					nivelStr = "Intermedio";
					break;
				case 2:
					nivelStr = "Avanzado";
					break;
				case 3:
					nivelStr = "Experto";
					break;
				default:
					if (nv > 0)
						nivelStr = String.valueOf(nv);
					break;
				}
			}

			// Calcular tiempo previsto (usar método centralizado en Workout)
			int tiempoPrevisto = (w != null) ? w.getTiempoPrevistoSegundos() : 0;

			String fecha = sdf.format(uw.getFecha());

			// Porcentaje completado basado en número de ejercicios completados
			int porcentaje = (w != null) ? w.calcularPorcentajeCompletado(uw) : 0;
			String tiempoTotalStr = String.format("%02d:%02d", tiempoTotal / 60, tiempoTotal % 60);
			String tiempoPrevistoStr = String.format("%02d:%02d", tiempoPrevisto / 60, tiempoPrevisto % 60);

			String[] fila = new String[6];
			fila[0] = nombre;
			fila[1] = nivelStr;
			fila[2] = tiempoTotalStr;
			fila[3] = tiempoPrevistoStr;
			fila[4] = fecha;
			fila[5] = porcentaje + "%";

			modelo.addRow(fila);
		}
	}

	/**
	 * Aplica el filtro seleccionado en el combobox para mostrar solo ciertos
	 * niveles.
	 */
	public void mFiltrarNiveles() {
		String sel = (String) vistaWorkouts.getComboBox().getSelectedItem();
		int filtro = -1;
		if (sel != null && sel.startsWith("Nivel ")) {
			try {
				// Es "Nivel X"
				filtro = Integer.parseInt(sel.substring(6));
			} catch (NumberFormatException ex) {
				// Es "Todos los niveles"
				filtro = -1;
			}
		}
		mRellenarTablaWorkouts(filtro);
	}

	/**
	 * Abre el vídeo del workout si se hace clic en la columna correspondiente de la
	 * tabla.
	 *
	 * - Comprueba la columna del modelo para identificar la celda de video.
	 */
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
				// Abrir URL en el navegador del sistema
				Desktop.getDesktop().browse(new URI(url));

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Carga los ejercicios de un workout seleccionado y los muestra en la tabla
	 * correspondiente.
	 *
	 * - Si no hay ejercicios en memoria y hay conexión, los obtiene remotamente.
	 */
	public void mCargarEjercicios(Workout workout) {
		if (workout == null)
			return;
		vistaWorkouts.getModeloEjercicios().setRowCount(0);
		if (workout.getEjercicios().size() == 0 && conexionInternet) {
			ArrayList<Ejercicio> ejerciciosArray = Ejercicio.fbObtenerEjerciciosWorkout(workout);
			workout.setEjercicios(ejerciciosArray);

		}
		for (int i = 0; i < workout.getEjercicios().size(); i++) {
			String[] fila = new String[6];
			fila[0] = workout.getEjercicios().get(i).getIdEjercicio();
			fila[1] = workout.getEjercicios().get(i).getNombre();
			fila[2] = workout.getEjercicios().get(i).getDescripcion();
			fila[3] = String.format("%02d:%02d", workout.getEjercicios().get(i).getTiempoDescanso() / 60,
					workout.getEjercicios().get(i).getTiempoDescanso() % 60);
			vistaWorkouts.getModeloEjercicios().addRow(fila);
		}
		mMostrarOcultarEjercicios(true);
	}

	/**
	 * Muestra u oculta los componentes relacionados con los ejercicios en la vista
	 * de workouts.
	 */
	public void mMostrarOcultarEjercicios(boolean mostrar) {
		vistaWorkouts.getTableEjercicios().setVisible(mostrar);
		vistaWorkouts.getLblEventos().setVisible(mostrar);
		vistaWorkouts.getScrollPaneEventos().setVisible(mostrar);
		vistaWorkouts.getBtnEmpezarWorkout().setVisible(mostrar);
	}

	/**
	 * Devuelve el workout actualmente seleccionado en la tabla o null si no hay
	 * selección.
	 */
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

	public Ejercicio mEjercicioActual() {

		Workout workoutActual = mWorkoutSeleccionado();
		if (workoutActual != null) {
			for (Ejercicio e : workoutActual.getEjercicios()) {
				if (e.isActual()) {
					return e;
				}
			}
		}
		return null;
	}

	/**
	 * Desconecta al usuario actual, resetea la vista y crea un nuevo objeto Usuario
	 * vacío.
	 */
	public void mDesconectar() {
		vistaWorkouts.setVisible(false);
		vistaInicio.setVisible(true);
		vistaInicio.getPanelLogin().setVisible(true);
		vistaInicio.getPanelRegistro().setVisible(false);
		vVaciarLogin();
		usuario = new Usuario();
	}

	/**
	 * Valida de forma básica la estructura de un correo electrónico. Retorna true
	 * si parece válido, false en caso contrario.
	 *
	 * - No es un validador exhaustivo, pero previene errores comunes.
	 */
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

	/**
	 * Abre el panel de edición de perfil (modo registro) y rellena los campos si
	 * hay un usuario activo.
	 */
	public void mEditarPerfil() {
		vSetModoRegistro();
		vistaInicio.getPanelLogin().setVisible(false);
		vistaInicio.getPanelRegistro().setVisible(true);
		vistaInicio.setVisible(true);
	}

	/**
	 * Limpia y restablece los campos del formulario de login con placeholders
	 * iniciales.
	 */
	public void vVaciarLogin() {
		JTextField txtEmail = vistaInicio.getPanelLogin().getTextFieldEmail();
		JTextField txtContrasena = vistaInicio.getPanelLogin().getTextFieldContrasena();

		txtEmail.setText("");
		txtContrasena.setText("");

		vistaInicio.placeholder("Introduce tu correo electrónico", Color.GRAY, txtEmail);
		vistaInicio.placeholder("********", Color.GRAY, txtContrasena);
		vistaInicio.getPanelLogin().getLblError().setText("");

	}

	/**
	 * Limpia y restablece los campos del formulario de registro con placeholders
	 * iniciales.
	 */
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
		txtEmail.setEnabled(true);

		vistaInicio.placeholder("Introduce tu nombre", Color.GRAY, txtNombre);
		vistaInicio.placeholder("Introduce tus apellidos", Color.GRAY, txtApellidos);
		vistaInicio.placeholder("Introduce tu correo electrónico", Color.GRAY, txtEmail);
		vistaInicio.placeholder("********", Color.GRAY, txtContrasena);
		vistaInicio.placeholder("Selecciona tu fecha de nacimiento", Color.GRAY, dateField);

		vistaInicio.getPanelRegistro().getLblError().setText("");

	}

	/**
	 * Configura la vista de registro para modo "nuevo usuario" o "editar perfil"
	 * según si hay un usuario activo.
	 */
	public void vSetModoRegistro() {
		JLabel lblRegistro = vistaInicio.getPanelRegistro().getLblRegistro();
		JButton btnRegistrar = vistaInicio.getPanelRegistro().getBtnRegistrar();
		if (usuario.getEmail() != null) {
			lblRegistro.setText("Editar perfil");
			btnRegistrar.setText("Guardar");
			vVaciarRegistro();
			vistaInicio.getPanelRegistro().getTxtNombre().setText(usuario.getNombre());
			vistaInicio.getPanelRegistro().getTxtApellidos().setText(usuario.getApellidos());
			vistaInicio.getPanelRegistro().getTxtEmail().setText(usuario.getEmail());
			vistaInicio.getPanelRegistro().getTxtEmail().setEnabled(false); // No permitir cambiar email
			vistaInicio.getPanelRegistro().getTxtContrasena().setText(usuario.getContrasena());
			vistaInicio.getPanelRegistro().getDateChooser().setDate(usuario.getFec_nac());

			// Reaplicar placeholders (visual)
			vistaInicio.placeholder("Introduce tu nombre", Color.GRAY, vistaInicio.getPanelRegistro().getTxtNombre());
			vistaInicio.placeholder("Introduce tus apellidos", Color.GRAY,
					vistaInicio.getPanelRegistro().getTxtApellidos());
			vistaInicio.placeholder("Introduce tu correo electrónico", Color.GRAY,
					vistaInicio.getPanelRegistro().getTxtEmail());
			vistaInicio.placeholder("********", Color.GRAY, vistaInicio.getPanelRegistro().getTxtContrasena());
			vistaInicio.placeholder("Selecciona tu fecha de nacimiento", Color.GRAY,
					(JTextField) vistaInicio.getPanelRegistro().getDateChooser().getDateEditor().getUiComponent());
		} else {
			vVaciarRegistro();
			lblRegistro.setText("Registrarse");
			btnRegistrar.setText("Registrar");
		}
	}

	/**
	 * Inicia el hilo de backup si hay conexión a internet. Si no hay conexión,
	 * muestra mensaje en la vista.
	 */
	public void iniciarBackup() {
		if(conexionInternet) {
			HiloBackup hiloBackup = new HiloBackup(vistaWorkouts.getLblBackups());
			hiloBackup.start();
		} else {
			// Mostrar mensaje de que se trabaja desde backup local
			vistaWorkouts.getLblBackups().setText("Sin conexión. Datos desde backup");
			vistaWorkouts.getLblBackups().setForeground(Color.white);
		}
	}

	/**
	 * Abre la pantalla de ejercicio, carga datos del workout/ejercicio y prepara
	 * paneles/cronómetros.
	 *
	 * - Crea un HiloWorkout que gestiona los cronómetros y la lógica del workout.
	 */
	private void abrirPantallaEjercicio() {
		Workout w = mWorkoutSeleccionado();
		if (w == null)
			return;
		workoutEnCurso = w;
		// Asegurar ejercicios cargados
		if (workoutEnCurso.getEjercicios() == null || workoutEnCurso.getEjercicios().isEmpty()) {
			ArrayList<Ejercicio> ejerciciosArray = Ejercicio.fbObtenerEjerciciosWorkout(workoutEnCurso);
			workoutEnCurso.setEjercicios(ejerciciosArray);
		}

		// Datos de cabecera workout
		vistaEjercicio.getLblNombreWorkout().setText(workoutEnCurso.getNombre());
		vistaEjercicio.getLblWorkoutDescripcion().setText(workoutEnCurso.getDescripcion());

		// Construir paneles de series por ejercicio y mostrar el primero
		vistaEjercicio.panelSeriesPorEjercicio.clear();
		for (Ejercicio ej : workoutEnCurso.getEjercicios()) {
			vistaEjercicio.panelSeriesPorEjercicio.add(vistaEjercicio.crearPanelSeriesEjercicio(ej));
		}
		if (!workoutEnCurso.getEjercicios().isEmpty()) {
			Ejercicio ej0 = workoutEnCurso.getEjercicios().get(0);
			vistaEjercicio.getLblNombreEjercicio().setText(ej0.getNombre());
			vistaEjercicio.getLblEjercicioDescripcion().setText(ej0.getDescripcion());
			vistaEjercicio.getLblCronometroDescanso().setText(formatearSeg(ej0.getTiempoDescanso()));
			vistaEjercicio.getLblCronometroPreparacion().setText("00:05");
			vistaEjercicio.mostrarPanelSeries(0);
		}

		// Inicialización de labels y botones en la pantalla de ejercicio
		vistaEjercicio.getLblCronometroWorkout().setText("00:00");
		vistaEjercicio.getLblCronometroEjercicio().setText("00:00");
		vistaEjercicio.getBtnEmpezar().setText("Empezar");
		vistaEjercicio.getBtnSiguiente().setVisible(true);
		vistaEjercicio.getBtnSiguiente().setEnabled(false);

		// Crear gestor de cronos (pasar usuario, vistaWorkouts y referencia al
		// controlador para callback)
		hiloWorkout = new HiloWorkout(vistaEjercicio, workoutEnCurso, usuario, vistaWorkouts, this);

		vistaWorkouts.setVisible(false);
		vistaEjercicio.setVisible(true);
	}

	/**
	 * Gestiona los estados del botón empezar/pausar/reanudar delegando al hilo que
	 * controla el workout.
	 */
	private void manejarEmpezarPausarReanudar() {
		if (hiloWorkout == null)
			return;
		if (!hiloWorkout.isRunning()) {
			hiloWorkout.start();
			vistaEjercicio.getBtnEmpezar().setText("Pausar");
			return;
		}
		if (hiloWorkout.isPaused()) {
			hiloWorkout.reanudar();
			vistaEjercicio.getBtnEmpezar().setText("Pausar");
		} else {
			hiloWorkout.pause();
			vistaEjercicio.getBtnEmpezar().setText("Reanudar");
		}
	}

	private String formatearSeg(int segundos) {
		int mm = Math.max(0, segundos) / 60;
		int ss = Math.max(0, segundos) % 60;
		return String.format("%02d:%02d", mm, ss);
	}

	public void mostrarDatosUsuario() {
		vistaWorkouts.getLblNivel().setText("Nivel: " + usuario.getNivel());
	}
	public void terminarWorkout() {
		hiloWorkout = null;
	}
	

	public boolean tieneConexionInternet() {
		return conexionInternet;
	}

	/**
	 * Inicia un monitor en segundo plano que comprueba la conexion.
	 *
	 * - Utiliza un ScheduledExecutorService para comprobar cada 15s la conexión a
	 *   Internet y actualizar la variable conexionInternet.
	 */
	private void iniciarMonitorConexionInternet() {
		if (monitorConexion != null)
			return;
		monitorConexion = Executors.newSingleThreadScheduledExecutor(r -> {
			Thread t = new Thread(r, "MonitorConexionInternet");
			t.setDaemon(true);
			return t;
		});
		// Comprobar inmediatamente y luego cada 15s
		monitorConexion.scheduleAtFixedRate(() -> {
			boolean ok = comprobarConexionInternet();
			if (ok != conexionInternet) {
				// Si cambia la conexión, aquí podrías recargar datos o notificar a la UI
				// recargarDatos();
			}
			conexionInternet = ok;

		}, 0, 15, TimeUnit.SECONDS);
	}

	/**
	 * Comprueba de forma ligera si hay conexión a internet intentando abrir un
	 * socket hacia 8.8.8.8:53 (DNS). Devuelve true si la conexión es posible.
	 *
	 * - Método rápido y sin dependencias externas; no garantiza acceso HTTP.
	 */
	private boolean comprobarConexionInternet() {
		SocketAddress address = new InetSocketAddress("8.8.8.8", 53);
		try (Socket socket = new Socket()) {
			socket.connect(address, 2000); // timeout 2s
			return true;
		} catch (IOException ex) {
			return false;
		}
	}

}

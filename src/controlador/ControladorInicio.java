package controlador;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import javax.swing.JOptionPane;

import modelo.Ejercicio;
import modelo.Usuario;
import modelo.Workout;
import vista.Inicio;
import vista.Workouts;

public class ControladorInicio implements ActionListener, ListSelectionListener {

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

		// Login
		vistaInicio.getPanelLogin().getBtnIniciarSesion().setActionCommand("INICIAR_SESION");
		vistaInicio.getPanelLogin().getBtnIniciarSesion().addActionListener(this);

		final JLabel lblRegistrar = vistaInicio.getPanelLogin().getLblRegistrar();
		lblRegistrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblRegistrar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				actionPerformed(new ActionEvent(lblRegistrar, ActionEvent.ACTION_PERFORMED, "MOSTRAR_REGISTRO"));
			}
		});

		// Registro
		vistaInicio.getPanelRegistro().getBtnRegistrar().setActionCommand("REGISTRAR");
		vistaInicio.getPanelRegistro().getBtnRegistrar().addActionListener(this);

		vistaInicio.getPanelRegistro().getBtnAtras().setActionCommand("ATRAS");
		vistaInicio.getPanelRegistro().getBtnAtras().addActionListener(this);

		// Workouts
		vistaWorkouts.getBtnDesconectar().setActionCommand("DESCONECTAR");
		vistaWorkouts.getBtnDesconectar().addActionListener(this);
		vistaWorkouts.getTableWorkouts().getSelectionModel().addListSelectionListener(this);
		vistaWorkouts.getComboBox().addActionListener(e -> {
		    String sel = (String) vistaWorkouts.getComboBox().getSelectedItem();
		    int filtro = 0;
		    if (sel != null && sel.startsWith("Nivel ")) {
		        try {
		            filtro = Integer.parseInt(sel.substring(6));
		        } catch (NumberFormatException ex) {
		            filtro = 0;
		        }
		    }
		    mRellenarTablaWorkouts(filtro);
		});
		vistaWorkouts.getTableWorkouts().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

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
		});
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();

		switch (cmd) {
		case "INICIAR_SESION":
			mIniciarSesion();
			break;

		case "REGISTRAR":
			mRegistro();
			break;

		case "ATRAS":
			vistaInicio.getPanelRegistro().setVisible(false);
			vistaInicio.getPanelLogin().setVisible(true);
			break;
		case "MOSTRAR_REGISTRO":
			vistaInicio.getPanelLogin().setVisible(false);
			vistaInicio.getPanelRegistro().setVisible(true);
			break;
		case "DESCONECTAR":
			mDesconectar();
			break;
		default:
			break;
		}

	}
	public void valueChanged(ListSelectionEvent event) {
		Workout seleccionado = mWorkoutSeleccionado();
		if (seleccionado != null) {
			mCargarEjercicios(seleccionado);
		}
	}

	public void mIniciarSesion() {
		String email = vistaInicio.getPanelLogin().getTextFieldEmail().getText().trim();
		String password = vistaInicio.getPanelLogin().getTextFieldPassword().getText().trim();

		// si esta bien
		if (usuario.validarLogin(email, password)) {
			JOptionPane optionPane = new JOptionPane("Login correcto. Bienvenido " + usuario.getNombre(),
					JOptionPane.INFORMATION_MESSAGE);
			mCargarWorkouts();

			JDialog dialog = optionPane.createDialog(vistaInicio, "Login exitoso");
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

			Timer timer = new Timer(1000, evt -> dialog.dispose());
			timer.setRepeats(false);
			timer.start();

			dialog.setVisible(true);
			vistaInicio.setVisible(false);
			vistaWorkouts.setVisible(true);
			// campo empty
		} else if (email.isEmpty() || password.isEmpty()) {

			JOptionPane.showMessageDialog(vistaInicio, "TODOS LOS CAMPOS SON OBLIGATIOROS", "Error",
					JOptionPane.ERROR_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(vistaInicio, "Email o contraseña incorrectos", "Error",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	public void mRegistro() {
		String nombre = vistaInicio.getPanelRegistro().getTxtNombre().getText().trim();
		String apellidos = vistaInicio.getPanelRegistro().getTxtApellidos().getText().trim();
		String email = vistaInicio.getPanelRegistro().getTxtEmail().getText().trim();
		String password = vistaInicio.getPanelRegistro().getTxtContrasena().getText().trim();
		Date fechaNacimiento = vistaInicio.getPanelRegistro().getDateChooser().getDate();

		if (nombre.isEmpty() || apellidos.isEmpty() || email.isEmpty() || password.isEmpty()
				|| fechaNacimiento == null) {
			JOptionPane.showMessageDialog(vistaInicio, "TODOS LOS CAMPOS SON OBLIGATORIOS", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		Usuario nuevoUsuario = new Usuario(nombre, apellidos, email, password, fechaNacimiento, 1, "usuario");

		if (nuevoUsuario.mAnadirUsuario()) {
			JOptionPane.showMessageDialog(vistaInicio, "Usuario registrado correctamente", "Éxito",
					JOptionPane.INFORMATION_MESSAGE);
			vistaInicio.getPanelRegistro().setVisible(false);
			vistaInicio.getPanelLogin().setVisible(true);
		} else {
			JOptionPane.showMessageDialog(vistaInicio, "Error al registrar usuario", "Error",
					JOptionPane.ERROR_MESSAGE);
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
	    vistaWorkouts.getModeloWorkouts().setRowCount(0);
	    if (workouts == null) return;
	    boolean seleccionadoEncontrado = false;
	    Workout seleccionado = mWorkoutSeleccionado();
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
	            if(seleccionado!=null && w.getIdWorkout().equals(seleccionado.getIdWorkout())) {
	            	seleccionadoEncontrado = true;
	            }
	        }
	    }
		mMostrarOcultarEjercicios(seleccionadoEncontrado);

	}

	public void mCargarEjercicios(Workout workout) {
		vistaWorkouts.getModeloEjercicios().setRowCount(0);
		ArrayList<Ejercicio> ejerciciosArray = Ejercicio.mObtenerEjerciciosWorkout(workout);
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
		vistaInicio.getPanelLogin().getTextFieldEmail().setText("");
		vistaInicio.getPanelLogin().getTextFieldPassword().setText("");
		usuario = new Usuario();
	}
}

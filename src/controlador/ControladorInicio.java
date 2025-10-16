package controlador;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.JOptionPane;

import modelo.Usuario;
import vista.Inicio;
import vista.Workouts;

public  class ControladorInicio implements ActionListener 	{

    private Inicio vistaInicio;
    private Workouts vistaWorkouts;

    // Constructor
    public ControladorInicio(Inicio vistaInicio) {
        this.vistaInicio = vistaInicio;
        vistaWorkouts = new Workouts();
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
        }


    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd == null) return;

        // Login
      
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
        default:
            break;
    }
        
    }
    
    public void mIniciarSesion() {
        String email = vistaInicio.getPanelLogin().getTextFieldEmail().getText().trim();
        String password = vistaInicio.getPanelLogin().getTextFieldPassword().getText().trim();

        Usuario usuario = new Usuario();
        // si esta bien
        if (usuario.validarLogin(email, password)) {
            JOptionPane optionPane = new JOptionPane(
                "Login correcto. Bienvenido " + usuario.getNombre(),
                JOptionPane.INFORMATION_MESSAGE
            );
            
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
       
        	JOptionPane.showMessageDialog(vistaInicio, "TODOS LOS CAMPOS SON OBLIGATIOROS", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
        	JOptionPane.showMessageDialog(vistaInicio, "Email o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
        }
        	
	}
    
    public void mRegistro() {
        String nombre = vistaInicio.getPanelRegistro().getTxtNombre().getText().trim();
        String apellidos = vistaInicio.getPanelRegistro().getTxtApellidos().getText().trim();
        String email = vistaInicio.getPanelRegistro().getTxtEmail().getText().trim();
        String password = vistaInicio.getPanelRegistro().getTxtContrasena().getText().trim();
        Date fechaNacimiento = vistaInicio.getPanelRegistro().getDateChooser().getDate();

        if (nombre.isEmpty() || apellidos.isEmpty() || email.isEmpty() || password.isEmpty() || fechaNacimiento == null) {
            JOptionPane.showMessageDialog(vistaInicio, "TODOS LOS CAMPOS SON OBLIGATORIOS", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Usuario nuevoUsuario = new Usuario(nombre, apellidos, email, password, fechaNacimiento, 1, "usuario");

        if (nuevoUsuario.mAnadirUsuario()) {
            JOptionPane.showMessageDialog(vistaInicio, "Usuario registrado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            vistaInicio.getPanelRegistro().setVisible(false);
            vistaInicio.getPanelLogin().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(vistaInicio, "Error al registrar usuario", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionListener;

import modelo.Usuario;
import vista.Inicio;

public  class ControladorInicio implements ActionListener 	{

    private Inicio vistaInicio;

    // Constructor
    public ControladorInicio(Inicio vistaInicio) {
        this.vistaInicio = vistaInicio;
        inicializarControlador();
    }

    private void inicializarControlador() {
        // Login
        vistaInicio.getPanelLogin().getBtnIniciarSesion().addActionListener(this);

        // Registro
        vistaInicio.getPanelRegistro().getBtnRegistrar().addActionListener(this);
        vistaInicio.getPanelRegistro().getBtnAtras().addActionListener(this);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        // Login
        if (source == vistaInicio.getPanelLogin().getBtnIniciarSesion()) {
            String email = vistaInicio.getPanelLogin().getTextFieldEmail().getText().trim();
            String password = vistaInicio.getPanelLogin().getTextFieldPassword().getText().trim();

            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(vistaInicio, "Debe rellenar todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Usuario usuario = new Usuario();
            // Aquí deberías implementar un método para validar usuario por email y contraseña
            // Por ahora solo mostramos un mensaje de prueba
            JOptionPane.showMessageDialog(vistaInicio, "Login realizado con: " + email);

        }

        // Registro
        else if (source == vistaInicio.getPanelRegistro().getBtnRegistrar()) { 
            String nombre = vistaInicio.getPanelRegistro().getTxtNombre().getText().trim();
            String apellidos = vistaInicio.getPanelRegistro().getTxtApellidos().getText().trim();
            String email = vistaInicio.getPanelRegistro().getTxtEmail().getText().trim();
            String password = vistaInicio.getPanelRegistro().getTxtContrasena().getText().trim();
            String fechaStr = vistaInicio.getPanelRegistro().getTxtFecNac().getText().trim();

            if (nombre.isEmpty() || apellidos.isEmpty() || email.isEmpty() || password.isEmpty() || fechaStr.isEmpty()) {
                JOptionPane.showMessageDialog(vistaInicio, "Debe rellenar todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Date fechaNacimiento = null;
            try {
                fechaNacimiento = new SimpleDateFormat("dd/MM/yyyy").parse(fechaStr);
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(vistaInicio, "Formato de fecha incorrecto. Use dd/MM/yyyy", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Usuario nuevoUsuario = new Usuario(nombre, apellidos, email, password, fechaNacimiento, 1, "usuario");

            if (nuevoUsuario.mAnadirUsuario()) {
                JOptionPane.showMessageDialog(vistaInicio, "Usuario registrado correctamente");
                vistaInicio.getPanelRegistro().setVisible(false);
                vistaInicio.getPanelLogin().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(vistaInicio, "Error al registrar usuario", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        // Botón Atras en registro
        else if (source == vistaInicio.getPanelRegistro().getBtnAtras()) {
            vistaInicio.getPanelRegistro().setVisible(false);
            vistaInicio.getPanelLogin().setVisible(true);
        }
    }
}

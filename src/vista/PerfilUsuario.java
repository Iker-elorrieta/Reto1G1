package vista;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;
import javax.swing.border.*;

import modelo.Usuario;

public class PerfilUsuario extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField txtNombre, txtApellidos, txtEmail, txtContrasena, txtFechaNac;
	private JPanel panelLogo;
	private Usuario usuario;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				PerfilUsuario frame = new PerfilUsuario();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public PerfilUsuario() {
		this.usuario = new Usuario();

		setTitle("Perfil del usuario");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(450, 650);
		setLocationRelativeTo(null);

		JPanel contentPane = new JPanel() {
			private static final long serialVersionUID = 1L;
			private Image backgroundImage = new ImageIcon("fotos/fondo1.png").getImage();

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
			}
		};
		contentPane.setLayout(null);
		setContentPane(contentPane);

		panelLogo = new JPanel() {
			private static final long serialVersionUID = 1L;
			private Image logo = new ImageIcon("fotos/logo.png").getImage();

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(logo, 0, 0, getWidth(), getHeight(), this);
			}
		};
		panelLogo.setBounds(150, 10, 150, 150);
		panelLogo.setOpaque(false);
		contentPane.add(panelLogo);

		JPanel panelContenedor = new JPanel();
		panelContenedor.setLayout(null);
		panelContenedor.setBounds(40, 170, 360, 430);
		panelContenedor.setBackground(new Color(0, 0, 0, 120));
		panelContenedor.setBorder(
				BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE), "Datos del usuario",
						TitledBorder.LEFT, TitledBorder.TOP, new Font("Tahoma", Font.BOLD, 14), Color.WHITE));
		contentPane.add(panelContenedor);

		JPanel panelDatos = new JPanel();
		panelDatos.setLayout(null);
		panelDatos.setBounds(10, 30, 340, 330);
		panelDatos.setOpaque(false);
		panelContenedor.add(panelDatos);

		txtNombre = crearCampo(panelDatos, "Nombre:", 0);
		txtApellidos = crearCampo(panelDatos, "Apellidos:", 55);
		txtEmail = crearCampo(panelDatos, "Email:", 110);
		txtContrasena = crearCampo(panelDatos, "Contraseña:", 165);
		txtFechaNac = crearCampo(panelDatos, "Fecha nacimiento (yyyy-MM-dd):", 220);

		JPanel panelBotones = new JPanel();
		panelBotones.setBounds(10, 370, 340, 50);
		panelBotones.setOpaque(false);
		panelContenedor.add(panelBotones);

		JButton btnGuardar = new JButton("Guardar cambios");
		JButton btnCancelar = new JButton("Cancelar");
		panelBotones.add(btnGuardar);
		panelBotones.add(btnCancelar);

		btnGuardar.addActionListener(e -> guardarEnFirestore());

		btnCancelar.addActionListener(e -> dispose());
	}

	private JTextField crearCampo(JPanel panel, String texto, int y) {
		JLabel lbl = new JLabel(texto);
		lbl.setForeground(Color.WHITE);
		lbl.setBounds(10, y, 200, 20);
		panel.add(lbl);

		JTextField txt = new JTextField();
		txt.setBounds(10, y + 20, 320, 25);
		panel.add(txt);
		return txt;
	}

	private void guardarEnFirestore() {
		boolean exito = true;
		try {

			if (txtNombre.getText().isEmpty() || txtEmail.getText().isEmpty() || txtContrasena.getText().isEmpty()) {
				JOptionPane.showMessageDialog(this, "Nombre, Email y Contraseña son obligatorios");
				return;
			}

			usuario.setNombre(txtNombre.getText());
			usuario.setApellidos(txtApellidos.getText());
			usuario.setEmail(txtEmail.getText());
			usuario.setContrasena(txtContrasena.getText());

			Date fecha;
			try {
				fecha = sdf.parse(txtFechaNac.getText());
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Formato de fecha incorrecto. Use yyyy-MM-dd");
				return;
			}
			usuario.setFec_nac(fecha);
			usuario.setNivel(1);
			usuario.setTipo("normal");

			if (usuario.getIdUsuario() == null || usuario.getIdUsuario().isEmpty()) {
				usuario.mAnadirUsuario();
			} else {
				usuario.mActualizarUsuario();
			}

		} catch (Exception ex) {
			exito = false;
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
		}
		if (exito) {
			JOptionPane.showMessageDialog(this, "Usuario guardado correctamente en Firestore");
		} else {
			JOptionPane.showMessageDialog(this, "Error al guardar usuario en Firestore");
		}

	}
}

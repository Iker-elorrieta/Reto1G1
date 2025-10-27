package vista;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import com.toedter.calendar.JDateChooser;

public class PanelRegistro extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private JTextField txtNombre;
	private JTextField txtApellidos;
	private JTextField txtEmail;
	private JPasswordField txtContrasena;
	private JButton btnRegistrar;
	private JButton btnAtras;
	private JDateChooser dateChooser;
	private JLabel lblError;
	private JPanel contentPane;
	private JLabel lblRegistro;

	
	public PanelRegistro() {
		setBackground(new Color(255, 255, 255));
		setOpaque(false);
		setBounds(38, 150, 374, 410);
		setVisible(false);
		setLayout(null);
		

		contentPane = new JPanel();
		contentPane.setOpaque(false);
		contentPane.setLayout(null);
		contentPane.setBounds(0, 0, 374, 410);
		add(contentPane);
		
		// Título
		lblRegistro = new JLabel("Registrarse") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(getBackground());
				int arc = 30;
				g2.fillRoundRect(0, 0, getWidth(), getHeight() + arc, arc, arc);
				
				g2.dispose();
				super.paintComponent(g);
			}
		};
		lblRegistro.setFont(new Font("Raleway", Font.BOLD, 24));
		lblRegistro.setForeground(Color.BLACK);
		lblRegistro.setBackground(Color.LIGHT_GRAY);
		lblRegistro.setOpaque(false);
		lblRegistro.setHorizontalAlignment(JLabel.CENTER);
		lblRegistro.setBounds(0, 0, 374, 43);
		contentPane.add(lblRegistro);
		
		// Nombre
		JLabel lblNombre = new JLabel("Nombre");
		lblNombre.setFont(new Font("Raleway", Font.BOLD, 15));
		lblNombre.setForeground(new Color(0, 0, 0));
		lblNombre.setBounds(38, 55, 294, 14);
		contentPane.add(lblNombre);
		
		txtNombre = new JTextField();
		txtNombre.setFont(new Font("Raleway", Font.PLAIN, 15));
		txtNombre.setBounds(38, 75, 294, 30);
		contentPane.add(txtNombre);
		txtNombre.setColumns(10);
		
		// Apellidos
		JLabel lblApellidos = new JLabel("Apellidos");
		lblApellidos.setFont(new Font("Raleway", Font.BOLD, 15));
		lblApellidos.setForeground(new Color(0, 0, 0));
		lblApellidos.setBounds(38, 110, 294, 14);
		contentPane.add(lblApellidos);
		
		txtApellidos = new JTextField();
		txtApellidos.setFont(new Font("Raleway", Font.PLAIN, 15));
		txtApellidos.setBounds(38, 130, 294, 30);
		contentPane.add(txtApellidos);
		txtApellidos.setColumns(10);
		
		// Email
		JLabel lblEmail = new JLabel("Email");
		lblEmail.setFont(new Font("Raleway", Font.BOLD, 15));
		lblEmail.setForeground(new Color(0, 0, 0));
		lblEmail.setBounds(38, 165, 294, 14);
		contentPane.add(lblEmail);
		
		txtEmail = new JTextField();
		txtEmail.setFont(new Font("Raleway", Font.PLAIN, 15));
		txtEmail.setBounds(38, 185, 294, 30);
		contentPane.add(txtEmail);
		txtEmail.setColumns(10);
		
		// Contraseña
		JLabel lblContrasena = new JLabel("Contraseña");
		lblContrasena.setFont(new Font("Raleway", Font.BOLD, 15));
		lblContrasena.setForeground(new Color(0, 0, 0));
		lblContrasena.setBounds(38, 220, 294, 14);
		contentPane.add(lblContrasena);
		
		txtContrasena = new JPasswordField();
		txtContrasena.setFont(new Font("Raleway", Font.PLAIN, 15));
		txtContrasena.setBounds(38, 240, 294, 30);
		contentPane.add(txtContrasena);
		txtContrasena.setColumns(10);
		
		// Fecha de nacimiento
		JLabel lblFecNac = new JLabel("Fecha de nacimiento");
        lblFecNac.setFont(new Font("Raleway", Font.BOLD, 15));
        lblFecNac.setForeground(new Color(0, 0, 0));
        lblFecNac.setBounds(38, 275, 294, 14);
        contentPane.add(lblFecNac);

        dateChooser = new JDateChooser();
        dateChooser.setBounds(38, 295, 294, 30);
        dateChooser.setFont(new Font("Raleway", Font.PLAIN, 15));
        contentPane.add(dateChooser);

		
		// Botón Registrar
		btnRegistrar = new JButton("Registrar");
		btnRegistrar.setFont(new Font("Raleway", Font.BOLD, 15));
		btnRegistrar.setForeground(new Color(255, 255, 255));
		btnRegistrar.setBackground(new Color(0, 0, 0));
		btnRegistrar.setBounds(38, 340, 140, 30);
		contentPane.add(btnRegistrar);
		
		// Botón Atrás
		btnAtras = new JButton("Atrás");
		btnAtras.setFont(new Font("Raleway", Font.BOLD, 15));
		btnAtras.setForeground(new Color(255, 255, 255));
		btnAtras.setBackground(new Color(100, 100, 100));
		btnAtras.setBounds(192, 340, 140, 30);
		contentPane.add(btnAtras);
		
		lblError = new JLabel("");
		lblError.setForeground(Color.RED);
		lblError.setFont(new Font("Raleway", Font.BOLD, 11));
		lblError.setBounds(38, 381, 294, 14);
		contentPane.add(lblError);
		
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(getBackground());
		g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
		g2.dispose();
	}
	

	
	// Getters para los campos
	public JTextField getTxtNombre() {
		return txtNombre;
	}
	
	public JTextField getTxtApellidos() {
		return txtApellidos;
	}
	
	public JTextField getTxtEmail() {
		return txtEmail;
	}
	
	public JTextField getTxtContrasena() {
		return txtContrasena;
	}
	
	public JButton getBtnRegistrar() {
		return btnRegistrar;
	}
	
	public JButton getBtnAtras() {
		return btnAtras;
	}
	
	public JDateChooser getDateChooser() {
	        return dateChooser;
	}

	public JLabel getLblError() {
		return lblError;
	}

	public void setLblError(JLabel lblError) {
		this.lblError = lblError;
	}
	
	
	public JPanel getContentPane() {
        return contentPane;
    }
	
	public JLabel getLblRegistro() {
		return lblRegistro;
	}

	public void setLblRegistro(JLabel lblRegistro) {
		this.lblRegistro = lblRegistro;
	}
	
	
	
}
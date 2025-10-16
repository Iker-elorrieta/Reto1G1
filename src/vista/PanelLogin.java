package vista;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class PanelLogin extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private JPasswordField textFieldPassword;
	private JButton btnIniciarSesion;
	private JLabel lblRegistrar;
	
	public PanelLogin() {
		setBackground(new Color(255, 255, 255));
		setOpaque(false);
		setBounds(38, 150, 374, 285);
		setVisible(false);
		setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Correo electrónico");
		lblNewLabel.setFont(new Font("Raleway", Font.BOLD, 15));
		lblNewLabel.setForeground(new Color(0, 0, 0));
		lblNewLabel.setBounds(38, 71, 294, 14);
		add(lblNewLabel);
		
		textField = new JTextField();
		textField.setFont(new Font("Raleway", Font.PLAIN, 15));
		textField.setBounds(38, 96, 294, 30);
		add(textField);
		textField.setColumns(10);
		
		JLabel lblPassword = new JLabel("Contraseña");
		lblPassword.setFont(new Font("Raleway", Font.BOLD, 15));
		lblPassword.setForeground(new Color(0, 0, 0));
		lblPassword.setBounds(38, 136, 294, 14);
		add(lblPassword);
		
		textFieldPassword = new JPasswordField();
		textFieldPassword.setBounds(38, 161, 294, 30);
		add(textFieldPassword);
		
		lblRegistrar = new JLabel("¿No tienes cuenta? Registrate ahora");
		lblRegistrar.setFont(new Font("Raleway", Font.BOLD, 11));
		lblRegistrar.setForeground(new Color(0, 0, 0));
		lblRegistrar.setHorizontalAlignment(JLabel.CENTER);
		lblRegistrar.setBounds(38, 247, 294, 14);
		add(lblRegistrar);
		
		btnIniciarSesion = new JButton("Iniciar sesión");
		btnIniciarSesion.setFont(new Font("Raleway", Font.BOLD, 15));
		btnIniciarSesion.setForeground(new Color(255, 255, 255));
		btnIniciarSesion.setBackground(new Color(0, 0, 0));
		btnIniciarSesion.setBounds(38, 206, 294, 30);
		add(btnIniciarSesion);
		
		JLabel lblLogin = new JLabel("Iniciar sesión") {
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
		lblLogin.setFont(new Font("Raleway", Font.BOLD, 24));
		lblLogin.setForeground(Color.BLACK);
		lblLogin.setBackground(Color.LIGHT_GRAY);
		lblLogin.setOpaque(false);
		lblLogin.setHorizontalAlignment(JLabel.CENTER);
		lblLogin.setBounds(0, 0, 374, 43);
		add(lblLogin);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(getBackground());
		g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
		g2.dispose();
	}
	
	/************** Getters **************/
	public JTextField getTextFieldEmail() {
		return textField;
	}
	
	public JTextField getTextFieldPassword() {
		return textFieldPassword;
	}
	
	public JButton getBtnIniciarSesion() {
		return btnIniciarSesion;
	}
	
	public JLabel getLblRegistrar() {
		return lblRegistrar;
	}

}
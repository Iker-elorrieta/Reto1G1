package vista;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class Inicio extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private PanelLogin panelLogin;
	private PanelRegistro panelRegistro;
	private JPanel panelLogoGrande;
	private JPanel panelLogoPequeno;

	/**
	 * Create the frame.
	 */
	public Inicio() {
		setTitle("Daji Squad Gym");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(450, 650);
		setLocationRelativeTo(null);

		contentPane = crearPanelconImagen("fotos/fondo1.png");
		panelLogoGrande = crearPanelconImagen("fotos/logo.png");
		panelLogoPequeno = crearPanelconImagen("fotos/logo.png");
		panelLogin = new PanelLogin();
		panelRegistro = new PanelRegistro();

		panelLogoGrande.setOpaque(false);
		panelLogoPequeno.setOpaque(false);

		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		contentPane.add(panelLogin);
		contentPane.add(panelRegistro);
		panelLogoGrande.setBounds(38, 80, 350, 350);
		contentPane.add(panelLogoGrande);

		panelLogoPequeno.setBounds(165, 10, 120, 120);
		contentPane.add(panelLogoPequeno);

		panelLogoPequeno.setVisible(false);
	}

	public static JPanel crearPanelconImagen(String rutaImagen) {
		return new JPanel() {
			private static final long serialVersionUID = 1L;
			private Image backgroundImage = new ImageIcon(rutaImagen).getImage();

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
			}
		};

	}

	public static void placeholder(String texto, Color color, JTextField textField) {
		textField.setForeground(color);
		textField.setText(texto);
		textField.putClientProperty("placeholder", Boolean.TRUE);
		if (textField instanceof JPasswordField) {
			((JPasswordField) textField).setEchoChar((char) 0);
		}
		FocusListener[] focusListeners = textField.getFocusListeners();
		for (FocusListener listener : focusListeners) {
			textField.removeFocusListener(listener);
		}
		textField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (textField.getText().equals(texto)) {
					textField.setText("");
					textField.setForeground(Color.BLACK);
					textField.putClientProperty("placeholder", Boolean.FALSE);
					if (textField instanceof JPasswordField) {
						((JPasswordField) textField).setEchoChar((char) '*');
					}
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (textField.getText().isEmpty()) {
					textField.setForeground(color);
					textField.setText(texto);
					textField.putClientProperty("placeholder", Boolean.TRUE);
					if (textField instanceof JPasswordField) {
						((JPasswordField) textField).setEchoChar((char) 0);
					}
				} else {
					textField.putClientProperty("placeholder", Boolean.FALSE);
					if (textField instanceof JPasswordField) {
						((JPasswordField) textField).setEchoChar((char) '*');
					}

				}
			}
		});
	}

	/************** Getters y Setters **************/
	public PanelLogin getPanelLogin() {
		return panelLogin;
	}

	public PanelRegistro getPanelRegistro() {
		return panelRegistro;
	}

	public JPanel getPanelLogoGrande() {
		return panelLogoGrande;
	}

	public void setPanelLogoGrande(JPanel panelLogoGrande) {
		this.panelLogoGrande = panelLogoGrande;
	}

	public JPanel getPanelLogoPequeno() {
		return panelLogoPequeno;
	}

	public void setPanelLogoPequeno(JPanel panelLogoPequeno) {
		this.panelLogoPequeno = panelLogoPequeno;
	}

}
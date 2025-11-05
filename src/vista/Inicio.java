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
	 * Crea la ventana principal de la aplicación.
	 * Aquí se inicializan componentes visuales, se establece tamaño, título y
	 * se colocan los paneles en su posición.
	 */
	public Inicio() {
		setTitle("Squad Gym");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(450, 650);
		setLocationRelativeTo(null);

		contentPane = crearPanelconImagen("/fondo1.png");
		panelLogoGrande = crearPanelconImagen("/logo.png");
		panelLogoPequeno = crearPanelconImagen("/logo.png");
		panelLogin = new PanelLogin();
		panelRegistro = new PanelRegistro();

		// Estos paneles NO pintan su propio fondo (se quiere que se vea la imagen del contentPane)
		panelLogoGrande.setOpaque(false);
		panelLogoPequeno.setOpaque(false);

		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null); // layout absoluto: las posiciones se fijan con setBounds

		contentPane.add(panelLogin);
		contentPane.add(panelRegistro);
		panelLogoGrande.setBounds(38, 80, 350, 350);
		contentPane.add(panelLogoGrande);

		panelLogoPequeno.setBounds(165, 10, 120, 120);
		contentPane.add(panelLogoPequeno);

		// Por defecto el logo pequeño está oculto 
		panelLogoPequeno.setVisible(false);
	}

	/**
	 * Crea y devuelve un JPanel cuyos componentes se pintan sobre una imagen de fondo.
	 * La imagen se escala al tamaño del panel usando drawImage.
	 *
	 * @param rutaImagen ruta relativa en el classpath (por ejemplo: "/fondo1.png")
	 * @return JPanel con comportamiento personalizado en paintComponent
	 */
	public static JPanel crearPanelconImagen(String rutaImagen) {
		return new JPanel() {
			private static final long serialVersionUID = 1L;
			// Imagen que se dibujará como fondo del panel
			private Image backgroundImage = new ImageIcon(Inicio.class.getResource(rutaImagen)).getImage();

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				// Dibuja la imagen de fondo estirada para cubrir todo el panel
				g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
			}
		};

	}

	/**
	 * Método utilitario para simular un placeholder en JTextField/JPasswordField.
	 */
	public void placeholder(String texto, Color color, JTextField textField) {
	    textField.setForeground(color);
	    if (textField.getText().isEmpty()) {
	        // Campo vacío: mostrar placeholder
	        textField.setText(texto);
	        textField.putClientProperty("placeholder", Boolean.TRUE);
	        if (textField instanceof JPasswordField) {
	            // Mostrar texto de placeholder para JPasswordField (no ocultar con asteriscos)
	            ((JPasswordField) textField).setEchoChar((char) 0);
	        }
	    } else {
	        // Campo ya contiene texto real
	        textField.putClientProperty("placeholder", Boolean.FALSE);
	        if (textField instanceof JPasswordField) {
	            ((JPasswordField) textField).setEchoChar((char) '*');
	        }
	    }
	    // Eliminamos listeners de foco previos para no acumular múltiples listeners iguales
	    FocusListener[] focusListeners = textField.getFocusListeners();
	    for (FocusListener listener : focusListeners) {
	        textField.removeFocusListener(listener);
	    }
	    // Añadimos listener que gestiona la aparición/desaparición del placeholder según el foco
	    textField.addFocusListener(new FocusAdapter() {
	        @Override
	        public void focusGained(FocusEvent e) {
	            if (textField.getText().equals(texto)) {
	                // Al recibir el foco, si está el placeholder, lo borramos para que el usuario escriba
	                textField.setText("");
	                textField.setForeground(Color.BLACK);
	                textField.putClientProperty("placeholder", Boolean.FALSE);
	                if (textField instanceof JPasswordField) {
	                    // Restauramos el echo char para ocultar la contraseña al escribir
	                    ((JPasswordField) textField).setEchoChar((char) '*');
	                }
	            }
	        }

	        @Override
	        public void focusLost(FocusEvent e) {
	            if (textField.getText().isEmpty()) {
	                // Si al perder foco el campo queda vacío, mostramos de nuevo el placeholder
	                textField.setForeground(color);
	                textField.setText(texto);
	                textField.putClientProperty("placeholder", Boolean.TRUE);
	                if (textField instanceof JPasswordField) {
	                    // Mostrar placeholder sin ocultación
	                    ((JPasswordField) textField).setEchoChar((char) 0);
	                }
	            } else {
	                // Si hay texto, aseguramos que el placeholder está desactivado
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
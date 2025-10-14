package vista;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Registro extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panelLogin ;
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Registro frame = new Registro();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Registro() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 697, 699);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		contentPane = new JPanel() {
            private static final long serialVersionUID = 1L;
            private Image backgroundImage = new ImageIcon("fotos/logo.png").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panelLogin.setBackground(new Color(255, 255, 255));
		panelLogin.setBounds(150, 207, 374, 247);
		contentPane.add(panelLogin);
		panelLogin.setVisible(false);
		panelLogin.setLayout(null);
		
		
		JLabel lblNewLabel = new JLabel("User");
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setBounds(42, 38, 111, 14);
		panelLogin.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setBounds(42, 63, 275, 20);
		panelLogin.add(textField);
		textField.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setForeground(new Color(255, 255, 255));
		lblPassword.setBounds(42, 94, 111, 14);
		panelLogin.add(lblPassword);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(42, 119, 275, 20);
		panelLogin.add(textField_1);
		
		JLabel lblNewLabel_1 = new JLabel("¿No tiene cuenta?");
		lblNewLabel_1.setForeground(new Color(255, 255, 255));
		lblNewLabel_1.setBounds(132, 198, 125, 14);
		panelLogin.add(lblNewLabel_1);
		
		JButton btnNewButton = new JButton("Iniciar Sesion");
		btnNewButton.setBounds(122, 164, 125, 23);
		panelLogin.add(btnNewButton);

	}

}

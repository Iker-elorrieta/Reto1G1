package vista;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Ejercicio extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Font fuenteBold = new Font("Raleway", Font.BOLD, 20);
	private JButton btnAtras;
	private JButton btnEmpezar;

	public Ejercicio() {
		setTitle("Squad Gym - Ejercicios");
		setResizable(false);
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1000, 650);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(null);
		contentPane.setOpaque(true);
		contentPane.setBackground(Color.WHITE);
		contentPane.setLayout(null);
		setContentPane(contentPane);

		JPanel panelAtras = new JPanel() {
			private static final long serialVersionUID = 1L;
			private Image backgroundImage;
			{
				backgroundImage = new ImageIcon(Inicio.class.getResource("/fondo1.png")).getImage();
				setOpaque(false); // hacer el panel transparente
			}

			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				try {
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					int arc = 30;
					RoundRectangle2D round = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), arc, arc);
					g2.setClip(round);
					if (backgroundImage != null) {
						g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
					} else {
						g2.setColor(getBackground());
						g2.fill(round);
					}
					super.paintComponent(g2);
				} finally {
					g2.dispose();
				}
			}
		};

		panelAtras.setBorder(null);
		panelAtras.setBounds(0, 0, 984, 611);
		panelAtras.setLayout(null);
		panelAtras.setOpaque(false);
		contentPane.add(panelAtras);

		JPanel panelLogo = new JPanel() {
			private static final long serialVersionUID = 1L;
			private Image backgroundImage = new ImageIcon(Inicio.class.getResource("/logo.png")).getImage();

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
			}
		};
		panelLogo.setBounds(10, 11, 170, 170);
		panelLogo.setOpaque(false);
		panelAtras.add(panelLogo);
		
		btnEmpezar = new JButton("Empezar");
		btnEmpezar.setForeground(Color.BLACK);
		btnEmpezar.setFont(new Font("Dialog", Font.BOLD, 20));
		btnEmpezar.setBackground(Color.WHITE);
		btnEmpezar.setBounds(421, 489, 123, 38);
		panelAtras.add(btnEmpezar);
		
		btnAtras = new JButton("Atrás");
		btnAtras.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Ejercicio.this.dispose();
			}
		});
		btnAtras.setBounds(10, 564, 123, 38);
		contentPane.add(btnAtras);
		btnAtras.setForeground(Color.BLACK);
		btnAtras.setFont(fuenteBold);
		btnAtras.setBackground(Color.WHITE);
	}

	public static Color colorTexto(Color fondo) {
		double iluminacion = (0.299 * fondo.getRed() + 0.587 * fondo.getGreen() + 0.114 * fondo.getBlue()) / 255;
		return iluminacion < 0.5 ? Color.WHITE : Color.BLACK;
	}


	public JButton getBtnAtras() {
		return btnAtras;
	}
}

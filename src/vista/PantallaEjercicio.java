package vista;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import javax.swing.SwingConstants;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PantallaEjercicio extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblNombreWorkout;
	private JLabel lblWorkoutDescripcion;
	private JLabel lblNombreEjercicio;
	private JLabel lblEjercicioDescripcion;
	private JPanel panelEjercicio;
	private JPanel panelSeries;

	/**
	 * Create the frame.
	 */
	public PantallaEjercicio() {
		setTitle("Squad Gym - Ejercicio");
		setResizable(false);
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 810, 537);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panelIzquierda = new JPanel() {
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
		panelIzquierda.setLayout(null);
		panelIzquierda.setOpaque(false);
		panelIzquierda.setBorder(null);
		panelIzquierda.setBounds(10, 11, 771, 128);
		contentPane.add(panelIzquierda);
		panelEjercicio = new JPanel() {
			private static final long serialVersionUID = 1L;
			{
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

					g2.setColor(getBackground());
					g2.fill(round);

					super.paintComponent(g2);
				} finally {
					g2.dispose();
				}
			}
		};
		panelEjercicio.setBackground(new Color(195, 195, 195));
		panelEjercicio.setLayout(null);
		panelEjercicio.setOpaque(false);
		panelEjercicio.setBorder(null);
		panelEjercicio.setBounds(10, 150, 771, 325);
		contentPane.add(panelEjercicio);

		lblNombreEjercicio = new JLabel("Workout");
		lblNombreEjercicio.setForeground(Color.BLACK);
		lblNombreEjercicio.setFont(new Font("Raleway", Font.BOLD, 30));
		lblNombreEjercicio.setBounds(14, 11, 650, 36);
		panelEjercicio.add(lblNombreEjercicio);

		lblEjercicioDescripcion = new JLabel("Descripcion");
		lblEjercicioDescripcion.setVerticalAlignment(SwingConstants.TOP);
		lblEjercicioDescripcion.setHorizontalAlignment(SwingConstants.LEFT);
		lblEjercicioDescripcion.setForeground(Color.BLACK);
		lblEjercicioDescripcion.setFont(new Font("Raleway", Font.PLAIN, 15));
		lblEjercicioDescripcion.setBounds(14, 57, 650, 36);
		panelEjercicio.add(lblEjercicioDescripcion);

		JLabel lblCronometroEjercicio = new JLabel("00:00");
		lblCronometroEjercicio.setForeground(Color.BLACK);
		lblCronometroEjercicio.setFont(new Font("Raleway", Font.BOLD, 30));
		lblCronometroEjercicio.setBounds(674, 11, 87, 36);
		panelEjercicio.add(lblCronometroEjercicio);

		JLabel lblNombreCronoDescanso = new JLabel("Descanso:");
		lblNombreCronoDescanso.setBounds(674, 47, 87, 14);
		panelEjercicio.add(lblNombreCronoDescanso);
		lblNombreCronoDescanso.setForeground(new Color(0, 0, 0));

		JLabel lblCronometroDescanso = new JLabel("00:45");
		lblCronometroDescanso.setBounds(674, 61, 46, 14);
		panelEjercicio.add(lblCronometroDescanso);
		lblCronometroDescanso.setForeground(new Color(0, 0, 0));
		
		panelSeries = new JPanel();
		panelSeries.setBackground(new Color(195, 195, 195));
		panelSeries.setBounds(14, 86, 747, 228);
		panelEjercicio.add(panelSeries);
		JLabel lblCronometroWorkout = new JLabel("00:00");
		lblCronometroWorkout.setBounds(674, 11, 87, 36);
		panelIzquierda.add(lblCronometroWorkout);
		lblCronometroWorkout.setFont(new Font("Raleway", Font.BOLD, 30));
		lblCronometroWorkout.setForeground(new Color(255, 255, 255));

		JPanel panelLogo = new JPanel() {
			private static final long serialVersionUID = 1L;
			private Image backgroundImage = new ImageIcon(Inicio.class.getResource("/logo.png")).getImage();

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
			}
		};
		panelLogo.setOpaque(false);
		panelLogo.setBounds(14, 11, 100, 100);
		panelIzquierda.add(panelLogo);

		lblNombreWorkout = new JLabel("Workout");
		lblNombreWorkout.setBounds(133, 11, 531, 36);
		panelIzquierda.add(lblNombreWorkout);
		lblNombreWorkout.setFont(new Font("Raleway", Font.BOLD, 30));
		lblNombreWorkout.setForeground(new Color(255, 255, 255));

		lblWorkoutDescripcion = new JLabel("Descripcion");
		lblWorkoutDescripcion.setVerticalAlignment(SwingConstants.TOP);
		lblWorkoutDescripcion.setHorizontalAlignment(SwingConstants.LEFT);
		lblWorkoutDescripcion.setBounds(133, 58, 531, 59);
		panelIzquierda.add(lblWorkoutDescripcion);
		lblWorkoutDescripcion.setForeground(new Color(255, 255, 255));
		lblWorkoutDescripcion.setFont(new Font("Raleway", Font.PLAIN, 15));

		JButton btnNewButton_1 = new JButton("");
		btnNewButton_1.setBounds(674, 58, 89, 23);
		panelIzquierda.add(btnNewButton_1);
		btnNewButton_1.setBackground(new Color(51, 153, 0));

		JButton btnNewButton = new JButton("Salir");
		btnNewButton.setBounds(674, 93, 89, 23);
		panelIzquierda.add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton.setForeground(Color.WHITE);
		btnNewButton.setBackground(Color.RED);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});

	}

	public JLabel getLblNombreEjercicio() {
		return lblNombreEjercicio;
	}

	public void setLblNombreEjercicio(JLabel lblNombreEjercicio) {
		this.lblNombreEjercicio = lblNombreEjercicio;
	}

	public JLabel getLblNombreWorkout() {
		return lblNombreWorkout;
	}

	public void setLblNombreWorkout(JLabel lblNombreWorkout) {
		this.lblNombreWorkout = lblNombreWorkout;
	}

	public JLabel getLblEjercicioDescripcion() {
		return lblEjercicioDescripcion;
	}

	public void setLblEjercicioDescripcion(JLabel lblEjercicioDescripcion) {
		this.lblNombreEjercicio = lblEjercicioDescripcion;
	}

	public JLabel getLblWorkoutDescripcion() {
		return lblWorkoutDescripcion;
	}

	public void setLblWorkoutDescripcion(JLabel lblWorkoutDescripcion) {
		this.lblWorkoutDescripcion = lblWorkoutDescripcion;
	}

	public JPanel crearSerie(String nombreSerie, String foto, int posicion) {
		int panelWidth = 200;
		int panelHeight = 200;
		int startX = 14;
		int startY = 104;
		int gap = 20;

		int x = startX + posicion * (panelWidth + gap);

		JPanel panelSerie1 = new JPanel() {
			private static final long serialVersionUID = 1L;
			{
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

					g2.setColor(getBackground());
					g2.fill(round);

					super.paintComponent(g2);
				} finally {
					g2.dispose();
				}
			}
		};
		panelSerie1.setBounds(x, startY, panelWidth, panelHeight);
		panelSerie1.setForeground(Color.WHITE);
		panelSerie1.setBackground(new Color(255, 255, 255));
		panelSerie1.setLayout(null);

		JPanel panelImagenSerie = new JPanel() {
			private static final long serialVersionUID = 1L;
			private Image backgroundImage;

			{
				if (foto != null) {
					try {
						var req = HttpRequest.newBuilder(URI.create(foto)).build();
						var res = HttpClient.newHttpClient().send(req, HttpResponse.BodyHandlers.ofInputStream());
						backgroundImage = ImageIO.read(res.body());
					} catch (Exception e) {

					}
				}
			}

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (backgroundImage != null) {
					g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
				}
			}
		};
		panelImagenSerie.setOpaque(false);
		panelImagenSerie.setBounds(30, 0, 130, 130);
		panelSerie1.add(panelImagenSerie);

		JLabel lblNombreSerie = new JLabel(nombreSerie);
		lblNombreSerie.setForeground(Color.BLACK);
		lblNombreSerie.setFont(new Font("Raleway", Font.BOLD, 15));
		lblNombreSerie.setBounds(10, 138, 150, 26);
		panelSerie1.add(lblNombreSerie);

		JLabel lblCronometroSerie = new JLabel("00:00");
		lblCronometroSerie.setForeground(Color.BLACK);
		lblCronometroSerie.setFont(new Font("Raleway", Font.BOLD, 15));
		lblCronometroSerie.setBounds(10, 163, 150, 26);
		panelSerie1.add(lblCronometroSerie);

		return panelSerie1;
	}

	public JPanel getPanelSeries() {
		return panelSeries;
	}

	public void setPanelSeries(JPanel panelSeries) {
		this.panelSeries = panelSeries;
	}


}

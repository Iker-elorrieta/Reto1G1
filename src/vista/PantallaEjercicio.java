package vista;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import modelo.Ejercicio;
import modelo.Serie;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.geom.RoundRectangle2D;
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
import java.util.ArrayList;
import java.util.List;

public class PantallaEjercicio extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblNombreWorkout;
	private JLabel lblWorkoutDescripcion;
	private JLabel lblNombreEjercicio;
	private JLabel lblEjercicioDescripcion;
	private JPanel panelEjercicio;
	private JPanel panelSeries;
	private JLabel lblCronometroDescanso;
	public List<JPanel> panelSeriesPorEjercicio = new ArrayList<>();
	private JLabel lblCronometroWorkout;
	private JLabel lblCronometroEjercicio;
	private JLabel lblCronometroPreparacion;
	private JButton btnEmpezar; // Ahora sirve para Empezar/Pausar/Reanudar
	private JButton btnSalir;
	private JButton btnSiguiente;

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
		lblNombreEjercicio.setBounds(14, 11, 521, 36);
		panelEjercicio.add(lblNombreEjercicio);

		lblEjercicioDescripcion = new JLabel("Descripcion");
		lblEjercicioDescripcion.setVerticalAlignment(SwingConstants.TOP);
		lblEjercicioDescripcion.setHorizontalAlignment(SwingConstants.LEFT);
		lblEjercicioDescripcion.setForeground(Color.BLACK);
		lblEjercicioDescripcion.setFont(new Font("Raleway", Font.PLAIN, 15));
		lblEjercicioDescripcion.setBounds(14, 57, 650, 20);
		panelEjercicio.add(lblEjercicioDescripcion);

		lblCronometroEjercicio = new JLabel("00:00");
		lblCronometroEjercicio.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCronometroEjercicio.setForeground(Color.BLACK);
		lblCronometroEjercicio.setFont(new Font("Raleway", Font.BOLD, 30));
		lblCronometroEjercicio.setBounds(674, 11, 87, 36);
		panelEjercicio.add(lblCronometroEjercicio);

		lblCronometroDescanso = new JLabel("");
		lblCronometroDescanso.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCronometroDescanso.setBounds(644, 48, 117, 14);
		panelEjercicio.add(lblCronometroDescanso);
		lblCronometroDescanso.setForeground(new Color(0, 0, 0));

		panelSeries = new JPanel();
		panelSeries.setBackground(new Color(195, 195, 195));
		panelSeries.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
		panelSeries.setBounds(0, 86, 761, 239);
		panelEjercicio.add(panelSeries);
		
		lblCronometroPreparacion = new JLabel("");
		lblCronometroPreparacion.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCronometroPreparacion.setForeground(Color.BLACK);
		lblCronometroPreparacion.setBounds(644, 48, 117, 14);
		panelEjercicio.add(lblCronometroPreparacion);
		lblCronometroWorkout = new JLabel("00:00");
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

		btnEmpezar = new JButton("Empezar");
		btnEmpezar.setBounds(567, 58, 89, 23);
		panelIzquierda.add(btnEmpezar);
		btnEmpezar.setBackground(new Color(51, 153, 0));
		btnEmpezar.setForeground(Color.WHITE);
		btnEmpezar.setFont(new Font("Raleway", Font.BOLD, 12));

		btnSiguiente = new JButton("Siguiente");
		btnSiguiente.setBounds(567, 93, 89, 23);
		panelIzquierda.add(btnSiguiente);
		btnSiguiente.setBackground(new Color(0, 102, 204));
		btnSiguiente.setForeground(Color.WHITE);
		btnSiguiente.setFont(new Font("Raleway", Font.BOLD, 12));
		btnSiguiente.setVisible(false);

		btnSalir = new JButton("Salir");
		btnSalir.setBounds(674, 58, 89, 46);
		panelIzquierda.add(btnSalir);
	
		btnSalir.setForeground(Color.WHITE);
		btnSalir.setBackground(Color.RED);

	}
	
	

	public JLabel getLblCronometroPreparacion() {
		return lblCronometroPreparacion;
	}



	public void setLblCronometroPreparacion(JLabel lblCronometroPreparacion) {
		this.lblCronometroPreparacion = lblCronometroPreparacion;
	}



	public JButton getBtnEmpezar() {
		return btnEmpezar;
	}

	public void setBtnEmpezar(JButton btnEmpezar) {
		this.btnEmpezar = btnEmpezar;
	}

	public JButton getBtnSalir() {
		return btnSalir;
	}

	public void setBtnSalir(JButton btnSalir) {
		this.btnSalir = btnSalir;
	}

	public JButton getBtnSiguiente() {
		return btnSiguiente;
	}

	public void setBtnSiguiente(JButton btnSiguiente) {
		this.btnSiguiente = btnSiguiente;
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

	public JLabel getLblCronometroDescanso() {
		return lblCronometroDescanso;
	}

	public void setLblCronometroDescanso(JLabel lblCronometroDescanso) {
		this.lblCronometroDescanso = lblCronometroDescanso;
	}
	

	public JLabel getLblCronometroWorkout() {
		return lblCronometroWorkout;
	}

	public void setLblCronometroWorkout(JLabel lblCronometroWorkout) {
		this.lblCronometroWorkout = lblCronometroWorkout;
	}

	public JLabel getLblCronometroEjercicio() {
		return lblCronometroEjercicio;
	}

	public void setLblCronometroEjercicio(JLabel lblCronometroEjercicio) {
		this.lblCronometroEjercicio = lblCronometroEjercicio;
	}
	

	public JPanel getPanelSeries() {
		return panelSeries;
	}

	public void setPanelSeries(JPanel panelSeries) {
		this.panelSeries = panelSeries;
	}

	public void mostrarPanelSeries(int i) {
		panelSeries.removeAll();
		panelSeries.add(panelSeriesPorEjercicio.get(i));
		panelSeries.revalidate();
		panelSeries.repaint();
	}

	public JLabel crearLabelCrono() {
		JLabel lbl = new JLabel("00:00");
		return lbl;
	}

	public JPanel crearPanelSeriesEjercicio(Ejercicio ej) {
	    JPanel panel = new JPanel();
	    panel.setOpaque(false);
	    panel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));

	    for (Serie s : ej.getSeries()) {

	        JPanel panelSerie = new JPanel() {
	            private static final long serialVersionUID = 1L;
	            {
	                setOpaque(false);
	            }

	            @Override
	            protected void paintComponent(Graphics g) {
	                Graphics2D g2 = (Graphics2D) g.create();
	                try {
	                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	                    int arc = 30;
	                    RoundRectangle2D round = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), arc, arc);
	                    g2.setClip(round);

	                    g2.setColor(new Color(255, 255, 255));
	                    g2.fill(round);

	                    super.paintComponent(g2);
	                } finally {
	                    g2.dispose();
	                }
	            }
	        };

	        panelSerie.setPreferredSize(new Dimension(200, 200));
	        panelSerie.setForeground(Color.WHITE);
	        panelSerie.setBackground(new Color(255, 255, 255));
	        panelSerie.setLayout(null);

	        // 📌 Imagen de la serie
	        JPanel panelImagenSerie = new JPanel() {
	            private static final long serialVersionUID = 1L;
	            private Image backgroundImage;

	            {
	                if (s.getFoto() != null) {
	                    try {
	                        var req = HttpRequest.newBuilder(URI.create(s.getFoto())).build();
	                        var res = HttpClient.newHttpClient().send(req, HttpResponse.BodyHandlers.ofInputStream());
	                        backgroundImage = ImageIO.read(res.body());
	                    } catch (Exception e) {}
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
	        panelSerie.add(panelImagenSerie);

	        JLabel lblNombreSerie = new JLabel(s.getNombre());
	        lblNombreSerie.setForeground(Color.BLACK);
	        lblNombreSerie.setFont(new Font("Raleway", Font.BOLD, 15));
	        lblNombreSerie.setBounds(10, 138, 150, 26);
	        panelSerie.add(lblNombreSerie);

	       int mins = s.getTiempo() / 60;
	       int secs = s.getTiempo() % 60;
	        String tiempoInicial = String.format("%02d:%02d", mins, secs);
	        JLabel lblCronometroSerie = new JLabel(tiempoInicial);
	        lblCronometroSerie.setForeground(Color.BLACK);
	        lblCronometroSerie.setFont(new Font("Raleway", Font.BOLD, 15));
	        lblCronometroSerie.setBounds(10, 163, 150, 26);
	        panelSerie.add(lblCronometroSerie);

	        s.setCronometro(lblCronometroSerie);

	        panel.add(panelSerie);
	    }

	    return panel;
	}
}

package vista;

import java.util.Collections;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.DefaultComboBoxModel;

public class HistoricoWorkouts extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private DefaultTableModel modeloEjercicios;
	private Font fuenteBold = new Font("Raleway", Font.BOLD, 20);
	private DefaultComboBoxModel<String> modeloComboBox = new DefaultComboBoxModel<String>();
	private JTable table;
	private JButton btnAtras;

	public HistoricoWorkouts() {
		setTitle("Squad Gym - Historial Workouts");
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

		panelIzquierda.setBorder(null);
		panelIzquierda.setBounds(10, 10, 964, 194);
		panelIzquierda.setLayout(null);
		panelIzquierda.setOpaque(false);
		contentPane.add(panelIzquierda);

		JPanel panelLogo = new JPanel() {
			private static final long serialVersionUID = 1L;
			private Image backgroundImage = new ImageIcon(Inicio.class.getResource("/logo.png")).getImage();

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
			}
		};
		panelLogo.setBounds(30, 10, 170, 170);
		panelLogo.setOpaque(false);
		panelIzquierda.add(panelLogo);

		JLabel lblWorkouts = new JLabel("Historial de workouts");
		lblWorkouts.setForeground(new Color(255, 255, 255));
		lblWorkouts.setBounds(227, 75, 647, 51);
		panelIzquierda.add(lblWorkouts);
		lblWorkouts.setFont(new Font("Raleway", Font.BOLD, 30));

		/************** TABLA HISTORIAL WORKOUTS **************/
		modeloEjercicios = new DefaultTableModel(new String[] { "ID", "Nombre", "Descripción", "Descanso" }, 0);
		// Ordenar por fecha
		TableRowSorter<TableModel> sortEjercicios = new TableRowSorter<>(modeloEjercicios);
		sortEjercicios.setSortKeys(Collections.singletonList(new RowSorter.SortKey(1, SortOrder.ASCENDING)));

		modeloEjercicios = new DefaultTableModel(new String[] { "Nombre Workout", "Nivel", "Tiempo Total",
				"Tiempo Previsto", "Fecha", "% Ejercicios Completados" }, 0);
		table = new JTable(modeloEjercicios);
		table.setFont(new Font("Raleway", Font.PLAIN, 15));
		table.getTableHeader().setFont(new Font("Raleway", Font.BOLD, 15));
		table.setRowHeight(25);
		// Desactivar ediciones
		table.setDefaultEditor(Object.class, null);

		JScrollPane scrollPaneHistorial = new JScrollPane();
		scrollPaneHistorial.setBounds(10, 230, 964, 323);
		scrollPaneHistorial.setViewportView(table);
		contentPane.add(scrollPaneHistorial);

		btnAtras = new JButton("Atrás");
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

	public DefaultTableModel getModeloEjercicios() {
		return modeloEjercicios;
	}

	public void setModeloEjercicios(DefaultTableModel modeloEjercicios) {
		this.modeloEjercicios = modeloEjercicios;
	}

	public DefaultComboBoxModel<String> getModeloComboBox() {
		return modeloComboBox;
	}

	public void setModeloComboBox(DefaultComboBoxModel<String> modeloComboBox) {
		this.modeloComboBox = modeloComboBox;
	}

	public JButton getBtnAtras() {
		return btnAtras;
	}

}

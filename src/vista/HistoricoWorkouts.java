package vista;

import java.util.ArrayList;
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
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class HistoricoWorkouts extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private DefaultTableModel modeloWorkouts;
	private DefaultTableModel modeloEjercicios;
	private JTable tableWorkouts;
	private Font fuenteBold = new Font("Raleway", Font.BOLD, 20);
	private DefaultComboBoxModel<String> modeloComboBox = new DefaultComboBoxModel<String>();
	private JTable table;
	private JButton btnAtras;
	
	public HistoricoWorkouts() {
		setTitle("Daji Squad Gym");
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

				setOpaque(false); // keep transparent so rounded shape shows
			}

			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				try {
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					int arc = 30;
					RoundRectangle2D round = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), arc, arc);

					// Clip to rounded rectangle so children are painted inside the rounded area
					g2.setClip(round);

					// Draw background image inside the clipped area
					if (backgroundImage != null) {
						g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
					} else {
						// optional: fill with background color inside the rounded shape
						g2.setColor(getBackground());
						g2.fill(round);
					}

					// Paint children using the clipped Graphics2D
					super.paintComponent(g2);
				} finally {
					g2.dispose();
				}
			}
		};

		panelIzquierda.setBorder(null);
		panelIzquierda.setBounds(10, 10, 304, 590);
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
		panelLogo.setBounds(30, 10, 230, 230);
		panelLogo.setOpaque(false);
		panelIzquierda.add(panelLogo);
		
		btnAtras = new JButton("ATRAS");
		btnAtras.setForeground(Color.BLACK);
		btnAtras.setFont(new Font("Dialog", Font.BOLD, 20));
		btnAtras.setBackground(Color.WHITE);
		btnAtras.setBounds(10, 541, 284, 38);
		panelIzquierda.add(btnAtras);

		// TABLA VIAJES

		modeloWorkouts = new DefaultTableModel(
				new String[] { "ID", "Nivel", "Nombre", "Descripcion", "URLVideo", "Video" }, 0);
		tableWorkouts = new JTable(modeloWorkouts);
		tableWorkouts.setFont(new Font("Raleway", Font.PLAIN, 15));
		tableWorkouts.getTableHeader().setFont(new Font("Raleway", Font.PLAIN, 15));
		tableWorkouts.setRowHeight(25);
		// Desactivar ediciones
		tableWorkouts.setDefaultEditor(Object.class, null);
		// Ocultar columna ViajeID
		tableWorkouts.getColumnModel().getColumn(0).setMinWidth(0);
		tableWorkouts.getColumnModel().getColumn(0).setMaxWidth(0);
		// Mostrar nivel corto
		tableWorkouts.getColumnModel().getColumn(1).setMinWidth(60);
		tableWorkouts.getColumnModel().getColumn(1).setMaxWidth(60);
		// Ocultar columna URLVIDEO
		tableWorkouts.getColumnModel().getColumn(4).setMinWidth(0);
		tableWorkouts.getColumnModel().getColumn(4).setMaxWidth(0);
		// Mostrar columna ver video corta
		tableWorkouts.getColumnModel().getColumn(5).setMinWidth(80);
		tableWorkouts.getColumnModel().getColumn(5).setMaxWidth(80);
		// Mostrar nombre corto
		tableWorkouts.getColumnModel().getColumn(2).setMinWidth(150);
		tableWorkouts.getColumnModel().getColumn(2).setMaxWidth(150);
		// Desactivar mover columnas
		tableWorkouts.getTableHeader().setReorderingAllowed(false);
		// Ordenar por fecha de inicio
		TableRowSorter<TableModel> sort = new TableRowSorter<>(modeloWorkouts);
		tableWorkouts.setRowSorter(sort);
		sort.setSortKeys(Collections.singletonList(new RowSorter.SortKey(1, SortOrder.ASCENDING)));

		JScrollPane scrollPaneWorkouts = new JScrollPane();
		scrollPaneWorkouts.setBounds(324, 91, 650, 208);
		scrollPaneWorkouts.getViewport().setBackground(Color.WHITE);
		contentPane.add(scrollPaneWorkouts);
		scrollPaneWorkouts.setViewportView(tableWorkouts);

		// TABLA EVENTOS

		modeloEjercicios = new DefaultTableModel(new String[] { "ID", "Nombre", "Descripción", "Descanso" }, 0);
		// Ordenar por fecha
		TableRowSorter<TableModel> sortEjercicios = new TableRowSorter<>(modeloEjercicios);
		sortEjercicios.setSortKeys(Collections.singletonList(new RowSorter.SortKey(1, SortOrder.ASCENDING)));

		// LABELS

		JLabel lblWorkouts = new JLabel("Workouts");
		lblWorkouts.setFont(new Font("Raleway", Font.PLAIN, 30));
		lblWorkouts.setBounds(327, 29, 160, 51);
		contentPane.add(lblWorkouts);
				
		modeloEjercicios = new DefaultTableModel(
		        new String[] { "Nombre Workout", "Nivel", "Tiempo Total", "Tiempo Previsto", "Fecha", "% Ejercicios Completados" }, 0);
		table = new JTable(modeloEjercicios);
		table.setFont(new Font("Raleway", Font.PLAIN, 15));
		table.getTableHeader().setFont(new Font("Raleway", Font.BOLD, 15));
		table.setRowHeight(25);
		// Desactivar ediciones
		table.setDefaultEditor(Object.class, null);

		JScrollPane scrollPaneHistorial = new JScrollPane();
		scrollPaneHistorial.setBounds(324, 344, 650, 234);
		scrollPaneHistorial.setViewportView(table);
		contentPane.add(scrollPaneHistorial);
	}

	public static Color colorTexto(Color fondo) {
		double iluminacion = (0.299 * fondo.getRed() + 0.587 * fondo.getGreen() + 0.114 * fondo.getBlue()) / 255;
		return iluminacion < 0.5 ? Color.WHITE : Color.BLACK;
	}
	
	

	public DefaultTableModel getModeloWorkouts() {
		return modeloWorkouts;
	}

	public void setModeloWorkouts(DefaultTableModel modeloWorkouts) {
		this.modeloWorkouts = modeloWorkouts;
	}

	public DefaultTableModel getModeloEjercicios() {
		return modeloEjercicios;
	}

	public void setModeloEjercicios(DefaultTableModel modeloEjercicios) {
		this.modeloEjercicios = modeloEjercicios;
	}

	public JTable getTableWorkouts() {
		return tableWorkouts;
	}

	public void setTableWorkouts(JTable tableWorkouts) {
		this.tableWorkouts = tableWorkouts;
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

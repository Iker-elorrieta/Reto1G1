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
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Workouts extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private DefaultTableModel modeloWorkouts;
	private DefaultTableModel modeloEjercicios;
	private JTable tableWorkouts;
	private JTable tableEjercicios;
	private JLabel lblEjercicios;
	private JScrollPane scrollPaneEjercicios;
	private Font fuenteBold = new Font("Raleway", Font.BOLD, 20);
	private JButton btnEmpezarWorkout;
	private JButton btnHistoricoWorkouts;
	private JButton btnDesconectar;
	private JButton btnEditarPerfil;
	private DefaultComboBoxModel<String> modeloComboBox = new DefaultComboBoxModel<String>();
	private JComboBox<String> comboBox;
	private JLabel lblBackups;
	private JLabel lblNivel;

	public Workouts() {
		setTitle("Squad Gym - Workouts");
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
		panelIzquierda.setBounds(10, 10, 304, 590);
		panelIzquierda.setLayout(null);
		panelIzquierda.setOpaque(false);
		contentPane.add(panelIzquierda);

		btnDesconectar = new JButton("Desconectar");
		btnDesconectar.setForeground(new Color(0, 0, 0));
		btnDesconectar.setFont(fuenteBold);
		btnDesconectar.setBackground(new Color(255, 255, 255));
		btnDesconectar.setBounds(10, 540, 284, 38);
		panelIzquierda.add(btnDesconectar);

		btnEditarPerfil = new JButton("Editar perfil");
		btnEditarPerfil.setForeground(new Color(0, 0, 0));
		btnEditarPerfil.setFont(fuenteBold);
		btnEditarPerfil.setBackground(new Color(255, 255, 255));
		btnEditarPerfil.setBounds(10, 491, 284, 38);
		panelIzquierda.add(btnEditarPerfil);

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

		btnHistoricoWorkouts = new JButton("Historial de workouts");
		btnHistoricoWorkouts.setForeground(Color.BLACK);
		btnHistoricoWorkouts.setFont(fuenteBold);
		btnHistoricoWorkouts.setBackground(Color.WHITE);
		btnHistoricoWorkouts.setBounds(10, 442, 284, 38);
		panelIzquierda.add(btnHistoricoWorkouts);
		


		/************** TABLA WORKOUTS **************/
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
		sort.setSortKeys(Collections.singletonList(new RowSorter.SortKey(1, SortOrder.DESCENDING)));

		JScrollPane scrollPaneWorkouts = new JScrollPane();
		scrollPaneWorkouts.setBounds(327, 91, 647, 208);
		scrollPaneWorkouts.getViewport().setBackground(Color.WHITE);
		contentPane.add(scrollPaneWorkouts);
		scrollPaneWorkouts.setViewportView(tableWorkouts);

		/**************  TABLA EJERCICIOS **************/
		modeloEjercicios = new DefaultTableModel(new String[] { "ID", "Nombre", "Descripción", "Descanso" }, 0);
		tableEjercicios = new JTable(modeloEjercicios);
		tableEjercicios.setFont(new Font("Raleway", Font.PLAIN, 15));
		tableEjercicios.getTableHeader().setFont(new Font("Raleway", Font.PLAIN, 15));
		tableEjercicios.setRowHeight(25);
		// Desactivar ediciones
		tableEjercicios.setDefaultEditor(Object.class, null);
		// Ocultar columna EventoID
		tableEjercicios.getColumnModel().getColumn(0).setMinWidth(0);
		tableEjercicios.getColumnModel().getColumn(0).setMaxWidth(0);
		// Mostrar nombre corto
		tableEjercicios.getColumnModel().getColumn(1).setMinWidth(150);
		tableEjercicios.getColumnModel().getColumn(1).setMaxWidth(150);
		// Mostrat tiempo descanso corto
		tableEjercicios.getColumnModel().getColumn(3).setMinWidth(100);
		tableEjercicios.getColumnModel().getColumn(3).setMaxWidth(100);
		// Desactivar mover columnas
		tableEjercicios.getTableHeader().setReorderingAllowed(false);
		// Ordenar por fecha
		TableRowSorter<TableModel> sortEjercicios = new TableRowSorter<>(modeloEjercicios);
		tableEjercicios.setRowSorter(sortEjercicios);
		sortEjercicios.setSortKeys(Collections.singletonList(new RowSorter.SortKey(1, SortOrder.ASCENDING)));
		scrollPaneEjercicios = new JScrollPane();
		scrollPaneEjercicios.setBounds(327, 392, 647, 208);
		contentPane.add(scrollPaneEjercicios);
		scrollPaneEjercicios.setViewportView(tableEjercicios);
		scrollPaneEjercicios.getViewport().setBackground(Color.WHITE);
		btnEmpezarWorkout = new JButton("Empezar workout");
		btnEmpezarWorkout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
			}
		});
		btnEmpezarWorkout.setForeground(Color.BLACK);
		btnEmpezarWorkout.setFont(fuenteBold);
		btnEmpezarWorkout.setBackground(new Color(128, 255, 0));
		btnEmpezarWorkout.setBounds(759, 341, 215, 38);
		contentPane.add(btnEmpezarWorkout);

		comboBox = new JComboBox<String>();
		comboBox.setModel(modeloComboBox);
		comboBox.setFont(fuenteBold);
		comboBox.setBounds(759, 35, 215, 34);
		contentPane.add(comboBox);

		// LABELS
		JLabel lblWorkouts = new JLabel("Workouts");
		lblWorkouts.setFont(new Font("Raleway", Font.PLAIN, 30));
		lblWorkouts.setBounds(327, 29, 160, 51);
		contentPane.add(lblWorkouts);
		
		lblBackups = new JLabel("");
		lblBackups.setForeground(new Color(128, 255, 0));
		lblBackups.setBounds(10, 417, 284, 14);
		lblBackups.setFont(new Font("Raleway", Font.BOLD, 15));
		panelIzquierda.add(lblBackups);
		
		lblNivel = new JLabel("");
		lblNivel.setForeground(new Color(255, 255, 255));
		lblNivel.setFont(new Font("Raleway", Font.BOLD, 15));
		lblNivel.setBounds(10, 393, 284, 14);
		panelIzquierda.add(lblNivel);

		lblEjercicios = new JLabel("Ejercicios");
		lblEjercicios.setFont(new Font("Raleway", Font.PLAIN, 30));
		lblEjercicios.setBounds(327, 330, 139, 51);
		contentPane.add(lblEjercicios);

		tableEjercicios.setVisible(false);
		lblEjercicios.setVisible(false);
		btnEmpezarWorkout.setVisible(false);
		scrollPaneEjercicios.setVisible(false);
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

	public JTable getTableEjercicios() {
		return tableEjercicios;
	}

	public void setTableEjercicios(JTable tableEjercicios) {
		this.tableEjercicios = tableEjercicios;
	}

	public JLabel getLblEventos() {
		return lblEjercicios;
	}

	public void setLblEventos(JLabel lblEventos) {
		this.lblEjercicios = lblEventos;
	}

	public JLabel getLblBackups() {
		return lblBackups;
	}

	public void setLblBackups(JLabel lblBackups) {
		this.lblBackups = lblBackups;
	}

	public JScrollPane getScrollPaneEventos() {
		return scrollPaneEjercicios;
	}

	public void setScrollPaneEventos(JScrollPane scrollPaneEventos) {
		this.scrollPaneEjercicios = scrollPaneEventos;
	}

	public JButton getBtnEmpezarWorkout() {
		return btnEmpezarWorkout;
	}

	public void setBtnEmpezarWorkout(JButton btnEmpezarWorkout) {
		this.btnEmpezarWorkout = btnEmpezarWorkout;
	}

	public JButton getBtnHistoricoWorkouts() {
		return btnHistoricoWorkouts;
	}

	public void setBtnHistoricoWorkouts(JButton btnHistoricoWorkouts) {
		this.btnHistoricoWorkouts = btnHistoricoWorkouts;
	}

	public JButton getBtnDesconectar() {
		return btnDesconectar;
	}
	
	public JButton getBtnEditarPerfil() {
	    return btnEditarPerfil;
	}

	public void setBtnDesconectar(JButton btnDesconectar) {
		this.btnDesconectar = btnDesconectar;
	}

	public DefaultComboBoxModel<String> getModeloComboBox() {
		return modeloComboBox;
	}

	public void setModeloComboBox(DefaultComboBoxModel<String> modeloComboBox) {
		this.modeloComboBox = modeloComboBox;
	}

	public JComboBox<String> getComboBox() {
		return comboBox;
	}

	public void setComboBox(JComboBox<String> comboBox) {
		this.comboBox = comboBox;
	}
	public JLabel getLblNivel() {
		return lblNivel;
	}

	public void setLblNivel(JLabel lblNivel) {
		this.lblNivel = lblNivel;
	}
}

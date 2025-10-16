package vista;


import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFrame;
import javax.swing.JPanel;


import java.awt.Color;
import java.awt.EventQueue;

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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import modelo.Ejercicio;
import modelo.Workout;




public class Workouts extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private DefaultTableModel modeloWorkouts;
	private DefaultTableModel modeloEjercicios;
	private JTable tableWorkouts;
	private JTable tableEjercicios;
	private JLabel lblEventos;
	private JScrollPane scrollPaneEventos;
	private Font fuenteBold = new Font("Raleway", Font.BOLD, 20);
	 
	
	public Workouts() {
		setTitle("Daji Squad Gym");
		setResizable(false);
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1000, 650);
		setLocationRelativeTo(null);
		
		contentPane = new JPanel();
		contentPane.setBorder(null);
		contentPane.setLayout(null);
		setContentPane(contentPane);

		JPanel panelIzquierda = new JPanel() {
            private static final long serialVersionUID = 1L;
            private Image backgroundImage;
            // load image once, try classpath first then fallback to file path
            {
                java.net.URL imgUrl = getClass().getResource("/fotos/fondo1.png");
                if (imgUrl != null) {
                    backgroundImage = new ImageIcon(imgUrl).getImage();
                } else {
                    backgroundImage = new ImageIcon("fotos/fondo1.png").getImage();
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                try {
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    int arc = 30;
                    // paint rounded background
                    g2.setColor(getBackground());
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), arc, arc));
                    // draw image scaled to panel size (if available)
                    if (backgroundImage != null) {
                        g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                    }
                } finally {
                    g2.dispose();
                }
                // allow normal painting of children/borders
                super.paintComponent(g);
            }
        };
		panelIzquierda.setBorder(null);
		panelIzquierda.setBounds(0, 0, 304, 700);
		panelIzquierda.setLayout(null);
		panelIzquierda.setOpaque(false);
		contentPane.add(panelIzquierda);

		JButton btnDesconectar = new JButton("Desconectar");
		btnDesconectar.setForeground(new Color(0, 0, 0));
		btnDesconectar.setFont(fuenteBold);
		btnDesconectar.setBackground(new Color(255, 255, 255));
		btnDesconectar.setBounds(10, 540, 284, 38);
		panelIzquierda.add(btnDesconectar);

		JButton btnGenerarOfertaViaje = new JButton("Editar perfil");
		btnGenerarOfertaViaje.setForeground(new Color(0, 0, 0));
		btnGenerarOfertaViaje.setFont(fuenteBold);
		btnGenerarOfertaViaje.setBackground(new Color(255, 255, 255));
		btnGenerarOfertaViaje.setBounds(10, 491, 284, 38);
		panelIzquierda.add(btnGenerarOfertaViaje);
		
		JPanel panelLogo = new JPanel() {
            private static final long serialVersionUID = 1L;
			private Image backgroundImage = new ImageIcon("fotos/logo.png").getImage(); 

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
		panelLogo.setBounds(10, 20, 280, 280);
		panelLogo.setOpaque(false);
		panelIzquierda.add(panelLogo);

		
		// TABLA VIAJES
		
		
		modeloWorkouts = new DefaultTableModel(new String[]{"ID", "Nombre", "Descripcion", "Nivel","Video"}, 0);
		tableWorkouts = new JTable(modeloWorkouts);
		tableWorkouts.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		tableWorkouts.getTableHeader().setFont(new Font("Segoe UI", Font.PLAIN, 15));
		tableWorkouts.setRowHeight(25);
		// Desactivar ediciones
		tableWorkouts.setDefaultEditor(Object.class, null);
		// Ocultar columna ViajeID
		tableWorkouts.getColumnModel().getColumn(0).setMinWidth(0);
		tableWorkouts.getColumnModel().getColumn(0).setMaxWidth(0);
		// Desactivar mover columnas
		tableWorkouts.getTableHeader().setReorderingAllowed(false);
		// Ordenar por fecha de inicio
		TableRowSorter<TableModel> sort = new TableRowSorter<>(modeloWorkouts);
		tableWorkouts.setRowSorter(sort);
		sort.setSortKeys(Collections.singletonList(new RowSorter.SortKey(1, SortOrder.ASCENDING)));
	
		
		JScrollPane scrollPaneViajes = new JScrollPane();
		scrollPaneViajes.setBounds(327, 91, 647, 208);
		scrollPaneViajes.getViewport().setBackground(Color.WHITE);
		contentPane.add(scrollPaneViajes);
		scrollPaneViajes.setViewportView(tableWorkouts);

		// TABLA EVENTOS
		

		modeloEjercicios = new DefaultTableModel(new String[]{"EventoID", "Nombre", "Tipo", "Fecha", "Precio"}, 0);
		tableEjercicios = new JTable(modeloEjercicios);
		tableEjercicios.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		tableEjercicios.getTableHeader().setFont(new Font("Segoe UI", Font.PLAIN, 15));
		tableEjercicios.setRowHeight(25);
		// Desactivar ediciones
		tableEjercicios.setDefaultEditor(Object.class, null);
		// Ocultar columna EventoID
		tableEjercicios.getColumnModel().getColumn(0).setMinWidth(0);
		tableEjercicios.getColumnModel().getColumn(0).setMaxWidth(0);
		// Desactivar mover columnas
		tableEjercicios.getTableHeader().setReorderingAllowed(false);
		// Ordenar por fecha
		TableRowSorter<TableModel> sortEventos = new TableRowSorter<>(modeloEjercicios);
		tableEjercicios.setRowSorter(sortEventos);
		sortEventos.setSortKeys(Collections.singletonList(new RowSorter.SortKey(3, SortOrder.ASCENDING)));
		scrollPaneEventos = new JScrollPane();
		scrollPaneEventos.setBounds(327, 392, 647, 208);
		contentPane.add(scrollPaneEventos);
		scrollPaneEventos.setViewportView(tableEjercicios);
		scrollPaneEventos.getViewport().setBackground(Color.WHITE);
		
	
	
	
		
		// LABELS
	
		
		JLabel lblViaje = new JLabel("Workouts");
		lblViaje.setFont(new Font("Segoe UI", Font.PLAIN, 30));
		lblViaje.setBounds(327, 29, 160, 51);
		contentPane.add(lblViaje);

		lblEventos = new JLabel("Ejercicios");
		lblEventos.setFont(new Font("Segoe UI", Font.PLAIN, 30));
		lblEventos.setBounds(327, 330, 139, 51);
		contentPane.add(lblEventos);
		tableEjercicios.setVisible(false);
		lblEventos.setVisible(false);
		scrollPaneEventos.setVisible(false);
		tableWorkouts.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				Workout seleccionado = workoutSeleccionado();
				if (seleccionado != null) {
					cargarEjercicios(seleccionado);
				}
			}
		});
		cargarEjercicios();
	}
	
	public  void cargarEjercicios() {
		modeloWorkouts.setRowCount(0);
		ArrayList<Workout> workoutsArray =Workout.mObtenerWorkout();
		for (int i = 0; i < workoutsArray.size(); i++) {
			String[] fila = new String[6];
			fila[0] = workoutsArray.get(i).getIdWorkout();
			fila[1] = workoutsArray.get(i).getNombre();
			fila[2] = workoutsArray.get(i).getDescripcion();
			fila[3] = workoutsArray.get(i).getNivel()+"";
			fila[4] = workoutsArray.get(i).getVideo();

			modeloWorkouts.addRow(fila);
		}
	}
	
	public void cargarEjercicios(Workout workout) {
		modeloEjercicios.setRowCount(0);
		ArrayList<Ejercicio> ejerciciosArray =Ejercicio.mObtenerEjerciciosWorkout(workout);
		for (int i = 0; i < ejerciciosArray.size(); i++) {
			String[] fila = new String[6];
			fila[0] = ejerciciosArray.get(i).getIdEjercicio();
			fila[1] = ejerciciosArray.get(i).getNombre();
			fila[2] = ejerciciosArray.get(i).getDescripcion();
			fila[3] = ejerciciosArray.get(i).getTiempoDescanso()+"";

			modeloEjercicios.addRow(fila);
		}
		tableEjercicios.setVisible(true);
		lblEventos.setVisible(true);
		scrollPaneEventos.setVisible(true);
	}
	public Workout workoutSeleccionado() {
		if (tableWorkouts.getSelectedRow() != -1) {
			String IDSeleccionado = tableWorkouts.getValueAt(tableWorkouts.getSelectedRow(), 0).toString();
			ArrayList<Workout> workoutsArray =Workout.mObtenerWorkout();
			for (int i = 0; i < workoutsArray.size(); i++) {
				if (workoutsArray.get(i).getIdWorkout().equals(IDSeleccionado)) {
					return workoutsArray.get(i);
				}
			}
		}
		return null;
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
}

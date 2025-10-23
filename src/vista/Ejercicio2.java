package vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.LineBorder;
import javax.swing.JLabel;

public class Ejercicio extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Ejercicio frame = new Ejercicio();
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
	public Ejercicio() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 807, 538);
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton = new JButton("Salir");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton.setForeground(Color.WHITE);
		btnNewButton.setBackground(Color.RED);
		btnNewButton.setBounds(692, 465, 89, 23);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("");
		btnNewButton_1.setBackground(new Color(51, 153, 0));
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton_1.setBounds(393, 438, 47, 50);
		contentPane.add(btnNewButton_1);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(Color.WHITE));
		panel.setForeground(Color.WHITE);
		panel.setBackground(Color.DARK_GRAY);
		panel.setBounds(10, 11, 255, 58);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JPanel panel_3 = new JPanel();
		panel_3.setForeground(Color.WHITE);
		panel_3.setBorder(new LineBorder(Color.WHITE));
		panel_3.setBackground(Color.DARK_GRAY);
		panel_3.setBounds(10, 80, 167, 23);
		contentPane.add(panel_3);
		panel_3.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setForeground(Color.WHITE);
		panel_1.setBorder(new LineBorder(Color.WHITE));
		panel_1.setBackground(Color.DARK_GRAY);
		panel_1.setBounds(275, 11, 255, 58);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		JPanel panel_2 = new JPanel();
		panel_2.setForeground(Color.WHITE);
		panel_2.setBorder(new LineBorder(Color.WHITE));
		panel_2.setBackground(Color.DARK_GRAY);
		panel_2.setBounds(536, 11, 255, 58);
		contentPane.add(panel_2);
		panel_2.setLayout(null);
		
		JPanel panel_3_1 = new JPanel();
		panel_3_1.setForeground(Color.WHITE);
		panel_3_1.setBorder(new LineBorder(Color.WHITE));
		panel_3_1.setBackground(Color.DARK_GRAY);
		panel_3_1.setBounds(10, 114, 167, 70);
		contentPane.add(panel_3_1);
		panel_3_1.setLayout(null);
		
		JPanel panel_3_2 = new JPanel();
		panel_3_2.setForeground(Color.WHITE);
		panel_3_2.setBorder(new LineBorder(Color.WHITE));
		panel_3_2.setBackground(Color.DARK_GRAY);
		panel_3_2.setBounds(275, 190, 255, 42);
		contentPane.add(panel_3_2);
		panel_3_2.setLayout(null);
		
		JPanel panel_3_2_1 = new JPanel();
		panel_3_2_1.setForeground(Color.WHITE);
		panel_3_2_1.setBorder(new LineBorder(Color.WHITE));
		panel_3_2_1.setBackground(Color.DARK_GRAY);
		panel_3_2_1.setBounds(275, 251, 255, 42);
		contentPane.add(panel_3_2_1);
		panel_3_2_1.setLayout(null);
		
		JPanel panel_3_2_2 = new JPanel();
		panel_3_2_2.setForeground(Color.WHITE);
		panel_3_2_2.setBorder(new LineBorder(Color.WHITE));
		panel_3_2_2.setBackground(Color.DARK_GRAY);
		panel_3_2_2.setBounds(275, 312, 255, 42);
		contentPane.add(panel_3_2_2);
		panel_3_2_2.setLayout(null);

	}
}

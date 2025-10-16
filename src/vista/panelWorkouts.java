package vista;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class panelWorkouts extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	//private JPanel panelWorkout;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					panelWorkouts frame = new panelWorkouts();
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
	public panelWorkouts() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 697, 699);
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
		
		JPanel panelWorkout = new JPanel();
		panelWorkout.setBounds(46, 220, 588, 325);
		contentPane.add(panelWorkout);
		
		
	}
}

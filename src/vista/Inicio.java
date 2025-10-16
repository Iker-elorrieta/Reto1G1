package vista;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controlador.ControladorInicio;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.EventQueue;

public class Inicio extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private PanelLogin panelLogin;
	private PanelRegistro panelRegistro;
	private JPanel panelLogoGrande;
	private JPanel panelLogoPequeno;
	

	/**
	 * Create the frame.
	 */
	public Inicio() {
		setTitle("Daji Squad Gym");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(450, 650);
		setLocationRelativeTo(null); 
		
		contentPane = new JPanel() {
            private static final long serialVersionUID = 1L;
			private Image backgroundImage = new ImageIcon("fotos/fondo1.png").getImage(); 

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        
        panelLogin = new PanelLogin();
        panelRegistro = new PanelRegistro();
        
		contentPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				panelLogin.setVisible(true);
				panelRegistro.setVisible(false);
				panelLogoGrande.setVisible(false);
				panelLogoPequeno.setVisible(true);
			}
		});
		
		
	
		panelLogoGrande = new JPanel() {
            private static final long serialVersionUID = 1L;
			private Image backgroundImage = new ImageIcon("fotos/logo.png").getImage(); 

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
		panelLogoGrande.setOpaque(false);
		panelLogoPequeno = new JPanel() {
            private static final long serialVersionUID = 1L;
			private Image backgroundImage = new ImageIcon("fotos/logo.png").getImage(); 

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panelLogoPequeno.setOpaque(false);
        panelLogoGrande.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				panelLogin.setVisible(true);
				panelRegistro.setVisible(false);
				panelLogoGrande.setVisible(false);
				panelLogoPequeno.setVisible(true);
			}
		});
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		contentPane.add(panelLogin);
		contentPane.add(panelRegistro);
		panelLogoGrande.setBounds(38, 80, 350, 350);
		contentPane.add(panelLogoGrande);
		
		panelLogoPequeno.setBounds(155, 10, 120, 120);
		contentPane.add(panelLogoPequeno);
		panelLogoPequeno.setVisible(false);
	}
	
	
	/************** Getters y Setters **************/
	public PanelLogin getPanelLogin() {
	    return panelLogin;
	}
	public PanelRegistro getPanelRegistro() {
	    return panelRegistro;
	}
	public JButton getBtnRegistrar() {
		return panelRegistro.getBtnRegistrar();
	}
	

}
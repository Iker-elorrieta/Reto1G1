package vista;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class PerfilUsuario extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField txtNombre, txtApellidos, txtEmail, txtContrasena, txtFechaNac;
    private JPanel panelLogo;
    
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                PerfilUsuario frame = new PerfilUsuario();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public PerfilUsuario() {
        setTitle("Perfil del usuario");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 650);
        setLocationRelativeTo(null); 

        
        JPanel contentPane = new JPanel() {
            private static final long serialVersionUID = 1L;
            private Image backgroundImage = new ImageIcon("fotos/fondo1.png").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        contentPane.setLayout(null);
        setContentPane(contentPane);

        
        panelLogo = new JPanel() {
            private static final long serialVersionUID = 1L;
            private Image logo = new ImageIcon("fotos/logo.png").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(logo, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panelLogo.setBounds(150, 10, 150, 150);
        panelLogo.setOpaque(false);
        contentPane.add(panelLogo);

      
        JPanel panelContenedor = new JPanel();
        panelContenedor.setLayout(null);
        panelContenedor.setBounds(40, 170, 360, 430);
        panelContenedor.setBackground(new Color(0, 0, 0, 120)); 
        panelContenedor.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE),
                "Datos del usuario",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Tahoma", Font.BOLD, 14),
                Color.WHITE
        ));
        contentPane.add(panelContenedor);

   
        JPanel panelDatos = new JPanel();
        panelDatos.setLayout(null);
        panelDatos.setBounds(10, 30, 340, 330);
        panelDatos.setOpaque(false);
        panelContenedor.add(panelDatos);

        
        txtNombre = crearCampo(panelDatos, "Nombre:", 0);
        txtApellidos = crearCampo(panelDatos, "Apellidos:", 55);
        txtEmail = crearCampo(panelDatos, "Email:", 110);
        txtContrasena = crearCampo(panelDatos, "Contraseña:", 165);
        txtFechaNac = crearCampo(panelDatos, "Fecha nacimiento:", 220);

        
        JPanel panelBotones = new JPanel();
        panelBotones.setBounds(10, 370, 340, 50);
        panelBotones.setOpaque(false);
        panelContenedor.add(panelBotones);

        JButton btnGuardar = new JButton("Guardar cambios");
        JButton btnCancelar = new JButton("Cancelar");
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

      
        btnGuardar.addActionListener(e -> {
            JOptionPane.showMessageDialog(null,
                    "Perfil actualizado:\n"
                    + "Nombre: " + txtNombre.getText() + "\n"
                    + "Apellidos: " + txtApellidos.getText() + "\n"
                    + "Email: " + txtEmail.getText() + "\n"
                    + "Contraseña: " + txtContrasena.getText() + "\n"
                    + "Fecha Nacimiento: " + txtFechaNac.getText());
        });

        btnCancelar.addActionListener(e -> dispose());
    }

   
    private JTextField crearCampo(JPanel panel, String texto, int y) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(Color.WHITE);
        lbl.setBounds(10, y, 150, 20);
        panel.add(lbl);

        JTextField txt = new JTextField();
        txt.setBounds(10, y + 20, 320, 25);
        panel.add(txt);
        return txt;
    }
}

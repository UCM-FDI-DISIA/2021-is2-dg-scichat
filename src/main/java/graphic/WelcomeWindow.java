package graphic;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class WelcomeWindow extends JPanel {
    private static final long serialVersionUID = 1L;

    MainWindow father;

    private JButton newButton;
    private JButton loadButton;

    public WelcomeWindow(MainWindow father) {
        this.father = father;
        initGUI();
    }

    public void initGUI() {
        // aqui van las movidas de la propia ventana
        this.setBackground(Color.WHITE);
        this.setLayout(new GridBagLayout());

        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);
        this.add(container);

        // esta es la imagen del tablero
        ImageIcon icon = new ImageIcon("resources/tablero.png");

        // aqui van las movidas del titulo
        JLabel title = new JLabel("DAMAS CHINAS");
        title.setForeground(new Color(86, 81, 177));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("Impact", Font.BOLD, 70));

        //este es el panel principal, situado debajo del titulo
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // estos paneles son para rellenar el panel principal
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.WHITE);
        panel.add(leftPanel, BorderLayout.WEST);
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        panel.add(rightPanel, BorderLayout.EAST);
        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.WHITE);

        // en este subpanel, que va arriba, es donde va la imagen
        JLabel label = new JLabel();
        label.setIcon(icon);
        topPanel.add(label);
        panel.add(topPanel, BorderLayout.NORTH);
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        // este panel es que ira al centro del panel principal y que contiene los botones
        JPanel panel2 = new JPanel();
        panel2.setBackground(Color.WHITE);

        // boton de nueva partida y toda su configuracion
        newButton = new JButton("Nueva Partida");
        newButton.setPreferredSize(new Dimension(200, 50));
        newButton.setForeground(new Color(86, 81, 177));
        newButton.setFont(new Font("Impact", 0, 16));
        newButton.setFocusable(false);
        newButton.setBorderPainted(true);
        newButton.setFocusPainted(false);
        newButton.setContentAreaFilled(false);
        newButton.addActionListener(
            new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    father.initLocalServer();
                }
            }
        );
        panel2.add(newButton, BorderLayout.WEST);

        // boton de cargar partida y toda su configuracion
        loadButton = new JButton("Cargar Partida");
        loadButton.setPreferredSize(new Dimension(200, 50));
        loadButton.setForeground(new Color(86, 81, 177));
        loadButton.setFont(new Font("Impact", 0, 16));
        loadButton.setFocusable(false);
        loadButton.setBorderPainted(true);
        loadButton.setFocusPainted(false);
        loadButton.setContentAreaFilled(false);
        loadButton.addActionListener(
            new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    father.initSelectFile();
                }
            }
        );
        panel2.add(loadButton, BorderLayout.EAST);
        // aqui lo añadimos todo a la ventana
        panel.add(panel2, BorderLayout.CENTER);
        container.add(panel, BorderLayout.CENTER);
        container.add(title, BorderLayout.NORTH);
    }
}

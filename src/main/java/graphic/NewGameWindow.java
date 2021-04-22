package graphic;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewGameWindow extends JFrame {
    private int numPlayers;

    NewGameWindow() {
        super("Nueva Partida");
        this.initGUI();
        /// Tamaño mínimo de 800x800
        this.setMinimumSize(new Dimension(800, 800));
        this.setVisible(true);
    }

    private void initGUI() {
        JPanel mainPanel = new JPanel();
        /// Añadir espacios en el panel
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        mainPanel.setBackground(Color.white);
        /// Configurar como panel principal de la ventana
        this.setContentPane(mainPanel);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.setAlignmentY(Component.TOP_ALIGNMENT);

        JLabel titleLabel = new JLabel("Nueva Partida");
        titleLabel.setFont(new Font("Comic Sans", Font.BOLD, 40));
        mainPanel.add(titleLabel, Component.LEFT_ALIGNMENT);

        JPanel numPlayerSection = new JPanel(new FlowLayout(FlowLayout.LEFT));
        numPlayerSection.setBackground(Color.WHITE);
        JComboBox<Integer> numPlayerComboBox = new JComboBox<>(new Integer[]{2, 3, 4, 6});
        numPlayerComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewGameWindow.this.numPlayers = (Integer) numPlayerComboBox.getSelectedItem();
            }
        });
        numPlayerSection.add(new JLabel("Número de jugadores: "));
        numPlayerSection.add(numPlayerComboBox);

        mainPanel.add(numPlayerSection);
    }

    public static void main(String[] args) {
        new NewGameWindow();
    }
}

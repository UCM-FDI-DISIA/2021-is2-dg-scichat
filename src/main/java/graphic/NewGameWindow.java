package graphic;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import logic.gameObjects.Player;
import utils.PieceColor;

public class NewGameWindow extends JFrame {
    private int numPlayers = 2;
    private ArrayList<Player> players = new ArrayList<>();
    private JPanel playersConfigPanel;

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
        mainPanel.setLayout(new GridBagLayout());

        TitledBorder titled = new TitledBorder("Nueva Partida");
        titled.setTitleFont(new Font("Comic sans", Font.BOLD, 20));
        titled.setBorder(BorderFactory.createLineBorder(Color.GRAY, 4));
        mainPanel.setBorder(titled);

        JPanel numPlayerSection = new JPanel(new FlowLayout(FlowLayout.LEFT));
        numPlayerSection.setBackground(Color.WHITE);
        JComboBox<Integer> numPlayerComboBox = new JComboBox<>(
            new Integer[] { 2, 3, 4, 6 }
        );

        numPlayerComboBox.addActionListener(
            e -> {
                NewGameWindow.this.numPlayers =
                    (Integer) numPlayerComboBox.getSelectedItem();
                NewGameWindow.this.refreshPlayerConfiguration();
            }
        );

        numPlayerSection.add(new JLabel("Número de jugadores: "));
        numPlayerSection.add(numPlayerComboBox);
        mainPanel.add(numPlayerSection);

        this.playersConfigPanel = new JPanel(new GridLayout(6, 1));
        mainPanel.add(this.playersConfigPanel);
        refreshPlayerConfiguration();
    }

    /// Método para crear configuraciones para cada jugador
    void refreshPlayerConfiguration() {
        /// Tantas columnas como el numero de jugadores que haya
        this.playersConfigPanel.setLayout(new GridLayout(this.numPlayers, 1));

        /// Vaciar la sección, lo renderizamos de nuevo con el numero de jugadores correcto
        this.playersConfigPanel.removeAll();

        /// Para cada jugador, se necesita un combobox para seleccionar el color
        ArrayList<DefaultComboBoxModel<PieceColor>> colorComboBoxes = new ArrayList<>();

        for (int i = 0; i < this.numPlayers; ++i) {
            colorComboBoxes.add(NewGameWindow.DefaultAvailableColors());
        }

        for (int i = 0; i < this.numPlayers; ++i) {
            JPanel playerConfigPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel playerName = new JLabel("Jugador #" + (i + 1));

            DefaultComboBoxModel<PieceColor> comboBoxModel = colorComboBoxes.get(i);
            JComboBox colorCombo = new JComboBox(comboBoxModel);

            final int _index = i;
            final PieceColor _initialColor = comboBoxModel.getElementAt(0);
            /// Seleccionar por defecto el primer color disponible
            comboBoxModel.setSelectedItem(_initialColor);

            for (int j = 0; j < this.numPlayers; ++j) {
                if (i == j) continue;
                /// Quitar el color seleccionado en el resto de combobox
                colorComboBoxes.get(j).removeElement(_initialColor);
            }

            colorCombo.addActionListener(
                new ActionListener() {
                    private PieceColor current = _initialColor;
                    private int index = _index;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!colorCombo.getSelectedItem().equals(current)) {
                            /// Cuando se selecciona un nuevo color
                            /// Liberar antiguo color y eliminar nueva
                            PieceColor newSelected = (PieceColor) colorCombo.getSelectedItem();

                            for (int i = 0; i < numPlayers; ++i) {
                                if (i == index) continue;
                                colorComboBoxes.get(i).addElement(current);
                                colorComboBoxes.get(i).removeElement(newSelected);
                            }
                        }
                    }
                }
            );

            playerConfigPanel.add(playerName);
            playerConfigPanel.add(colorCombo);
            this.playersConfigPanel.add(playerConfigPanel);
        }

        this.validate();
        this.repaint();
    }

    static DefaultComboBoxModel<PieceColor> DefaultAvailableColors() {
        DefaultComboBoxModel<PieceColor> availableColors = new DefaultComboBoxModel<PieceColor>();
        availableColors.addElement(PieceColor.GREEN);
        availableColors.addElement(PieceColor.YELLOW);
        availableColors.addElement(PieceColor.ORANGE);
        availableColors.addElement(PieceColor.RED);
        availableColors.addElement(PieceColor.MAGENTA);
        availableColors.addElement(PieceColor.BLUE);

        return availableColors;
    }

    public static void main(String[] args) {
        new NewGameWindow();
    }
}

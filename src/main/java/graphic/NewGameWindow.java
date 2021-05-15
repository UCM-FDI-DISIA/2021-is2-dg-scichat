package graphic;

import exceptions.OccupiedCellException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import logic.Board;
import logic.gameObjects.HumanPlayer;
import logic.gameObjects.Player;
import utils.Mode;
import utils.PieceColor;

public class NewGameWindow extends JDialog {
    private int numPlayers;
    private final ArrayList<DefaultComboBoxModel<PieceColor>> colorComboBoxes = new ArrayList<>();
    private final ArrayList<Integer> botStrategy = new ArrayList<>();
    private final Queue<Board.Side> availableSides = new LinkedList<>();

    private JPanel playersConfigPanel = new JPanel();
    private JPanel container;
    private JComboBox<Mode> modeJComboBox;

    private int status;

    NewGameWindow(Frame parent) {
        super(parent, "Nuevo Juego", true);
        this.numPlayers = 2;
        this.initGUI();
        /// Tamaño mínimo de 800x800
        this.setMinimumSize(new Dimension(600, 275));
    }

    private void initGUI() {
        this.setResizable(false);
        JPanel mainPanel = new JPanel();
        this.setContentPane(mainPanel);
        /// Añadir espacios en el panel
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        setBackground(Color.white);
        /// Configurar como panel principal de la ventana
        mainPanel.setLayout(new GridBagLayout());

        this.container = new JPanel(new BorderLayout(2, 10));
        container.add(this.playersConfigPanel, BorderLayout.CENTER);

        TitledBorder titled = new TitledBorder("Nueva Partida");
        titled.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        container.setBorder(
            new CompoundBorder(titled, BorderFactory.createEmptyBorder(10, 10, 10, 10))
        );

        /// Preparar el contenido de panel de configuracion
        preparePlayersConfigPanel();
        refreshPlayerConfiguration();

        mainPanel.add(container);

        JButton startButton = new JButton("Start");
        startButton.addActionListener(
            e -> {
                this.status = 1;
                //Para limpiar los sides estaticos de la partida anterior
                new Board();
                setVisible(false);
            }
        );
        container.add(startButton, BorderLayout.SOUTH);
    }

    void preparePlayersConfigPanel() {
        JPanel wrapper = new JPanel(new FlowLayout());

        /// Crear la configuracion de numero de jugadores
        JPanel numPlayerSection = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        numPlayerSection.setBackground(Color.WHITE);

        JComboBox<Integer> numPlayerComboBox = new JComboBox<>(
            new Integer[] { 2, 3, 4, 6 }
        );

        numPlayerComboBox.addActionListener(
            e -> {
                /// Cuando cambia el numero de jugadores, actualizar la lista de configuraciones
                NewGameWindow.this.numPlayers =
                    (Integer) numPlayerComboBox.getSelectedItem();
                NewGameWindow.this.refreshPlayerConfiguration();
            }
        );

        numPlayerSection.add(new JLabel("Número de jugadores: "));
        numPlayerSection.add(numPlayerComboBox);
        wrapper.add(numPlayerSection);

        /// Crear la configuracion del modo del juego
        JPanel modeSection = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        modeSection.setBackground(Color.WHITE);
        DefaultComboBoxModel<Mode> modeComboBoxModel = new DefaultComboBoxModel<>();
        modeComboBoxModel.addElement(Mode.Fast);
        modeComboBoxModel.addElement(Mode.Traditional);

        modeJComboBox = new JComboBox<>(modeComboBoxModel);
        modeSection.add(new JLabel("Modo del juego: "));
        modeSection.add(modeJComboBox);
        wrapper.add(modeSection);

        container.add(wrapper, BorderLayout.NORTH);
    }

    void setAvailableSides() {
        switch (this.numPlayers) {
            case 2:
                /// Entonces juega en arriba y abajo
                this.availableSides.add(Board.Side.Up);
                this.availableSides.add(Board.Side.Down);
                break;
            case 3:
                /// Juega en triangulo invertido
                this.availableSides.add(Board.Side.UpLeft);
                this.availableSides.add(Board.Side.UpRight);
                this.availableSides.add(Board.Side.Down);
                break;
            case 4:
                this.availableSides.add(Board.Side.UpLeft);
                this.availableSides.add(Board.Side.UpRight);
                this.availableSides.add(Board.Side.DownLeft);
                this.availableSides.add(Board.Side.DownRight);
                break;
            case 6:
                /// Entonces todos los lados están disponibles
                this.availableSides.addAll(Arrays.asList(Board.Side.values()));
                break;
        }
    }

    /// Método para crear configuraciones para cada jugador
    void refreshPlayerConfiguration() {
        /// Tantas columnas como el numero de jugadores que haya
        this.playersConfigPanel.setLayout(new GridLayout(this.numPlayers, 1));

        /// Vaciar la sección, lo renderizamos de nuevo con el numero de jugadores correcto
        this.playersConfigPanel.removeAll();

        /// Para cada jugador, se necesita un combobox para seleccionar el color
        this.colorComboBoxes.clear();

        this.botStrategy.clear();

        for (int i = 0; i < this.numPlayers; ++i) {
            colorComboBoxes.add(NewGameWindow.DefaultAvailableColors());
        }

        for (int i = 0; i < this.numPlayers; ++i) {
            JPanel playerConfigPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 10)
            );
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

                            current = newSelected;
                        }
                    }
                }
            );

            playerConfigPanel.add(playerName);
            playerConfigPanel.add(colorCombo);

            JComboBox<String> botStrategyComboBox = new JComboBox<>(
                new String[] {
                    "Jugador Humano",
                    "Estrategia 1",
                    "Estrategia 2",
                    "Estrategia 3"
                }
            );

            /// Por defecto, es jugador humano
            this.botStrategy.add(0);

            botStrategyComboBox.addActionListener(
                e -> {
                    this.botStrategy.set(_index, botStrategyComboBox.getSelectedIndex());
                }
            );

            playerConfigPanel.add(botStrategyComboBox);
            this.playersConfigPanel.add(playerConfigPanel);
        }

        switch (numPlayers) {
            case 2:
                setSize(600, 275);
                break;
            case 3:
                setSize(600, 315);
                break;
            case 4:
                setSize(600, 355);
                break;
            case 6:
                this.setSize(600, 450);
        }

        this.validate();
        this.repaint();
    }

    static DefaultComboBoxModel<PieceColor> DefaultAvailableColors() {
        DefaultComboBoxModel<PieceColor> availableColors = new DefaultComboBoxModel<>(
            PieceColor.availableColors
        );
        return availableColors;
    }

    ArrayList<Player> getPlayers() {
        /// Refrescar los lados disponibles
        this.setAvailableSides();

        /// Crear una lista de players con las opciones
        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < this.numPlayers; ++i) {
            PieceColor color = (PieceColor) this.colorComboBoxes.get(i).getSelectedItem();

            /// Si es 0, es jugador humano
            int botStrategy = this.botStrategy.get(i);

            try {
                players.add(
                    new HumanPlayer(
                        color,
                        this.availableSides.poll(),
                        new Integer(i + 1).toString()
                    )
                );
            } catch (OccupiedCellException e) {
                /// Esto nunca va a pasar
                e.printStackTrace();
            }
        }

        return players;
    }

    Mode getGameMode() {
        return (Mode) modeJComboBox.getSelectedItem();
    }

    public int open() {
        this.status = 0;
        setLocation(getParent().getLocation().x + 50, getParent().getLocation().y + 50);
        pack();
        setVisible(true);

        return status;
    }
}

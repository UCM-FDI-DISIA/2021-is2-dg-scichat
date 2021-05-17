package graphic;

import control.Controller;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import logic.Game;
import org.apache.commons.lang.time.DurationFormatUtils;

public class OptionsPanel extends JPanel implements GameObserver {
    private static final long serialVersionUID = 1L;

    private Controller ctrl;
    private boolean onGame = true;
    private Game game;
    private SwingWorker<Integer, Integer> hora;

    //Componentes
    private JLabel labelTime;
    private JLabel gameMode;
    private JLabel turn;
    private Circle playerColor;

    public OptionsPanel(Controller ctrl) {
        initGUI();
        this.ctrl = ctrl;
        ctrl.addObserver(this);
        hora =
            new SwingWorker<Integer, Integer>() {

                protected Integer doInBackground() {
                    while (onGame) {
                        try {
                            long calcTime = System.currentTimeMillis();
                            labelTime.setText(
                                DurationFormatUtils.formatDuration(
                                    game.getCurrentTime(),
                                    "HH:mm:ss"
                                )
                            );
                            Thread.sleep(500 - calcTime + System.currentTimeMillis());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    return null;
                }
            };
    }

    public void initGUI() {
        this.setPreferredSize(new Dimension(300, 700));
        this.setLayout(new GridLayout(6, 1, 0, 40));
        this.setBackground(new Color(192, 245, 224));

        // Añadir los componentes aqui para que los botones queden abajo

        // Etiqueta de ejemplo de la duración de la partida

        labelTime = new JLabel("00:00:00");
        labelTime.setHorizontalAlignment(SwingConstants.CENTER);
        labelTime.setFont(new Font("Impact", 0, 20));
        this.add(labelTime);

        // Etiqueta de ejemplo del modo de juego
        gameMode = new JLabel("Modo de juego tradicional");
        gameMode.setHorizontalAlignment(SwingConstants.CENTER);
        gameMode.setFont(new Font("Impact", 0, 20));
        this.add(gameMode);

        // Etiqueta de ejemplo del turno

        JPanel turnPanel = new JPanel(new FlowLayout());
        turnPanel.setBackground(null);
        turn = new JLabel("Turno del jugador 1");
        turn.setHorizontalAlignment(SwingConstants.CENTER);
        turn.setFont(new Font("Impact", 0, 20));

        playerColor = new Circle(20, Color.WHITE);
        turnPanel.add(turn);
        turnPanel.add(playerColor);
        this.add(turnPanel);

        // Botón para guardar la partida
        JButton saveButton = new JButton("Guardar partida");
        saveButton.setPreferredSize(new Dimension(250, 100));
        saveButton.setFont(new Font("Impact", 0, 20));
        saveButton.setFocusable(false);
        saveButton.setBorderPainted(true);
        saveButton.setFocusPainted(false);
        saveButton.setContentAreaFilled(false);
        saveButton.addActionListener(
            new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    new SaveGameWindow(ctrl).open();
                }
            }
        );
        this.add(saveButton);

        // Botón para rendirse durante una partida
        JButton surrenderButton = new JButton("Rendirse");
        surrenderButton.setPreferredSize(new Dimension(250, 100));
        surrenderButton.setFont(new Font("Impact", 0, 20));
        surrenderButton.setFocusable(false);
        surrenderButton.setBorderPainted(true);
        surrenderButton.setFocusPainted(false);
        surrenderButton.setContentAreaFilled(false);
        surrenderButton.addActionListener(
            new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    ctrl.surrender();
                }
            }
        );
        this.add(surrenderButton);

        JButton helpButton = new JButton("Ayuda");
        helpButton.setPreferredSize(new Dimension(250, 100));
        helpButton.setFont(new Font("Impact", 0, 20));
        helpButton.setFocusable(false);
        helpButton.setBorderPainted(true);
        helpButton.setFocusPainted(false);
        helpButton.setContentAreaFilled(false);
        helpButton.addActionListener(
            new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    new HelpWindow();
                }
            }
        );
        this.add(helpButton);
    }

    public void onGameEnded(Game game) {
        onGame = false;
    }

    public void onRegister(Game game) {
        this.game = game;
        this.gameMode.setText(game.getGameMode().toString());
        this.turn.setText("Turno del jugador " + game.getCurrentPlayer().getName());
        this.playerColor.setColor(game.getCurrentPlayer().getColor());
    }

    public void onGameStart(Game game) {
        hora.execute();
    }

    public void onEndTurn(Game game) {
        this.turn.setText("Turno del jugador " + game.getCurrentPlayer().getName());
        this.playerColor.setColor(game.getCurrentPlayer().getColor());
    }
}

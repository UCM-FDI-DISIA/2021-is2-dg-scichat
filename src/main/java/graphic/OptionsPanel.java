package graphic;

import control.Controller;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import logic.Game;
import org.apache.commons.lang.time.DurationFormatUtils;

public class OptionsPanel extends JPanel implements GameObserver {
    private Controller ctrl;
    private JLabel labelTime;
    private boolean onGame = true;
    private Game game;
    private SwingWorker<Integer, Integer> hora;


    public OptionsPanel(Controller ctrl) {
        initGUI();
        ctrl.addObserver(this);
        hora =
            new SwingWorker<Integer, Integer>() {

                protected Integer doInBackground() {
                    while (onGame) {
                        try {
                            long calcTime = System.currentTimeMillis();
                            labelTime.setText(
                                DurationFormatUtils.formatDuration(
                                    game.getCurrentPlayerTime(),
                                    "HH:mm:ss"
                                )
                            );
                            Thread.sleep(1000 - calcTime + System.currentTimeMillis());
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
        JLabel gameMode = new JLabel("Modo de juego tradicional");
        gameMode.setHorizontalAlignment(SwingConstants.CENTER);
        gameMode.setFont(new Font("Impact", 0, 20));
        this.add(gameMode);

        // Etiqueta de ejemplo del turno

        JLabel turn = new JLabel("Turno del jugador 1");
        turn.setHorizontalAlignment(SwingConstants.CENTER);
        turn.setFont(new Font("Impact", 0, 20));
        this.add(turn);

        // Botón para guardar la partida
        JButton saveButton = new JButton("Guardar partida");
        saveButton.setPreferredSize(new Dimension(250, 100));
        saveButton.setFont(new Font("Impact", 0, 20));
        saveButton.setFocusable(false);
        saveButton.setBorderPainted(true);
        saveButton.setFocusPainted(false);
        saveButton.setContentAreaFilled(false);
        this.add(saveButton);

        // Botón para rendirse durante una partida
        JButton surrenderButton = new JButton("Rendirse");
        surrenderButton.setPreferredSize(new Dimension(250, 100));
        surrenderButton.setFont(new Font("Impact", 0, 20));
        surrenderButton.setFocusable(false);
        surrenderButton.setBorderPainted(true);
        surrenderButton.setFocusPainted(false);
        surrenderButton.setContentAreaFilled(false);
        this.add(surrenderButton);
    }

    public void onGameEnded(Game game) {
        onGame = false;
    }

    public void onRegister(Game game) {
        this.game = game;
    }

    public void onGameStart(Game game) {
        hora.execute();
    }
}

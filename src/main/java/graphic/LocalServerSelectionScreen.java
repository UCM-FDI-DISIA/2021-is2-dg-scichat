package graphic;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class LocalServerSelectionScreen extends JDialog {
    MainWindow parent;

    public LocalServerSelectionScreen(MainWindow parent) {
        super(parent, "Nueva Partida", true);
        this.parent = parent;
        this.initGUI();
        //this.setMinimumSize(new Dimension(600, 275));
    }

    private void initGUI() {
        this.setResizable(false);
        JPanel mainPanel = new JPanel();
        this.setContentPane(mainPanel);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(Color.white);

        JPanel container = new JPanel(new BorderLayout(2, 10));
        //container.add(this.playersConfigPanel, BorderLayout.CENTER);

        TitledBorder titled = new TitledBorder("Nueva Partida");
        titled.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        container.setBorder(
            new CompoundBorder(titled, BorderFactory.createEmptyBorder(10, 10, 10, 10))
        );

        JButton local = new JButton("Juego local");
        local.addActionListener(
            e -> {
                setVisible(false);
                parent.initGameOptions();
            }
        );
        container.add(local, BorderLayout.EAST);

        JButton online = new JButton("Juego online");
        online.addActionListener(
            e -> {
                setVisible(false);
                parent.initOnlineConnect();
            }
        );
        container.add(online, BorderLayout.WEST);

        mainPanel.add(container);
    }

    public void open() {
        setLocation(getParent().getLocation().x + 100, getParent().getLocation().y + 100);
        pack();
        setVisible(true);
    }
}

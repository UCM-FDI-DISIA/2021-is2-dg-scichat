package graphic;

import control.Controller;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class LoadGameWindow extends JDialog {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    Controller controller;
    MainWindow father;
    String firstMsg = "Opcion de cargado 1";
    File firstFile;
    String secondMsg = "Opción de cargado 2";
    File secondFile;
    String thirdMsg = "Opción de cargado 3";
    File thirdFile;
    boolean nuevo;

    public LoadGameWindow(Controller control, MainWindow father) {
        this.controller = control;
        this.father = father;
        firstFile = new File("firstGame.json");
        secondFile = new File("secondGame.json");
        thirdFile = new File("thirdGame.json");
        initGUI();
    }

    public void initGUI() {
        this.setPreferredSize(new Dimension(500, 300));
        this.setBackground(Color.WHITE);
        this.setLayout(new BorderLayout());

        //Panel Superior
        JPanel firstPanel = new JPanel();
        firstPanel.setBackground(Color.WHITE);
        firstPanel.setPreferredSize(new Dimension(150, 85));
        JButton opt1 = new JButton(firstMsg);
        opt1.setFont(new Font("Tahoma", Font.PLAIN, 26));
        opt1.setFocusable(false);
        opt1.setBorderPainted(true);
        opt1.setFocusPainted(false);
        opt1.setContentAreaFilled(false);
        opt1.addActionListener(
            new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    controller.loadGame(firstFile);
                    controller.initGame();
                    father.initGame();
                    setVisible(false);
                }
            }
        );
        firstPanel.add(opt1, BorderLayout.CENTER);

        //Panel Medio
        JPanel secondPanel = new JPanel();
        secondPanel.setBackground(Color.WHITE);
        secondPanel.setPreferredSize(new Dimension(150, 85));
        JButton opt2 = new JButton(secondMsg);
        opt2.setFont(new Font("Tahoma", Font.PLAIN, 26));
        opt2.setFont(new Font("Tahoma", Font.PLAIN, 26));
        opt2.setFocusable(false);
        opt2.setBorderPainted(true);
        opt2.setFocusPainted(false);
        opt2.setContentAreaFilled(false);
        opt2.addActionListener(
            new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    controller.loadGame(secondFile);
                    controller.initGame();
                    father.initGame();
                    setVisible(false);
                }
            }
        );
        secondPanel.add(opt2, BorderLayout.CENTER);

        //Panel bajo
        JPanel thirdPanel = new JPanel();
        thirdPanel.setBackground(Color.WHITE);
        thirdPanel.setPreferredSize(new Dimension(150, 85));
        JButton opt3 = new JButton(thirdMsg);
        opt3.setFont(new Font("Tahoma", Font.PLAIN, 26));
        opt3.setFont(new Font("Tahoma", Font.PLAIN, 26));
        opt3.setFocusable(false);
        opt3.setBorderPainted(true);
        opt3.setFocusPainted(false);
        opt3.setContentAreaFilled(false);
        opt3.addActionListener(
            new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    controller.loadGame(thirdFile);
                    controller.initGame();
                    father.initGame();
                    setVisible(false);
                }
            }
        );
        thirdPanel.add(opt3, BorderLayout.CENTER);

        this.add(firstPanel, BorderLayout.NORTH);
        this.add(secondPanel, BorderLayout.CENTER);
        this.add(thirdPanel, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    public boolean open() {
        nuevo = false;
        setLocation(getParent().getLocation().x + 50, getParent().getLocation().y + 50);
        pack();
        setVisible(true);
        return nuevo;
    }
}

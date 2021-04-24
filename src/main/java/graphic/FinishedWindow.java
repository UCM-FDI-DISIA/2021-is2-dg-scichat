package graphic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import control.Controller;
import logic.gameObjects.Player;



public class FinishedWindow extends JFrame{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    
    MainWindow father;
    Player winner;
    Controller controller;
    
    
    public FinishedWindow() {
	initGUI();
    }
    
    public FinishedWindow(MainWindow father, Player winner, Controller control) {
	this.father = father;
	this.winner = winner;
	this.controller = control;
	initGUI();
    }
    
    public void initGUI() {
	
	//Creamos la ventana
	this.setSize(900, 400);
        this.setBackground(Color.WHITE);
        this.setLayout(new BorderLayout());
        
        //Marcamos el título en función de quien haya gando
        JLabel title = new JLabel();


        if(winner == null) {
            title = new JLabel("No ha ganado nadie");
        }
        else {
            title = new JLabel("Ha ganado el jugador " + winner.getId());
        }
        
        
        //jpanel para los botones
        
        JPanel optionsPanel = new JPanel();
        
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        
        //Botones diversos y sus action listeners
        //Creamos el botón de volver a inicio
        JButton newGameButton = new JButton("Volver a inicio");
        newGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent action) {
        	
            }
        });
        
        //Creamos el botón de salir del juego
        JButton exitButton = new JButton("Salir");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent action) {
        	System.exit(0);
            }
        });
        
        //Creamos el botón de revancha
        JButton revengeButton  = new JButton("Revancha");
        revengeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent action) {
        	father.initGame();
            }
        });


        
        
        
        
        optionsPanel.add(newGameButton, BorderLayout.CENTER);
        optionsPanel.add(exitButton, BorderLayout.CENTER);
        optionsPanel.add(revengeButton, BorderLayout.CENTER);
        this.add(optionsPanel, BorderLayout.CENTER);
        this.add(title, BorderLayout.NORTH);
        this.setVisible(true);
    }
    
    public static void main(String[] args) {
	SwingUtilities.invokeLater(new Runnable() {
	    	
		public void run() {
			new FinishedWindow();
		}
	});
}
}

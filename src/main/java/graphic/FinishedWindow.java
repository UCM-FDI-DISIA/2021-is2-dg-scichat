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
import javax.swing.border.EmptyBorder;

import com.mommoo.flat.button.FlatButton;
import com.mommoo.flat.component.FlatPanel;
import com.mommoo.flat.text.label.FlatLabel;

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
	
	
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	this.setBounds(450, 190, 1014, 597);
	this.setResizable(true);
        this.setBackground(Color.WHITE);
        this.setLayout(new BorderLayout());
        
        //Marcamos el título en función de quien haya gando
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setPreferredSize(new Dimension(200, 200));
        JLabel title = new JLabel();
        JPanel titleP = new JPanel();


        if(winner == null) {
            title = new JLabel("No ha ganado nadie");
        }
        else {
            title = new JLabel("Ha ganado el jugador " + winner.getId());
        }
        
        
        JPanel gamePanel = new JPanel();
        JLabel gameOver = new JLabel("GAME OVER");
        gameOver.setFont(new Font("Impact", 3, 52));
        gamePanel.add(gameOver, BorderLayout.CENTER);
        gamePanel.setPreferredSize(new Dimension(350, 400));
        gamePanel.setBackground(Color.WHITE);
        
        
        title.setFont(new Font("Tahoma", 3, 32));
        title.setForeground(new Color(86, 81, 177));
        title.setBounds(481, 286, 281, 68);

        titleP.add(title);
        titleP.setPreferredSize(new Dimension(500, 175));
        titleP.setBackground(Color.WHITE);
        

        titlePanel.add(titleP, BorderLayout.CENTER);
        
        titlePanel.setBackground(Color.WHITE);

        
        
        //jpanel para los botones
        
        JPanel optionsPanel = new JPanel();
        optionsPanel.setBackground(Color.WHITE);
        optionsPanel.setPreferredSize(new Dimension(220, 268));
        optionsPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        //Botones diversos y sus action listeners
        //Creamos el botón de volver a inicio
        JPanel leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(400, 150));
        JButton newGameButton = new JButton("Volver a inicio");
        newGameButton.setFont(new Font("Tahoma", Font.PLAIN, 26));
	newGameButton.setBounds(545, 392, 162, 73);
        newGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent action) {
        	father.initGameOptions();
            }
        });
        
        leftPanel.add(newGameButton, BorderLayout.CENTER);
        leftPanel.setBackground(Color.WHITE);
        //Creamos el botón de salir del juego
        JPanel centerPanel = new JPanel();
        centerPanel.setPreferredSize(new Dimension(400, 150));

        JButton exitButton = new JButton("Salir del juego");
        exitButton.setFont(new Font("Tahoma", Font.PLAIN, 26));
	exitButton.setBounds(545, 392, 162, 73);
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent action) {
        	System.exit(0);
            }
        });
        
        centerPanel.add(exitButton, BorderLayout.CENTER);
        centerPanel.setBackground(Color.WHITE);
        
        //Creamos el botón de revancha
        JPanel rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension(400, 150));

        JButton revengeButton = new JButton("Revancha");
        revengeButton.setFont(new Font("Tahoma", Font.PLAIN, 26));
        revengeButton.setBounds(545, 392, 162, 73);
        revengeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent action) {
        	father.initGame();
            }
        });

        rightPanel.add(revengeButton, BorderLayout.CENTER);
        rightPanel.setBackground(Color.WHITE);
        
   
        
        
        
        optionsPanel.add(leftPanel, BorderLayout.WEST);
        optionsPanel.add(centerPanel, BorderLayout.CENTER);
        optionsPanel.add(rightPanel, BorderLayout.EAST);

        this.add(optionsPanel, BorderLayout.SOUTH);
        this.add(titlePanel, BorderLayout.NORTH);
        this.add(gamePanel, BorderLayout.CENTER);
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

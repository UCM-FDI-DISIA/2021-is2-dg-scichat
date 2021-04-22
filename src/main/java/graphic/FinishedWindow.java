package graphic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;



public class FinishedWindow extends JFrame{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    MainWindow father;
    
    
    public FinishedWindow() {
	initGUI();
    }
    
    public FinishedWindow(MainWindow father) {
	this.father = father;
	initGUI();
    }
    
    public void initGUI() {
	//Creamos la ventana
	this.setSize(900, 400);
        this.setBackground(Color.WHITE);
        this.setLayout(new BorderLayout());
        
        //Creamos los componentes de la misma
        JLabel title = new JLabel("El juego ha terminado");
        
        title.setForeground(Color.black);
        title.setFont(new Font("Batang", 3, 70));
        
        //Botones diversos
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.BLUE);
        
        
        JPanel exitPanel = new JPanel();
        JPanel newGame = new JPanel();
        JButton exitGame = new JButton("Salir del juego");
        exitGame.setPreferredSize(new Dimension(200, 50));
        exitGame.setForeground(new Color(86, 81, 177));
        exitGame.setFont(new Font("Impact", 0, 16));
        exitGame.setFocusable(false);
        JButton reloadGame = new JButton("Nueva partida(mismos parámetros)");
        reloadGame.setPreferredSize(new Dimension(350, 50));
        reloadGame.setForeground(new Color(86, 81, 177));
        reloadGame.setFont(new Font("Impact", 0, 16));
        reloadGame.setFocusable(false);
        JButton backToStart = new JButton("Volver al menú de inicio");
        backToStart.setPreferredSize(new Dimension(200, 50));
        backToStart.setForeground(new Color(86, 81, 177));
        backToStart.setFont(new Font("Impact", 0, 16));
        backToStart.setFocusable(false);

        mainPanel.add(exitPanel);
        //mainPanel.add(newGame);
        exitPanel.add(exitGame, BorderLayout.CENTER);
        exitPanel.add(reloadGame, BorderLayout.CENTER);
        exitPanel.add(backToStart);

        
        
        this.add(title, BorderLayout.NORTH);
        this.add(mainPanel, BorderLayout.SOUTH);
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

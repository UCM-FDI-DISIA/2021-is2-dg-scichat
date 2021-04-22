package utils;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class Tools {
    public static void showComp(JComponent c) {
	JFrame f = new JFrame();
	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	f.add(c);
	
	f.pack();
	f.setVisible(true);
    }
}

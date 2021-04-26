package utils;

import control.Controller;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import logic.Game;

public class Tools {

    public static void showComp(JComponent c) {
        try {
            SwingUtilities.invokeAndWait(
                new Runnable() {

                    @Override
                    public void run() {
                        JFrame f = new JFrame();
                        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                        f.add(c);

                        f.pack();
                        f.setVisible(true);
                    }
                }
            );
        } catch (InvocationTargetException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

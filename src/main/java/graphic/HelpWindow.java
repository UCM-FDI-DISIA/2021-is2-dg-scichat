package graphic;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class HelpWindow extends JFrame {

    HelpWindow() {
        super("Ayuda del juego");
        JPanel mainPanel = new JPanel();

        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        this.setContentPane(mainPanel);

        this.getContentPane()
                .setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        JEditorPane panel = new JEditorPane();

        StringBuilder sb = new StringBuilder();
        String str;

        try {
            BufferedReader in = new BufferedReader(
                    new FileReader("src/main/java/view/HelpWindow.html")
            );
            while ((str = in.readLine()) != null) sb.append(str);

            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String content = sb.toString();

        panel.setContentType("text/html");
        panel.setText(content);

        this.getContentPane().add(panel);
        this.getContentPane().setBackground(Color.WHITE);

        this.setSize(new Dimension(800, 700));
        this.setPreferredSize(new Dimension(800, 700));
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new HelpWindow();
    }
}

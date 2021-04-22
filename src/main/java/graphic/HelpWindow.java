package graphic;

import java.awt.*;
import java.io.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class HelpWindow extends JFrame {

    HelpWindow() {
        super("Ayuda del juego");
        JPanel mainPanel = new JPanel();

        /// Configurar como panel principal de la ventana
        this.setContentPane(mainPanel);

        /// Añadir espacios en el panel
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        mainPanel.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        /// Crear un EditorPane para incrustar HTML
        JEditorPane panel = new JEditorPane();

        /// Cargar el contenido del fichero HTML en StringBuilder
        StringBuilder sb = new StringBuilder();

        try (BufferedReader in = new BufferedReader(new FileReader("resources/html/HelpWindow.html"))) {
            String str;
            while ((str = in.readLine()) != null) sb.append(str);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String content = sb.toString();

        panel.setContentType("text/html");
        panel.setText(content);

        mainPanel.add(panel);
        mainPanel.setBackground(Color.WHITE);

        /// Configurar tamaño de la ventana
        this.setSize(new Dimension(800, 700));
        this.setResizable(false);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new HelpWindow();
    }
}

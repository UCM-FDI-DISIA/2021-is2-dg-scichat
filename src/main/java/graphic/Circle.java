package graphic;

import java.awt.*;
import javax.swing.*;

public class Circle extends JPanel {
    private Color color;

    Circle(int ratio, Color _color) {
        this.setSize(new Dimension(ratio, ratio));
        this.color = _color;
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(this.color);
        g.fillOval(0, 0, g.getClipBounds().width, g.getClipBounds().height);
    }

    public void setColor(Color _color) {
        this.color = _color;
        this.repaint();
    }
}

package graphic;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JLabel;
import logic.Cell;

public class CellLabel extends JLabel {
    private static final long serialVersionUID = -6899489025051827529L;
    private static final int default_radius = 20;
    private static final Color default_bg_color = Color.black;

    private int radius, x, y;
    private Color borderColor, bgColor;
    //    private final Color accentColor = new Color(0x559999);
    private final Color selectColor = new Color(0xff66ff);
    private boolean selected = false;
    private BoardPanel parent = null;
    private Cell position = null;

    public CellLabel(BoardPanel parent, Cell cell, int radius) {
        this.x = cell.getCol();
        this.y = cell.getRow();
        this.parent = parent;
        this.position = cell;
        this.radius = radius;

        if (!this.position.isEmpty()) this.bgColor =
            this.position.getPiece().getColor().getColor(); else this.bgColor =
            default_bg_color;
        this.borderColor = Color.white;

        initGUI();

        PointerTracker tracker = new PointerTracker();
        this.addMouseListener(tracker);
        this.addMouseMotionListener(tracker);
    }

    public void initGUI() {
        this.setFocusable(false);
        this.setOpaque(false);
        this.setBackground(Color.white);
        this.place();
    }

    private void place() {
        int height = 2 * radius;
        int width = (int) (Math.sqrt(4.0 / 3) * height);

        int actual_x = (y % 2 == 0 ? 0 : -width / 2) + x * width;
        int actual_y = y * height;

        this.setBounds(actual_x, actual_y, 2 * (radius + 4), 2 * (radius + 4));
        this.setPreferredSize(new Dimension(2 * radius, 2 * radius));
    }

    public void setRadius(int r) {
        this.radius = r;
        this.place(); // Replace in the JPanel with new radius
    }

    public CellLabel(BoardPanel parent, Cell cell) {
        this(parent, cell, default_radius);
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2D = (Graphics2D) g;

        g2D.setPaint(bgColor);
        g2D.fillOval(0, 0, 2 * radius, 2 * radius);

        // System.out.println(this.toString() + " is hovered, color " + this.bgColor.toString());
        // if(selected) System.out.println(this.toString() + " is selected");
        g2D.setPaint((selected ? selectColor : borderColor));
        g2D.setStroke(new BasicStroke(2));
        g2D.drawOval(0, 0, 2 * radius, 2 * radius);
    }

    public void setColor() {
        setColor(default_bg_color);
    }

    public void setColor(Color color) {
        this.bgColor = color;
        this.validate();
        this.repaint();
    }

    public void setSelected(boolean b) {
        this.selected = b;
        this.validate();
        this.repaint();
    }

    public boolean getSelected() {
        return this.selected;
    }

    private boolean inside(Point pt) {
        double dx = pt.getX() - (radius);
        double dy = pt.getY() - (radius);
        double rv = (dx * dx + dy * dy);
        return rv < radius * radius;
    }

    private class PointerTracker implements MouseMotionListener, MouseListener {

        @Override
        public void mouseDragged(MouseEvent arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void mouseMoved(MouseEvent e) {
            // TODO Auto-generated method stub
            Point pointer = e.getPoint();

            //			System.out.println("(" + pointer.getX() + ", " + pointer.getY() + ")");
            if (inside(pointer)) {
                borderColor = bgColor;
            } else {
                borderColor = Color.white;
            }
            // https://stackoverflow.com/questions/8853397/repaint-in-java
            CellLabel.this.validate();
            CellLabel.this.repaint();
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            Point pointer = e.getPoint();
            if (inside(pointer)) {
                /*JOptionPane.showOptionDialog(
                    CellLabel.this,
                    "Se va a mandar la celda seleccinada a Controller\n" +
                    pointer.toString(),
                    "Error!",
                    JOptionPane.INFORMATION_MESSAGE,
                    JOptionPane.OK_OPTION,
                    null,
                    null,
                    null
                );*/
                CellLabel.this.parent.handleClick(CellLabel.this.position);
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public void mouseExited(MouseEvent e) {
            borderColor = Color.white;
            CellLabel.this.validate();
            CellLabel.this.repaint();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            // TODO Auto-generated method stub
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            // TODO Auto-generated method stub

        }
    }

    @Override
    public String toString() {
        return String.format("Label at %s", this.position.toString());
    }
}

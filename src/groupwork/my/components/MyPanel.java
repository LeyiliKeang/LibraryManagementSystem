package groupwork.my.components;

import javax.swing.*;
import java.awt.*;

public class MyPanel extends JPanel {
    private ImageIcon icon;

    public MyPanel() {
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (icon != null) {
            g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    }
}

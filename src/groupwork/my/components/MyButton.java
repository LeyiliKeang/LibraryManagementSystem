package groupwork.my.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyButton extends JButton {
    private ImageIcon icon;

    public MyButton() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me) {
//                setContentAreaFilled(true);
            }

            @Override
            public void mouseExited(MouseEvent me) {
//                setContentAreaFilled(false);
            }
        });
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

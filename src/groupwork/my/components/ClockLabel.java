package groupwork.my.components;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;

public class ClockLabel extends JLabel implements Runnable {
    private int secLen = 100;
    private int minutesLen = 80;
    private int hoursLen = 60;

    private int centerX = 100;
    private int centerY = 100;
    private Stroke HOURS_POINT_WIDTH = new BasicStroke(3f);
    private Stroke MINUTES_POINT_WIDTH = new BasicStroke(2f);
    private Stroke SEC_POINT_WIDTH = new BasicStroke(1f);

    public ClockLabel() {
    }

    private void drawClock(Graphics2D g2, Calendar calendar) {
        int millisecond = calendar.get(Calendar.MILLISECOND);
        int sec = calendar.get(Calendar.SECOND);
        int minutes = calendar.get(Calendar.MINUTE);
        int hours = calendar.get(Calendar.HOUR);
        double secAngle = (60 - sec) * 6 - (millisecond / 150);
        int minutesAngle = (60 - minutes) * 6;
        int hoursAngle = (12 - hours) * 360 / 12 - (minutes / 2);
        int secX = (int) (secLen * Math.sin(Math.toRadians(secAngle)));
        int secY = (int) (secLen * Math.cos(Math.toRadians(secAngle)));
        int minutesX = (int) (minutesLen * Math.sin(Math.toRadians(minutesAngle)));
        int minutesY = (int) (minutesLen * Math.cos(Math.toRadians(minutesAngle)));
        int hoursX = (int) (hoursLen * Math.sin(Math.toRadians(hoursAngle)));
        int hoursY = (int) (hoursLen * Math.cos(Math.toRadians(hoursAngle)));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.BLACK);
        g2.setStroke(HOURS_POINT_WIDTH);
        g2.drawLine(centerX, centerY, centerX - hoursX, centerY - hoursY);

        g2.setColor(new Color(0x2F2F2F));
        g2.setStroke(MINUTES_POINT_WIDTH);
        g2.drawLine(centerX, centerY, centerX - minutesX, centerY - minutesY);

        g2.setColor(Color.RED);
        g2.setStroke(SEC_POINT_WIDTH);
        g2.drawLine(centerX, centerY, centerX - secX, centerY - secY);
        g2.fillOval(centerX - 5, centerY - 5, 10, 10);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Composite composite = g2.getComposite();
        g2.setComposite(AlphaComposite.SrcOver.derive(0.6f));
        Calendar calendar = Calendar.getInstance();
        drawClock(g2, calendar);
        g2.setComposite(composite);
        g2.dispose();
    }

    @Override
    public void run() {
        while (true) {
            this.repaint();
        }
    }
}

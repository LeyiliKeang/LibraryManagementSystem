package groupwork.my;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MyMouseAdapter extends MouseAdapter {
    private boolean flag = false;
    private int clickNum = 0;

    @Override
    public void mouseClicked(MouseEvent e) {
        final MouseEvent me = e;
        this.flag = false;
        if (this.clickNum == 1) {
            this.mouseDoubleClicked(me);
            this.clickNum = 0;
            this.flag = true;
            return;
        }
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            private int n = 0;

            @Override
            public void run() {
                if (flag) {
                    n = 0;
                    clickNum = 0;
                    this.cancel();
                    return;
                }
                if (n == 1) {
                    mouseSingleClicked(me);
                    flag = true;
                    clickNum = 0;
                    n = 0;
                    this.cancel();
                    return;
                }
                clickNum++;
                n++;
            }
        }, new Date(), 200);
    }

    public void mouseSingleClicked(MouseEvent e) {

    }

    public void mouseDoubleClicked(MouseEvent e) {

    }
}

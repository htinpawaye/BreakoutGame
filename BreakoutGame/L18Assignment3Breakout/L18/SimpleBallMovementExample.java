import acm.graphics.GOval;
import acm.program.GraphicsProgram;

import java.awt.*;

public class SimpleBallMovementExample extends GraphicsProgram {
    public void run() {
        GOval ball = new GOval(0, 0, 30, 30);
        ball.setFilled(true);
        ball.setFillColor(Color.RED);

        double cx = (getWidth() - 30)/2.0;
        double cy = (getHeight() - 30)/2.0;
        ball.setLocation(cx, cy);

        add(ball);

        final int noOfSteps = 500;
        double dx = 2;
        double dy = 4;

        while (true) {
            ball.move(dx, dy);
            pause(10);
            // check collision with upper and lower borders of canvas
            if ((ball.getY() + 30) >= getHeight() || ball.getY() <= 0) {
                dy = -dy;
            }
            if ((ball.getX() + 30) >= getWidth() || ball.getX() <= 0) {
                dx =-dx;
            }
        }


    }
}

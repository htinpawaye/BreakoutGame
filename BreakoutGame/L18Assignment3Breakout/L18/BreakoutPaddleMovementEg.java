import acm.graphics.*;
import acm.program.GraphicsProgram;

import java.awt.event.MouseEvent;

public class BreakoutPaddleMovementEg extends GraphicsProgram {

        public static final int APPLICATION_WIDTH = 400;
        public static final int APPLICATION_HEIGHT = 600;

        final int PDL_WIDTH = 60;
        final int PDL_HEIGHT = 10;
        final int PDL_Y_OFFSET = 30;

        private GRect paddle = null;

        public void run() {
                addMouseListeners();
                setupPaddle();
        }

        // This method must be public scope, must be the same name with the same parameter type
        // You can put @Override like below, called annotation in Java, so that compiler
        // gives warning if your method's scope, name and param are not correct
        // You have to change so that the paddle doesn't move beyond the sides
        // paddle က ဘောင်ကိုကျော်မသွားအောင် လုပ်ဖို့လိုသေးတယ်
        public void mouseMoved(MouseEvent e) {
                double eX = e.getX();
                //println("Current mouse pos: (x = " + eX + ", " + e.getY() + ")");
                int dx = 1;
                while ((paddle.getX() + PDL_WIDTH/2) >= eX) {
                        paddle.move(-dx, 0);
                }
                while ((paddle.getX() + PDL_WIDTH/2) <= eX) {
                        paddle.move(dx, 0);
                }
        }





















































        void setupPaddle() {
                double x = getWidth() / 2 - PDL_WIDTH / 2;
                double y = getHeight() - PDL_Y_OFFSET - PDL_HEIGHT;
                paddle = new GRect(x, y, PDL_WIDTH, PDL_HEIGHT);
                paddle.setFilled(true);
                add(paddle);
        }
}

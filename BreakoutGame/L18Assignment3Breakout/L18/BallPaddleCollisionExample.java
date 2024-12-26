import acm.graphics.*;
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;

import java.awt.event.MouseEvent;

public class BallPaddleCollisionExample extends GraphicsProgram {

        public static final int APPLICATION_WIDTH = 400;
        public static final int APPLICATION_HEIGHT = 600;

        final int PDL_WIDTH = 60;
        final int PDL_HEIGHT = 10;
        final int PDL_Y_OFFSET = 30;

        private final int BALL_RADIUS = 10;

        private GOval ball = null;
        private GRect paddle = null;

        private double vx = 0;
        private double vy = 0;

        public void run() {
                addMouseListeners();
                setupPaddle();
                setupBall();
                initVelocity();
                moveBall();
        }

        void setupBall() {
                ball = new GOval(200, 300, BALL_RADIUS * 2, BALL_RADIUS * 2);
                ball.setFilled(true);
                add(ball);

        }

        void moveBall() {
                int delay = 15;
                while (true) {
                        ball.move(vx, vy);
                        if ((ball.getY() + BALL_RADIUS * 2) >= getHeight() || ball.getY() <= 0) {
                                vy = -vy;
                        }

                        if ((ball.getX() + BALL_RADIUS * 2) >= getWidth() || ball.getX() <= 0) {
                                vx = -vx;
                        }
                        handlePaddleCollision();
                        pause(delay);
                }
        }

        void handlePaddleCollision() {
                if (getCollidingObj() == paddle) {
                        vy = -vy;
                }
        }


        // !!! this is just an illustration of how ball and paddle collision
        // can be detected. Breakout game လို အပ်ချက်အတွက် ကိုယ့်နည်းကိုယ့်ဟန်နဲ့
        // စဉ်းစားပါ
        GObject getCollidingObj() {
                double x = ball.getX();
                double y = ball.getY();
                double blx = x;
                double bty = y + 2 * BALL_RADIUS;

                double brx = x + 2 * BALL_RADIUS;
                double bby = y + 2 * BALL_RADIUS;

                GObject obj1 = getElementAt(blx, bty);
                GObject obj2 = getElementAt(brx, bty);
                GObject obj3 = getElementAt(brx, bby);
                GObject obj4 = getElementAt(blx, bby);

                if (obj1 != null) {
                        return obj1;
                }

                if (obj2 != null) {
                        return obj2;
                }

                // etc for obj 3 and 4

                // no collision
                return null;

        }


        public void mouseMoved(MouseEvent e) {
                double eX = e.getX();
                int dx = 1;
                while ((paddle.getX() + PDL_WIDTH / 2) >= eX) {
                        paddle.move(-dx, 0);
                }
                while ((paddle.getX() + PDL_WIDTH / 2) <= eX) {
                        paddle.move(dx, 0);
                }
        }

        void initVelocity() {
                RandomGenerator rgen = RandomGenerator.getInstance();
                vy = 3;
                vx = rgen.nextDouble(1.0, 3.0);
                if (rgen.nextBoolean(0.5)) vx = -vx;
        }


        void setupPaddle() {
                double x = getWidth() / 2 - PDL_WIDTH / 2;
                double y = getHeight() - PDL_Y_OFFSET - PDL_HEIGHT;
                paddle = new GRect(x, y, PDL_WIDTH, PDL_HEIGHT);
                paddle.setFilled(true);
                add(paddle);
        }
}

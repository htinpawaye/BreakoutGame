import acm.graphics.GOval;
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;

public class BallBouncingAgainstWallEg extends GraphicsProgram {

        double vx;
        double vy;

        private static final int SIZE = 20;

        GOval ball;

        @Override
        public void run() {

                initVelocity();
                setupBall();
                moveBall();

        }

        void setupBall() {
                int x = (getWidth() - SIZE) / 2;
                int y = (getHeight() - SIZE) / 2;
                ball = new GOval(x, y, SIZE, SIZE);
                ball.setFilled(true);
                add(ball);
        }


        void moveBall() {
                final int delay = 15;
                while (true) {
                        ball.move(vx, vy);
                        if (ball.getY() + SIZE >= getHeight() || ball.getY() <= 0) {
                                vy = -vy;
                        }

                        if (ball.getX() + SIZE >= getWidth() || ball.getX() <= 0) {
                                vx = -vx;
                        }
                        pause(delay);
                }
        }

        void initVelocity() {
                RandomGenerator randomGen = RandomGenerator.getInstance();
                vy = 3;
                vx = randomGen.nextDouble(1.0, 3.0);
                if (randomGen.nextBoolean(0.5)) vx = -vx;
        }
}

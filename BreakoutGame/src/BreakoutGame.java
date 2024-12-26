import acm.graphics.*;
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class BreakoutGame extends GraphicsProgram {
    public static final int APPLICATION_WIDTH = 400;
    public static final int APPLICATION_HEIGHT = 600;

    final int PDL_WIDTH = 60;
    final int PDL_HEIGHT = 10;
    final int PDL_Y_OFFSET = 30;

    private final int BALL_RADIUS = 10;

    final int BRICKS_PER_ROW = 10;
    final int BRICK_ROWS = 10;
    final int BRICK_GAP = 4;

    final int BRICK_WIDTH = (APPLICATION_WIDTH - (BRICKS_PER_ROW - 1) * BRICK_GAP) / BRICKS_PER_ROW;
    final int BRICK_HEIGHT = 8;
    final int BRICK_Y_OFFSET = 70;

    private int TURNS = 3;

    private GOval ball = null;
    private GRect paddle = null;
    private int remainingBricks;

    private double vx ;
    private double vy;

    private static final Color[] rowColors = {Color.red, Color.red, Color.orange, Color.orange,
            Color.yellow, Color.yellow, Color.green, Color.green,
            Color.cyan, Color.cyan};

    private int brickCount = BRICK_ROWS * BRICKS_PER_ROW;

    private GLabel message;
    private GObject collider;
    private int turn;
    int delay = 10;

    public void run() {
        createBricks();
        setupPaddle();
        setupBall();
        initVelocity();
        turn = TURNS;
        brickCount = BRICK_ROWS * BRICKS_PER_ROW;
        addMouseListeners();
        waitForClick();
        while (true) {
            moveBall();
            checkCollision();
            if (brickCount == 0) {
                displayWinMessage();
                waitForClick();
                resetGame();
            }
            if (ball.getY() >= getHeight() - BALL_RADIUS * 2) {

                if (turn > 1) {
                    loseTurn();
                    remove(message);
                    gameStartAgain();
                    waitForClick();
                } else {
                    displayGameOver();
                    waitForClick();
                    resetGame();
                }
            }
            pause(delay);
        }
    }


    private void createBricks() {
        double LEFT_MARGIN = (APPLICATION_WIDTH - (BRICK_WIDTH * BRICKS_PER_ROW + BRICK_GAP * (BRICKS_PER_ROW - 1))) / 2.0;
        for (int i = 0; i < BRICKS_PER_ROW; i++) {
            for (int j = 0; j < BRICK_ROWS; j++) {
                GRect brick = new GRect((LEFT_MARGIN / 2.0) + i * (BRICK_WIDTH + BRICK_GAP),
                        (BRICK_Y_OFFSET) + j * (BRICK_HEIGHT + BRICK_GAP), BRICK_WIDTH, BRICK_HEIGHT);
                brick.setColor(rowColors[j]);
                brick.setFilled(true);
                add(brick);
            }
        }

    }

    void setupPaddle() {
        double x = getWidth() / 2 - PDL_WIDTH / 2;
        double y = getHeight() - PDL_Y_OFFSET - PDL_HEIGHT;
        paddle = new GRect(x, y, PDL_WIDTH, PDL_HEIGHT);
        paddle.setFilled(true);
        add(paddle);
    }

    public void mouseMoved(MouseEvent e) {
        double eX = e.getX();

        int dx = 1;

        int left = 0;
        int right = getWidth() - PDL_WIDTH - dx;
        while ((paddle.getX() + PDL_WIDTH / 2) >= eX &&
                (paddle.getX() - dx) > left) {
            paddle.move(-dx, 0);
        }
        while ((paddle.getX() + PDL_WIDTH / 2) <=eX &&
                (paddle.getX() + dx) < right) {
            paddle.move(dx, 0);
        }
    }

    void setupBall() {
        double x = (APPLICATION_WIDTH - BALL_RADIUS * 2) / 2;
        double y = (APPLICATION_HEIGHT - BALL_RADIUS * 2) / 2;
        ball = new GOval(x, y, BALL_RADIUS * 2, BALL_RADIUS * 2);
        ball.setFilled(true);
        add(ball);
    }

    void initVelocity() {
        RandomGenerator rgen = RandomGenerator.getInstance();
        vy = 3;
        vx = rgen.nextDouble(1.0, 3.0);
        if (rgen.nextBoolean(0.5)) vx = -vx;
    }

    private void moveBall() {
        ball.move(vx, vy);
        if (ball.getX() <= 0 || ball.getX() >= getWidth() - BALL_RADIUS * 2) {
            vx = -vx;
        }
        if (ball.getY() <= 0) {
            vy = -vy;
        }
    }

    private void checkCollision() {
        collider = getElementAt(ball.getX(), ball.getY());
        if (collider == null) {
            collider = getElementAt(ball.getX() + BALL_RADIUS * 2, ball.getY());
        }
        if (collider == null) {
            collider = getElementAt(ball.getX(), ball.getY() + BALL_RADIUS * 2);
        }
        if (collider == null) {
            collider = getElementAt(ball.getX() + BALL_RADIUS * 2, ball.getY() + BALL_RADIUS * 2);
        }
        if (collider != null) {
            vy = -vy;
            if (collider != paddle) {
                remove(collider);
                brickCount--;
            }
        }
    }

    private void loseTurn(){
        removeAll();
        turn--;
        message = new GLabel("You Loose! Click to play next turn.");
        message.setFont("SansSerif-18");
        message.setColor(Color.RED);
        double x = (APPLICATION_WIDTH - message.getWidth()) / 2;
        double y = (APPLICATION_HEIGHT - message.getAscent()) / 2;
        add(message, x, y);
        waitForClick();
    }

    private void gameStartAgain(){
        createBricks();
        setupPaddle();
        setupBall();
    }

    private void displayGameOver() {
        removeAll();

        message = new GLabel("Game Over! Click to restart.");
        message.setFont("SansSerif-18");
        message.setColor(Color.RED);
        double x = (APPLICATION_WIDTH - message.getWidth()) / 2;
        double y = (APPLICATION_HEIGHT - message.getAscent()) / 2;
        add(message, x, y);

    }

    private void displayWinMessage() {
        removeAll();
        message = new GLabel("You Win! Click to play again.");
        message.setFont("SansSerif-18");
        message.setColor(Color.RED);
        double x = (APPLICATION_WIDTH - message.getWidth()) / 2;
        double y = (APPLICATION_HEIGHT - message.getAscent()) / 2;
        add(message, x, y);
    }

    private void resetGame(){
        removeAll();
        turn = TURNS;
        brickCount = BRICK_ROWS * BRICKS_PER_ROW;
        createBricks();
        setupBall();
        setupPaddle();
        waitForClick();
    }

}

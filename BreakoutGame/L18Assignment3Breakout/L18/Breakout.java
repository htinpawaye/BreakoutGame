//import acm.graphics.*;
//import acm.program.GraphicsProgram;
//
//public class Breakout extends GraphicsProgram {
//
//        public static final int APPLICATION_WIDTH = 400;
//        public static final int APPLICATION_HEIGHT = 600;
//
//        final int PDL_WIDTH = 60;       // paddle width
//        final int PDL_HEIGHT = 10;      // paddle height
//        final int PDL_Y_OFFSET = 30;    // distance between bottom wall and bottom of baddle
//
//        private final int BALL_RADIUS = 10;     // size of ball will be (2 * BALL_RADIUS), you can change this value according to your taste
//
//        final int BRICKS_PER_ROW = 10;
//        final int BRICK_ROWS = 10;
//        final int BRICK_GAP = 4;        // horizontal and vertical gap between bricks rows and brick columns
//        final int BRICK_WIDTH =
//                        (APPLICATION_WIDTH - (BRICKS_PER_ROW - 1) * BRICK_GAP) / BRICKS_PER_ROW;
//        final int BRICK_HEIGHT = 8;
//        final int BRICK_Y_OFFSET = 70;  // distance from y = 0 (above wall) to top of the first row of bricks
//
//        final int TURNS = 3;            // Number of turns for one game
//
//        private GOval ball = null;
//        private GRect paddle = null;
//        private int remainingBricks;    // to store remaining brick count
//
//        private double vx = 0;          // horizontal velocity
//        private double vy = 0;          // vertical velocity
//
//        public void run() {
//
//        }
//
//        // overall game logic
//
//        // your methods
//        // setUp bricks
//        // setUp paddle
//        // set up ball
//        // set up init velocity
//        // mouseMoved
//        // getCollidingObject
//
//}
//

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram
{
        int score = 0;
        int best = 0;
        int level = 1;
        int numLives = 5;
        int powerCount = 5;


        //width of game display
        private static final int WIDTH = 400;

        //height of game display
        private static final int HEIGHT = 600;

        //width of paddle
        private static final int PADDLE_WIDTH = 60;

        //height of paddle
        private static final int PADDLE_HEIGHT = 10;

        //offset of paddle up from the bottom
        private static final int PADDLE_Y_OFFSET = 30;

        //number of bricks per row
        private static final int NBRICKS_PER_ROW = 10;

        //number of rows of bricks
        private static final int NBRICK_ROWS = 10;

        //separation between bricks
        private static final int BRICK_SEP = 4;

        //width of each brick (based on the dimensions of the game display)
        private static final int BRICK_WIDTH = WIDTH / NBRICKS_PER_ROW - BRICK_SEP;

        //height of brick
        private static final int BRICK_HEIGHT = 8;

        //radius of ball in pixels
        private static final int BALL_RADIUS = 6;

        //offset of the top brick row from top
        private static final int BRICK_Y_OFFSET = 70;

        //number of turns
        private static final int NTURNS = 3;

        //the paddle
        private GRect paddle;

        //the ball
        private GOval ball;

        //ball velocity in both directions (x-direction, and y-direction)
        private double vx, vy;

        //records the last x position of the mouse (see mouseMoved method)
        private double lastX;

        //used for mouse events (only moves the paddle every 5th mouse move)
        private int toggle = 0;

        //Random Generator for Ball
        private RandomGenerator rgen = new RandomGenerator();

        //Creates the score label
        GLabel scoreLabel = new GLabel("Score: " + score, 25, 25);

        //Creates the level label
        GLabel levelLabel = new GLabel("Level: " + level, 150, 25);

        //Creates the best score label
        GLabel bestLabel = new GLabel("Best Score: " + best, 275, 25);

        //Creates the number of lives label
        GLabel numLivesLabel = new GLabel("Number of Lives: " + numLives, 125, 50);

        //Creates Background Image
        //	private GImage coolpic;
        //GImage background = new GImage("coolpic");
        //	add(coolpic);


        //main method -- called when the program is run
        public static void main(String[] args)
        {
                String[] sizeArgs = { "width=" + WIDTH, "height=" + HEIGHT };
                new Breakout().start(sizeArgs);
        }

        //run method -- called indirectly from the main method
        public void run()
        {
                setup();
                play();
        }

        //setup method -- called from the run method
        public void setup()
        {
                //add(background);
                add(scoreLabel);
                add(levelLabel);
                add(bestLabel);
                add(numLivesLabel);
                createBricks();
                createPaddle();
                createBall();
                addMouseListeners();
        }

        //createBricks method -- called from the setup method
        //Also creates other obstacles
        public void createBricks()
        {
                //make the bricks
                for(int x = 0; x < NBRICK_ROWS; x++)
                {
                        for(int y = 0; y < NBRICKS_PER_ROW; y++)
                        {
                                GRect brick = new GRect((y * BRICK_WIDTH) + BRICK_SEP*y + BRICK_SEP/2,
                                        BRICK_Y_OFFSET + (BRICK_HEIGHT * x) + BRICK_SEP*x,
                                        BRICK_WIDTH,
                                        BRICK_HEIGHT);
                                brick.setFilled(true);

                                if(x <= 1)
                                {
                                        brick.setFillColor(Color.RED);
                                }
                                else if(x <= 3)
                                {
                                        brick.setFillColor(Color.ORANGE);
                                }
                                else if(x <= 5)
                                {
                                        brick.setFillColor(Color.YELLOW);
                                }
                                else if(x <= 7)
                                {
                                        brick.setFillColor(Color.GREEN);
                                }
                                else if(x <= 9)
                                {
                                        brick.setFillColor(Color.CYAN);
                                }

                                add(brick);

                        }
                }
        }

        //createPaddle method -- called from the setup method
        public void createPaddle()
        {
                paddle = new GRect(0,590,PADDLE_WIDTH,PADDLE_HEIGHT);
                paddle.setFilled(true);
                paddle.setFillColor(Color.black);
                add(paddle);
        }

        //createBall method -- called from the setup method
        public void createBall()
        {
                ball = new GOval(WIDTH/2-6, HEIGHT-50,12,12);
                ball.setFilled(true);
                ball.setFillColor(Color.black);
                add(ball);
        }

        public void loseLife()
        {
                numLives--;
                pause(4000);
                vy = -vy;

                if(numLives == 0)
                {
                        vx = 0;
                        vy = 0;

                        numLives = 5;

                        run();
                }
        }



        //play method -- called from the run method after setup
        public void play()
        {
                startTheBall();
                playBall();
        }

        //startTheBall method -- called from the play method
        public void startTheBall()
        {
                vx = rgen.nextDouble(1.0, 3.0);
                if (rgen.nextBoolean(0.5)) vx = -vx;
                vy = -1.0;
        }

        //Check the score method - called to allow game play
        public void checkScore()
        {

                if(score > 0 && score < 100)
                {
                        scoreLabel.setLabel("Score: " + score);
                        numLivesLabel.setLabel("Number of Lives: " + numLives);
                }
                if(score >= 100)
                {
                        scoreLabel.setLabel("Score: " + score);
                        numLivesLabel.setLabel("Number of Lives: " + numLives);
                }
                if(score >= 250)
                {
                        scoreLabel.setLabel("Score: " + score);
                        numLivesLabel.setLabel("Number of Lives: " + numLives);
                }
                if(score >= 500)
                {
                        scoreLabel.setLabel("Score: " + score);
                        numLivesLabel.setLabel("Number of Lives: " + numLives);
                }

        }




        //playBall method -- called from the play method
        public void playBall()
        {
                //continuous loop
                while(true)
                {
                        //Check Score
                        checkScore();
                        //move the ball
                        ball.move(vx, vy);
                        //pause
                        pause(1);

                        //check for contact along the outer walls

                        if(ball.getX() < 0)
                        {
                                vx = -vx;
                        }
                        if(ball.getX() > WIDTH)
                        {
                                vx = -vx;
                        }
                        if(ball.getY() < 0)
                        {
                                vy = -vy;
                        }
                        if(ball.getY() > HEIGHT)
                        {
                                loseLife();
                                vy = -vy;
                        }

                        //check for collisions with bricks or paddle
                        GObject collider = getCollidingObject();

                        //if the ball collided with the paddle
                        if(collider == paddle)
                        {
                                //reverse the y velocity
                                vy = -vy;
                        }
                        //otherwise if the ball collided with a brick
                        else if(collider instanceof GRect)
                        {
                                //reverse y velocity
                                vy = -vy;
                                //remove the brick
                                remove(collider);
                                //Add points for scoring
                                score = score + 10;
                        }

                        //set best score label with record score for game
                        if(score >= best)
                        {
                                best = score;
                                bestLabel.setLabel("Best Score: " + best);
                        }

                }

        }


        //getCollidingObject -- called from the playBall method
        //discovers and returns the object that the ball collided with
        private GObject getCollidingObject()
        {
                if(getElementAt(ball.getX(), ball.getY()) != null)
                        return getElementAt(ball.getX(), ball.getY());
                else if(getElementAt(ball.getX()+BALL_RADIUS*2, ball.getY()) != null)
                        return getElementAt(ball.getX()+BALL_RADIUS*2, ball.getY());
                else if(getElementAt(ball.getX()+BALL_RADIUS*2, ball.getY()+BALL_RADIUS*2) != null)
                        return getElementAt(ball.getX()+BALL_RADIUS*2, ball.getY()+BALL_RADIUS*2);
                else if(getElementAt(ball.getX(), ball.getY()+BALL_RADIUS*2) != null)
                        return getElementAt(ball.getX(), ball.getY()+BALL_RADIUS*2);
                else
                        return null;
        }

        //mouseMoved method -- called by the mouseListener when the mouse is moved
        //anywhere within the boundaries of the run window
        public void mouseMoved(MouseEvent e)
        {
                //only move the paddle every 5th mouse event
                //otherwise the play slows every time the mouse moves
                if(toggle == 5)
                {
                        //get the x-coordinate of the mouse
                        double eX = e.getX();

                        //if the mouse moved to the right
                        if(eX - lastX > 0)
                        {
                                //if paddle is not already at the right wall
                                if(paddle.getX() < WIDTH - PADDLE_WIDTH)
                                {
                                        //move to the right
                                        paddle.move(eX - lastX, 0);
                                }
                        }
                        else //(if the mouse moved to the left)
                        {
                                //if paddle is not already at the left wall
                                if(paddle.getX() > 0)
                                {
                                        //move to the left
                                        paddle.move(eX - lastX, 0);
                                }
                        }

                        //record this mouse x position for next mouse event
                        GPoint last = new GPoint(e.getPoint());
                        lastX = last.getX();

                        //reset toggle to 1
                        toggle = 1;
                }
                else
                {
                        //increment toggle by 1
                        //(when toggle gets to 5 the code will move the paddle
                        // and reset toggle back to 1)
                        toggle++;
                }

        }

}

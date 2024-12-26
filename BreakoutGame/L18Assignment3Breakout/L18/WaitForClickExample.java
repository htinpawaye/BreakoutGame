import acm.graphics.GRect;
import acm.program.GraphicsProgram;

import java.awt.*;

public class WaitForClickExample extends GraphicsProgram {
        public void run() {
                GRect rect = new GRect(50, 50, 100, 100);
                add(rect);
                waitForClick();
                rect.setFilled(true);
                rect.setFillColor(Color.RED);
        }
}

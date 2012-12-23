

package draw4; 

import java.awt.*; 
import draw1.*;

public class Eraser extends RectangleShape {

  public void draw(Graphics g) {
    int x = Math.min(x1, x2); 
    int y = Math.min(y1, y2); 
    int w = Math.abs(x1 - x2) + 1; 
    int h = Math.abs(y1 - y2) + 1;
    g.setColor(Color.WHITE);
    g.fillRect(x, y, w, h);
  }

}

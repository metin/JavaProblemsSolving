

package draw2; 

import java.awt.*; 
import draw1.*;

public class FilledOvalShape extends OvalShape {

  public void draw(Graphics g) {
    int x = Math.min(x1, x2); 
    int y = Math.min(y1, y2); 
    int w = Math.abs(x1 - x2) + 1; 
    int h = Math.abs(y1 - y2) + 1;     
    if (color != null) {
      g.setColor(color);
    }
    g.fillOval(x, y, w, h);
  }

}

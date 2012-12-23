

package draw1; 

import java.awt.*; 

public class LineShape extends TwoEndsShape {

  public void draw(Graphics g) {
    if (color != null) {
      g.setColor(color);
    }
    g.drawLine(x1, y1, x2, y2); 
  }

  public void drawOutline(Graphics g, int x1, int y1, int x2, int y2) {
    g.drawLine(x1, y1, x2, y2); 
  }

}

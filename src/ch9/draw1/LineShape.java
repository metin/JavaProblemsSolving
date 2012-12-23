

package draw1; 

import java.awt.*; 

public class LineShape extends TwoEndsShape {

  public void draw(Graphics g) {
    super.beforDraw(g);
    Graphics2D g2 = (Graphics2D)g;
    g2.drawLine(x1, y1, x2, y2); 
  }

  public void drawOutline(Graphics g, int x1, int y1, int x2, int y2) {
    g.drawLine(x1, y1, x2, y2); 
  }

}

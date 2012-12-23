

package scribble3; 

import java.awt.*;

public class ScribbleTool extends AbstractTool { 

  public ScribbleTool(ScribbleCanvas canvas, String name) {
    super(canvas, name);
  }

  public void startShape(Point p) {
    curStroke = new Stroke(canvas.getCurColor()); 
    curStroke.setThickness(canvas.getCurThickness());
    curStroke.addPoint(p); 
  }

  public void addPointToShape(Point p) {
    if (curStroke != null) { 
      curStroke.addPoint(p); 
      Graphics g = canvas.getGraphics();
      Graphics2D g2 = (Graphics2D)g;
      g2.setStroke(canvas.getCurThickness());
      g2.setColor(canvas.getCurColor());
      g2.drawLine(canvas.x, canvas.y, p.x, p.y);
    }
  }

  public void endShape(Point p) {
    if (curStroke != null) { 
      curStroke.addPoint(p); 
      canvas.addShape(curStroke); 
      curStroke = null; 
    }
  }

  protected Stroke curStroke = null; 

}

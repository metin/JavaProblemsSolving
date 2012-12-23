

package draw3; 

import java.awt.*; 
import scribble3.*;

public class TextTool extends AbstractTool implements KeyboardTool {

  public TextTool(ScribbleCanvas canvas, String name) {
    super(canvas, name);
    text = new StringBuffer();    
  }

  public void startShape(Point p) {
    text.delete(0, text.length());
    curShape = new Text(); 
    curShape.setColor(canvas.getCurColor());
    curShape.setLocation(p.x, p.y); 
    if (canvas instanceof KeyboardDrawingCanvas) { 
      curShape.setFont(((KeyboardDrawingCanvas) canvas).getFont());
    }    
    canvas.addShape(curShape); 
  }

  public void addCharToShape(char c) {
    text.append(c);
    curShape.setText(text.toString());
    canvas.repaint();
  }

  public void addPointToShape(Point p) {} 
  public void endShape(Point p) {}

  protected StringBuffer text;
  protected Text curShape; 

}

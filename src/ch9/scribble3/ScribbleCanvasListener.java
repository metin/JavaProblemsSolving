

package scribble3; 

import java.awt.*;
import java.awt.event.*;

public class ScribbleCanvasListener 
    implements MouseListener, MouseMotionListener {

  public ScribbleCanvasListener(ScribbleCanvas canvas) {
    this.canvas = canvas; 
    tool = new ScribbleTool(canvas, "Scribble"); 
  }

  public void mousePressed(MouseEvent e) {
    Point p = e.getPoint(); 
    tool.startShape(p);    
    canvas.mouseButtonDown = true;
    canvas.x = p.x; 
    canvas.y = p.y; 
  } 

  public void mouseDragged(MouseEvent e) {
    Point p = e.getPoint(); 
    if (canvas.mouseButtonDown) {
      tool.addPointToShape(p);
      canvas.x = p.x; 
      canvas.y = p.y; 
    }       
  }

  public void mouseReleased(MouseEvent e) {
    Point p = e.getPoint(); 
    tool.endShape(p);       
    canvas.mouseButtonDown = false;       
  }    

  public void mouseClicked(MouseEvent e) {}
  public void mouseEntered(MouseEvent e) {}  
  public void mouseExited(MouseEvent e) {}
  public void mouseMoved(MouseEvent e) {}     

  protected ScribbleCanvasListener(ScribbleCanvas canvas, Tool tool) {
    this.canvas = canvas; 
    this.tool = tool;
  }

  protected ScribbleCanvas canvas; 
  protected Tool tool; 

}


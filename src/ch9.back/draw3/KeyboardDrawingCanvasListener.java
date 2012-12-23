

package draw3; 

import java.awt.*; 
import java.awt.event.*; 
import draw1.*;

public class KeyboardDrawingCanvasListener extends DrawingCanvasListener implements KeyListener { 

  public KeyboardDrawingCanvasListener(DrawingCanvas canvas) { 
    super(canvas); 
  }

  public void keyPressed(KeyEvent e) {
    if (tool instanceof KeyboardTool) {	
      KeyboardTool keyboardTool = (KeyboardTool) tool; 
      keyboardTool.addCharToShape((char) e.getKeyChar()); 
    }
  } 
    
  public void keyReleased(KeyEvent e) {} 
  public void keyTyped(KeyEvent e) {}
  
  public void mouseClicked(MouseEvent e) {
    canvas.requestFocus(); 
  }

}

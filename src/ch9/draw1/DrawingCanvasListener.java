

package draw1; 

import java.awt.*;
import java.awt.event.*;
import scribble3.*;

public class DrawingCanvasListener extends ScribbleCanvasListener { 

  public DrawingCanvasListener(DrawingCanvas canvas) { 
    super(canvas, null); 
  }

  public Tool getTool() { 
    return tool; 
  }

  public void setTool(Tool tool) { 
    this.tool = tool;
  }

}

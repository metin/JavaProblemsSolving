

package draw2; 

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*; 
import scribble3.*;
import draw1.*;

public class DrawingPad extends draw1.DrawingPad { 

  public DrawingPad(String title) {
    super(title); 
  }

  protected void initTools() { 
    toolkit = new ToolKit(); 
    toolkit.addTool(new ScribbleTool(canvas, "Scribble")); 
    toolkit.addTool(new TwoEndsShapeTool(canvas, "Line", new LineShape())); 
    toolkit.addTool(new TwoEndsShapeTool(canvas, "Oval", new OvalShape())); 
    toolkit.addTool(new TwoEndsShapeTool(canvas, "Rect", new RectangleShape())); 
    toolkit.addTool(new TwoEndsShapeTool(canvas, "Filled Oval", new FilledOvalShape())); 
    toolkit.addTool(new TwoEndsShapeTool(canvas, "Filled Rect", new FilledRectangleShape())); 
    drawingCanvas.setTool(toolkit.getTool(0));
  }

  public static void main(String[] args) {
    JFrame frame = new draw2.DrawingPad("Drawing Pad");
    frame.setSize(width, height);
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    frame.setLocation(screenSize.width/2 - width/2,
		      screenSize.height/2 - height/2);
    frame.show();
  }

}



package draw1; 

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.*;
import java.io.*;
import java.awt.event.*;
import java.util.EventListener;
import javax.swing.*; 
import scribble3.*;

public class DrawingCanvas extends ScribbleCanvas {

  public DrawingCanvas() {
  }

  public void setTool(Tool tool) { 
    drawingCanvasListener.setTool(tool);
  }

  public Tool getTool() { 
    return drawingCanvasListener.getTool(); 
  }

  // factory method 
  protected EventListener makeCanvasListener() {
    return (drawingCanvasListener = new DrawingCanvasListener(this)); 
  }

  protected DrawingCanvasListener drawingCanvasListener; 

}

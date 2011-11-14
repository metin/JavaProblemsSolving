

package scribble3; 

import java.util.*;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Color;
import java.awt.Graphics;

public class Stroke extends Shape { 

  public Stroke() {} 
  
  public Stroke(Color color) {
    super(color); 
  } 

  public void addPoint(Point p) {
    if (p != null) { 
      points.add(p); 
    }
  }

  public List getPoints() { 
    return points; 
  }

  public void draw(Graphics g) {
    Graphics2D g2 = (Graphics2D)g;
    if (color != null) {
      g2.setColor(color);
    }
    if (thickness != null) {
      g2.setStroke(thickness);
    }

    Point prev = null; 
    Iterator iter = points.iterator(); 
    while (iter.hasNext()) { 
      Point cur = (Point) iter.next(); 
      if (prev != null) {
        g2.drawLine(prev.x, prev.y, cur.x, cur.y); 
      }
      prev = cur; 
    }
  }

  // The list of points on the stroke
  // elements are instances of java.awt.Point 
  protected List points = new ArrayList();  
  
}

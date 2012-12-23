

package scribble3; 

import java.awt.*;
import java.io.Serializable;

public abstract class Shape implements Serializable { 

  public Shape() {} 
  
  public Shape(Color color) {
    this.color = color; 
  } 

  public void setColor(Color color) {
    this.color = color; 
  } 

  public Color getColor() {
    return color; 
  }

  public void setThickness(BasicStroke thickness) {
    this.thickness = thickness; 
  } 

  public BasicStroke getThickness() {
    return thickness; 
  }
  
  public abstract void draw(Graphics g); 

  protected Color color = Color.black; 
  protected BasicStroke thickness;


}

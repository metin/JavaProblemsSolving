

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

  public abstract void draw(Graphics g); 

  protected Color color = Color.black; 

}

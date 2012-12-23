
package maze; 

import java.awt.*; 

public interface MapSite extends Cloneable { 

  public Object clone() throws CloneNotSupportedException;
  public void enter(Maze maze); 
  public void draw(Graphics g, int x, int y, int w, int h); 

}

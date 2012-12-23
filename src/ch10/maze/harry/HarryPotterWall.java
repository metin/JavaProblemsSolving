
package maze.harry; 

import java.awt.*; 
import maze.*;

class HarryPotterWall extends Wall {

  public static final Color WALL_COLOR = new Color(178, 34, 34);

  public void draw(Graphics g, int x, int y, int w, int h) {
    g.setColor(WALL_COLOR); 
    g.fillRect(x, y, w, h); 
  }

}


package maze.snow; 

import java.awt.*; 
import maze.*;

class SnowWhiteWall extends Wall {

  public static final Color WALL_COLOR = new Color(255, 20, 147);

  public void draw(Graphics g, int x, int y, int w, int h) {
    g.setColor(WALL_COLOR); 
    g.fillRect(x, y, w, h); 
  }

}

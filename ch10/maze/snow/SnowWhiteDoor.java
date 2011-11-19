
package maze.snow; 

import java.awt.*;
import maze.*;  

class SnowWhiteDoor extends Door {
  
  public SnowWhiteDoor(Room room1, Room room2) { 
    super(room1, room2); 
  }
  
  public void draw(Graphics g, int x, int y, int w, int h) {
    g.setColor(SnowWhiteWall.WALL_COLOR); 
    g.fillRect(x, y, w, h); 
    if (orientation == Orientation.VERTICAL) { 
      y += 2 * w; h -= 4 * w;
    } else { 
      x += 2 * h; w -= 4 * h; 
    }
    if (open) { 
      g.setColor(SnowWhiteRoom.ROOM_COLOR);
      g.fillRect(x, y, w, h); 
    } else { 
      g.setColor(Color.orange);
      g.fillRect(x, y, w, h); 
      g.setColor(Color.black);
      g.drawRect(x, y, w, h); 
    }    
  }
  
} 

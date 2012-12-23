
package maze.harry; 

import java.awt.*; 
import maze.*;

class HarryPotterDoor extends Door {
  
  public HarryPotterDoor(Room room1, Room room2) { 
    super(room1, room2); 
  }
  
  public void draw(Graphics g, int x, int y, int w, int h) {
    g.setColor(HarryPotterWall.WALL_COLOR); 
    g.fillRect(x, y, w, h); 
    if (orientation == Orientation.VERTICAL) { 
      y += 2 * w; h -= 4 * w;
    } else { 
      x += 2 * h; w -= 4 * h; 
    }
    if (open) { 
      g.setColor(HarryPotterRoom.ROOM_COLOR);
      g.fillRect(x, y, w, h); 
    } else { 
      g.setColor(new Color(139, 69, 0));
      g.fillRect(x, y, w, h); 
      g.setColor(Color.black);
      g.drawRect(x, y, w, h); 
    }    
  }
  
} 

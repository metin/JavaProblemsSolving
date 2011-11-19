
package maze; 

import java.awt.*; 
import java.applet.AudioClip; 

public class Room implements MapSite { 

  public static final Color ROOM_COLOR = new Color(152, 251, 152);
  public static final Color PLAYER_COLOR = Color.red;

  public Room(int roomNumber) { 
    this.roomNumber = roomNumber; 
  }

  public Object clone() throws CloneNotSupportedException { 
    Room room = (Room) super.clone();
    room.sides = new MapSite[4]; 
    for (int i = 0; i < 4; i++) {
      if (sides[i] != null) { 
	room.sides[i] = (MapSite) sides[i].clone(); 
      }
    }
    return room; 
  }
  
  public MapSite getSide(Direction dir) { 
    if (dir != null) { 
      return sides[dir.getOrdinal()]; 
    }
    return null; 
  }

  public void setSide(Direction dir, MapSite site) { 
    if (dir != null) { 
      sides[dir.getOrdinal()] = site; 
      if (site instanceof Door) { 
	Door door = (Door) site;
	if (dir == Direction.NORTH ||
	    dir == Direction.SOUTH) {
	  door.setOrientation(Orientation.HORIZONTAL); 
	} else { 
	  door.setOrientation(Orientation.VERTICAL); 
	}
      }
    }
  }
  
  public void setRoomNumber(int roomNumber) { 
    this.roomNumber = roomNumber; 
  }

  public int getRoomNumber() { 
    return roomNumber; 
  }

  public Point getLocation() { 
    return location; 
  }

  public void setLocation(Point location) { 
    this.location = location;
  }

  public void enter(Maze maze) {
    maze.setCurrentRoom(this);
    gong.play();
  }

  public boolean isInRoom() { 
    return inroom; 
  }

  public void setInRoom(boolean inroom) { 
    this.inroom = inroom;
  }

  public void draw(Graphics g, int x, int y, int w, int h) {
    g.setColor(ROOM_COLOR); 
    g.fillRect(x, y, w, h); 
    if (inroom) { 
      g.setColor(PLAYER_COLOR);
      g.fillOval(x + w / 2 - 5, y + h / 2 - 5, 10, 10); 
    }
  }

  protected int roomNumber = 0; 
  protected boolean inroom = false; 
  protected MapSite[] sides = new MapSite[4]; 
  protected Point location = null;   

  protected static AudioClip gong = util.AudioUtility.getAudioClip("audio/gong.au"); 
  //protected static AudioClip spacemusic = util.AudioUtility.getAudioClip("audio/spacemusic.au"); 
} 

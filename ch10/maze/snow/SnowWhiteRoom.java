
package maze.snow; 

import java.awt.*; 
import java.applet.AudioClip; 
import maze.*;

class SnowWhiteRoom extends Room { 

  public static final Color ROOM_COLOR = new Color(255, 218, 185);
  public static final Color PLAYER_COLOR = Color.white;

  public SnowWhiteRoom(int roomNumber) { 
    super(roomNumber);
  }

  public void enter(Maze maze) {
    maze.setCurrentRoom(this);
    tiptoe.play();
  }

  public void draw(Graphics g, int x, int y, int w, int h) {
    g.setColor(ROOM_COLOR); 
    g.fillRect(x, y, w, h); 
    if (inroom) { 
      g.setColor(PLAYER_COLOR);
      g.fillOval(x + w / 2 - 5, y + h / 2 - 5, 10, 10); 
    }
  }

  protected static AudioClip tiptoe = util.AudioUtility.getAudioClip("audio/tiptoe.thru.the.tulips.au");

}

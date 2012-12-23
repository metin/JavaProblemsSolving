
package maze; 

import java.awt.*;
import javax.swing.*;

public class MazePrototypeFactory extends MazeFactory { 

  public MazePrototypeFactory(Maze mazePrototype, 
			      Wall wallPrototype, 
			      Room roomPrototype, 
			      Door doorPrototype) { 
    this.mazePrototype = mazePrototype;
    this.wallPrototype = wallPrototype;
    this.roomPrototype = roomPrototype;
    this.doorPrototype = doorPrototype;
  }

  public Maze makeMaze() {
    try { 
      return (Maze) mazePrototype.clone(); 
    } catch (CloneNotSupportedException e) {
      System.err.println("CloneNotSupportedException: " + e.getMessage());
    }
    return null; 
  }

  public Wall makeWall() { 
    try { 
      return (Wall) wallPrototype.clone(); 
    } catch (CloneNotSupportedException e) {
      System.err.println("CloneNotSupportedException: " + e.getMessage());
    }
    return null; 
  }

  public Room makeRoom(int roomNumber) { 
    try { 
      Room room = (Room) roomPrototype.clone(); 
      room.setRoomNumber(roomNumber); 
      return room; 
    } catch (CloneNotSupportedException e) {
      System.err.println("CloneNotSupportedException: " + e.getMessage());
    }
    return null; 
  }

  public Door makeDoor(Room room1, Room room2) { 
    try { 
      Door door = (Door) doorPrototype.clone();
      door.setRooms(room1, room2); 
      return door;
    } catch (CloneNotSupportedException e) {
      System.err.println("CloneNotSupportedException: " + e.getMessage());
    }
    return null; 
  }

  protected Maze mazePrototype;
  protected Wall wallPrototype;
  protected Room roomPrototype; 
  protected Door doorPrototype;

  public static void main(String[] args) { 
    Maze maze; 
    MazePrototypeFactory factory = null;
    MazeFactory prototypeFactory = null;
    if (args.length > 0) { 
      if ("Harry".equals(args[0])) { 
	prototypeFactory = new maze.harry.HarryPotterMazeFactory(); 
      } else if ("Snow".equals(args[0])) { 
	prototypeFactory = new maze.snow.SnowWhiteMazeFactory(); 
      }
    }
    if (prototypeFactory == null) { 
      prototypeFactory = new MazeFactory(); 
    }
    factory = new MazePrototypeFactory(prototypeFactory.makeMaze(),
				       prototypeFactory.makeWall(),
				       prototypeFactory.makeRoom(0),
				       prototypeFactory.makeDoor(null, null));
    maze = MazeGameAbstractFactory.createMaze(factory); 
    maze.setCurrentRoom(1); 
    maze.showFrame("Maze -- Prototype");
  }  

}

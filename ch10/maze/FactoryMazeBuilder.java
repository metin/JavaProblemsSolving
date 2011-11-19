
package maze; 

public class FactoryMazeBuilder implements MazeBuilder { 

  public FactoryMazeBuilder(MazeFactory factory) {
    this.factory = factory; 
  }

  /**
   *  Start to build a new Maze
   */ 
  public void newMaze() {
    maze = factory.makeMaze(); 
  } 

  /**
   *  Fetch the Maze that have been built. 
   */ 
  public Maze getMaze() {
    return maze; 
  } 

  /**
   *  Build a new room in the current Maze. 
   */ 
  public void buildRoom(int roomNumber) {
    if (maze == null) { 
      newMaze();
    }
    Room room = factory.makeRoom(roomNumber);
    for (Direction dir = Direction.first(); dir != null; dir = dir.next()) { 
      room.setSide(dir, factory.makeWall()); 
    }
    maze.addRoom(room);  
  }

  /**
   *  Build a new door in the current Maze between two the specified rooms.
   *
   *  @param roomNumber1 specifies the room number of the first room   
   *  @param roomNumber2 specifies the room number of the second room   
   *  @param dir         specifies the side of the first room at which the door will be located
   *                     the second room will be on the other side of the door. 
   */
  public void buildDoor(int roomNumber1, int roomNumber2, Direction dir, boolean open) { 
    if (maze == null) { 
      newMaze();
    }
    Room room1 = maze.findRoom(roomNumber1);
    Room room2 = maze.findRoom(roomNumber2);
    if (room1 != null && 
	room2 != null &&
	dir != null) { 
      Door door = factory.makeDoor(room1, room2); 
      room1.setSide(dir, door); 
      room2.setSide(dir.opposite(), door); 
      door.setOpen(open);
    }
  }

  protected MazeFactory factory;
  protected Maze maze; 

}

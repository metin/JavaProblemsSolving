
package maze; 

public interface MazeBuilder { 

  /**
   *  Start to build a new Maze
   */ 
  public void newMaze(); 

  /**
   *  Fetch the Maze that have been built. 
   */ 
  public Maze getMaze(); 

  /**
   *  Build a new room in the current Maze. 
   */ 
  public void buildRoom(int roomNumber); 

  /**
   *  Build a new door in the current Maze between two the specified rooms.
   *
   *  @param roomNumber1 specifies the room number of the first room   
   *  @param roomNumber2 specifies the room number of the second room   
   *  @param dir         specifies the side of the first room at which the door will be located
   *                     the second room will be on the other side of the door. 
   *  @param open        whether the door is open or not
   */
  public void buildDoor(int roomNumber1, int roomNumber2, 
			Direction dir, boolean open); 

}


package maze.harry; 

import maze.*; 

public class HarryPotterMazeFactory extends MazeFactory { 

  public Wall makeWall() { 
    return new HarryPotterWall(); 
  }

  public Room makeRoom(int roomNumber) { 
    return new HarryPotterRoom(roomNumber); 
  }

  public Door makeDoor(Room room1, Room room2) { 
    return new HarryPotterDoor(room1, room2); 
  }

}

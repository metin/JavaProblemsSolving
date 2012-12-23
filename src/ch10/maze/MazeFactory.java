
package maze; 

public class MazeFactory { 

  public Maze makeMaze() {
    return new Maze(); 
  }

  public Wall makeWall() { 
    return new Wall(); 
  }

  public Room makeRoom(int roomNumber) { 
    return new Room(roomNumber); 
  }

  public Door makeDoor(Room room1, Room room2) { 
    return new Door(room1, room2); 
  }

}

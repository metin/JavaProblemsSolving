
package maze; 

import java.awt.*;
import javax.swing.*;

/*
 *  Build a Maze game. 
 *
 *  This design uses Factory Methods design pattern. 
 *
 *  Compare this design with the one using Abstract Factory design pattern:
 *    MazeGameAbstractFactory   
 *
 */
public class MazeGameCreator { 

  // This method must not be static
  public Maze createMaze() { 
    Maze maze = makeMaze(); 
    Room room1 = makeRoom(1); 
    Room room2 = makeRoom(2); 
    Room room3 = makeRoom(3); 
    Room room4 = makeRoom(4); 
    Room room5 = makeRoom(5); 
    Room room6 = makeRoom(6); 
    Room room7 = makeRoom(7); 
    Room room8 = makeRoom(8); 
    Room room9 = makeRoom(9); 
    Door door1 = makeDoor(room1, room2);
    Door door2 = makeDoor(room2, room3);
    Door door3 = makeDoor(room4, room5);
    Door door4 = makeDoor(room5, room6);
    Door door5 = makeDoor(room5, room8);
    Door door6 = makeDoor(room6, room9);
    Door door7 = makeDoor(room7, room8);
    Door door8 = makeDoor(room1, room4);
    
    door1.setOpen(true); 
    door2.setOpen(false); 
    door3.setOpen(true); 
    door4.setOpen(true); 
    door5.setOpen(false); 
    door6.setOpen(true); 
    door7.setOpen(true); 
    door8.setOpen(true); 

    room1.setSide(Direction.NORTH, door8);
    room1.setSide(Direction.EAST, makeWall());
    room1.setSide(Direction.SOUTH, makeWall());
    room1.setSide(Direction.WEST, door1);

    room2.setSide(Direction.NORTH, makeWall());
    room2.setSide(Direction.EAST, door1);
    room2.setSide(Direction.SOUTH, makeWall());
    room2.setSide(Direction.WEST, door2);

    room3.setSide(Direction.NORTH, makeWall());
    room3.setSide(Direction.EAST, door2);
    room3.setSide(Direction.SOUTH, makeWall());
    room3.setSide(Direction.WEST, makeWall());

    room4.setSide(Direction.NORTH, makeWall());
    room4.setSide(Direction.EAST, makeWall());
    room4.setSide(Direction.SOUTH, door8);
    room4.setSide(Direction.WEST, door3);

    room5.setSide(Direction.NORTH, door5);
    room5.setSide(Direction.EAST, door3);
    room5.setSide(Direction.SOUTH, makeWall());
    room5.setSide(Direction.WEST, door4);

    room6.setSide(Direction.NORTH, door6);
    room6.setSide(Direction.EAST, door4);
    room6.setSide(Direction.SOUTH, makeWall());
    room6.setSide(Direction.WEST, makeWall());

    room7.setSide(Direction.NORTH, makeWall());
    room7.setSide(Direction.EAST, makeWall());
    room7.setSide(Direction.SOUTH, makeWall());
    room7.setSide(Direction.WEST, door7);

    room8.setSide(Direction.NORTH, makeWall());
    room8.setSide(Direction.EAST, door7);
    room8.setSide(Direction.SOUTH, door5);
    room8.setSide(Direction.WEST, makeWall());

    room9.setSide(Direction.NORTH, makeWall());
    room9.setSide(Direction.EAST, makeWall());
    room9.setSide(Direction.SOUTH, door6);
    room9.setSide(Direction.WEST, makeWall());

    maze.addRoom(room1);
    maze.addRoom(room2);
    maze.addRoom(room3);
    maze.addRoom(room4);
    maze.addRoom(room5);
    maze.addRoom(room6);
    maze.addRoom(room7);
    maze.addRoom(room8);
    maze.addRoom(room9);

    return maze;
  }

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

  public static void main(String[] args) { 
    Maze maze; 
    MazeGameCreator creator = null;

    if (args.length > 0) { 
      if ("Harry".equals(args[0])) { 
	creator = new maze.harry.HarryPotterMazeGameCreator(); 
      } else if ("Snow".equals(args[0])) { 
	creator = new maze.snow.SnowWhiteMazeGameCreator(); 
      }
    }
    if (creator == null) { 
      creator = new MazeGameCreator(); 
    }
    maze = creator.createMaze(); 
    maze.setCurrentRoom(1); 
    maze.showFrame("Maze -- Factory Method");
  }

}


package maze; 

import java.awt.*;
import javax.swing.*;

/*
 *  Build a Maze game. 
 *
 *  This implmentation uses Abstract Factory design pattern. 
 *
 *  Run the program as follows:
 *   java maze.MazeGameAbstractFactory Harry
 *         -- uses the HarryPotterMazeFactory
 *   java maze.MazeGameAbstractFactory Snow
 *         -- uses the SnowWhiteMazeFactory
 *   java maze.MazeGameAbstractFactory
 *         -- uses the default MazeFactory
 *
 */
public class MazeGameAbstractFactory { 

  public static Maze createMaze(MazeFactory factory) { 
    Maze maze = factory.makeMaze(); 
    Room room1 = factory.makeRoom(1); 
    Room room2 = factory.makeRoom(2); 
    Room room3 = factory.makeRoom(3); 
    Room room4 = factory.makeRoom(4); 
    Room room5 = factory.makeRoom(5); 
    Room room6 = factory.makeRoom(6); 
    Room room7 = factory.makeRoom(7); 
    Room room8 = factory.makeRoom(8); 
    Room room9 = factory.makeRoom(9); 
    Door door1 = factory.makeDoor(room1, room2);
    Door door2 = factory.makeDoor(room2, room3);
    Door door3 = factory.makeDoor(room4, room5);
    Door door4 = factory.makeDoor(room5, room6);
    Door door5 = factory.makeDoor(room5, room8);
    Door door6 = factory.makeDoor(room6, room9);
    Door door7 = factory.makeDoor(room7, room8);
    Door door8 = factory.makeDoor(room1, room4);
    
    door1.setOpen(true); 
    door2.setOpen(false); 
    door3.setOpen(true); 
    door4.setOpen(true); 
    door5.setOpen(false); 
    door6.setOpen(true); 
    door7.setOpen(true); 
    door8.setOpen(true); 

    room1.setSide(Direction.NORTH, door8);
    room1.setSide(Direction.EAST, factory.makeWall());
    room1.setSide(Direction.SOUTH, factory.makeWall());
    room1.setSide(Direction.WEST, door1);

    room2.setSide(Direction.NORTH, factory.makeWall());
    room2.setSide(Direction.EAST, door1);
    room2.setSide(Direction.SOUTH, factory.makeWall());
    room2.setSide(Direction.WEST, door2);

    room3.setSide(Direction.NORTH, factory.makeWall());
    room3.setSide(Direction.EAST, door2);
    room3.setSide(Direction.SOUTH, factory.makeWall());
    room3.setSide(Direction.WEST, factory.makeWall());

    room4.setSide(Direction.NORTH, factory.makeWall());
    room4.setSide(Direction.EAST, factory.makeWall());
    room4.setSide(Direction.SOUTH, door8);
    room4.setSide(Direction.WEST, door3);

    room5.setSide(Direction.NORTH, door5);
    room5.setSide(Direction.EAST, door3);
    room5.setSide(Direction.SOUTH, factory.makeWall());
    room5.setSide(Direction.WEST, door4);

    room6.setSide(Direction.NORTH, door6);
    room6.setSide(Direction.EAST, door4);
    room6.setSide(Direction.SOUTH, factory.makeWall());
    room6.setSide(Direction.WEST, factory.makeWall());

    room7.setSide(Direction.NORTH, factory.makeWall());
    room7.setSide(Direction.EAST, factory.makeWall());
    room7.setSide(Direction.SOUTH, factory.makeWall());
    room7.setSide(Direction.WEST, door7);

    room8.setSide(Direction.NORTH, factory.makeWall());
    room8.setSide(Direction.EAST, door7);
    room8.setSide(Direction.SOUTH, door5);
    room8.setSide(Direction.WEST, factory.makeWall());

    room9.setSide(Direction.NORTH, factory.makeWall());
    room9.setSide(Direction.EAST, factory.makeWall());
    room9.setSide(Direction.SOUTH, door6);
    room9.setSide(Direction.WEST, factory.makeWall());

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

  public static void main(String[] args) { 
    Maze maze; 
    MazeFactory factory = null;

    if (args.length > 0) { 
      if ("Harry".equals(args[0])) { 
	factory = new maze.harry.HarryPotterMazeFactory(); 
      } else if ("Snow".equals(args[0])) { 
	factory = new maze.snow.SnowWhiteMazeFactory(); 
      }
    }
    if (factory == null) { 
      factory = new MazeFactory(); 
    }
    maze = createMaze(factory); 
    maze.setCurrentRoom(1); 
    maze.showFrame("Maze -- Abstract Factory");
  }

}

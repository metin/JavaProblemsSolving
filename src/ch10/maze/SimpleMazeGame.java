
package maze; 

import java.awt.*;
import javax.swing.*;

/*
 *  Build a Maze game. 
 *
 *  This implmentation does not use any design patterns. 
 *  Compare the createMaze() method of this class to the ccorresponding method of the following 
 *  classes which create the same Maze using different creational design patterns. 
 *   - MazeGameAbstractFactory: using Abstract Factory design pattern
 *   - MazeGameBuilder: using Builder design pattern
 *
 *  Run the program as follows:
 *   java maze.SimpleMazeGame Large 
 *         -- create a large Maze game
 *   java maze.SimpleMazeGame 
 *         -- create a small Maze game
 */
public class SimpleMazeGame { 

  /**
   *  Creates a small maze with 2 rooms. 
   */ 
  public static Maze createMaze() { 
    Maze maze = new Maze(); 
    Room room1 = new Room(1); 
    Room room2 = new Room(2); 
    Door door = new Door(room1, room2);

    room1.setSide(Direction.NORTH, new Wall());
    room1.setSide(Direction.EAST, door);
    room1.setSide(Direction.SOUTH, new Wall());
    room1.setSide(Direction.WEST, new Wall());
    
    room2.setSide(Direction.NORTH, new Wall());
    room2.setSide(Direction.EAST, new Wall());
    room2.setSide(Direction.SOUTH, new Wall());
    room2.setSide(Direction.WEST, door);

    maze.addRoom(room1);
    maze.addRoom(room2);

    return maze; 
  }

  /**
   *  Creates a large maze with 9 rooms. 
   */ 
  public static Maze createLargeMaze() { 
    Maze maze = new Maze(); 
    Room room1 = new Room(1); 
    Room room2 = new Room(2); 
    Room room3 = new Room(3); 
    Room room4 = new Room(4); 
    Room room5 = new Room(5); 
    Room room6 = new Room(6); 
    Room room7 = new Room(7); 
    Room room8 = new Room(8); 
    Room room9 = new Room(9); 
    Door door1 = new Door(room1, room2);
    Door door2 = new Door(room2, room3);
    Door door3 = new Door(room4, room5);
    Door door4 = new Door(room5, room6);
    Door door5 = new Door(room5, room8);
    Door door6 = new Door(room6, room9);
    Door door7 = new Door(room7, room8);
    Door door8 = new Door(room1, room4);
    
    door1.setOpen(true); 
    door2.setOpen(false); 
    door3.setOpen(true); 
    door4.setOpen(true); 
    door5.setOpen(false); 
    door6.setOpen(true); 
    door7.setOpen(true); 
    door8.setOpen(true); 

    room1.setSide(Direction.NORTH, door8);
    room1.setSide(Direction.EAST, new Wall());
    room1.setSide(Direction.SOUTH, new Wall());
    room1.setSide(Direction.WEST, door1);

    room2.setSide(Direction.NORTH, new Wall());
    room2.setSide(Direction.EAST, door1);
    room2.setSide(Direction.SOUTH, new Wall());
    room2.setSide(Direction.WEST, door2);

    room3.setSide(Direction.NORTH, new Wall());
    room3.setSide(Direction.EAST, door2);
    room3.setSide(Direction.SOUTH, new Wall());
    room3.setSide(Direction.WEST, new Wall());

    room4.setSide(Direction.NORTH, new Wall());
    room4.setSide(Direction.EAST, new Wall());
    room4.setSide(Direction.SOUTH, door8);
    room4.setSide(Direction.WEST, door3);

    room5.setSide(Direction.NORTH, door5);
    room5.setSide(Direction.EAST, door3);
    room5.setSide(Direction.SOUTH, new Wall());
    room5.setSide(Direction.WEST, door4);

    room6.setSide(Direction.NORTH, door6);
    room6.setSide(Direction.EAST, door4);
    room6.setSide(Direction.SOUTH, new Wall());
    room6.setSide(Direction.WEST, new Wall());

    room7.setSide(Direction.NORTH, new Wall());
    room7.setSide(Direction.EAST, new Wall());
    room7.setSide(Direction.SOUTH, new Wall());
    room7.setSide(Direction.WEST, door7);

    room8.setSide(Direction.NORTH, new Wall());
    room8.setSide(Direction.EAST, door7);
    room8.setSide(Direction.SOUTH, door5);
    room8.setSide(Direction.WEST, new Wall());

    room9.setSide(Direction.NORTH, new Wall());
    room9.setSide(Direction.EAST, new Wall());
    room9.setSide(Direction.SOUTH, door6);
    room9.setSide(Direction.WEST, new Wall());

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
    if (args.length > 0 &&
	"Large".equals(args[0])) { 
      maze = createLargeMaze();       
    } else {
      maze = createMaze(); 
    }
    maze.setCurrentRoom(1); 
    maze.showFrame("Maze");
  }


}

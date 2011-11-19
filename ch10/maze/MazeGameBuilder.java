
package maze; 

import java.awt.*;
import javax.swing.*;

/*
 *  Build a Maze game. 
 *
 *  This implmentation uses Builder and Abstract Factory design patterns. 
 *
 *  Run the program as follows:
 *   java maze.MazeGameBuilder Harry
 *         -- uses the FactoryMazeBuilder with the HarryPotterMazeFactory
 *   java maze.MazeGameBuilder Snow
 *         -- uses the FactoryMazeBuilder with the SnowWhiteMazeFactory
 *   java maze.MazeGameBuilder Simple
 *         -- uses the FactoryMazeBuilder with the default MazeFactory
 *   java maze.MazeGameBuilder
 *         -- uses the SimpleMazeBuilder, which does not use a factory
 *
 */
public class MazeGameBuilder { 

  public static Maze createMaze(MazeBuilder builder) { 
    builder.newMaze();
    builder.buildRoom(1); 
    builder.buildRoom(2); 
    builder.buildRoom(3); 
    builder.buildRoom(4); 
    builder.buildRoom(5); 
    builder.buildRoom(6); 
    builder.buildRoom(7); 
    builder.buildRoom(8); 
    builder.buildRoom(9); 

    builder.buildDoor(1, 2, Direction.WEST, true); 
    builder.buildDoor(2, 3, Direction.WEST, false); 
    builder.buildDoor(4, 5, Direction.WEST, true); 
    builder.buildDoor(5, 6, Direction.WEST, true); 
    builder.buildDoor(5, 8, Direction.NORTH, false); 
    builder.buildDoor(6, 9, Direction.NORTH, true); 
    builder.buildDoor(7, 8, Direction.WEST, true); 
    builder.buildDoor(1, 4, Direction.NORTH, true); 

    return builder.getMaze(); 
  }

  public static void main(String[] args) { 
    Maze maze; 
    MazeBuilder builder; 
    MazeFactory factory = null;

    if (args.length > 0) { 
      if ("Harry".equals(args[0])) { 
	factory = new maze.harry.HarryPotterMazeFactory(); 
      } else if ("Snow".equals(args[0])) { 
	factory = new maze.snow.SnowWhiteMazeFactory(); 
      } else if ("Default".equals(args[0])) {
	factory = new MazeFactory(); 
      }
    }
    if (factory != null) { 
      builder = new FactoryMazeBuilder(factory);
    } else { 
      builder = new SimpleMazeBuilder();     
    }
    maze = createMaze(builder); 
    maze.setCurrentRoom(1); 
    maze.showFrame("Maze -- Builder");
  }

}

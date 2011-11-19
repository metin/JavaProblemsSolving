
package maze; 

import java.util.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;

public class UniversalMazeFactory extends MazeFactory {

  public static MazeFactory getInstance() { 
    if (theInstance == null) { 
      if (usePrototype) { 
	MazeFactory factory = null;
	switch (theme) { 
	case HARRY_PORTER_THEME: 
	  factory = new maze.harry.HarryPotterMazeFactory(); 
	  break; 
	case SNOW_WHITE_THEME: 
	  factory = new maze.snow.SnowWhiteMazeFactory(); 
	  break; 
	}
	if (factory == null) { 
	  factory = new MazeFactory(); 
	}
	theInstance = new MazePrototypeFactory(factory.makeMaze(),
					       factory.makeWall(),
					       factory.makeRoom(0),
					       factory.makeDoor(null, null));
      } else { 
	switch (theme) { 
	case HARRY_PORTER_THEME: 
	  theInstance = new maze.harry.HarryPotterMazeFactory();
	  break; 
	case SNOW_WHITE_THEME: 
	  theInstance = new maze.snow.SnowWhiteMazeFactory();
	  break; 
	default:
	  theInstance = new MazeFactory();
	  break; 
	}
      }
    }
    return theInstance; 
  }

  protected UniversalMazeFactory() {}

  private static MazeFactory theInstance = null; 

  private static final int SIMPLE_THEME = 0; 
  private static final int HARRY_PORTER_THEME = 1; 
  private static final int SNOW_WHITE_THEME = 2; 
  private static boolean usePrototype = true; 
  private static int theme = SIMPLE_THEME; 

  static {
    Properties configProperties = new Properties();
    try {
      configProperties.load(new FileInputStream("maze.properties")); 
    } catch (IOException e) {}    
    String value;
    value = System.getProperty("maze.theme"); 
    if (value == null) { 
      value = configProperties.getProperty("maze.theme"); 
    }
    if (value != null) { 
      if ("Harry".equals(value)) { 
	theme = HARRY_PORTER_THEME; 
      } else if ("Snow".equals(value)) {
	theme = SNOW_WHITE_THEME;
      }
    }
    
    value = System.getProperty("maze.prototype"); 
    if (value == null) { 
      value = configProperties.getProperty("maze.prototype"); 
    }
    if (value != null) { 
      usePrototype = Boolean.getBoolean(value);
    }
  }


  public static void main(String[] args) { 
    MazeFactory factory = UniversalMazeFactory.getInstance();
    Maze maze = MazeGameAbstractFactory.createMaze(factory); 
    maze.setCurrentRoom(1); 
    maze.showFrame("Maze -- Universal Factory");
  }

}

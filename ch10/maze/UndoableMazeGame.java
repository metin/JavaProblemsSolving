
package maze; 

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class UndoableMazeGame {

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
    maze = MazeGameBuilder.createMaze(builder); 
    maze.setCurrentRoom(1); 

    JMenuBar menubar = new JMenuBar();
    JMenu menu = new JMenu("Command"); 
    JMenuItem undoMenuItem = new JMenuItem("undo"); 
    undoMenuItem.addActionListener(new MazeCommandAction(maze));
    menu.add(undoMenuItem); 
    menubar.add(menu); 
    
    JFrame frame;    
    frame = new JFrame("Maze -- Builder");
    frame.getContentPane().setLayout(new BorderLayout());
    frame.getContentPane().add(menubar, BorderLayout.NORTH); 
    frame.getContentPane().add(new Maze.MazePanel(maze), BorderLayout.CENTER); 
    frame.pack();
    Dimension frameDim = frame.getSize(); 
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    frame.setLocation(screenSize.width / 2 - frameDim.width / 2, 
		      screenSize.height / 2 - frameDim.height / 2);    
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); 
    frame.setVisible(true);
  }

  static class MazeCommandAction implements ActionListener { 
    
    public MazeCommandAction(Maze maze) { 
      this.maze = maze; 
    }

    public void actionPerformed(ActionEvent event) { 
      maze.undoCommand(); 
    }

    protected Maze maze;
  }

}

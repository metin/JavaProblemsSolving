package maze;

import java.applet.Applet;



public class MazeGameApplet extends Applet{

  // This method must not be static
  public MazeGameApplet () {
    
  }
  
  public void init(){
    Maze maze;
    MazeGameCreator creator = null;
    String theme = getParameter("theme");
    if(theme == null) theme = "";
    if ("Harry".equals(theme)) {
      creator = new maze.harry.HarryPotterMazeGameCreator();
    } else if ("Snow".equals(theme)) {
      creator = new maze.snow.SnowWhiteMazeGameCreator();
    }else {
      creator = new MazeGameCreator();
    }
    maze = creator.createMaze();
    maze.setCurrentRoom(8);
    maze.addToComponent(this);
  }



}

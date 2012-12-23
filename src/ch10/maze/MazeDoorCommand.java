
package maze;

public class MazeDoorCommand implements UndoableCommand { 

  public MazeDoorCommand(MapSite side) { 
    this.side = side; 
  }

  public void execute() {
    if ((side != null) && (side instanceof Door)) {
      Door d = (Door) side;
      d.setOpen(true);
    }
  }

  public void undo() {
    if ((side != null) && (side instanceof Door)) {
      Door d = (Door) side;
      d.setOpen(false);
    }
  }

  protected MapSite side; 

}

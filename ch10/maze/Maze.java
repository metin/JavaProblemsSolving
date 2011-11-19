package maze;

import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Maze implements Cloneable {

  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  public void addRoom(Room room) {
    if (room != null) {
      rooms.add(room);
    }
  }

  public Room findRoom(int roomNumber) {
    for (int i = 0; i < rooms.size(); i++) {
      Room room = (Room) rooms.get(i);
      if (roomNumber == room.getRoomNumber()) {
        return room;
      }
    }
    return null;
  }

  public void setCurrentRoom(int roomNumber) {
    Room room = findRoom(roomNumber);
    setCurrentRoom(room);
  }

  public void setCurrentRoom(Room room) {
    if (room != curRoom) {
      if (curRoom != null) {
        curRoom.setInRoom(false);
      }
      if (room != null) {
        room.setInRoom(true);
        curRoom = room;
      }
      if (view != null) {
        view.repaint();
      }
    }
  }

  public Room getCurrentRoom() {
    return curRoom;
  }

  public void move(Direction direction) {
    if (curRoom != null) {
      MapSite side = curRoom.getSide(direction);
      if (side != null) {
        side.enter(this);
      }
    }
  }

  public void draw(Graphics g) {
    if (dim == null) {
      calculateDimension();
    }
    int dx = MARGIN + -offset.x * ROOM_SIZE;
    int dy = MARGIN + -offset.y * ROOM_SIZE;

    if (debug) {
      System.out.println("Maze.Draw(): offset=" + offset.x + ", " + offset.y);
    }

    // draw rooms first
    for (int i = 0; i < rooms.size(); i++) {
      Room room = (Room) rooms.get(i);
      if (room != null) {
        Point location = room.getLocation();
        if (location != null) {

          if (debug) {
            System.out.println("Maze.Draw(): Room " + room.getRoomNumber()
                + " location: " + location.x + ", " + location.y);
          }

          room.draw(g, dx + location.x * ROOM_SIZE,
              dy + location.y * ROOM_SIZE, ROOM_SIZE, ROOM_SIZE);
        }
      }
    }
    // draw walls and doors
    for (int i = 0; i < rooms.size(); i++) {
      Room room = (Room) rooms.get(i);
      if (room != null) {
        Point location = room.getLocation();
        if (location != null) {
          for (Direction dir = Direction.first(); dir != null; dir = dir.next()) {
            MapSite side = room.getSide(dir);
            if (side != null) {
              if (dir == Direction.NORTH) {
                side.draw(g, dx + location.x * ROOM_SIZE - WALL_THICKNESS / 2,
                    dy + location.y * ROOM_SIZE - WALL_THICKNESS / 2, ROOM_SIZE
                        + WALL_THICKNESS, WALL_THICKNESS);
              } else if (dir == Direction.EAST) {
                side.draw(g, dx + location.x * ROOM_SIZE + ROOM_SIZE
                    - WALL_THICKNESS / 2, dy + location.y * ROOM_SIZE
                    - WALL_THICKNESS / 2, WALL_THICKNESS, ROOM_SIZE
                    + WALL_THICKNESS);
              } else if (dir == Direction.SOUTH) {
                side.draw(g, dx + location.x * ROOM_SIZE - WALL_THICKNESS / 2,
                    dy + location.y * ROOM_SIZE + ROOM_SIZE - WALL_THICKNESS
                        / 2, ROOM_SIZE + WALL_THICKNESS, WALL_THICKNESS);
              } else {
                side.draw(g, dx + location.x * ROOM_SIZE - WALL_THICKNESS / 2,
                    dy + location.y * ROOM_SIZE - WALL_THICKNESS / 2,
                    WALL_THICKNESS, ROOM_SIZE + WALL_THICKNESS);
              }
            }
          }
        }
      }
    }
  }

  public Dimension getDimension() {
    if (dim == null) {
      calculateDimension();
    }
    return dim;
  }

  protected void calculateDimension() {
    if (rooms.size() > 0) {
      int minX = 0, maxX = 0, minY = 0, maxY = 0;
      Room room = (Room) rooms.get(0);
      room.setLocation(new Point(0, 0));
      boolean changed = true;
      while (changed && !isAllRoomsSet()) {
        changed = false;
        for (int i = 0; i < rooms.size(); i++) {
          room = (Room) rooms.get(i);
          Point location = room.getLocation();
          if (location != null) {
            for (Direction dir = Direction.first(); dir != null; dir = dir
                .next()) {
              MapSite side = room.getSide(dir);
              if (side instanceof Door) {
                Door door = (Door) side;
                Room otherSide = door.otherSideFrom(room);
                if (otherSide != null && otherSide.getLocation() == null) {
                  if (dir == Direction.NORTH) {
                    otherSide
                        .setLocation(new Point(location.x, location.y - 1));
                    minY = Math.min(minY, location.y - 1);
                  } else if (dir == Direction.EAST) {
                    otherSide
                        .setLocation(new Point(location.x + 1, location.y));
                    maxX = Math.max(maxX, location.x + 1);
                  } else if (dir == Direction.SOUTH) {
                    otherSide
                        .setLocation(new Point(location.x, location.y + 1));
                    maxY = Math.max(maxY, location.y + 1);
                  } else {
                    otherSide
                        .setLocation(new Point(location.x - 1, location.y));
                    minX = Math.min(minX, location.x - 1);
                  }
                  changed = true;
                }
              }
            }
          }
        }
      }
      offset = new Point(minX, minY);
      dim = new Dimension(maxX - minX + 1, maxY - minY + 1);
    } else {
      offset = new Point(0, 0);
      dim = new Dimension(0, 0);
    }
  }

  protected boolean isAllRoomsSet() {
    for (int i = 0; i < rooms.size(); i++) {
      Room room = (Room) rooms.get(i);
      if (room.getLocation() == null) {
        return false;
      }
    }
    return true;
  }

  protected void setView(Component view) {
    this.view = view;
  }

  protected void doCommand(Command command) {
    if (command != null) {
      moves.push(command);
      command.execute();
    }
  }

  protected void undoCommand() {
    if (!moves.empty()) {
      Object top = moves.peek(); // looking at the top element without popping
                                 // it
      if (top instanceof UndoableCommand) {
        moves.pop();
        UndoableCommand undoableCommand = (UndoableCommand) top;
        undoableCommand.undo();
      }
    }
  }

  protected List rooms = new ArrayList();
  protected Dimension dim;
  protected Point offset;
  protected Room curRoom = null;
  protected Stack moves = new Stack();

  protected Component view;

  private static final int ROOM_SIZE = 40;
  private static final int WALL_THICKNESS = 6;
  private static final int MARGIN = 20;

  private static final boolean debug = true;

  protected void showFrame(String frameTitle) {
    JFrame frame;
    frame = new JFrame(frameTitle);
    frame.setContentPane(new Maze.MazePanel(this));
    frame.pack();
    Dimension frameDim = frame.getSize();
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    frame.setLocation(screenSize.width / 2 - frameDim.width / 2,
        screenSize.height / 2 - frameDim.height / 2);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }

  public static class MazePanel extends JPanel {

    public MazePanel(Maze maze) {
      this.maze = maze;
      if (maze != null) {
        maze.setView(this);
        Dimension d = maze.getDimension();
        if (d != null) {
          dim = new Dimension(d.width * ROOM_SIZE + 2 * MARGIN, d.height
              * ROOM_SIZE + 2 * MARGIN);
        }
        addKeyListener(new MazeKeyListener(maze));
      }
    }

    public void paint(Graphics g) {
      Dimension d = getSize();
      g.setColor(Color.white);
      g.fillRect(0, 0, d.width, d.height);
      if (maze != null) {
        maze.draw(g);
      }
      requestFocus();
    }

    // public boolean isFocusTraversable() { // pre 1.4
    public boolean isFocusable() { // 1.4
      return true;
    }

    public Dimension getPreferredSize() {
      return dim;
    }

    public Dimension getMinimumSize() {
      return dim;
    }

    private Maze maze;
    private Dimension dim;

  }

  static class MazeKeyListener extends KeyAdapter {

    MazeKeyListener(Maze maze) {
      this.maze = maze;
    }

    public void keyPressed(KeyEvent e) {
      System.out.println("Key pressed");
      Command command = null;
      int code = e.getKeyCode();
      switch (code) {
      case KeyEvent.VK_UP:
        System.out.println("Up key");
        command = new MazeMoveCommand(maze, Direction.NORTH);
        break;
      case KeyEvent.VK_DOWN:
        System.out.println("Down key");
        command = new MazeMoveCommand(maze, Direction.SOUTH);
        maze.move(Direction.SOUTH);
        break;
      case KeyEvent.VK_LEFT:
        System.out.println("Left key");
        command = new MazeMoveCommand(maze, Direction.WEST);
        break;
      case KeyEvent.VK_RIGHT:
        System.out.println("Right key");
        command = new MazeMoveCommand(maze, Direction.EAST);
        break;
      default:
        System.out.println("Key press ignored");
      }
      if (command != null) {
        maze.doCommand(command);
      }
    }

    Maze maze;
  }

}

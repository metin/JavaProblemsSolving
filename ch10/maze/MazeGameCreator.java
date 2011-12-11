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

    Door door12 = makeDoor(room1, room2);
    Door door23 = makeDoor(room2, room3);
    
    room1.setSide(Direction.NORTH, makeWall());
    room1.setSide(Direction.EAST, makeWall());
    room1.setSide(Direction.SOUTH, makeWall());
    room1.setSide(Direction.WEST, door12);

    room2.setSide(Direction.NORTH, makeWall());
    room2.setSide(Direction.EAST, door12);
    room2.setSide(Direction.SOUTH, makeWall());
    room2.setSide(Direction.WEST, door23);

    room3.setSide(Direction.NORTH, makeWall());
    room3.setSide(Direction.EAST, door23);
    room3.setSide(Direction.SOUTH, makeWall());
    room3.setSide(Direction.WEST, makeWall());

    room4.setSide(Direction.NORTH, makeWall());
    room4.setSide(Direction.EAST, makeWall());
    room4.setSide(Direction.SOUTH, makeWall());
    room4.setSide(Direction.WEST, makeWall());

    room5.setSide(Direction.NORTH, makeWall());
    room5.setSide(Direction.EAST, makeWall());
    room5.setSide(Direction.SOUTH, makeWall());
    room5.setSide(Direction.WEST, makeWall());
    
    Room room6 = makeRoom(6); 
    Room room7 = makeRoom(7); 
    Room room8 = makeRoom(8); 
    Room room9 = makeRoom(9); 
    Room room10 = makeRoom(10); 

    Door door67 = makeDoor(room6, room7);
    Door door78 = makeDoor(room7, room8);
    Door door89 = makeDoor(room8, room9);
    Door door910 = makeDoor(room9, room10);

    room6.setSide(Direction.NORTH, makeWall());
    room6.setSide(Direction.EAST, makeWall());
    room6.setSide(Direction.SOUTH, makeWall());
    room6.setSide(Direction.WEST, door67);

    room7.setSide(Direction.NORTH, makeWall());
    room7.setSide(Direction.EAST, door67);
    room7.setSide(Direction.SOUTH, makeWall());
    room7.setSide(Direction.WEST, door78);

    room8.setSide(Direction.NORTH, makeWall());
    room8.setSide(Direction.EAST, door78);
    room8.setSide(Direction.SOUTH, makeWall());
    room8.setSide(Direction.WEST, door89);
    
    room9.setSide(Direction.NORTH, makeWall());
    room9.setSide(Direction.EAST, door89);
    room9.setSide(Direction.SOUTH, makeWall());
    room9.setSide(Direction.WEST, door910);

    room10.setSide(Direction.NORTH, makeWall());
    room10.setSide(Direction.EAST, door910);
    room10.setSide(Direction.SOUTH, makeWall());
    room10.setSide(Direction.WEST, makeWall());

    Room room11 = makeRoom(11); 
    Room room12 = makeRoom(12); 
    Room room13 = makeRoom(13); 
    Room room14 = makeRoom(14); 
    Room room15 = makeRoom(15);
    
    Door door1112 = makeDoor(room11, room12);
    Door door1213 = makeDoor(room12, room13);
    Door door1314 = makeDoor(room13, room14);
    Door door1415 = makeDoor(room14, room15);

    room11.setSide(Direction.NORTH, makeWall());
    room11.setSide(Direction.EAST, makeWall());
    room11.setSide(Direction.SOUTH, makeWall());
    room11.setSide(Direction.WEST, door1112);

    room12.setSide(Direction.NORTH, makeWall());
    room12.setSide(Direction.EAST, door1112);
    room12.setSide(Direction.SOUTH, makeWall());
    room12.setSide(Direction.WEST, door1213);
    
    room13.setSide(Direction.NORTH, makeWall());
    room13.setSide(Direction.EAST, door1213);
    room13.setSide(Direction.SOUTH, makeWall());
    room13.setSide(Direction.WEST, door1314);

    room14.setSide(Direction.NORTH, makeWall());
    room14.setSide(Direction.EAST, door1314);
    room14.setSide(Direction.SOUTH, makeWall());
    room14.setSide(Direction.WEST, door1415);

    room15.setSide(Direction.NORTH, makeWall());
    room15.setSide(Direction.EAST, door1415);
    room15.setSide(Direction.SOUTH, makeWall());
    room15.setSide(Direction.WEST, makeWall());

    Room room16 = makeRoom(16);
    Room room17 = makeRoom(17); 
    Room room18 = makeRoom(18); 
    Room room19 = makeRoom(19); 
    Room room20 = makeRoom(20); 

    Door door1617 = makeDoor(room16, room17);
    Door door1718 = makeDoor(room17, room18);
    Door door1819 = makeDoor(room18, room19);
    Door door1920 = makeDoor(room19, room20);

    room16.setSide(Direction.NORTH, makeWall());
    room16.setSide(Direction.EAST, makeWall());
    room16.setSide(Direction.SOUTH, makeWall());
    room16.setSide(Direction.WEST, door1617);

    room17.setSide(Direction.NORTH, makeWall());
    room17.setSide(Direction.EAST, door1617);
    room17.setSide(Direction.SOUTH, makeWall());
    room17.setSide(Direction.WEST, door1718);

    room18.setSide(Direction.NORTH, makeWall());
    room18.setSide(Direction.EAST, door1718);
    room18.setSide(Direction.SOUTH, makeWall());
    room18.setSide(Direction.WEST, door1819);

    room19.setSide(Direction.NORTH, makeWall());
    room19.setSide(Direction.EAST, door1819);
    room19.setSide(Direction.SOUTH, makeWall());
    room19.setSide(Direction.WEST, door1920);

    room20.setSide(Direction.NORTH, makeWall());
    room20.setSide(Direction.EAST, door1920);
    room20.setSide(Direction.SOUTH, makeWall());
    room20.setSide(Direction.WEST, makeWall());

    door12.setOpen(true); 
    door23.setOpen(false); 
    door67.setOpen(false); 
    door78.setOpen(true); 
    door910.setOpen(true); 
    door910.setOpen(true); 
    door1112.setOpen(true); 
    door1314.setOpen(true); 
    door1415.setOpen(true); 
    door1617.setOpen(true); 

    Door door16 = makeDoor(room1, room6);
    room1.setSide(Direction.NORTH, door16);
    room6.setSide(Direction.SOUTH, door16);

    Door door712 = makeDoor(room7, room12);
    room7.setSide(Direction.NORTH, door712);
    room12.setSide(Direction.SOUTH, door712);

    Door door1318 = makeDoor(room13, room18);
    room13.setSide(Direction.NORTH, door1318);
    room18.setSide(Direction.SOUTH, door1318);

    Door door510 = makeDoor(room5, room10);
    room5.setSide(Direction.NORTH, door510);
    room10.setSide(Direction.SOUTH, door510);

    Door door49 = makeDoor(room4, room9);
    room4.setSide(Direction.NORTH, door49);
    room9.setSide(Direction.SOUTH, door49);
    
    door16.setOpen(true); 
    door712.setOpen(true); 
    door1318.setOpen(true); 

    maze.addRoom(room1);
    maze.addRoom(room2);
    maze.addRoom(room3);
    maze.addRoom(room4);
    maze.addRoom(room5);
    maze.addRoom(room6);
    maze.addRoom(room7);
    maze.addRoom(room8);
    maze.addRoom(room9);
    maze.addRoom(room10);
    maze.addRoom(room11);
    maze.addRoom(room12);
    maze.addRoom(room13);
    maze.addRoom(room14);
    maze.addRoom(room15);
    maze.addRoom(room16);
    maze.addRoom(room17);
    maze.addRoom(room18);
    maze.addRoom(room19);
    maze.addRoom(room20);
    
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


}
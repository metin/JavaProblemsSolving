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
public class MazeGameCreator20 {

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
    Room room10 = makeRoom(10);
    Room room11 = makeRoom(11);
    Room room12 = makeRoom(12);
    Room room13 = makeRoom(13);
    Room room14 = makeRoom(14);
    Room room15 = makeRoom(15);
    Room room16 = makeRoom(16);
    Room room17 = makeRoom(17);
    Room room18 = makeRoom(18);
    Room room19 = makeRoom(19);
    Room room20 = makeRoom(20);

//    Door door1 = makeDoor(room1, room2);
//    Door door2 = makeDoor(room2, room3);
//    Door door3 = makeDoor(room4, room5);
//    Door door4 = makeDoor(room5, room6);
//    Door door5 = makeDoor(room5, room8);
//    Door door6 = makeDoor(room6, room9);
//    Door door7 = makeDoor(room7, room8);
//    Door door8 = makeDoor(room1, room4);
    Door door1 = makeDoor(room1, room2);
    Door door2 = makeDoor(room2, room3);
    Door door3 = makeDoor(room3, room4);
    Door door4 = makeDoor(room4, room5);
    Door door5 = makeDoor(room5, room10);
    Door door6 = makeDoor(room6, room7);
    Door door7 = makeDoor(room7, room8);
    Door door8 = makeDoor(room8, room9);
    Door door9 = makeDoor(room9, room10);
    Door door10 = makeDoor(room6, room11);
    Door door11 = makeDoor(room11, room12);
    Door door12 = makeDoor(room12, room13);
    Door door13 = makeDoor(room13, room14);
    Door door14 = makeDoor(room14, room15);
    Door door15 = makeDoor(room15, room20);
    Door door16 = makeDoor(room16, room17);
    Door door17 = makeDoor(room17, room18);
    Door door18 = makeDoor(room18, room19);
    Door door19 = makeDoor(room19, room20);

    
    door1.setOpen(true);
    door2.setOpen(false);
    door3.setOpen(true);
    door4.setOpen(true);
    door5.setOpen(false);
    door6.setOpen(true);
    door7.setOpen(true);
    door8.setOpen(true);
    door9.setOpen(true);
    door10.setOpen(true);
    door11.setOpen(true);
    door12.setOpen(true);
    door13.setOpen(true);
    door14.setOpen(true);
    door15.setOpen(true);
    door16.setOpen(true);
    door17.setOpen(true);
    door18.setOpen(true);
    door19.setOpen(true);

    room1.setSide(Direction.NORTH, makeWall());
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
    room3.setSide(Direction.WEST, door3);

    room4.setSide(Direction.NORTH, makeWall());
    room4.setSide(Direction.EAST, door4);
    room4.setSide(Direction.SOUTH, makeWall());
    room4.setSide(Direction.WEST, door3);

    room5.setSide(Direction.NORTH, door5);
    room5.setSide(Direction.EAST, door4);
    room5.setSide(Direction.SOUTH, makeWall());
    room5.setSide(Direction.WEST, makeWall());

    room6.setSide(Direction.NORTH, door10);
    room6.setSide(Direction.EAST, makeWall());
    room6.setSide(Direction.SOUTH, makeWall());
    room6.setSide(Direction.WEST, door6);

    room7.setSide(Direction.NORTH, makeWall());
    room7.setSide(Direction.EAST, door7);
    room7.setSide(Direction.SOUTH, makeWall());
    room7.setSide(Direction.WEST, door6);

    room8.setSide(Direction.NORTH, makeWall());
    room8.setSide(Direction.EAST, door7);
    room8.setSide(Direction.SOUTH, makeWall());
    room8.setSide(Direction.WEST, door8);

    room9.setSide(Direction.NORTH, makeWall());
    room9.setSide(Direction.EAST, door8);
    room9.setSide(Direction.SOUTH, makeWall());
    room9.setSide(Direction.WEST, door9);

    room10.setSide(Direction.NORTH, makeWall());
    room10.setSide(Direction.EAST, door9);
    room10.setSide(Direction.SOUTH, door5);
    room10.setSide(Direction.WEST, makeWall());
    
    room11.setSide(Direction.NORTH, makeWall());
    room11.setSide(Direction.EAST, makeWall());
    room11.setSide(Direction.SOUTH, door10);
    room11.setSide(Direction.WEST, door11);
    
    room12.setSide(Direction.NORTH, makeWall());
    room12.setSide(Direction.EAST, door11);
    room12.setSide(Direction.SOUTH, makeWall());
    room12.setSide(Direction.WEST, door12);
    
    room13.setSide(Direction.NORTH, makeWall());
    room13.setSide(Direction.EAST, door12);
    room13.setSide(Direction.SOUTH, makeWall());
    room13.setSide(Direction.WEST, door13);
    
    room14.setSide(Direction.NORTH, makeWall());
    room14.setSide(Direction.EAST, door13);
    room14.setSide(Direction.SOUTH, makeWall());
    room14.setSide(Direction.WEST, door14);
    
    room15.setSide(Direction.NORTH, door15);
    room15.setSide(Direction.EAST, door14);
    room15.setSide(Direction.SOUTH, makeWall());
    room15.setSide(Direction.WEST, makeWall());
    
    room16.setSide(Direction.NORTH, makeWall());
    room16.setSide(Direction.EAST, makeWall());
    room16.setSide(Direction.SOUTH, makeWall());
    room16.setSide(Direction.WEST, door16);

    room17.setSide(Direction.NORTH, makeWall());
    room17.setSide(Direction.EAST, door16);
    room17.setSide(Direction.SOUTH, makeWall());
    room17.setSide(Direction.WEST, door17);

    room18.setSide(Direction.NORTH, makeWall());
    room18.setSide(Direction.EAST, door17);
    room18.setSide(Direction.SOUTH, makeWall());
    room18.setSide(Direction.WEST, door18);    
    
    room19.setSide(Direction.NORTH, makeWall());
    room19.setSide(Direction.EAST, door18);
    room19.setSide(Direction.SOUTH, makeWall());
    room19.setSide(Direction.WEST, door19);

    room20.setSide(Direction.NORTH, makeWall());
    room20.setSide(Direction.EAST, door19);
    room20.setSide(Direction.SOUTH, door15);
    room20.setSide(Direction.WEST, makeWall());
    
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

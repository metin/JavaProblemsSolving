
package maze; 

/*
 *  An enumeration type 
 */ 
public class Direction implements Comparable { 

  public static final Direction NORTH = new Direction("North");
  public static final Direction EAST  = new Direction("East");
  public static final Direction SOUTH = new Direction("South");
  public static final Direction WEST  = new Direction("West");

  public String toString() { 
    return name; 
  }

  public int getOrdinal() { 
    return ordinal;
  }

  public int compareTo(Object o) { 
    if (o instanceof Direction) { 
      return ordinal - ((Direction) o).getOrdinal();
    }
    return 0;
  }

  public static Direction first() { 
    return values[0]; 
  }

  public Direction next() { 
    if (ordinal < values.length - 1) { 
      return values[ordinal + 1]; 
    } else { 
      return null; 
    }
  }

  public Direction opposite() { 
    return values[(ordinal + 2) % 4]; 
  }

  /* 
  public static Direction first() { 
    return NORTH; 
  }

  public Direction next() { 
    if (this == NORTH) { 
      return EAST; 
    } else if (this == EAST) { 
      return SOUTH; 
    } else if (this == SOUTH) { 
      return WEST; 
    } else if (this == WEST) { 
      return null; 
    }
    return null;
  }

  public Direction opposite() { 
    if (this == NORTH) { 
      return SOUTH; 
    } else if (this == EAST) { 
      return WEST; 
    } else if (this == SOUTH) { 
      return NORTH; 
    } else if (this == WEST) { 
      return EAST; 
    }
    return null;
  }
  */ 
 
  private Direction(String name) { 
    this.name = name; 
  }
  
  private static int nextOrdinal = 0;  
  private final String name; 
  private final int ordinal = nextOrdinal++; 

  private static final Direction[] values = { NORTH, EAST, SOUTH, WEST }; 

} 

package maze; 

/*
 *  An enumeration type 
 */ 
public class Orientation { 

  public static final Orientation VERTICAL = new Orientation("Vertical");
  public static final Orientation HORIZONTAL = new Orientation("Horizontal");

  public String toString() { 
    return name; 
  }

  private Orientation(String name) { 
    this.name = name; 
  }
  
  private final String name; 

}

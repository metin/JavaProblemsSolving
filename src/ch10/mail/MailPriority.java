
package mail; 

/*
 *  An enumeration type 
 */ 
public class MailPriority implements Comparable { 

  public static final MailPriority LOW = new MailPriority("Low");
  public static final MailPriority MEDIUM = new MailPriority("Medium");
  public static final MailPriority HIGH = new MailPriority("High");
  public static final MailPriority VERY_HIGH = new MailPriority("Very high");

  public String toString() { 
    return name; 
  }

  public int getOrdinal() { 
    return ordinal;
  }

  public int compareTo(Object o) { 
    if (o instanceof MailPriority) { 
      return ordinal - ((MailPriority) o).getOrdinal();
    }
    return 0;
  }
 
  private MailPriority(String name) { 
    this.name = name; 
  }
  
  private static int nextOrdinal = 0;  
  private final String name; 
  private final int ordinal = nextOrdinal++; 

} 

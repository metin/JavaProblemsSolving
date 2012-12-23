
package mail; 

/*
 *  An enumeration type 
 */ 
public class MailStatus implements Comparable { 

  public static final MailStatus NEW = new MailStatus("New");
  public static final MailStatus READ = new MailStatus("Read");
  public static final MailStatus REPLIED = new MailStatus("Replied");
  public static final MailStatus FORWARDED = new MailStatus("Forwarded");

  public String toString() { 
    return name; 
  }

  public int getOrdinal() { 
    return ordinal;
  }

  public int compareTo(Object o) { 
    if (o instanceof MailStatus) { 
      return ordinal - ((MailStatus) o).getOrdinal();
    }
    return 0;
  }

  private MailStatus(String name) { 
    this.name = name; 
  }

  private static int nextOrdinal = 0;  
  private final String name; 
  private final int ordinal = nextOrdinal++; 

} 

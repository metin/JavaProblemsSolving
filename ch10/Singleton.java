
public class Singleton { 

  public static Singleton getInstance() { 
    if (theInstance == null) { 
      theInstance = new Singleton(); 
    }
    return theInstance; 
  }

  private Singleton() { 
    // user code
  }

  private static Singleton theInstance = null;  

  // user code

}

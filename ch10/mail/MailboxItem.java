
package mail;

public abstract class MailboxItem {   

  public String getName() {
    return name; 
  }
 
  public void setName(String name) { 
    this.name = name; 
  }

  public MailFolder getOwner() { 
    return owner; 
  }

  public void setOwner(MailFolder owner) { 
    this.owner = owner; 
  }

  public String toString() { 
    return name; 
  }

  public abstract int count(); 
  public abstract int countNewMail(); 

  protected MailboxItem(String name, MailFolder owner) {
    this.name = name;
    this.owner = owner; 
  }
  
  protected String name; 
  protected MailFolder owner; 

}

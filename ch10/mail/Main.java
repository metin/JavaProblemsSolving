
package mail; 

import java.util.*; 

public class Main { 

  static Mail[] se450 = {
    new Mail("Bill Gates", "Need extension for final project", 
	     getTime(2001, Calendar.NOVEMBER, 20, 23, 50),
	     MailPriority.VERY_HIGH, MailStatus.REPLIED),
    new Mail("Linus Torvalds", "Final project: Linux -- a small OS", 
	     getTime(2001, Calendar.NOVEMBER, 19, 23, 39),
	     MailPriority.VERY_HIGH, MailStatus.REPLIED),
    new Mail("John Valissides", "Question on Facade pattern", 
	     getTime(2001, Calendar.NOVEMBER, 18, 14, 56),
	     MailPriority.VERY_HIGH, MailStatus.REPLIED),
  }; 

  static Mail[] se452 = {
    new Mail("Marc Andreesen", "Problem with Netscape 7.02", 
	     getTime(2001, Calendar.OCTOBER, 9, 23, 30),
	     MailPriority.HIGH, MailStatus.NEW),
    new Mail("James Gosling", "HW4 submission, version 3.14159", 
	     getTime(2001, Calendar.OCTOBER, 2, 22, 40),
	     MailPriority.HIGH, MailStatus.NEW),
  }; 

  static Mail[] work = {
    new Mail("Ralph Johnson", "CFP: PLOP", 
	     getTime(2001, Calendar.NOVEMBER, 16, 20, 10),
	     MailPriority.HIGH, MailStatus.NEW),
    new Mail("Chris Galvin", "Your proposal", 
	     getTime(2001, Calendar.OCTOBER, 24, 4, 21),
	     MailPriority.HIGH, MailStatus.NEW),
    new Mail("David Boise", "Abstract of my talk", 
	     getTime(2001, Calendar.DECEMBER, 2, 9, 24),
	     MailPriority.HIGH, MailStatus.NEW),
  }; 

  static Mail[] news = {
    new Mail("WSJ.com", "Yahoo files for bankruptcy", 
	     getTime(2002, Calendar.JULY, 6, 7, 00),
	     MailPriority.HIGH, MailStatus.NEW),
    new Mail("CERT", "Security alert, Windows XXP-3", 
	     getTime(2003, Calendar.JANUARY, 1, 0, 00),
	     MailPriority.VERY_HIGH, MailStatus.NEW),
    new Mail("Scott McNealy", "Top 10 reasons for breaking up Microsoft", 
	     getTime(2001, Calendar.SEPTEMBER, 21, 9, 30),
	     MailPriority.MEDIUM, MailStatus.READ),
    new Mail("Gordon Moore", "10 myths of Moors's law", 
	     getTime(2001, Calendar.SEPTEMBER, 11, 6, 23),
	     MailPriority.MEDIUM, MailStatus.READ),
  };

  static Mail[] junks = {
    new Mail("William Gates, III", ".NET Framework", 
	     getTime(2001, Calendar.OCTOBER, 26, 22, 12),
	     MailPriority.HIGH, MailStatus.NEW),
    new Mail("AOL", "Free unlimited DSL with ...", 
	     getTime(2001, Calendar.OCTOBER, 2, 23, 59),
	     MailPriority.HIGH, MailStatus.NEW),
  }; 

  public static MailFolder buildInbox() { 
    MailFolder inboxFolder = new MailFolder("Inbox"); 
    MailFolder coursesFolder = new MailFolder("Courses");
    MailFolder se450Folder = new MailFolder("SE450");
    se450Folder.add(se450); 
    MailFolder se452Folder = new MailFolder("SE452");
    se452Folder.add(se452); 
    coursesFolder.add(se450Folder);     
    coursesFolder.add(se452Folder);     
    MailFolder workFolder = new MailFolder("Work");
    workFolder.add(work);
    MailFolder newsFolder = new MailFolder("News");
    newsFolder.add(news);
    MailFolder junksFolder = new MailFolder("Junk Mails");
    junksFolder.add(junks); 
    inboxFolder.add(coursesFolder); 
    inboxFolder.add(workFolder); 
    inboxFolder.add(newsFolder); 
    inboxFolder.add(junksFolder); 
    return inboxFolder; 
  }
  
  public static void main(String[] args) { 
    MailFolder inboxFolder = buildInbox();
    System.out.println("You " + inboxFolder.count() + " mails in Inbox");
    System.out.println("You " + inboxFolder.countNewMail() + " new mails in Inbox");
  }

  protected static Date getTime(int year, int month, int date, int hour, int minute) { 
    Calendar calendar = Calendar.getInstance();
    calendar.set(year, month, date, hour, minute, 0);
    return calendar.getTime();
  }              
              
}


package mail;

import java.util.*; 

public class Mail extends MailboxItem { 

  public Mail(String from,
	      String subject,
	      Date date,
	      MailPriority priority,
	      MailStatus status) { 
    this(from, subject, null, date, priority, status, null, null); 
  }

  public Mail(String from,
	      String subject,
	      MailFolder owner,
	      Date date,
	      MailPriority priority,
	      MailStatus status,
	      String message,
	      List attachments) {
    super(subject, owner); 
    this.from = from;
    this.date = date;
    this.priority = priority;
    this.status = status;
    this.message = message;
    this.attachments = attachments;
  }

  public String getSubject() {
    return name;
  }

  public void setSubject(String subject) {
    name = subject;
  }

  public Date getDate() { 
    return date;
  }

  public void setDate(Date date) { 
    this.date = date;
  }

  public MailPriority getPriority() {   
    return priority;  
  }

  public void setPriority(MailPriority priority) {   
    this.priority = priority;  
  }

  public MailStatus getStatus() {  
    return status; 
  }

  public void setStatus(MailStatus status) {  
    this.status = status; 
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public List getAttachments() {  
    return attachments; 
  }

  public void addAttachment(Object attachment) { 
    if (attachment != null) { 
      attachments.add(attachment);
    }
  }

  public int count() {
    return 1; 
  }

  public int countNewMail() {    
    return (status == MailStatus.NEW ? 1 : 0); 
  }

  public String toString() { 
    return "From: " + from + "; Subject: " + name + ";\n  Received: " + date + 
      "; Priority: " + priority + "; Status: " + status + ";"; 
  }

  protected Date date;
  protected MailPriority priority;  
  protected MailStatus status;  
  protected String from;
  protected String message;
  protected List attachments; 

}

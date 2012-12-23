
package mail.gui;

import java.util.Date;
import java.util.Comparator;
import adapter.TableEntry;
import mail.*;

public class MailEntry implements TableEntry { 

  // position of each column 
  public static final int PRIORITY_COLUMN = 0; 
  public static final int STATUS_COLUMN   = 1; 
  public static final int FROM_COLUMN     = 2; 
  public static final int RECEIVED_COLUMN = 3; 
  public static final int SUBJECT_COLUMN  = 4; 

  public static final String[] columnNames = { 
    "Priority",
    "Status",
    "From", 
    "Received",
    "Subject",
  };

  public static final String[] columnTips = { 
    "Priority",
    "Status",
    "From", 
    "Received",
    "Subject",
  };

  public static final Comparator[] comparators = {
    new MailEntryComparator(PRIORITY_COLUMN),
    new MailEntryComparator(STATUS_COLUMN),
    new MailEntryComparator(FROM_COLUMN),
    new MailEntryComparator(RECEIVED_COLUMN),
    new MailEntryComparator(SUBJECT_COLUMN),
  };

  public static final int[] columnWidths = { 50, 50, 100, 150, 200}; 

  public MailEntry(Mail mail) { 
    this.mail = mail; 
  }

  public int getColumnCount() {
    return columnNames.length;
  }

  public String getColumnName(int col) {
    if (col >= 0 && 
	col < columnNames.length) { 
      return columnNames[col]; 
    }
    return null;
  }

  public Object getColumnValue(int col) {
    if (mail != null && 
	col >= 0 && 
	col < columnNames.length) { 
      switch (col) { 
      case PRIORITY_COLUMN: return mail.getPriority();  
      case STATUS_COLUMN:   return mail.getStatus();  
      case FROM_COLUMN:     return mail.getFrom();  
      case RECEIVED_COLUMN: return mail.getDate();  
      case SUBJECT_COLUMN:  return mail.getSubject();  
      } 
    }
    return null;
  }

  public String getColumnTip(int col) {
    if (col >= 0 && 
	col < columnTips.length) { 
      return columnTips[col]; 
    }
    return null;
  }

  public Class getColumnClass(int col) {
    if (col == PRIORITY_COLUMN) { 
      return MailPriority.class;
    } else if (col == STATUS_COLUMN) {
      return MailStatus.class;
    } else if (col == RECEIVED_COLUMN) {
      return Date.class;
    } else { 
      return String.class;
    }
  }

  public Comparator getColumnComparator(int col) {
    if (col >= 0 && 
	col < comparators.length) { 
      return comparators[col]; 
    }
    return null;
  }

  public int getColumnWidth(int col) {
    if (col >= 0 && 
	col < columnWidths.length) { 
      return columnWidths[col]; 
    }
    return -1;
  }

  protected Mail mail; 

  static public class MailEntryComparator implements Comparator {

    public MailEntryComparator(int col) { 
      this.col = col; 
    }

    public int compare(Object o1, Object o2) {
      if (o1 != null && 
	  o2 != null &&
	  o1 instanceof MailEntry &&
	  o2 instanceof MailEntry) {
	MailEntry e1 = (MailEntry) o1; 
	MailEntry e2 = (MailEntry) o2; 
	if (col == PRIORITY_COLUMN) { 
	  return -((Comparable) e1.getColumnValue(col)).compareTo(e2.getColumnValue(col));
	} else if (col == STATUS_COLUMN) { 
	  return ((Comparable) e1.getColumnValue(col)).compareTo(e2.getColumnValue(col));
	} else if (col == RECEIVED_COLUMN) { 
	  return ((Date) e1.getColumnValue(col)).compareTo((Date) e2.getColumnValue(col));
	} else { 
	  return ((String) e1.getColumnValue(col)).compareTo((String) e2.getColumnValue(col)); 
	}	
      }
      return 0;
    }
   
    protected int col; 
  }
  

}

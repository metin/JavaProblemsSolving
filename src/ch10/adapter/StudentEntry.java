
package adapter;

import java.util.Comparator;

/**
 *  Adapter design pattern
 */
public class StudentEntry extends Student implements TableEntry { 

  // position of each column 
  public static final int ID_COLUMN             = 0; 
  public static final int FIRST_NAME_COLUMN     = 1; 
  public static final int LAST_NAME_COLUMN      = 2; 
  public static final int STREET_ADDRESS_COLUMN = 3; 
  public static final int STATE_COLUMN          = 4; 
  public static final int CITY_COLUMN           = 5; 
  public static final int COUNTRY_COLUMN        = 6; 
  public static final int POSTAL_CODE_COLUMN    = 7; 
  public static final int TELEPHONE_COLUMN      = 8; 
  public static final int GPA_COLUMN            = 9; 
  public static final int TOTAL_CREDITS_COLUMN  = 10; 

  public static final String[] columnNames = { 
    "ID", 
    "First Name", 
    "Last Name", 
    "Street Address", 
    "State", 
    "City", 
    "Country", 
    "Postal Code", 
    "Telephone", 
    "GPA", 
    "Total Credits", 
  };

  public static final String[] columnTips = { 
    "ID", 
    "First Name", 
    "Last Name", 
    "Street Address", 
    "State", 
    "City", 
    "Country", 
    "Postal Code", 
    "Telephone", 
    "GPA", 
    "Total Credits", 
  }; 

  public static final Comparator[] comparators = {
    new StudentEntryComparator(ID_COLUMN),
    new StudentEntryComparator(FIRST_NAME_COLUMN),
    new StudentEntryComparator(LAST_NAME_COLUMN),
    new StudentEntryComparator(STREET_ADDRESS_COLUMN),
    new StudentEntryComparator(STATE_COLUMN),
    new StudentEntryComparator(CITY_COLUMN),
    new StudentEntryComparator(COUNTRY_COLUMN),
    new StudentEntryComparator(POSTAL_CODE_COLUMN),
    new StudentEntryComparator(TELEPHONE_COLUMN),
    new StudentEntryComparator(GPA_COLUMN),
    new StudentEntryComparator(TOTAL_CREDITS_COLUMN),
  };

  public StudentEntry(String ID, 
		      String firstName, 
		      String lastName, 
		      String streetAddress, 
		      String state, 
		      String city, 
		      String country, 
		      String postalCode, 
		      String telephone, 
		      float GPA, 
		      int totalCredits) {
    super(ID, firstName, lastName, streetAddress, state, city, country, postalCode, 
	  telephone, GPA, totalCredits);
  }

  public StudentEntry(Student student) { 
    if (student != null) {
      this.ID = student.ID;
      this.firstName = student.firstName;
      this.lastName = student.lastName;
      this.streetAddress = student.streetAddress;
      this.state = student.state;
      this.city = student.city;
      this.country = student.country;
      this.postalCode = student.postalCode;
      this.telephone = student.telephone;
      this.GPA = student.GPA;
      this.totalCredits = student.totalCredits;
    }
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
    if (col >= 0 && 
	col < columnNames.length) { 
      switch (col) { 
      case ID_COLUMN:             return ID;  
      case FIRST_NAME_COLUMN:     return firstName;
      case LAST_NAME_COLUMN:      return lastName;
      case STREET_ADDRESS_COLUMN: return streetAddress;
      case STATE_COLUMN:          return state;
      case CITY_COLUMN:           return city;
      case COUNTRY_COLUMN:        return country;
      case POSTAL_CODE_COLUMN:    return postalCode;
      case TELEPHONE_COLUMN:      return telephone;
      case GPA_COLUMN:            return new Float(GPA);
      case TOTAL_CREDITS_COLUMN:  return new Integer(totalCredits);
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
    if (col == GPA_COLUMN) { 
      return Float.class;
    } else if (col == TOTAL_CREDITS_COLUMN) {
      return Integer.class;
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
    return -1;
  }

  static public class StudentEntryComparator implements Comparator {

    public StudentEntryComparator(int col) { 
      this.col = col; 
    }

    public int compare(Object o1, Object o2) {
      if (o1 != null && 
	  o2 != null &&
	  o1 instanceof StudentEntry &&
	  o2 instanceof StudentEntry) {
	StudentEntry e1 = (StudentEntry) o1; 
	StudentEntry e2 = (StudentEntry) o2; 
	if (col == GPA_COLUMN) { 
	  return (int) (e1.getGPA() * 1000 - e2.getGPA() * 1000);
	} else if (col == TOTAL_CREDITS_COLUMN) {
	  return (e1.getTotalCredits() - e2.getTotalCredits());
	} else { 
	  return ((String) e1.getColumnValue(col)).compareTo((String) e2.getColumnValue(col)); 
	}	
      }
      return 0;
    }
   
    protected int col; 
  }

}

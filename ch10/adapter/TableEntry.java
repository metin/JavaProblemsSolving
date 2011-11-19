
package adapter;

import java.util.Comparator;

public interface TableEntry { 

  public int getColumnCount();
  public String getColumnName(int col); 
  public Object getColumnValue(int col); 
  public String getColumnTip(int col);   
  public Class getColumnClass(int col);
  public Comparator getColumnComparator(int col); 
  public int getColumnWidth(int col);

}

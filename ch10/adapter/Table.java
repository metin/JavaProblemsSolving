package adapter;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.util.*;

public class Table extends JTable {

  public Table() {
    this(null);
  }

  public Table(List entries) {
    super(new ListTableModel(entries));
    model = (ListTableModel) dataModel;

    getTableHeader().addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        Point p = e.getPoint();
        JTableHeader header = (JTableHeader) e.getSource();
        int column = header.columnAtPoint(p);
        if (model.sort(column)) {
          clearSelection();
          updateUI();
        }
      }
    });

    setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    int columnCount = model.getColumnCount();
    for (int i = 0; i < columnCount; i++) {
      TableColumn column = getColumnModel().getColumn(i);
      DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
      String tip = model.getColumnTip(i);
      renderer.setToolTipText(tip);
      column.setCellRenderer(renderer);
      int w = model.getColumnWidth(i);
      if (w > 0) {
        column.setPreferredWidth(w);
      }
    }
  }

  protected ListTableModel model;

  static class ListTableModel extends AbstractTableModel {

    public ListTableModel(List entries) {
      if (entries != null && entries.size() > 0) {
        Object obj = entries.get(0);
        if (obj != null && obj instanceof TableEntry) {
          this.prototype = (TableEntry) obj;
          setData(entries);
        }
      }
    }

    public ListTableModel(TableEntry prototype) {
      this.prototype = prototype;
    }

    public int getColumnCount() {
      if (prototype != null) {
        return prototype.getColumnCount();
      }
      return 0;
    }

    public int getRowCount() {
      if (entries != null) {
        return entries.size();
      } else {
        return 0;
      }
    }

    public String getColumnName(int col) {
      if (prototype != null) {
        return prototype.getColumnName(col);
      }
      return null;
    }

    public Object getValueAt(int row, int col) {
      if (entries != null) {
        TableEntry entry = getTableEntry(row);
        if (entry != null) {
          return entry.getColumnValue(col);
        }
      }
      return null;
    }

    public Class getColumnClass(int col) {
      if (prototype != null) {
        return prototype.getColumnClass(col);
      }
      return String.class;
    }

    public String getColumnTip(int col) {
      if (prototype != null) {
        return prototype.getColumnTip(col);
      }
      return null;
    }

    public Comparator getColumnComparator(int col) {
      if (prototype != null) {
        return prototype.getColumnComparator(col);
      }
      return null;
    }

    public int getColumnWidth(int col) {
      if (prototype != null) {
        return prototype.getColumnWidth(col);
      }
      return -1;
    }

    public boolean isCellEditable(int row, int col) {
      return false;
    }

    public void setValueAt(Object value, int row, int col) {
    }

    public void clearData() {
      entries = null;
    }

    public void setData(List entries) {
      this.entries = entries;
    }

    public boolean sort(int col) {
      if (entries != null && col >= 0 && col < getColumnCount()) {
        Comparator c = getColumnComparator(col);
        if (c != null) {
          Collections.sort(entries, c);
          return true;
        }
      }
      return false;
    }

    public TableEntry getTableEntry(int i) {
      if (entries != null && i >= 0 && i < entries.size()) {
        return (TableEntry) entries.get(i);
      }
      return null;
    }

    protected TableEntry prototype;
    protected List entries; // elements are instance of TableEntry

  }

}

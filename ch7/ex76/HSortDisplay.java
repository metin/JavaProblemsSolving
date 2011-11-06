package ex76;

import java.awt.*; 

public class HSortDisplay implements SortDisplay {
  public int getArraySize(Dimension d) {
    return d.height / 2; 
  }

  public void display(int a[], Graphics g, Dimension d) {
    double f = d.width / (double) a.length;
    g.setColor(Color.white);
    g.fillRect(0, 0, d.width, d.height); 
    int y = d.height - 1;    
    g.setColor(Color.black);
    y = d.height - 1;
    for (int i = a.length; --i >= 0; y -= 2)
      g.drawLine(0, y, (int)(a[i] * f), y);
  }
}


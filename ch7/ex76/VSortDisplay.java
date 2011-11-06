package ex76;

import java.awt.*;

public class VSortDisplay implements SortDisplay {

  public int getArraySize(Dimension d) {
    return d.width / 2; 
  }

  public void display(int a[], Graphics g, Dimension d) {
    g.setColor(Color.white);
    g.fillRect(0, 0, d.width, d.height);
    int x = d.width - 1;
    double f = d.height / (double) a.length;
    g.setColor(Color.black);
    for (int i = a.length; --i >= 0; x -= 2)
      g.drawLine(x, 0, x, (int)(a[i] * f));
  }
}


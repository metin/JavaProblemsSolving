package ex76;

import java.awt.*;

public class BSortDisplay implements SortDisplay {

  public int getArraySize(Dimension d) {
    return d.width / 2; 
  }

  public void display(int a[], Graphics g, Dimension d) {
    g.setColor(Color.white);
    g.fillRect(0, 0, d.width, d.height);
    double f = d.height / (double) a.length;
    int x = d.width - 1;
    g.setColor(Color.black);
    for (int i = a.length; --i >= 0; x -= 2)
      g.drawLine(x, d.height, x, d.height - (int)(a[i] * f));

  }
}


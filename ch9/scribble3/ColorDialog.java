

package scribble3; 

import java.applet.Applet;
import java.awt.*; 
import java.awt.event.*; 
import javax.swing.*; 

public class ColorDialog extends JDialog implements ActionListener { 

  public ColorDialog(Applet owner, String title) { 
    this(owner, title, Color.black); 
  }

  public ColorDialog(Applet owner, String title, Color color) { 
    //super(owner, title, true); 
    this.color = color; 

    getContentPane().setLayout(new BorderLayout());

    JPanel topPanel = new JPanel(); 
    topPanel.setLayout(new BorderLayout()); 
    colorPanel = new ColorPanel(20, 20, 8, 8);
    topPanel.add(colorPanel, BorderLayout.CENTER);
    moreColorButton = new JButton("More colors"); 
    moreColorButton.addActionListener(this); 
    topPanel.add(moreColorButton, BorderLayout.SOUTH);
    getContentPane().add(topPanel, BorderLayout.CENTER); 

    JPanel bottomPanel = new JPanel();
    bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT)); 
    okButton = new JButton("Ok"); 
    okButton.addActionListener(this); 
    bottomPanel.add(okButton);
    cancelButton = new JButton("Cancel"); 
    cancelButton.addActionListener(this); 
    bottomPanel.add(cancelButton);
    getContentPane().add(bottomPanel, BorderLayout.SOUTH);

    pack();
  }

  public Color showDialog() { 
    result = null;
    colorPanel.setColor(color); 
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension dialogSize = getSize(); 
    setLocation(screenSize.width / 2 - dialogSize.width / 2,
		screenSize.height / 2 - dialogSize.height / 2);
    show();
    if (result != null) { 
      color = result; 
    }
    return result; 
  }

  public void actionPerformed(ActionEvent e) { 
    Object source = e.getSource();
    if (source == okButton) { 
      result = colorPanel.getColor(); 
    } else if (source == moreColorButton) { 
      Color selectedColor = chooser.showDialog(ColorDialog.this, 
					       "Choose color", 
					       color); 
      if (selectedColor != null) { 
		colorPanel.setColor(selectedColor); 
		colorPanel.repaint();
      }
      return;
    }
    setVisible(false); 
  }

  protected JButton okButton; 
  protected JButton cancelButton; 
  protected JButton moreColorButton; 
  protected ColorPanel colorPanel; 
  protected JColorChooser chooser = new JColorChooser(); 
  protected Color color = null; 

  protected Color result = null; 

  class ColorPanel extends JPanel { 

    ColorPanel(int cellWidth, int cellHeight, int xpad, int ypad) { 
      if (cellWidth < 5) {
	cellWidth = 5; 
      }
      if (cellHeight < 5) {
	cellHeight = 5; 
      }
      if (xpad < 2) { 
	xpad = 2; 
      }
      if (ypad < 2) { 
	ypad = 2; 
      }
      this.cellWidth = cellWidth;
      this.cellHeight = cellHeight;
      this.xpad = xpad;
      this.ypad = ypad;
      rowCount = colorGrid.length;
      columnCount = colorGrid[0].length;
      dimension = new Dimension((cellWidth + xpad) * columnCount + xpad,
				(cellHeight + ypad) * (rowCount + 1) + ypad); 
      addMouseListener(new MouseAdapter() {
	  public void mousePressed(MouseEvent event) { 
	    Point p = event.getPoint(); 
	    int i = (p.y / (ColorPanel.this.cellHeight + ColorPanel.this.ypad)); 
	    int j = (p.x / (ColorPanel.this.cellWidth + ColorPanel.this.xpad)); 
	    if (i < rowCount &&
		j < columnCount) { 
	      color = colorGrid[i][j]; 
	      repaint();
	    }
	  }
	});
    } 

    public void setColor(Color color) { 
      this.color = color; 
    }

    public Color getColor() { 
      return color; 
    }

    public Dimension getMinimumSize() { 
      return dimension; 
    }

    public Dimension getPreferredSize() { 
      return dimension; 
    }

    public void paint(Graphics g) { 
      Dimension dim = getSize(); 
      g.setColor(Color.lightGray);
      g.fillRect(0, 0, dim.width, dim.height); 
      int x, y; 
      for (int i = 0; i < rowCount; i++) { 
	for (int j = 0; j < columnCount; j++) { 
	  x = (cellWidth + xpad) * j + xpad;
	  y = (cellHeight + ypad) * i + ypad; 
	  g.setColor(colorGrid[i][j]); 
	  g.fillRect(x, y, cellWidth, cellHeight);
	  g.setColor(Color.black); 
	  g.drawRect(x, y, cellWidth, cellHeight); 
	}
      }
      x = xpad; 
      y = (cellHeight + ypad) * rowCount + ypad; 
      int width = (cellWidth + xpad) * columnCount - xpad;
      g.setColor(color); 
      g.fillRect(x, y, width, cellHeight);
      g.setColor(Color.black); 
      g.drawRect(x, y, width, cellHeight);
    }

    protected Color color; 

    protected int cellWidth;
    protected int cellHeight;
    protected int rowCount;
    protected int columnCount;
    protected int xpad;
    protected int ypad;
    protected Dimension dimension; 

    protected Color[][] colorGrid = { 
      { Color.white, Color.lightGray, Color.darkGray, Color.black},
      { Color.gray, Color.blue, Color.cyan,  Color.green }, 
      { Color.yellow, Color.orange, Color.pink, Color.red },
      { Color.magenta, new Color(230, 230, 250), new Color(0, 0, 128), new Color(64, 224, 208) } }; 
    
  }

}

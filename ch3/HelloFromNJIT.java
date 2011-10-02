import javax.swing.*;
import java.awt.*;
import java.applet.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class HelloFromNJIT extends Applet implements ActionListener
{
    Button btn;
    private LayoutManager Layout;
    private Image im;
    private String[] availavleImages = new String[]{"njit_logo.jpg", 
                        "other.jpg", "java.jpg", "flames.png", "linux.jpg"};
    private String currentImage;
    public void init() {
        Layout = new GridBagLayout();
        setLayout (Layout);
        GridBagConstraints c = new GridBagConstraints();
        btn = new Button("Change");
        c.fill = GridBagConstraints.VERTICAL;
        c.gridx = 1;
        c.gridy = 0;
        add(btn, c);
        btn.addActionListener(this);
        randomizeImage();
    }

    public void paint(Graphics g)
    {
        Dimension d = getSize();
        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);
        g.setFont(new Font("Sans-serif", Font.BOLD, 24));
        g.setColor(new Color(255, 215, 0));
        g.drawString("Current Image:"+currentImage, 40, 25);
        g.drawImage(im, 20, 60, 200, 200, this);
    }
    
    public void actionPerformed(ActionEvent evt)
    {
        randomizeImage();
        repaint();
    }
    
    /* By clicking the button, a random image from predefined set is shown*/
    private void randomizeImage()
    {
        Random rnd = new Random();
        int index = rnd.nextInt(availavleImages.length);
        im = getImage(getCodeBase(), availavleImages[index]);
        currentImage =  availavleImages[index];
    }
}

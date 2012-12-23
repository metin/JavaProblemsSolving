package spaceinvaders;

import javax.swing.JFrame;

public class SpaceInvaders extends JFrame implements Commons {

    public SpaceInvaders()
    {
        //add(new Board());
        setTitle("Space Invaders");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(BOARD_WIDTH, BOARD_HEIGTH);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    public static void main(String[] args) {
        new SpaceInvaders();
    }
}
package net.maxpilipovic.mygame;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    //SCREEN SETTINGS
    final int originalTileSize = 16; //16x16 tile
    final int scale = 3;

    final int tileSize = originalTileSize * scale; //48x48 tile
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol; //48 * 16 = 768 pixels
    final int screenLength = tileSize * maxScreenRow;//48 x 12 = 576 pixels

    //Crating a Thread (Game Clock)
    Thread gameThread;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenLength));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); //If set to true, all drawing from this component will be done in off-screen painting buffer //Improves game rendering



    }

    //Starts the gameThread thread and creates a new object passing 'this', which is this class (gameThread)
    //EP1
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

    }
}

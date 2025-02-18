package net.maxpilipovic.mygame;

import net.maxpilipovic.entity.Player;
import net.maxpilipovic.tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    //SCREEN SETTINGS
    final int originalTileSize = 16; //16x16 tile
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; //48x48 tile
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; //48 * 16 = 768 pixels
    public final int screenLength = tileSize * maxScreenRow;//48 x 12 = 576 pixels

    //WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldLength = tileSize * maxWorldRow;
    //FPS
    int FPS = 60;

    //Instantiating
    //Creating a tile
    TileManager tileM = new TileManager(this);
    //Creating KeyHandler
    keyHandler keyH = new keyHandler();
    //Crating a Thread (Game Clock)
    Thread gameThread;
    //Creating Collision Detection
    public CollisionChecker cChecker = new CollisionChecker(this);
    //Creating Player
    public Player player = new Player(this, keyH);


    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenLength));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); //If set to true, all drawing from this component will be done in off-screen painting buffer //Improves game rendering
        this.addKeyListener(keyH);
        this.setFocusable(true); //GamePanel can be focused to "accept" receive key input.
    }

    //Starts the gameThread thread and creates a new object passing 'this', which is this class (gameThread)
    //EP1
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 100000000 / FPS; //1mil is 1 second in nanoseconds
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        long timer = 0;
        int drawCount = 0;

        //While the gameThread exists...
        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                //1. UPDATE information such as character position
                update();
                //2. DRAW the screen with the updated information
                repaint();
                delta--;
                drawCount++;
            }

            if (timer >= 100000000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update() {
        player.update();
    }

    public void paintComponent(Graphics g) {

        //Parent class of this class. Meaning the constructor of JPanel (The parent)
        super.paintComponent(g);

        //Graphics2D extends the Graphics class to provide more sophisticated control
        Graphics2D g2 = (Graphics2D) g;

        //Draw tiles before player, or you cant see player.
        tileM.draw(g2);
        player.draw(g2);

        //Saves memory. Releases unused resources.
        g2.dispose();
    }
}

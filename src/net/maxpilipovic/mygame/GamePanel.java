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

    //FPS
    int FPS = 60;

    //Creating KeyHandler
    keyHandler keyH = new keyHandler();
    //Crating a Thread (Game Clock)
    Thread gameThread;

    //Set player's default position
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

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
        double nextDrawTime = System.nanoTime() + drawInterval;
        long lasTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        //While the gameThread exists...
        while (gameThread != null) {
            currentTime = System.nanoTime();
            //1. UPDATE information such as character position
            update();
            //2. DRAW the screen with the updated information
            repaint();

            //SLEEP - GAME LOOP METHOD
            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 100000; //Converts to milliseconds

                if (remainingTime < 0) {
                    remainingTime = 0;
                    drawCount++;
                }
                Thread.sleep((long) remainingTime);
                long lastTime = currentTime;
                timer += (currentTime - lastTime);

                if (timer >= 1000000000) {
                    System.out.println("FPS:" + drawCount);
                    drawCount = 0;
                    timer = 0;
                }
                nextDrawTime += drawInterval;

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void update() {
        if (keyH.upPressed == true) {
            playerY -= playerSpeed;
        }
        else if (keyH.downPressed == true) {
            playerY += playerSpeed;
        }
        else if (keyH.leftPressed == true) {
            playerX -= playerSpeed;
        }
        else if (keyH.rightPressed == true) {
            playerX += playerSpeed;
        }

    }

    public void paintComponent(Graphics g) {

        //Parent class of this class. Meaning the constructor of JPanel (The parent)
        super.paintComponent(g);

        //Graphics2D extends the Graphics class to provide more sophisticated control
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.white);
        g2.fillRect(playerX, playerY, tileSize, tileSize);

        //Saves memory. Releases unused resources.
        g2.dispose();
    }
}

package net.maxpilipovic.mygame;

import net.maxpilipovic.entity.Player;
import net.maxpilipovic.object.SuperObject;
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

    //FPS
    int FPS = 60;

    //Instantiating
    //Creating a tile
    TileManager tileM = new TileManager(this);
    //Creating KeyHandler
    keyHandler keyH = new keyHandler();
    //Create Sound
    Sound music = new Sound();
    Sound se = new Sound();
    //Creating Collision Detection
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    //Creating UI
    public UI ui = new UI(this);
    //Crating a Thread (Game Clock)
    Thread gameThread;
    //Creating Player
    public Player player = new Player(this, keyH);
    //Create Objects (Size of 10 during the game)
    public SuperObject obj[] = new SuperObject[10];


    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenLength));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); //If set to true, all drawing from this component will be done in off-screen painting buffer //Improves game rendering
        this.addKeyListener(keyH);
        this.setFocusable(true); //GamePanel can be focused to "accept" receive key input.
    }

    public void setupGame() {
        aSetter.setObject();
        playMusic(0);
    }
    //Starts the gameThread thread and creates a new object passing 'this', which is this class (gameThread)
    //EP1
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS; //1mil is 1 second in nanoseconds
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

            if (timer >= 1000000000) {
                //System.out.println("FPS: " + drawCount);
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

        //DEBUG
        long drawStart = 0;
        if (keyH.checkDrawTime == true) {
            drawStart = System.nanoTime();
        }

        //Draw tiles before player, or you cant see player.
        tileM.draw(g2);

        //Draw Object
        for (int i = 0; i < obj.length; i++) {
            if (obj[i] != null) {
                obj[i].draw(g2, this);
            }
        }
        //Draw Player
        player.draw(g2);

        //Draw UI
        ui.draw(g2);

        //DEBUG
        if (keyH.checkDrawTime == true) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;
            g2.setColor(Color.white);
            g2.drawString("Draw Time: " + passed, 10, 400);
            System.out.println("Draw Time: " + passed);
        }

        //Saves memory. Releases unused resources.
        g2.dispose();
    }

    public void playMusic(int i) {
        //Sound effects to loop, e.g game music.
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic() {
        music.stop();
    }

    public void playSE(int i) {
        //Play sound effect method for powerups they dont need to loop
        se.setFile(i);
        se.play();
    }
}

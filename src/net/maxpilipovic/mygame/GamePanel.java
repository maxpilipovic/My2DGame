package net.maxpilipovic.mygame;

import net.maxpilipovic.entity.Entity;
import net.maxpilipovic.entity.Player;
import net.maxpilipovic.tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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

    //---Instantiating---

    //SYSTEM
    //Creating a tile
    TileManager tileM = new TileManager(this);
    //Creating KeyHandler
    public keyHandler keyH = new keyHandler(this);
    //Create Sound
    Sound music = new Sound();
    Sound se = new Sound();
    //Creating Collision Detection
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    //Creating UI
    public UI ui = new UI(this);
    public EventHandler eHandler = new EventHandler(this);
    //Crating a Thread (Game Clock)
    Thread gameThread;

    //ENTITY AND OBJECT
    //Creating Player
    public Player player = new Player(this, keyH);
    //Create Objects (Size of 10 during the game)
    public Entity obj[] = new Entity[20];
    public Entity npc[] = new Entity[10];
    public Entity monster[] = new Entity[20];
    public ArrayList<Entity> projectileList = new ArrayList<>();
    ArrayList<Entity> entityList = new ArrayList<>();


    //GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int characterState = 4;



    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenLength));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); //If set to true, all drawing from this component will be done in off-screen painting buffer //Improves game rendering
        this.addKeyListener(keyH);
        this.setFocusable(true); //GamePanel can be focused to "accept" receive key input.
    }

    public void setupGame() {
        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMonster();
        //playMusic(0);
        gameState = titleState;
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
        if (gameState == playState) {
            //PLAYER
            player.update();

            //NPC
            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) {
                    npc[i].update();
                }
            }

            for (int i = 0; i < monster.length; i++) {
                if (monster[i] != null) {
                    if (monster[i].alive == true && monster[i].dying == false) {
                        monster[i].update();
                    }
                    if (monster[i].alive == false) {
                        monster[i].checkDrop();
                        monster[i] = null;
                    }
                }
            }
            for (int i = 0; i < projectileList.size(); i++) {
                if (projectileList.get(i) != null) {
                    if (projectileList.get(i).alive == true) {
                        projectileList.get(i).update();
                    }
                    if (projectileList.get(i).alive == false) {
                        projectileList.remove(i);
                    }
                }
            }
        }

        if (gameState == pauseState) {
            //Nothing for now
        }

    }

    public void paintComponent(Graphics g) {
        //Parent class of this class. Meaning the constructor of JPanel (The parent)
        super.paintComponent(g);
        //Graphics2D extends the Graphics class to provide more sophisticated control
        Graphics2D g2 = (Graphics2D) g;

        //DEBUG
        long drawStart = 0;
        if (keyH.showDebug == true) {
            drawStart = System.nanoTime();
        }
        //TITLE SCREEN
        if (gameState == titleState) {
            ui.draw(g2);

        } else {
            //OTHERS
            //Draw tiles before player, or you cant see player.
            tileM.draw(g2);

            //Add all entities into this list
            entityList.add(player);

            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) {
                    entityList.add(npc[i]);
                }
            }

            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null) {
                    entityList.add((obj[i]));
                }
            }

            for (int i = 0; i < monster.length; i++) {
                if (monster[i] != null) {
                    entityList.add((monster[i]));
                }
            }
            for (int i = 0; i < projectileList.size(); i++) {
                if (projectileList.get(i) != null) {
                    entityList.add(projectileList.get(i));
                }
            }

            //Sort
            Collections.sort(entityList, new Comparator<Entity>() {

                @Override
                public int compare(Entity e1, Entity e2) {

                    int result = Integer.compare(e1.worldY, e2.worldY);

                    return result;
                }
            });

            //DRAW ENTITIES
            for (int i = 0; i < entityList.size(); i++) {
                entityList.get(i).draw(g2);
            }
            //EMPTY ENTITY LIST (OR other the entityList gets larger every loop)
            entityList.clear();

            //Draw UI
            ui.draw(g2);
        }

        //DEBUG
        if (keyH.showDebug == true) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;

            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.setColor(Color.white);
            int x = 10;
            int y = 400;

            int lineHeight = 20;

            g2.drawString("World X" + player.worldX, x, y); y+= lineHeight;
            g2.drawString("World Y" + player.worldY, x, y); y+= lineHeight;
            g2.drawString("Col" + (player.worldX + player.solidArea.x) / tileSize, x, y); y+= lineHeight;
            g2.drawString("Row" + (player.worldY + player.solidArea.y) / tileSize, x, y); y+= lineHeight;

            g2.drawString("Draw Time: " + passed, x, y);
            //System.out.println("Draw Time: " + passed);
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

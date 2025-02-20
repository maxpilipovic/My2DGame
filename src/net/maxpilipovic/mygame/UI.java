package net.maxpilipovic.mygame;

import net.maxpilipovic.object.OBJ_Key;

import java.awt.Font;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

public class UI {

    GamePanel gp;
    Font arial_40, arial_80B;
    BufferedImage keyImage;

    //For picking up items and displaying message
    public boolean messageOn = false;
    public String message;
    int messageCounter = 0;

    //For play time!
    public double playTime;

    //Format play time
    DecimalFormat dFormat = new DecimalFormat("#0.00");

    //Game Finished
    public boolean gameFinished = false;


    public UI(GamePanel gp) {
        this.gp = gp;

        arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_80B = new Font("Arial", Font.BOLD, 80);
        OBJ_Key key = new OBJ_Key(gp);
        keyImage = key.image;
    }

    public void showMessage(String text) {
        message = text;
        messageOn = true;
    }

    public void draw(Graphics2D g2) {
        if (gameFinished == true) {

            String text;
            int textLength;
            int x;
            int y;

            g2.setFont(arial_40);
            g2.setColor(Color.white);

            text = "You found the treasure!";
            textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
            x = gp.screenWidth / 2 - textLength / 2;
            y = gp.screenLength / 2 - (gp.tileSize * 3);

            //Display text
            g2.drawString(text, x, y);

            g2.setFont(arial_40);
            g2.setColor(Color.white);

            text = "You time is: " + dFormat.format(playTime) + "!";
            textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
            x = gp.screenWidth / 2 - textLength / 2;
            y = gp.screenLength / 2 + (gp.tileSize * 4);

            //Display text
            g2.drawString(text, x, y);

            //Display "Congratulations Text"
            g2.setFont(arial_80B);
            g2.setColor(Color.yellow);
            text = "Congratulations";
            textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
            x = gp.screenWidth / 2 - textLength / 2;
            y = gp.screenLength / 2 + (gp.tileSize * 2);


            //Display text
            g2.drawString(text, x, y);

            //Stop Game
            gp.gameThread = null;



        } else {
            g2.setFont(arial_40);
            g2.setColor(Color.white);
            g2.drawImage(keyImage, gp.tileSize/2, gp.tileSize/2, gp.tileSize, gp.tileSize, null);
            g2.drawString("x " + gp.player.hasKey, 74, 65);

            //Play Time
            playTime += (double)1/60;
            g2.drawString("Time: " + dFormat.format(playTime), gp.tileSize * 11, 65);

            //Message
            if (messageOn == true) {
                g2.setFont(g2.getFont().deriveFont(30F));
                g2.drawString(message, gp.tileSize/2, gp.tileSize * 5);

                messageCounter++;

                if (messageCounter > 120) {
                    messageCounter = 0;
                    messageOn = false;
                }
            }

        }

    }
}

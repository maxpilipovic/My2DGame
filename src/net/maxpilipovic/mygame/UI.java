package net.maxpilipovic.mygame;

import net.maxpilipovic.object.OBJ_Key;

import java.awt.Font;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

public class UI {

    GamePanel gp;
    Graphics2D g2;
    Font arial_40, arial_80B;
    //BufferedImage keyImage; //Key image is treasure hunt game

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
        //OBJ_Key key = new OBJ_Key(gp); //Object key
        //keyImage = key.image;
    }

    public void showMessage(String text) {
        message = text;
        messageOn = true;
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;

        g2.setFont(arial_40);
        g2.setColor(Color.white);

        if (gp.gameState == gp.playState) {
            //Do playstate stuff

        }

        if (gp.gameState == gp.pauseState) {
            drawPauseScreen();

        }
    }

    public void drawPauseScreen() {
        g2.getFont().deriveFont(Font.BOLD, 80F);
        String text = "PAUSED";
        int x = getXforCenterText(text);
        int y = gp.screenLength / 2;

        g2.drawString(text, x, y);
    }

    public int getXforCenterText(String text) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth / 2 - length / 2;
        return x;
    }
}

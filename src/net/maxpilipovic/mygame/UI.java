package net.maxpilipovic.mygame;

import net.maxpilipovic.entity.Entity;
import net.maxpilipovic.object.OBJ_Heart;
import net.maxpilipovic.object.OBJ_ManaCrystal;

import java.awt.Font;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class UI {

    GamePanel gp;
    Graphics2D g2;
    Font maruMonica, purisaB;

    BufferedImage heart_full, heart_half, heart_blank, crystal_full, crystal_blank;

    //For picking up items and displaying message
    public boolean messageOn = false;
    //public String message = "";
    //int messageCounter = 0;
    ArrayList<String> message = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();

    //Game Finished
    public boolean gameFinished = false;

    public String currentDialogue = "";

    //Menu Selection
    public int commandNum = 0;

    public int slotCol = 0;
    public int slotRow = 0;

    public UI(GamePanel gp) {
        this.gp = gp;

        //Get font
        try {
            InputStream is = getClass().getResourceAsStream("/font/x12y16pxMaruMonica.ttf");
            maruMonica = Font.createFont(Font.TRUETYPE_FONT, is);

            is = getClass().getResourceAsStream("/font/Purisa Bold.ttf");
            purisaB = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //CREATE HUD OBJECT
        Entity heart = new OBJ_Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;
        Entity crystal = new OBJ_ManaCrystal(gp);
        crystal_full = crystal.image;
        crystal_blank = crystal.image2;

    }

    public void addMessage(String text) {
        message.add(text);
        messageCounter.add(0);
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;

        g2.setFont(maruMonica);
        g2.setColor(Color.white);

        //Title State
        if(gp.gameState == gp.titleState) {
            drawTitleScreen();
        }
        //Play State
        if (gp.gameState == gp.playState) {
            //Do playstate stuff
            drawPlayerLife();
            drawManaCrystal();
            drawMessage();

        }

        //Pause State
        if (gp.gameState == gp.pauseState) {

            //System.out.println("Pause state activated");
            drawPauseScreen();
            drawPlayerLife();
            drawManaCrystal();

        }

        //Dialogue State
        if (gp.gameState == gp.dialogueState) {

            drawPlayerLife();
            drawManaCrystal();
            drawDialogueScreen();
        }
        //CHARACTER STATE
        if (gp.gameState == gp.characterState) {
            drawCharacterScreen();
            drawInventory();
        }

    }

    public void drawPlayerLife() {
        int x = gp.tileSize / 2;
        int y = gp.tileSize / 2;
        int i = 0;

        //DRAW 3 BLANK HEARTS
        while (i < gp.player.maxLife / 2) {
            g2.drawImage(heart_blank, x, y, null);
            i++;
            x += gp.tileSize;
        }

        //RESET
        x = gp.tileSize/2;
        y = gp.tileSize/2;
        i = 0;

        //DRAW CURRENT LIFE
        while (i < gp.player.life) {
            g2.drawImage(heart_half, x, y, null);
            i++;

            if (i < gp.player.life) {
                g2.drawImage(heart_full, x, y, null);
            }
            i++;
            x += gp.tileSize;
        }

    }

    public void drawManaCrystal() {
        int x = (gp.tileSize / 2) - 5;
        int y = (int)(gp.tileSize* 1.5);
        int i = 0;

        //DRAW BLANK HEARTS
        while (i < gp.player.maxMana) {
            g2.drawImage(crystal_blank, x, y, null);
            i++;
            x += 35;
        }

        //RESET
        x = (gp.tileSize / 2) - 5;
        y = (int)(gp.tileSize* 1.5);
        i = 0;

        //DRAW CURRENT LIFE
        while (i < gp.player.mana) {
            g2.drawImage(crystal_full, x, y, null);
            i++;
            x += 35;
        }
    }

    public void drawMessage() {
        int messageX = gp.tileSize;
        int messageY = gp.tileSize * 4;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));

        for (int i = 0; i < message.size(); i++) {

            if(message.get(i) != null) {

                //SHADOWING
                g2.setColor(Color.black);
                g2.drawString(message.get(i), messageX + 2, messageY + 2);

                //ACTUAL WHITE TEXT
                g2.setColor(Color.white);
                g2.drawString(message.get(i), messageX, messageY);

                int counter = messageCounter.get(i) + 1; //messageCounter++
                messageCounter.set(i, counter); //Set counter to the array
                messageY += 50;

                if (messageCounter.get(i) > 180) {
                    message.remove(i);
                    messageCounter.remove(i);
                }

            }
        }
    }

    public void drawPauseScreen() {
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
        String text = "PAUSED";
        int x = getXforCenterText(text);
        int y = gp.screenLength / 2;

        g2.drawString(text, x, y);
    }

    public void drawTitleScreen() {
        //BACKGROUND COLOR
        g2.setColor(new Color(0, 0, 0));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenLength);

        //TITLE NAME
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
        String text = "Blue Boy Adventure";
        int x = getXforCenterText(text);
        int y = gp.tileSize * 3;

        //SHADOW
        g2.setColor(Color.gray);
        g2.drawString(text, x + 5, y + 5);

        //MAIN COLOR
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        //BLUE BOY IMAGE
        x = gp.screenWidth / 2 - (gp.tileSize * 2)/2;
        y += gp.tileSize * 2;
        g2.drawImage(gp.player.down1, x, y, gp.tileSize * 2, gp.tileSize * 2, null);

        //MENU
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));
        text = "NEW GAME";
        x = getXforCenterText(text);
        y += gp.tileSize * 3.5;
        g2.drawString(text, x, y);
        if (commandNum == 0) {
            g2.drawString(">", x-gp.tileSize, y);
        }
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));
        text = "LOAD GAME";
        x = getXforCenterText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if (commandNum == 1) {
            g2.drawString(">", x-gp.tileSize, y);
        }

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));
        text = "QUIT";
        x = getXforCenterText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if (commandNum == 2) {
            g2.drawString(">", x-gp.tileSize, y);
        }

    }

    public void drawDialogueScreen() {
        //WINDOW
        int x = gp.tileSize * 2;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.tileSize * 4;

        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32F));
        x += gp.tileSize;
        y += gp.tileSize;

        for (String line : currentDialogue.split("/n")) {
            g2.drawString(line, x, y);
            y += 40;
        }
    }

    public void drawCharacterScreen() {
        //CREATE A FRAME
        final int frameX = gp.tileSize * 2;
        final int frameY = gp.tileSize;
        final int frameWidth = gp.tileSize * 5;
        final int frameHeight = gp.tileSize * 10;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));

        int textX = frameX + 20;
        int textY = frameY + gp.tileSize;
        final int lineHeight = 35; //Same as font size

        g2.drawString("Level", textX, textY);
        textY += lineHeight;
        g2.drawString("Life", textX, textY);
        textY += lineHeight;
        g2.drawString("Mana", textX, textY);
        textY += lineHeight;
        g2.drawString("Strength", textX, textY);
        textY += lineHeight;
        g2.drawString("Dexterity", textX, textY);
        textY += lineHeight;
        g2.drawString("Attack", textX, textY);
        textY += lineHeight;
        g2.drawString("Defense", textX, textY);
        textY += lineHeight;
        g2.drawString("Exp", textX, textY);
        textY += lineHeight;
        g2.drawString("Next Level", textX, textY);
        textY += lineHeight;
        g2.drawString("Coin", textX, textY);
        textY += lineHeight + 10;
        g2.drawString("Weapon", textX, textY);
        textY += lineHeight + 15;
        g2.drawString("Shield", textX, textY);
        textY += lineHeight;

        //VALUES
        int tailX = (frameX + frameWidth) - 30;
        //RESET textY
        textY = frameY + gp.tileSize;
        String value;

        value = String.valueOf(gp.player.level);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.life + "/" + gp.player.maxLife);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.mana + "/" + gp.player.maxMana);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.strength);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.dexterity);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.attack);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.defense);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.exp);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.nextLevelExp);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.coin);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        g2.drawImage(gp.player.currentWeapon.down1, tailX - gp.tileSize, textY - 24, null);
        textY += gp.tileSize;

        g2.drawImage(gp.player.currentShield.down1, tailX - gp.tileSize, textY - 24, null);

    }

    public void drawInventory() {

        //FRAME
        int frameX = gp.tileSize * 12;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize * 6;
        int frameHeight = gp.tileSize * 5;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        //SLOT
        final int slotXstart = frameX + 20;
        final int slotYstart = frameY + 20;
        int slotX = slotXstart;
        int slotY = slotYstart;
        int slotSize = gp.tileSize + 3;

        //DRAW PLAYERS ITEMS
        for (int i = 0; i < gp.player.inventory.size(); i++) {

            //EQUIP CURSOR
            if (gp.player.inventory.get(i) == gp.player.currentWeapon || gp.player.inventory.get(i) == gp.player.currentShield) {
                g2.setColor(new Color(240, 190, 90));
                g2.fillRoundRect(slotX, slotY, gp.tileSize, gp.tileSize, 10, 10);
            }

            g2.drawImage(gp.player.inventory.get(i).down1, slotX, slotY, null);
            slotX += slotSize;

            if (i == 4 || i == 9 || i == 14) {
                slotX = slotXstart;
                slotY += slotSize;
            }
        }

        //CURSOR
        int cursorX = slotXstart + (slotSize * slotCol);
        int cursorY = slotYstart + (slotSize * slotRow);
        int cursorWidth = gp.tileSize;
        int cursorHeight = gp.tileSize;

        //DRAW CURSOR
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);

        //DESCRIPTION FRAME
        int dFrameX = frameX;
        int dFrameY = frameY + frameHeight;
        int dFrameWidth = frameWidth; //Same as one above
        int dFrameHeight = gp.tileSize * 3;

        //Draw description text
        int textX = dFrameX + 20;
        int textY = dFrameY + gp.tileSize;
        g2.setFont(g2.getFont().deriveFont(28F));

        int itemIndex = getItemIndexOnSlot();

        if (itemIndex < gp.player.inventory.size()) {
            drawSubWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight);
            for (String line: gp.player.inventory.get(itemIndex).description.split("\n")) {
                g2.drawString(line, textX, textY);
                textY += 32;
            }
        }
    }

    public int getItemIndexOnSlot() {
        int itemIndex = slotCol + (slotRow * 5);
        return itemIndex;
    }

    public void drawSubWindow(int x, int y, int width, int height) {

        //Make new color with rgb
        Color c = new Color(0, 0, 0, 210);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    public int getXforCenterText(String text) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth / 2 - length / 2;
        return x;
    }

    public int getXforAlignToRightText(String text, int tailX) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = tailX - length;
        return x;
    }
}

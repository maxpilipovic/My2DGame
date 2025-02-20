package net.maxpilipovic.mygame;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UtilityTool {
    public BufferedImage scaleImage(BufferedImage original, int width, int height) {

        //Instantiate the BufferedImage (Like a blank canvas)
        BufferedImage scaledImage = new BufferedImage(width, height, original.getType());

        //Whatever g2 is going to draw is saved inside scaledImage
        Graphics2D g2 = scaledImage.createGraphics();

        //Draws the image with the scaled size
        g2.drawImage(original, 0, 0, width, height, null);

        g2.dispose();

        return scaledImage;
    }
}

package net.maxpilipovic.object;

import net.maxpilipovic.entity.Entity;
import net.maxpilipovic.mygame.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Heart extends Entity {

    public OBJ_Heart(GamePanel gp) {
        super(gp);
        name = "Heart";
        image = setup("/objects/heart_full");
        image2 = setup("/objects/heart_half");
        image3 = setup("/objects/heart_blank");


    }
}

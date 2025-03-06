package net.maxpilipovic.object;

import net.maxpilipovic.entity.Entity;
import net.maxpilipovic.mygame.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Door extends Entity {

    public OBJ_Door(GamePanel gp) {

        super(gp);

        name = "Door";
        down1 = setup("/objects/door");
        collision = true;
    }
}

package net.maxpilipovic.object;

import net.maxpilipovic.entity.Entity;
import net.maxpilipovic.mygame.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Boots extends Entity {
    GamePanel gp;

    public OBJ_Boots(GamePanel gp) {
        super(gp);
        name = "Boots";
        down1 = setup("/objects/boots");
    }

}

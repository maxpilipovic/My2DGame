package net.maxpilipovic.object;

import net.maxpilipovic.entity.Entity;
import net.maxpilipovic.mygame.GamePanel;

public class OBJ_Potion_Red extends Entity {

    GamePanel gp;
    int value;

    public OBJ_Potion_Red(GamePanel gp) {
        super(gp);

        this.gp = gp;

        type = type_consumable;
        name = "Red Potion";
        value = 5;
        down1 = setup("/objects/potion_red", gp.tileSize, gp.tileSize);
        description = "[Red Potion]\nHeals your life by " + value + ".";
    }

    @Override
    public void use(Entity entity) {
        gp.gameState = gp.dialogueState;
        //Remove extra \n
        gp.ui.currentDialogue = "You drink the " + name + "!/n"
                + "Your life has been recovered by " + value + ".";
        entity.life += value;
        gp.playSE(2);
    }
}

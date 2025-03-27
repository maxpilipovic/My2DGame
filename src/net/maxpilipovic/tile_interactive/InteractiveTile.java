package net.maxpilipovic.tile_interactive;

import net.maxpilipovic.entity.Entity;
import net.maxpilipovic.mygame.GamePanel;

public class InteractiveTile extends Entity {

    GamePanel gp;
    public boolean destructible = false;

    public InteractiveTile(GamePanel gp, int col, int row) {
        super(gp);
        this.gp = gp;
    }

    public boolean isCorrectItem(Entity entity) {
        boolean isCorrectItem = false;

        return isCorrectItem;
    }

    public void playSE() {

    }

    public InteractiveTile getDestroyedForm() {
        InteractiveTile tile  = null;
        return tile;
    }
    public void update() {
        if (invicable == true) {
            invincibleCounter++;
            if (invincibleCounter > 20) {
                invicable = false;
                invincibleCounter = 0;
            }
        }
    }
}

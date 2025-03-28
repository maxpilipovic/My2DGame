package net.maxpilipovic.tile_interactive;

import net.maxpilipovic.entity.Entity;
import net.maxpilipovic.mygame.GamePanel;

import java.awt.*;

public class IT_DryTree extends InteractiveTile {

    public IT_DryTree(GamePanel gp, int col, int row) {
        super(gp, col, row);
        this.gp = gp;

        this.worldX = gp.tileSize * col;
        this.worldY = gp.tileSize * row;

        down1 = setup("/tiles_interactive/drytree", gp.tileSize, gp.tileSize);
        destructible = true;

        life = 3;
    }

    @Override
    public boolean isCorrectItem(Entity entity) {
        boolean isCorrectItem = false;

        if (entity.currentWeapon.type == type_axe) {
            isCorrectItem = true;
        }

        return isCorrectItem;
    }

    @Override
    public void playSE() {
        gp.playSE(11);
    }

    @Override
    public InteractiveTile getDestroyedForm() {
        InteractiveTile tile  = new IT_Trunk(gp, worldX/gp.tileSize, worldY/gp.tileSize);
        return tile;
    }

    @Override
    public Color getParticleColor()  {
        Color color = new Color(65, 50, 30);

        return color;
    }

    @Override
    public int getParticleSize() {
        int size = 6; //6 Pixels
        return size;
    }

    @Override
    public int getParticleSpeed() {
        int speed = 1;
        return speed;
    }

    @Override
    public int getParticleMaxLife() {
        int maxLife = 20;
        return maxLife;
    }
}

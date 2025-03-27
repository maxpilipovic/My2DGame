package net.maxpilipovic.tile_interactive;

import net.maxpilipovic.mygame.GamePanel;

public class IT_Trunk extends InteractiveTile {

    public IT_Trunk(GamePanel gp, int col, int row) {
        super(gp, col, row);
        this.gp = gp;

        this.worldX = gp.tileSize * col;
        this.worldY = gp.tileSize * row;

        down1 = setup("/tiles_interactive/trunk", gp.tileSize, gp.tileSize);

        //Create new solid area so player can walk through
        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = 0;
        solidArea.height = 0;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

    }
}

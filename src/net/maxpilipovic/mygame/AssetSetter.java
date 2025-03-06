package net.maxpilipovic.mygame;

import net.maxpilipovic.entity.NPC_OldMan;
import net.maxpilipovic.monster.MON_GreenSlime;
import net.maxpilipovic.object.OBJ_Boots;
import net.maxpilipovic.object.OBJ_Chest;
import net.maxpilipovic.object.OBJ_Door;
import net.maxpilipovic.object.OBJ_Key;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {
        //Create objects

    }

    public void setNPC() {
        gp.npc[0] = new NPC_OldMan(gp);
        gp.npc[0].worldX = gp.tileSize * 21;
        gp.npc[0].worldY = gp.tileSize * 21;

    }

    public void setMonster() {
        gp.monster[0] = new MON_GreenSlime(gp);
        gp.monster[0].worldX = gp.tileSize * 23;
        gp.monster[0].worldY = gp.tileSize * 36;

        gp.monster[1] = new MON_GreenSlime(gp);
        gp.monster[1].worldX = gp.tileSize * 23;
        gp.monster[1].worldY = gp.tileSize * 37;
    }
}

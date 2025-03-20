package net.maxpilipovic.entity;

import net.maxpilipovic.mygame.GamePanel;
import net.maxpilipovic.mygame.UtilityTool;
import net.maxpilipovic.mygame.keyHandler;
import net.maxpilipovic.object.OBJ_Key;
import net.maxpilipovic.object.OBJ_Shield_Wood;
import net.maxpilipovic.object.OBJ_Sword_Normal;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Player extends Entity {
    keyHandler keyH;

    public final int screenX;
    public final int screenY;

    public int standCounter = 0;

    public boolean attackCancelled = false;

    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int maxInventorySize = 20;
    //public int hasKey = 0; Don't use anymore


    public Player(GamePanel gp, keyHandler keyH) {

        super(gp);

        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - (gp.tileSize/2);
        screenY = gp.screenLength / 2 - (gp.tileSize/2); //screenLength is height

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        solidArea.width = 32;
        solidArea.height = 32;

        attackArea.width = 36;
        attackArea.height = 36;

        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
        setItems();
    }

    public void setDefaultValues() {

        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        //TEST
        //worldX = gp.tileSize * 10;
        //worldY = gp.tileSize * 13;

        speed = 4;
        direction = "down";

        //Player Status
        level = 1;
        maxLife = 6;
        life = maxLife;
        strength = 1; //More strength, more damage he gives
        dexterity = 1; //More dexerity he has, less damage he receives
        exp = 0;
        nextLevelExp = 5;
        coin = 0;
        currentWeapon = new OBJ_Sword_Normal(gp);
        currentShield = new OBJ_Shield_Wood(gp);
        attack = getAttack(); //Total attack value is decided by strength and weapon
        defense = getDefense(); //Total defense value is decided by dexterity and shield
    }

    public void setItems() {
        inventory.add(currentWeapon);
        inventory.add(currentShield);
        inventory.add(new OBJ_Key(gp));
    }

    public int getAttack() {
        return attack = strength * currentWeapon.attackValue;
    }

    public int getDefense() {
        return defense = dexterity * currentShield.defenseValue;
    }
    public void update() {

        if (attacking == true) {
            attacking();
        }
        else if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed || keyH.enterPressed == true) {

            if (keyH.upPressed == true) {
                direction = "up";
            } else if (keyH.downPressed == true) {
                direction = "down";
            } else if (keyH.leftPressed == true) {
                direction = "left";
            } else if (keyH.rightPressed == true) {
                direction = "right";
            }

            //CHECK TILE COLLISION
            collisionOn = false;
            gp.cChecker.checkTile(this);

            //CHECK OBJECT COLLISION
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            //CHECK NPC COLLISION
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            //CHECK MONSTER COLLISION
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            contactMonster(monsterIndex);

            //CHECK EVENT
            gp.eHandler.checkEvent();

            //IF COLLISION IS FALSE, PLAYER CAN MOVE
            if (collisionOn == false && keyH.enterPressed == false) {
                switch (direction) {
                    case "up":
                        worldY -= speed;
                        break;
                    case "down":
                        worldY += speed;
                        break;
                    case "left":
                        worldX -= speed;
                        break;
                    case "right":
                        worldX += speed;
                        break;
                }
            }

            if (keyH.enterPressed == true && attackCancelled == false) {
                gp.playSE(7);
                attacking = true;
                spriteCounter = 0;
            }

            attackCancelled = false;
            gp.keyH.enterPressed = false;

            //This changes sprite form
            spriteCounter++;
            if (spriteCounter > 12) {
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        } else {
            standCounter ++;

            if (standCounter == 20) {
                standCounter = 0;
                spriteNum = 1;
            }
        }

        //TIMER for Invisible
        if (invicable == true) {
            invincibleCounter++;
            if (invincibleCounter > 60) { //1 second
                invicable = false;
                invincibleCounter = 0;
            }
        }
    }

    //This was i
    public void pickUpObject(int i) {
        if (i != 999) {

        }
    }

    public void attacking() {
        spriteCounter++;

        if (spriteCounter <= 5) {
            spriteNum = 1;
        }
        if (spriteCounter > 5 && spriteCounter <= 25) {
            spriteNum = 2;

            //Save the current worldX, worldY, solidArea
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            //Adjust player's worldX/Y for the attackArea
            switch(direction) {
                case "up":
                    worldY -= attackArea.height;
                    break;
                case "down":
                    worldY += attackArea.height;
                    break;
                case "left":
                    worldY -= attackArea.height;
                    break;
                case "right":
                    worldY -= attackArea.height;
                    break;
            }

            //AtackArea becomes solid area
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;

            //Check monster collision with the updated worldX, worldY and solidArea
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            damageMonster(monsterIndex);

            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;
        }

        if (spriteCounter > 25) {
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }
    }

    public void interactNPC(int i) {

        if (gp.keyH.enterPressed == true) {
            if (i != 999) {
                attackCancelled = true;
                gp.gameState = gp.dialogueState;
                gp.npc[i].speak();
            }
            //Removed due to water drinking bug
            //gp.keyH.enterPressed = false;
        }
    }

    public void contactMonster(int i) {
        if (i != 999) {

            if (invicable == false) {
                gp.playSE(6);

                int damage = gp.monster[i].attack - defense;

                if (damage < 0) {
                    damage = 0;
                }

                life -= damage;
                invicable = true;
            }
        }
    }

    public void damageMonster(int i) {
        if (i != 999) {

            if (gp.monster[i].invicable == false) {
                gp.playSE(5);

                int damage = attack - gp.monster[i].defense;

                if (damage < 0) {
                    damage = 0;
                }

                gp.monster[i].life -= damage;
                gp.ui.addMessage(damage + " damage!");
                gp.monster[i].invicable = true;
                gp.monster[i].damageReaction();

                //Killed Monster
                if (gp.monster[i].life <= 0) {
                    gp.monster[i].dying = true;
                    gp.ui.addMessage("Killed the " + gp.monster[i].name + "!");
                    gp.ui.addMessage("Exp + " + gp.monster[i].exp + "!");
                    exp += gp.monster[i].exp;
                    checkLevelUp();

                }
            }
        }
    }

    public void checkLevelUp() {
        if (exp >= nextLevelExp) {
            level++;
            nextLevelExp = nextLevelExp * 2;
            maxLife += 2;
            strength++;
            dexterity++;
            attack = getAttack();
            defense = getDefense();
            gp.playSE(8);
            gp.gameState = gp.dialogueState;
            gp.ui.currentDialogue = "You are level " + level + " now!\n "
                    + "You feel stronger!";
        }
    }

    public void getPlayerImage() {

        up1 = setup("/player/boy_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("/player/boy_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("/player/boy_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/player/boy_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/player/boy_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("/player/boy_left_2", gp.tileSize, gp.tileSize);
        right1 = setup("/player/boy_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("/player/boy_right_2", gp.tileSize, gp.tileSize);
    }

    public void getPlayerAttackImage() {
        attackUp1 = setup("/player/Attacking_sprites/boy_attack_up_1", gp.tileSize, gp.tileSize*2);
        attackUp2 = setup("/player/Attacking_sprites/boy_attack_up_2", gp.tileSize, gp.tileSize*2);
        attackDown1 = setup("/player/Attacking_sprites/boy_attack_down_1", gp.tileSize, gp.tileSize*2);
        attackDown2 = setup("/player/Attacking_sprites/boy_attack_down_2", gp.tileSize, gp.tileSize*2);
        attackLeft1 = setup("/player/Attacking_sprites/boy_attack_left_1", gp.tileSize*2, gp.tileSize);
        attackLeft2 = setup("/player/Attacking_sprites/boy_attack_left_2",gp.tileSize*2, gp.tileSize);
        attackRight1 = setup("/player/Attacking_sprites/boy_attack_right_1",gp.tileSize*2, gp.tileSize);
        attackRight2 = setup("/player/Attacking_sprites/boy_attack_right_2",gp.tileSize*2, gp.tileSize);

    }

    public void draw(Graphics2D g2) {
        //g2.setColor(Color.white);
        //g2.fillRect(x, y, gp.tileSize, gp.tileSize);

        BufferedImage image = null;
        int tempScreenX = screenX;
        int tempScreenY = screenY;

        switch (direction) {
            case "up":
                if (attacking == false) {
                    if (spriteNum == 1) {
                        image = up1;
                    }
                    if (spriteNum == 2) {
                        image = up2;
                    }
                }
                if (attacking == true) {
                    tempScreenY = screenY - gp.tileSize;
                    if (spriteNum == 1) {
                        image = attackUp1;
                    }
                    if (spriteNum == 2) {
                        image = attackUp2;
                    }
                }
                break;
            case "down":
                if (attacking == false) {
                    if (spriteNum == 1) {
                        image = down1;
                    }
                    if (spriteNum == 2) {
                        image = down2;
                    }
                }
                if (attacking == true) {
                    if (spriteNum == 1) {
                        image = attackDown1;
                    }
                    if (spriteNum == 2) {
                        image = attackDown2;
                    }
                }
                break;
            case "left":
                if (attacking == false) {
                    if (spriteNum == 1) {
                        image = left1;
                    }
                    if (spriteNum == 2) {
                        image = left2;
                    }
                }
                if (attacking == true) {
                    tempScreenX = screenX - gp.tileSize;
                    if (spriteNum == 1) {
                        image = attackLeft1;
                    }
                    if (spriteNum == 2) {
                        image = attackLeft2;
                    }
                }
                break;
            case "right":
                if (attacking == false) {
                    if (spriteNum == 1) {
                        image = right1;
                    }
                    if (spriteNum == 2) {
                        image = right2;
                    }
                }
                if (attacking == true) {
                    if (spriteNum == 1) {
                        image = attackRight1;
                    }
                    if (spriteNum == 2) {
                        image = attackRight2;
                    }
                }
                break;
        }
        if (invicable == true) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }

        g2.drawImage(image, tempScreenX, tempScreenY, null);

        //RESET ALPHA (OPACITY)
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        //DEBUG
        //g2.setFont(new Font("Arial", Font.PLAIN, 26));
        //g2.setColor(Color.white);
        //g2.drawString("Invincible: " + invincibleCounter, 10, 400);
    }

}

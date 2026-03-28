package All.Tower;

import All.Enemy.Enemy;
import Game.GamePanel;
import Game.Particle;
import Game.UpdatableAndDrawable;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Tower implements UpdatableAndDrawable{

    private GamePanel gamePanel;
    private int x;
    private int y;
    private String name;
    private double damage;
    private double attackSpeed;
    private int range;
    private int attackCount;
    private int sellCost;
    private Ellipse2D attackArea;
    private ArrayList<Enemy> enemyInArea;
    private String attackType;
    private boolean isGhostBuster;
    private boolean canCreateParticle;
    private String particleType;
    private boolean canCreateMoney;
    private int reward;

    //Image
    private Rectangle placedSolidArea; //Solid Area When placed

    private int count = 0;
    private boolean isSold = false;

    //Animation
    private ArrayList<BufferedImage> attacking, idle;
    private String state = "idle";
    private String direction = "right";
    private int frame = 0;
    private int animationCounter;
    private int animationSpeed = 3;

    public Tower(GamePanel gamePanel, int x, int y, String name) {
        this.gamePanel = gamePanel;
        this.x = x;
        this.y = y;
        this.name = name;
        this.enemyInArea = new ArrayList<>();
        this.attackArea = new Ellipse2D.Double(x - range / 2, y - range / 2, range, range);
        HashMap<String, ArrayList<BufferedImage>> assasinAnimation = gamePanel.getLoadData().getAnimation(name);
        idle = assasinAnimation.get("idle");
        attacking = assasinAnimation.get("attacking");
    }

    @Override
    public void update() {
        gamePanel.getCollisionChecker().checkEntity(this);

        if (enemyInArea.size() > 0) {
            Enemy enemy = enemyInArea.get(0);

            //Find direction
            if (enemy.getX() > x) {
                direction = "right";
            } else {
                direction = "left";
            }

            if (attackCount > attackSpeed) {
                state = "attacking";
                frame = 0;

                if (canCreateParticle == true) {
                    Particle particle = new Particle(enemy.getX(), enemy.getY(), 3, particleType);
                    gamePanel.getAllParticle().add(particle);
                }

                //Atack
                switch (attackType) {
                    case "Single":
                        enemy.setHealth(enemy.getHealth() - damage);
                        break;
                    case "AOE":
                        for (int i = 0; i < enemyInArea.size(); i++) {
                            Enemy e = enemyInArea.get(i);
                            e.setHealth(e.getHealth() - damage);
                        }
                        break;
                }
                attackCount = 0;

            } else {
                attackCount++;
            }
        } else {
            state = "idle";
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        this.attackArea = new Ellipse2D.Double(x - range / 2, y - range / 2, range, range); //Attack Range
        BufferedImage image;
        if (state.equals("attacking")) {
            if (frame < attacking.size()) {
                image = attacking.get(frame);
            } else {
                image = attacking.get(attacking.size() - 1); //Loop last frame when attack cooldown
            }
        } else {
            frame %= idle.size();
            image = idle.get(frame);
        }

        int tileSize = gamePanel.getTileSize();
        //Circle
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        g2.setColor(new Color(100, 149, 237));
        g2.fill(attackArea);

        //Shadow
        g2.setColor(Color.black);
        g2.fillOval(x - tileSize / 6, y + tileSize / 5, tileSize / 3, tileSize / 4);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        //Animation & Image
        int drawX = x - tileSize / 2;
        int drawY = y - tileSize / 2;
        if (direction.equals("left")) {
            g2.drawImage(image, drawX + tileSize, drawY, -tileSize, tileSize, null);
        } else {
            g2.drawImage(image, drawX, drawY, tileSize, tileSize, null);
        }
//        g2.fill(placedSolidArea);
        animationCounter++;
        if (animationCounter >= animationSpeed) {
            frame++;
            animationCounter = 0;
        }
    }

    public abstract void levelUp();

    public void sell() {
        isSold = true;
    }

    public Ellipse2D getAttackArea() {
        return attackArea;
    }

    public Rectangle getPlacedSolidArea() {
        return placedSolidArea;
    }

    public double getDamage() {
        return damage;
    }

    public double getAttackSpeed() {
        return attackSpeed;
    }

    public int getRange() {
        return range;
    }

    public ArrayList<Enemy> getEnemyInArea() {
        return enemyInArea;
    }

    public String getName() {
        return name;
    }

    public boolean getIsSold() {
        return isSold;
    }

    public boolean getIsGhostBuster() {
        return isGhostBuster;
    }

    public int getSellCost() {
        return sellCost;
    }

    public int getReward() {
        return reward;
    }

    public boolean getCanCreateMoney() {
        return canCreateMoney;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public void setAttackSpeed(double attackSpeed) {
        this.attackSpeed = attackSpeed;
        this.attackCount = (int) attackSpeed;
    }

    public void setSellCost(int sellCost) {
        this.sellCost = sellCost;
    }

    public void setAttackType(String attackType) {
        this.attackType = attackType;
    }

    public void setIsGhostBuster(boolean isGhostBuster) {
        this.isGhostBuster = isGhostBuster;
    }

    public void setCanCreateParticle(boolean canCreateParticle) {
        this.canCreateParticle = canCreateParticle;
    }
    
    public void setCanCreateParticle(boolean canCreateParticle, String particleType) {
        this.canCreateParticle = canCreateParticle;
        this.particleType = particleType;
    }

    public void setCanCreateMoney(boolean canCreateMoney) {
        this.canCreateMoney = canCreateMoney;
    }
    
    public void setCanCreateMoney(boolean canCreateMoney, int reward) {
        this.canCreateMoney = canCreateMoney;
        this.reward = reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public void setIsSold(boolean isSold) {
        this.isSold = isSold;
    }
    
    public void setRange(int range){
        this.range = range;
    }
    
    public void setPlacedSolidArea(Rectangle placedSolidArea){
        this.placedSolidArea = placedSolidArea;
    }
    
    public GamePanel getGamePanel(){
        return gamePanel;
    }
    
}

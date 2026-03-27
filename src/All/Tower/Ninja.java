package All.Tower;

import All.Enemy.FemaleGoblin;
import All.GamePanel;
import All.Particle;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class Ninja extends Tower {

    private GamePanel gamePanel;
    private int x;
    private int y;
    private String name = "Ninja";
    private static int baseRange = 300;
    private double damage = 80;
    private double attackSpeed = 200;
    private int attackCount;
    private int range = baseRange;
    private int sellCost = 60;
    private Ellipse2D attackArea;
    private ArrayList<FemaleGoblin> enemyInArea;
    private String attackType = "Single";
    private boolean isGhostBuster = false;
    private boolean canCreateParticle = true;
    private boolean canCreateMoney = false;
    private int reward = 0;

    //Image
    private static int solidWidth = 24;
    private static int solidHeight = 40;
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

    public Ninja(GamePanel gamePanel, int x, int y) {
        this.gamePanel = gamePanel;
        this.x = x;
        this.y = y;
        this.enemyInArea = new ArrayList<>();
        this.attackArea = new Ellipse2D.Double(x - range / 2, y - range / 2, range, range); //Attack Range
        this.placedSolidArea = new Rectangle(x - solidWidth / 2, y - solidHeight / 2, solidWidth, solidHeight); //Solid Area
        HashMap<String, ArrayList<BufferedImage>> assasinAnimation = gamePanel.getLoadAnimation().getAnimation(this.getName());
        idle = assasinAnimation.get("idle");
        attacking = assasinAnimation.get("attacking");
        attackCount = (int) attackSpeed;
    }

    @Override
    public void update() {
        gamePanel.getCollisionChecker().checkEntity(this);

        if (enemyInArea.size() > 0) {
            FemaleGoblin enemy = enemyInArea.get(0);

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
                    Particle particle = new Particle(enemy.getX(), enemy.getY(), 3, "Typhoon");
                    gamePanel.getAllParticle().add(particle);
                }

                //Atack
                switch (attackType) {
                    case "Single":
                        enemy.setHealth(enemy.getHealth() - damage);
                        break;
                    case "AOE":
                        for (int i = 0; i < enemyInArea.size(); i++) {
                            FemaleGoblin e = enemyInArea.get(i);
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

    @Override
    public void levelUp() {
        range += 10;
        attackSpeed -= 2;
    }

    @Override
    public void sell() {
        isSold = true;
    }

    public static int getSolidWidth() {
        return solidWidth;
    }

    public static int getSolidHeight() {
        return solidHeight;
    }

    public static int getBaseRange() {
        return baseRange;
    }

    @Override
    public Ellipse2D getAttackArea() {
        return attackArea;
    }

    @Override
    public Rectangle getPlacedSolidArea() {
        return placedSolidArea;
    }

    @Override
    public double getDamage() {
        return damage;
    }

    @Override
    public double getAttackSpeed() {
        return attackSpeed;
    }

    @Override
    public int getRange() {
        return range;
    }

    @Override
    public ArrayList<FemaleGoblin> getEnemyInArea() {
        return enemyInArea;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean getIsSold() {
        return isSold;
    }

    @Override
    public boolean getIsGhostBuster() {
        return isGhostBuster;
    }

    @Override
    public int getSellCost() {
        return sellCost;
    }

    public int getReward() {
        return reward;
    }

    public boolean getCanCreateMoney() {
        return canCreateMoney;
    }
}

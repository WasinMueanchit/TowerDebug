
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class Assasin {

    private GamePanel gamePanel;
    private String name;
    private int x;
    private int y;
    private static int baseRange = 150;
    private double damage;
    private double attackSpeed;
    private int attackCount;
    private int range;
    private int sellCost = 50;
    private Ellipse2D attackArea;
    private ArrayList<FemaleGoblin> enemyInArea;
    private String attackType;
    private boolean isGhostBuster;
    private boolean canCreateParticle = true;

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

    public Assasin(GamePanel gamePanel, String name, int x, int y, double damage, double attackSpeed, int range, String attackType, boolean isGhostBuster) {
        this.gamePanel = gamePanel;
        this.name = name;
        this.x = x;
        this.y = y;
        this.damage = damage;
        this.attackSpeed = attackSpeed;
        this.range = range;
        this.baseRange = this.range;
        this.enemyInArea = new ArrayList<>();
        this.attackType = attackType;
        this.isGhostBuster = isGhostBuster;
        this.attackArea = new Ellipse2D.Double(x - range / 2, y - range / 2, range, range); //Attack Range
        this.placedSolidArea = new Rectangle(x - solidWidth / 2, y - solidHeight / 2, solidWidth, solidHeight); //Solid Area
        HashMap<String, ArrayList<BufferedImage>> assasinAnimation = gamePanel.getLoadAnimation().getAnimation("Assasin");
        idle = assasinAnimation.get("idle");
        attacking = assasinAnimation.get("attacking");
        attackCount = (int) attackSpeed;
    }

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
                Particle particle = new Particle(enemy.getX(), enemy.getY(), 3,"Lightning");
                gamePanel.getAllParticle().add(particle);


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

    public void levelUp() {
        range += 10;
        attackSpeed -= 2;
    }

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

    public ArrayList<FemaleGoblin> getEnemyInArea() {
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
    
    public int getSellCost(){
        return sellCost;
    }
}

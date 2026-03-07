
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Assasin {

    private GamePanel gamePanel;
    private int x;
    private int y;
    private static int baseRange = 150;
    private double damage;
    private double attackSpeed;
    private int attackCount = 0;
    private int range;
    private Ellipse2D attackArea;
    private ArrayList<FemaleGoblin> enemyInArea;
    private String attackType;

    //Image
    private static int solidWidth = 24;
    private static int solidHeight = 40;

    int count = 0;

    //Animation
    private BufferedImage[] up, down, left, right, idle;
    private String state = "Idle";
    private String direction = "down";
    private int frame = 0;
    private int animationCounter = 0;
    private int animationSpeed = 3;

    public Assasin(GamePanel gamePanel, int x, int y, double damage, double attackSpeed, int range, String attackType) {
        this.gamePanel = gamePanel;
        this.x = x;
        this.y = y;
        this.damage = damage;
        this.attackSpeed = attackSpeed;
        this.range = range;
        this.baseRange = this.range;
        this.enemyInArea = new ArrayList<>();
        this.attackType = attackType;
        BufferedImage[][] assasinAnimation = gamePanel.getAssasinAnimation();
        up = assasinAnimation[0];
        down = assasinAnimation[1];
        left = assasinAnimation[2];
        right = assasinAnimation[3];
        idle = assasinAnimation[4];
    }

    public void update() {
        attackArea = new Ellipse2D.Double(x - range / 2, y - range / 2, range, range);
        gamePanel.getCollisionChecker().checkEntity(this);

        if (enemyInArea.size() > 0) {
            FemaleGoblin enemy = enemyInArea.get(0);

            if (attackCount > attackSpeed) {
                state = "Attacking";
                double dx = enemy.getX() - x;
                double dy = enemy.getY() - y;

                //Find direction
                if (Math.abs(dx) > Math.abs(dy)) {
                    if (dx > 0) {
                        direction = "right";
                    } else {
                        direction = "left";
                    }
                } else {
                    if (dy > 0) {
                        direction = "down";
                    } else {
                        direction = "up";
                    }
                }

                //Atack
                switch (attackType) {
                    case "Single":
                        enemy.setHealth(enemy.getHealth() - damage);
                        break;
                    case "AOE":
                        for (int i = 0; i < enemyInArea.size(); i++) {
                            enemyInArea.get(i).setHealth(enemy.getHealth() - damage);
                        }
                        break;
                }
                attackCount = 0;

            } else {
                attackCount++;
            }
        } else {
            state = "Idle";
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        if (state.equals("Attacking")) {
            switch (direction) {
                case "up":
                    image = up[frame];
                    break;
                case "down":
                    image = down[frame];
                    break;
                case "left":
                    image = left[frame];
                    break;
                case "right":
                    image = right[frame];
                    break;
            }
        } else {
            image = idle[frame];
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
        g2.drawImage(image, x - tileSize / 2, y - tileSize / 2, tileSize, tileSize, null);

        animationCounter++;
        if (animationCounter >= animationSpeed) {
            frame++;
            animationCounter = 0;
        }
        frame %= up.length;
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

    public ArrayList<FemaleGoblin> getEnemyInArea() {
        return enemyInArea;
    }
}

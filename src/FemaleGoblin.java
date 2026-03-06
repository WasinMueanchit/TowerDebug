
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class FemaleGoblin {

    private int speed;
    private int x;
    private int y;
    private GamePanel gamePanel;

    //Animation
    private BufferedImage[] up, down, left, right;
    private String direction = "down";
    private int frame = 0;
    private int animationCounter = 0;
    private int animationSpeed = 3;

    //Coordinates for entity
    private Point[] waypoints;
    private int currentTarget = 0;

    public FemaleGoblin(GamePanel gamePanel, Point[] waypoints, int speed) {
        this.gamePanel = gamePanel;
        this.waypoints = waypoints;
        this.speed = speed;
        this.x = waypoints[0].getX();
        this.y = waypoints[0].getY() - 20;
        BufferedImage[][] femaleGoblinAnimation = gamePanel.getFemaleGobinAnimation();
        up = femaleGoblinAnimation[0];
        down = femaleGoblinAnimation[1];
        left = femaleGoblinAnimation[2];
        right = femaleGoblinAnimation[3];
    }

    public void update() {
        Point target = waypoints[currentTarget];
        int targetX = target.getX();
        int targetY = target.getY() - 20;
        if (x != targetX) {
            if (x < targetX) {
                x = Math.min(x + speed, targetX);
                direction = "right";
            } else {
                x = Math.max(x - speed, targetX);
                direction = "left";
            }
        } else if (y != targetY) {
            if (y < targetY) {
                y = Math.min(y + speed, targetY);
                direction = "down";
            } else {
                y = Math.max(y - speed, targetY);
                direction = "up";
            }
        } else {
            if (currentTarget < waypoints.length - 1) {
                currentTarget++;
            }
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;
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

        int tileSize = gamePanel.getTileSize();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
        g2.fillOval(x + 20, y + 40, tileSize / 3, tileSize / 4);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        g2.drawImage(image, x, y, tileSize, tileSize, null);

        animationCounter++;
        if (animationCounter >= animationSpeed) {
            frame++;
            animationCounter = 0;
        }
        frame %= up.length;
    }
}

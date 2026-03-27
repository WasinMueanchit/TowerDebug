package All.Enemy;


import All.GamePanel;
import All.Point;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class FemaleGoblin {

    private int speed;
    private int x;
    private int y;
    private double maxHealth;
    private double health;
    private boolean alive = true;
    private boolean isGhost;
    private GamePanel gamePanel;
    private int reward = 5;
    
    private Rectangle solidArea;
    
    //Image
    private static int solidWidth = 24;
    private static int solidHeight = 40;

    //Animation
    private ArrayList<BufferedImage> up, down, left, right;
    private String direction = "down";
    private int frame = 0;
    private int animationCounter = 0;
    private int animationSpeed = 3;

    //Coordinates for entity
    private Point[] waypoints;
    private int currentTarget = 0;

    public FemaleGoblin(GamePanel gamePanel, Point[] waypoints, int speed, double health, boolean isGhost) {
        this.gamePanel = gamePanel;
        this.waypoints = waypoints;
        this.speed = speed;
        this.maxHealth = health;
        this.health = health;
        this.isGhost = isGhost;
        this.x = waypoints[0].getX();
        this.y = waypoints[0].getY() - 20;
        HashMap<String, ArrayList<BufferedImage>> femaleGoblinAnimation = gamePanel.getLoadAnimation().getAnimation("Female Goblin");
        up = femaleGoblinAnimation.get("up");
        down = femaleGoblinAnimation.get("down");
        left = femaleGoblinAnimation.get("left");
        right = femaleGoblinAnimation.get("right");
    }

    public void update() {
        if (health <= 0){
            alive = false;
        }
        solidArea = new Rectangle((x+gamePanel.getTileSize()/2)-solidWidth/2, (y+gamePanel.getTileSize()/2)-solidHeight/2, solidWidth ,solidHeight);
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
            }else{
                gamePanel.setBaseHP(gamePanel.getBaseHP() - health);
                alive = false;
            }
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        switch (direction) {
            case "up":
                image = up.get(frame);
                break;
            case "down":
                image = down.get(frame);
                break;
            case "left":
                image = left.get(frame);
                break;
            case "right":
                image = right.get(frame);
                break;
        }

        
        int tileSize = gamePanel.getTileSize();
        //Health Bar
        double oneScale = (double) 40/maxHealth;
        double hpBarValue = oneScale*health;
        g2.setColor(new Color(18, 10, 7));
        g2.fillRoundRect(x + 13, y - 12, 42, 7, 3, 3);
        g2.setColor(new Color(59, 188, 122));
        g2.fillRoundRect(x + 14, y - 11, (int)hpBarValue, 4, 3, 3);
        
//        g2.fill(solidArea);

        //Shadow
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        g2.setColor(Color.black);
        g2.fillOval(x + 20, y + 40, tileSize / 3, tileSize / 4);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        
        //Animatation & Image
        if(isGhost == true){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        }
        g2.drawImage(image, x, y, tileSize, tileSize, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        animationCounter++;
        if (animationCounter >= animationSpeed) {
            frame++;
            animationCounter = 0;
        }
        
        frame %= up.size();
    }
    
    public boolean getAlive(){
        return alive;
    }
    
    public Rectangle getSolidArea(){
        return solidArea;
    }
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
    
    public double getHealth(){
        return health;
    }
    
    public void setHealth(double health){
        this.health = health;
    }
    
    public boolean getIsGhost(){
        return isGhost;
    }
    
    public int getReward(){
        return reward;
    }
}

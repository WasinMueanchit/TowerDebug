package All.Enemy;

import All.GamePanel;
import All.Point;

public class Skeleton extends Enemy{
    private static int solidWidth = 24;
    private static int solidHeight = 40;
    private int health = 4000;
    private int speed = 2;
    public Skeleton(GamePanel gamePanel, Point[] waypoints, boolean isGhost) {
        super(gamePanel, waypoints, isGhost, "Skeleton");
        super.setMaxHealth(health);
        super.setHealth(health);
        super.setSpeed(speed);
        super.setReward(10);
        super.setSolidWidth(solidWidth);
        super.setSolidHeight(solidHeight);
    }
}

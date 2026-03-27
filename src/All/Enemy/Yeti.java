package All.Enemy;

import All.GamePanel;
import All.Point;

public class Yeti extends Enemy{
    private static int solidWidth = 24;
    private static int solidHeight = 40;
    private int health = 2500;
    private int speed = 2;
    public Yeti(GamePanel gamePanel, Point[] waypoints, boolean isGhost) {
        super(gamePanel, waypoints, isGhost, "Yeti");
        super.setMaxHealth(health);
        super.setHealth(health);
        super.setSpeed(speed);
        super.setReward(10);
        super.setSolidWidth(solidWidth);
        super.setSolidHeight(solidHeight);
    }
}

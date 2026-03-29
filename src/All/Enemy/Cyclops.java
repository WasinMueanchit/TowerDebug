package All.Enemy;

import Game.GamePanel;
import Game.Point;

public class Cyclops extends Enemy{
    private static int solidWidth = 24;
    private static int solidHeight = 40;
    private int health = 2000;
    private int speed = 2;
    public Cyclops(GamePanel gamePanel, Point[] waypoints, boolean isGhost) {
        super(gamePanel, waypoints, isGhost, "Cyclops");
        super.setMaxHealth(health);
        super.setHealth(health);
        super.setSpeed(speed);
        super.setReward(1000);
        super.setSolidWidth(solidWidth);
        super.setSolidHeight(solidHeight);
    }
}

package All.Enemy;

import Game.GamePanel;
import Game.Point;

public class MaleGoblin extends Enemy{
    private static int solidWidth = 24;
    private static int solidHeight = 40;
    private int health = 50;
    private int speed = 1;
    public MaleGoblin(GamePanel gamePanel, Point[] waypoints, boolean isGhost) {
        super(gamePanel, waypoints, isGhost, "Male Goblin");
        super.setMaxHealth(health);
        super.setHealth(health);
        super.setSpeed(speed);
        super.setReward(12);
        super.setSolidWidth(solidWidth);
        super.setSolidHeight(solidHeight);
    }
}

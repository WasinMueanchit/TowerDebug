package All.Enemy;


import Game.GamePanel;
import Game.Point;

public class FemaleGoblin extends Enemy{
    private static int solidWidth = 24;
    private static int solidHeight = 40;
    private int health = 100;
    private int speed = 1;
    public FemaleGoblin(GamePanel gamePanel, Point[] waypoints, boolean isGhost) {
        super(gamePanel, waypoints, isGhost, "Female Goblin");
        super.setMaxHealth(health);
        super.setHealth(health);
        super.setSpeed(speed);
        super.setReward(10);
        super.setSolidWidth(solidWidth);
        super.setSolidHeight(solidHeight);
    }
}

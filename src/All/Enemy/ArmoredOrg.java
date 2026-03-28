package All.Enemy;

import Game.GamePanel;
import Game.Point;

public class ArmoredOrg extends Enemy{
    private static int solidWidth = 24;
    private static int solidHeight = 40;
    private int health = 3000;
    private int speed = 2;
    public ArmoredOrg(GamePanel gamePanel, Point[] waypoints, boolean isGhost) {
        super(gamePanel, waypoints, isGhost, "Armored Org");
        super.setMaxHealth(health);
        super.setHealth(health);
        super.setSpeed(speed);
        super.setReward(10);
        super.setSolidWidth(solidWidth);
        super.setSolidHeight(solidHeight);
    }
}

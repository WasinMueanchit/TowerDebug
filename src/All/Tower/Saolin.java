package All.Tower;

import Game.GamePanel;
import java.awt.Rectangle;

public class Saolin extends Tower {

    private static int solidWidth = 24;
    private static int solidHeight = 40;
    private static int baseRange = 200;
    private static int cost = 130;
    private int level = 1;

    public Saolin(GamePanel gamePanel, int x, int y) {
        super(gamePanel, x, y, "Saolin");
        super.setRange(baseRange);
        super.setDamage(35);
        super.setAttackSpeed(50);
        super.setSellCost(60);
        super.setAttackType("Single");
        super.setIsGhostBuster(true);
        super.setCanCreateParticle(true, "Lightning");
        super.setCanCreateMoney(false);
        super.setPlacedSolidArea(new Rectangle(x - solidWidth / 2, y - solidHeight / 2, solidWidth, solidHeight));
    }

    int[] costs = {0, 80, 140};
    int[] damages = {35, 55, 95};
    int[] range = {200, 240, 280};
    int[] attackSpeed = {50, 45, 40};

    @Override
    public void levelUp() {
        int level = super.getLevel();
        if (level >= costs.length) {
            return;
        }
        int cost = costs[level];
        int currentCoin = super.getGamePanel().getCoin();

        if (currentCoin >= cost) {
            super.getGamePanel().setCoin(currentCoin - cost);
            super.setLevel(level + 1);
            super.setDamage(damages[level]);
            super.setRange(range[level]);
            super.setAttackSpeed(attackSpeed[level]);
        }
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

    public static int getCost() {
        return cost;
    }

    @Override
    public int[] getUpGradeCosts() {
        return costs;
    }
}

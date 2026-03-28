package All.Tower;

import Game.GamePanel;
import java.awt.Rectangle;

public class Gladiator extends Tower {

    private static int solidWidth = 24;
    private static int solidHeight = 40;
    private static int baseRange = 100;
    private static int cost = 50;
    private int level = 1;

    public Gladiator(GamePanel gamePanel, int x, int y) {
        super(gamePanel, x, y, "Gladiator");
        super.setRange(baseRange);
        super.setDamage(20);
        super.setAttackSpeed(40);
        super.setSellCost(25);
        super.setAttackType("Single");
        super.setIsGhostBuster(false);
        super.setCanCreateParticle(false);
        super.setCanCreateMoney(false);
        super.setPlacedSolidArea(new Rectangle(x - solidWidth / 2, y - solidHeight / 2, solidWidth, solidHeight));
    }

    int[] costs = {0, 40, 70};
    int[] damages = {20, 30, 40};
    int[] range = {100, 120, 140};
    int[] attackSpeed = {40, 35, 30};

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
            super.setDamage(damages[level - 1]);
            super.setRange(range[level - 1]);
            super.setAttackSpeed(attackSpeed[level - 1]);
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

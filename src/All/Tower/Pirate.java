package All.Tower;

import Game.GamePanel;
import java.awt.Rectangle;

public class Pirate extends Tower {

    private static int solidWidth = 24;
    private static int solidHeight = 40;
    private static int baseRange = 80;
    private static int cost = 150;
    private int level = 1;

    public Pirate(GamePanel gamePanel, int x, int y) {
        super(gamePanel, x, y, "Pirate");
        super.setRange(baseRange);
        super.setDamage(0);
        super.setAttackSpeed(0);
        super.setSellCost(50);
        super.setAttackType("Single");
        super.setIsGhostBuster(false);
        super.setCanCreateParticle(false);
        super.setCanCreateMoney(true, 100);
        super.setPlacedSolidArea(new Rectangle(x - solidWidth / 2, y - solidHeight / 2, solidWidth, solidHeight));
    }

    int[] costs = {0, 50, 60};
    int[] rewards = {100, 120, 140};

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
            super.setReward(rewards[level]);
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

package All.Tower;

import Game.GamePanel;
import java.awt.Rectangle;

public class Valkyrie extends Tower {

    private static int solidWidth = 24;
    private static int solidHeight = 40;
    private static int baseRange = 70;
    private static int cost = 100;
    private int level = 1;

    public Valkyrie(GamePanel gamePanel, int x, int y) {
        super(gamePanel, x, y, "Valkyrie");
        super.setRange(baseRange);
        super.setDamage(10);
        super.setAttackSpeed(10);
        super.setSellCost(50);
        super.setAttackType("Single");
        super.setIsGhostBuster(false);
        super.setCanCreateParticle(false);
        super.setCanCreateMoney(false);
        super.setPlacedSolidArea(new Rectangle(x - solidWidth / 2, y - solidHeight / 2, solidWidth, solidHeight));
    }

    int[] costs = {0, 70, 120};
    int[] damages = {10, 15, 20};
    int[] range = {70, 90, 110};
    int[] attackSpeed = {10, 9, 8};

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

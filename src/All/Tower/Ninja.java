package All.Tower;

import Game.GamePanel;
import java.awt.Rectangle;

public class Ninja extends Tower {

    private static int solidWidth = 24;
    private static int solidHeight = 40;
    private static int baseRange = 300;
    private static int cost = 120;
    private int level = 1;

    public Ninja(GamePanel gamePanel, int x, int y) {
        super(gamePanel, x, y, "Ninja");
        super.setRange(baseRange);
        super.setDamage(80);
        super.setAttackSpeed(200);
        super.setSellCost(60);
        super.setAttackType("Single");
        super.setIsGhostBuster(false);
        super.setCanCreateParticle(true, "Typhoon");
        super.setCanCreateMoney(false);
        super.setPlacedSolidArea(new Rectangle(x - solidWidth / 2, y - solidHeight / 2, solidWidth, solidHeight));
    }

    int[] costs = {0, 100, 150};
    int[] damages = {80, 130, 200};
    int[] range = {300, 340, 380};
    int[] attackSpeed = {200, 180, 160};

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

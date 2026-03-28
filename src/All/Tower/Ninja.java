package All.Tower;

import Game.GamePanel;
import java.awt.Rectangle;

public class Ninja extends Tower {

    private static int solidWidth = 24;
    private static int solidHeight = 40;
    private static int baseRange = 300;
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
        if (level >= costs.length) {
            return;
        }
        int cost = costs[level];
        int currentCoin = super.getGamePanel().getCoin();

        if (currentCoin >= cost) {
            super.getGamePanel().setCoin(currentCoin - cost);
            level++;
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
}

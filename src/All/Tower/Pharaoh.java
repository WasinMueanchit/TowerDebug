package All.Tower;

import Game.GamePanel;
import java.awt.Rectangle;

public class Pharaoh extends Tower {

    private static int solidWidth = 24;
    private static int solidHeight = 40;
    private static int baseRange = 150;
    private int level = 1;

    public Pharaoh(GamePanel gamePanel, int x, int y) {
        super(gamePanel, x, y, "Pharaoh");
        super.setRange(baseRange);
        super.setDamage(200);
        super.setAttackSpeed(250);
        super.setSellCost(75);
        super.setAttackType("AOE");
        super.setIsGhostBuster(false);
        super.setCanCreateParticle(false);
        super.setCanCreateMoney(false);
        super.setPlacedSolidArea(new Rectangle(x - solidWidth / 2, y - solidHeight / 2, solidWidth, solidHeight));
    }

    int[] costs = {0, 100, 180};
    int[] damages = {50, 80, 100};
    int[] range = {150, 160, 170};
    int[] attackSpeed = {150, 125, 100};

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

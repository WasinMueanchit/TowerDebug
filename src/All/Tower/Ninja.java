package All.Tower;

import All.GamePanel;
import java.awt.Rectangle;

public class Ninja extends Tower {

    private static int solidWidth = 24;
    private static int solidHeight = 40;
    private static int baseRange = 300;

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
        super.setReward(0);
        super.setPlacedSolidArea(new Rectangle(x - solidWidth / 2, y - solidHeight / 2, solidWidth, solidHeight));
    }

    @Override
    public void levelUp() {
        super.setRange(super.getRange() + 10);
        super.setAttackSpeed(super.getAttackSpeed() - 2);
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

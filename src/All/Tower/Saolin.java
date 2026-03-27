package All.Tower;

import All.GamePanel;
import java.awt.Rectangle;

public class Saolin extends Tower {

    private static int solidWidth = 24;
    private static int solidHeight = 40;
    private static int baseRange = 200;

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

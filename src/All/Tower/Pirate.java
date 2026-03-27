package All.Tower;

import All.GamePanel;
import java.awt.Rectangle;

public class Pirate extends Tower {

    private static int solidWidth = 24;
    private static int solidHeight = 40;
    private static int baseRange = 0;

    public Pirate(GamePanel gamePanel, int x, int y) {
        super(gamePanel, x, y, "Pirate");
        super.setRange(baseRange);
        super.setDamage(0);
        super.setAttackSpeed(0);
        super.setSellCost(50);
        super.setAttackType("Single");
        super.setIsGhostBuster(false);
        super.setCanCreateParticle(false);
        super.setCanCreateMoney(true, 30);
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

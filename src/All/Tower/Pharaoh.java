package All.Tower;

import Game.GamePanel;
import java.awt.Rectangle;

public class Pharaoh extends Tower {

    private static int solidWidth = 24;
    private static int solidHeight = 40;
    private static int baseRange = 150;

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

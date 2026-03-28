package Game;


import All.Tower.Tower;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

public class UpgradeUI {

    private Tower character;
    private String name;
    private double damage;
    private double attackSpeed;
    private int range;
    private RoundRectangle2D upgradeUI;
    private RoundRectangle2D upgradeButton;
    private RoundRectangle2D sellButton;

    public UpgradeUI() {
        upgradeUI = new RoundRectangle2D.Double(-20, 150, 200, 200, 30, 30);
        upgradeButton = new RoundRectangle2D.Double(10, 300, 80, 25, 8, 8);
        sellButton = new RoundRectangle2D.Double(100, 300, 60, 25, 8, 8);
    }

    public void draw(Graphics2D g2) {

        name = character.getName();
        damage = character.getDamage();
        attackSpeed = character.getAttackSpeed() / 60;
        range = character.getRange();

        // Fill color
        g2.setColor(new Color(92, 64, 51));
        g2.fill(upgradeUI);

        // Draw border
        g2.setColor(new Color(18, 10, 7));
        g2.setStroke(new BasicStroke(5));
        g2.draw(upgradeUI);

        // Draw text
        g2.setColor(Color.white);
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.drawString(name, 10, 195);
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        g2.drawString("Damage: " + damage, 10, 225);
        g2.drawString("Attack Speed: " + String.format("%.2f", attackSpeed) + "/s", 10, 250);
        g2.drawString("Range: " + range, 10, 275);

        // Fill button
        g2.setColor(new Color(164, 114, 91));
        g2.fill(upgradeButton);
        g2.fill(sellButton);

        g2.setColor(Color.white);
        g2.drawString("Upgrade", 23, 317);
        g2.drawString("Sell", 118, 317);
        
        //Draw border button
        g2.setColor(new Color(46, 32, 25));
        g2.setStroke(new BasicStroke(4));
        g2.draw(upgradeButton);
        g2.draw(sellButton);
    }

    public void setCharacter(Tower character) {
        this.character = character;
    }

    public RoundRectangle2D getUpgradeUI() {
        return upgradeUI;
    }

    public RoundRectangle2D getUpgradeButton() {
        return upgradeButton;
    }
    
    public RoundRectangle2D getSellButton(){
        return sellButton;
    }
}

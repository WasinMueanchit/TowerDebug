package Game;

import All.Tower.Gladiator;
import All.Tower.Ninja;
import All.Tower.Pharaoh;
import All.Tower.Pirate;
import All.Tower.Saolin;
import All.Tower.Valkyrie;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class CharacterBox {

    private GamePanel gamePanel;
    private int x;
    private int y;
    private int width;
    private int height;
    private String name;
    private BufferedImage image;
    private RoundRectangle2D area;

    public CharacterBox(GamePanel gamePanel, int x, int y, int width, int height, BufferedImage image, String name) {
        this.gamePanel = gamePanel;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
        this.name = name;
        this.area = new RoundRectangle2D.Double(x, y, width, height, 30, 30);
    }

    public void drawCharacterBox(Graphics2D g2) {
        g2.setColor(new Color(18, 10, 7));
        g2.fillRoundRect(x - 4, y - 4, width + 8, height + 8, 36, 36);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
        g2.setColor(new Color(100, 149, 237));
        g2.fill(area);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        g2.drawImage(image, x, y, width, height, null);
        int cost = 0;
        switch (name) {
            case "Gladiator":
                cost = Gladiator.getCost();
                break;
            case "Ninja":
                cost = Ninja.getCost();
                break;
            case "Pharaoh":
                cost = Pharaoh.getCost();
                break;
            case "Pirate":
                cost = Pirate.getCost();
                break;
            case "Saolin":
                cost = Saolin.getCost();
                break;
            case "Valkyrie":
                cost = Valkyrie.getCost();
                break;
        }
        g2.setColor(Color.white);
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.drawString(cost + "", (x + width / 2) - 8, y + height + 10);
    }

    public RoundRectangle2D getArea() {
        return area;
    }

    public String getName() {
        return name;
    }
}

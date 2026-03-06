
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Assasin {

    private GamePanel gamePanel;
    private int x;
    private int y;

    //Animation
    private BufferedImage[] up, down, left, right, idle;
    private String state = "Attacking";
    private String direction = "down";
    private int frame = 0;
    private int animationCounter = 0;
    private int animationSpeed = 3;

    public Assasin(GamePanel gamePanel, int x, int y) {
        this.gamePanel = gamePanel;
        this.x = x;
        this.y = y;
        BufferedImage[][] assasinAnimation = gamePanel.getAssasinAnimation();
        up = assasinAnimation[0];
        down = assasinAnimation[1];
        left = assasinAnimation[2];
        right = assasinAnimation[3];
        idle = assasinAnimation[4];
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        if (state.equals("Attacking")) {
            switch (direction) {
                case "up":
                    image = up[frame];
                    break;
                case "down":
                    image = down[frame];
                    break;
                case "left":
                    image = left[frame];
                    break;
                case "right":
                    image = right[frame];
                    break;
            }
        } else {
            image = idle[frame];
        }

        int tileSize = gamePanel.getTileSize();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
        g2.fillOval(x - tileSize / 6, y + tileSize / 5, tileSize / 3, tileSize / 4);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        g2.drawImage(image, x - tileSize / 2, y - tileSize / 2, tileSize, tileSize, null);

        animationCounter++;
        if (animationCounter >= animationSpeed) {
            frame++;
            animationCounter = 0;
        }
        frame %= up.length;
    }
}


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Particle {

    private GamePanel gamePanel;
    private int x;
    private int y;
    private int animationSpeed;
    private int animationCounter = 0;
    private int frame = 0;
    private boolean isAlive = true;
    private ArrayList<BufferedImage> animation;

    public Particle(int x, int y, int animationSpeed,String type) {
        this.x = x;
        this.y = y;
        this.animationSpeed = animationSpeed;
        animation = LoadAnimation.getAnimation("Particle").get(type);
    }

    public void update() {
        if (frame >= animation.size()) {
            isAlive = false;
            return;
        }
    }

    public void draw(Graphics2D g2) {
        if (frame >= animation.size()) {
            return;
        }
        BufferedImage image = animation.get(frame);
        int width = image.getWidth() / 4;
        int height = image.getHeight() / 4;
        g2.drawImage(image, x - (width / 2) + 30, y - (height / 2), width, height, null);
        animationCounter++;
        if(animationCounter >= animationSpeed){
            frame++;
            animationCounter = 0;
        }
    }

    public boolean getIsAlive() {
        return isAlive;
    }
}

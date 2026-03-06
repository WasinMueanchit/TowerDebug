
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class EnemyRun{
    private int speed;
    private int x;
    private int y;
    private GamePanel gamePanel;
    
    //Animation
    private BufferedImage[] up = new BufferedImage[12], down = new BufferedImage[12], left = new BufferedImage[12], right = new BufferedImage[12];
    private String direction = "down";
    private int frame = 0;
    
    //Coordinates for entity
    private Point[] waypoints;
    private int currentTarget = 0;
 
    public EnemyRun(GamePanel gamePanel, Point[] waypoints, int speed){
        this.gamePanel = gamePanel;
        this.waypoints = waypoints;
        this.speed = speed;
        this.x = waypoints[0].getX();
        this.y = waypoints[0].getY() - 20;
        getEnemyRun();
    }
    
    // Import Animation Images
    public void getEnemyRun(){
        try{
            for (int i=0; i < 12; i++){
                up[i] = ImageIO.read(getClass().getResourceAsStream("/source/Female Goblin/Back - Running/" + i + ".png"));
                down[i] = ImageIO.read(getClass().getResourceAsStream("/source/Female Goblin/Front - Running/" + i + ".png"));
                left[i] = ImageIO.read(getClass().getResourceAsStream("/source/Female Goblin/Left - Running/" + i + ".png"));
                right[i] = ImageIO.read(getClass().getResourceAsStream("/source/Female Goblin/Right - Running/" + i + ".png"));
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void update(){
        Point target = waypoints[currentTarget];
        int targetX = target.getX();
        int targetY = target.getY() - 20;
        if (x != targetX){
            if (x < targetX){
                x = Math.min(x + speed, targetX);
                direction = "right";
            }else{
                x = Math.max(x - speed, targetX);
                direction = "left";
            }
        }else if (y != targetY){
            if (y < targetY){
                y = Math.min(y + speed, targetY);
                direction = "down";
            }else{
                y = Math.max(y - speed, targetY);
                direction = "up";
            }
        }
         else{
            if (currentTarget < waypoints.length - 1) {
                    currentTarget++;
            }
        }
    }
    public void draw(Graphics2D g2){
        BufferedImage image = null;
        switch(direction){
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
//        g2.setColor(Color.white);
//        g2.fillRect(x, y, gamePanel.getTileSize(), gamePanel.getTileSize());
        g2.drawImage(image, x, y, gamePanel.getTileSize(), gamePanel.getTileSize(), null);
        frame %= 11;
        frame++;
    }
}

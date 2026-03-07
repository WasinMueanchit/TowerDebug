
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

public class Pointer {

    private GamePanel gamePanel;
    private int x = 0;
    private int y = 0;
    private String characterSelected = "Assasin";
    private boolean collisionOn = false;
    private Rectangle solidArea;
    private CollisionChecker collisionChecker;

    public Pointer(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void drawCharacterSelected(Graphics2D g2) {
        if (!characterSelected.equals("")) {
            int tileSize = gamePanel.getTileSize();
            int baseRange = 0, solidWidth = 0, solidHeight = 0;
            BufferedImage image = null;
            switch (characterSelected) {
                case "Assasin":
                    baseRange = Assasin.getBaseRange();
                    solidWidth = Assasin.getSolidWidth();
                    solidHeight = Assasin.getSolidHeight();
                    image = gamePanel.getAssasinAnimation()[4][0];
                    break;
            }
            solidArea = new Rectangle(-solidWidth/2, -solidHeight/2, solidWidth ,solidHeight);
            gamePanel.getCollisionChecker().checkTile(this);
            
            //Draw Range
            Ellipse2D attackArea = new Ellipse2D.Double(x-baseRange/2, y-baseRange/2, baseRange, baseRange);
            //Draw Color Background
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            if(collisionOn == false){
                g2.setColor(new Color(59, 188, 122));
                g2.fill(attackArea);
            }else{
                g2.setColor(new Color(206, 67, 98));
                g2.fill(attackArea);
            }
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            //Draw Animation & Image
            g2.drawImage(image, x-tileSize/2, y-tileSize/ 2, tileSize, tileSize, null);
        }
    }
    
    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }
    
    public String getCharacterSelected(){
        return characterSelected;
    }
    
    public Rectangle getSolidArea(){
        return solidArea;
    }

    public boolean getCollisionOn(){
        return collisionOn;
    }
    
    public void setCollisionOn(boolean collisionOn){
        this.collisionOn = collisionOn;
    }
}

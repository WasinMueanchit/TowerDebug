package Game;


import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GameEnd {
    private GamePanel gamePanel;
    private boolean isFinishing = false;
    private boolean isWin = false;
    private BufferedImage winBorder;
    private BufferedImage loseBorder;
    private Rectangle returnToMenuButton = new Rectangle(365, 268, 50, 50);
    private Rectangle nextLevelOrRetryButton = new Rectangle(515, 268, 50, 50);
    
    public GameEnd(GamePanel gamePanel){
        this.gamePanel = gamePanel;
        try{
            winBorder = ImageIO.read(getClass().getResourceAsStream("/source/UI/GameEnd/win.png"));
            loseBorder = ImageIO.read(getClass().getResourceAsStream("/source/UI/GameEnd/lose.png"));
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
    
    public void drawGameEndUI(Graphics2D g2){
        //Fade Background
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        g2.setColor(Color.black);
        g2.fillRect(0, 0, gamePanel.getWidth(), gamePanel.getHeight());
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        
        //Text & Button
        g2.setColor(Color.white);
        if(isWin == true){
            g2.drawImage(winBorder, 260, 160, 400, 220, null);
        }else{
            g2.drawImage(loseBorder, 260, 160, 400, 220, null);
        }
//        g2.fill(returnToMenuButton);
//        g2.fill(nextLevelButton);
    }
    
    public void setIsWin(boolean IsWin){
        this.isWin = IsWin;
    }
    
    public void setIsFinishing(boolean isFinishing){
        this.isFinishing = isFinishing;
    }
    
    public boolean getIsFinishing(){
        return isFinishing;
    }
    
    public Rectangle getReturnToMenuButton(){
        return returnToMenuButton;
    }
    
    public Rectangle getNextLevelOrRetryButton(){
        return nextLevelOrRetryButton;
    }
    
    public boolean getIsWin(){
        return isWin;
    }
}

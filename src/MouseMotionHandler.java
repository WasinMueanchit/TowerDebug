import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.*;

public class MouseMotionHandler implements MouseMotionListener{
    GamePanel gamePanel;
    
    public MouseMotionHandler(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }
   
    //Get coordinates of mouse
    @Override
    public void mouseMoved(MouseEvent e) {
        gamePanel.setMouseX(e.getX());
        gamePanel.setMouseY(e.getY());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }
}

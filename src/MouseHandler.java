import java.awt.event.*;

public class MouseHandler implements MouseMotionListener{
    GamePanel gamePanel;
    
    public MouseHandler(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }
   
    @Override
    public void mouseMoved(MouseEvent e) {
        gamePanel.setMouseX(e.getX());
        gamePanel.setMouseY(e.getY());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        
    }
}

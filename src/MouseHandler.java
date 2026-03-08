
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class MouseHandler implements MouseListener{
    private GamePanel gamePanel;
    private Pointer pointer;
    
    public MouseHandler(GamePanel gamePanel){
        this.gamePanel = gamePanel;
        this.pointer = gamePanel.getPointer();
    }

    //Click to put tower
    @Override
    public void mousePressed(MouseEvent e) {
        if (pointer.getCollisionOn() == false){            
            String character = pointer.getCharacterSelected();
            ArrayList<Assasin> allTower = gamePanel.getAllTower();
            int towerAmount = gamePanel.getTowerAmount();
            switch(character){
                case "Assasin":
                    allTower.add(new Assasin(gamePanel, pointer.getX(), pointer.getY(), 20, 20, 150, "Single"));
                    break;
            }
            gamePanel.setTowerAmount(towerAmount + 1);
        }
    }
    
    //Don't use
    @Override
    public void mouseClicked(MouseEvent e) {
    }
    @Override
    public void mouseReleased(MouseEvent e) {
    }
    @Override
    public void mouseEntered(MouseEvent e) {  
    }
    @Override
    public void mouseExited(MouseEvent e) {
    }
}

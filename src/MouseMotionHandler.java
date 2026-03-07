
import java.awt.event.*;

public class MouseMotionHandler implements MouseMotionListener{
    Pointer pointer;
    
    public MouseMotionHandler(Pointer pointer){
        this.pointer = pointer;
    }
   
    //Get coordinates of mouse
    @Override
    public void mouseMoved(MouseEvent e) {
        pointer.setX(e.getX());
        pointer.setY(e.getY());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }
}

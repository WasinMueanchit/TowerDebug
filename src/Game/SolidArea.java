package Game;


import java.awt.Rectangle;


public class SolidArea {
    private Rectangle solidArea;
    public SolidArea(int x, int y, int width, int height){
        this.solidArea = new Rectangle(x, y, width, height);
    }
    public SolidArea(Rectangle area){
        this.solidArea = area;
    }
    public Rectangle getSolidArea(){
        return solidArea;
    }
}

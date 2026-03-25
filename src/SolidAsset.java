
import java.awt.Rectangle;


public class SolidAsset {
    private Rectangle solidArea;
    public SolidAsset(int x, int y, int width, int height){
        this.solidArea = new Rectangle(x, y, width, height);
    }
    public Rectangle getSolidArea(){
        return solidArea;
    }
}

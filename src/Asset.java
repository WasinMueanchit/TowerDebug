
import java.awt.image.BufferedImage;

public class Asset {
    private BufferedImage image;
    private int width;
    private int height;
    
    public Asset(BufferedImage image, int width, int height){
        this.image = image;
        this.width = width;
        this.height = height;
    }
    
    //Setter & Getter
    public BufferedImage getImage() {
        return image;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    
}

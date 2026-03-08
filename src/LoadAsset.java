
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class LoadAsset {
    private GamePanel gamePanel;
    //UI Asset
    private BufferedImage heartImage;
    //Asset
    private HashMap<String, Asset> allAssetLevel1;

    public LoadAsset(GamePanel gamePanel){
        this.gamePanel = gamePanel;
        allAssetLevel1 = new HashMap<String, Asset>();
        loadAssetLevel1();
        loadAnotherAsset();
    }
    
    //Load Asset
    public void loadAnotherAsset(){
        try{
            heartImage = ImageIO.read(getClass().getResourceAsStream("/source/UI/Heart.png"));
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public void loadAssetLevel1(){
        File folder = new File("src/source/Assets/level1");
        File[] files = folder.listFiles();
        if (files != null){
            for (File file : files){
                try{
                    String fileName = file.getName();
                    BufferedImage image = ImageIO.read(file);
                    int width = (int) (image.getWidth() * gamePanel.getScale());
                    int height = (int) (image.getHeight() * gamePanel.getScale());
                    Asset asset = new Asset(image, width, height);
                    allAssetLevel1.put(fileName, asset);
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
    
    //Setter & Getter
    public HashMap<String, Asset> getAllAssetLevel1(){
        return allAssetLevel1;
    }
    
    public BufferedImage getHeartImage(){
        return heartImage;
    }
}

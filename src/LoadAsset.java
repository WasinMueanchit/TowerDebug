
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class LoadAsset {

    private GamePanel gamePanel;
    //UI Asset
    private BufferedImage heartImage;
    private BufferedImage coinImage;
    //Asset
    private HashMap<String, Asset> allAssetLevel1;
    private HashMap<String, Asset> allAssetLevel2;
    private HashMap<String, Asset> allAssetLevel3;
    private HashMap<String, Asset> allAssetLevel4;

    public LoadAsset(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        allAssetLevel1 = new HashMap<String, Asset>();
        allAssetLevel2 = new HashMap<String, Asset>();
        allAssetLevel3 = new HashMap<String, Asset>();
        allAssetLevel4 = new HashMap<String, Asset>();
        loadAssetLevel1();
        loadAssetLevel2();
        loadAssetLevel3();
        loadAssetLevel4();
        loadAnotherAsset();
    }

    //Load Asset
    public void loadAnotherAsset() {
        try {
            heartImage = ImageIO.read(getClass().getResourceAsStream("/source/UI/Heart.png"));
            coinImage = ImageIO.read(getClass().getResourceAsStream("/source/UI/Coin.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadAssetLevel1() {
        File folder = new File("src/source/Assets/level1");
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                try {
                    String fileName = file.getName();
                    BufferedImage image = ImageIO.read(file);
                    int width = (int) (image.getWidth() * gamePanel.getScale());
                    int height = (int) (image.getHeight() * gamePanel.getScale());
                    Asset asset = new Asset(image, width, height);
                    allAssetLevel1.put(fileName, asset);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void loadAssetLevel2() {
        File folder = new File("src/source/Assets/level2");
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                try {
                    String fileName = file.getName();
                    BufferedImage image = ImageIO.read(file);
                    int width = (int) (image.getWidth() * gamePanel.getScale());
                    int height = (int) (image.getHeight() * gamePanel.getScale());
                    Asset asset = new Asset(image, width, height);
                    allAssetLevel2.put(fileName, asset);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void loadAssetLevel3() {
        File folder = new File("src/source/Assets/level3");
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                try {
                    String fileName = file.getName();
                    BufferedImage image = ImageIO.read(file);
                    int width = (int) (image.getWidth() * gamePanel.getScale());
                    int height = (int) (image.getHeight() * gamePanel.getScale());
                    Asset asset = new Asset(image, width, height);
                    allAssetLevel3.put(fileName, asset);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void loadAssetLevel4() {
        File folder = new File("src/source/Assets/level4");
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                try {
                    String fileName = file.getName();
                    BufferedImage image = ImageIO.read(file);
                    int width = (int) (image.getWidth() * gamePanel.getScale());
                    int height = (int) (image.getHeight() * gamePanel.getScale());
                    Asset asset = new Asset(image, width, height);
                    allAssetLevel4.put(fileName, asset);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //Setter & Getter
    public HashMap<String, Asset> getAllAssetLevel1() {
        return allAssetLevel1;
    }

    public HashMap<String, Asset> getAllAssetLevel2() {
        return allAssetLevel2;
    }

    public HashMap<String, Asset> getAllAssetLevel3() {
        return allAssetLevel3;
    }

    public HashMap<String, Asset> getAllAssetLevel4() {
        return allAssetLevel4;
    }

    public BufferedImage getHeartImage() {
        return heartImage;
    }
    
    public BufferedImage getCoinImage() {
        return coinImage;
    }
}

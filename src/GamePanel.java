
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class GamePanel extends JPanel implements Runnable {

    //Initialize
    public final int originalTileSize = 128;
    public final double scale = 0.5;
    public final int tileSize = (int) (originalTileSize * scale);
    public final int maxScreenCol = 15;
    public final int maxScreenRow = 8;
    public final int screenWidth = tileSize * maxScreenCol; //Display : width 960px
    public final int screenHeight = tileSize * maxScreenRow; //Display : height 512px
    private Thread gameThread;

    //Mouse
    private int mouseX = 0;
    private int mouseY = 0;
    private String characterSelected = "Assasin";

    //Game Setting
    private int level = 4;
    private Point[] waypointsLevel1 = {new Point(0, tileSize * 3), new Point(tileSize * 3, tileSize * 5), new Point(tileSize * 6, tileSize * 1), new Point(tileSize * 9, tileSize * 6), new Point(tileSize * 12, tileSize * 3), new Point(tileSize * 15, tileSize * 3)};
    private Point[] waypointsLevel2 = {new Point(0, tileSize * 3), new Point(tileSize * 2, tileSize * 5), new Point(tileSize * 4, tileSize * 5), new Point(tileSize * 4, tileSize * 1), new Point(tileSize * 6, tileSize * 1), new Point(tileSize * 6, tileSize * 6), new Point(tileSize * 8, tileSize * 6), new Point(tileSize * 8, tileSize * 1), new Point(tileSize * 10, tileSize * 1), new Point(tileSize * 10, tileSize * 6), new Point(tileSize * 12, tileSize * 6), new Point(tileSize * 12, tileSize * 3), new Point(tileSize * 14, tileSize * 3)};
    private Point[] waypointsLevel3 = {new Point(0, 0), new Point(0, tileSize * 3), new Point(tileSize * 4, tileSize * 3), new Point(tileSize * 4, tileSize * 5), new Point(tileSize * 1, tileSize * 5), new Point(tileSize * 1, tileSize * 7), new Point(tileSize * 7, tileSize * 7), new Point(tileSize * 7, tileSize * 2), new Point(tileSize * 5, tileSize * 2), new Point(tileSize * 5, 0), new Point(tileSize * 11, 0), new Point(tileSize * 11, tileSize * 2), new Point(tileSize * 9, tileSize * 5), new Point(tileSize * 13, tileSize * 5), new Point(tileSize * 13, tileSize * 6), new Point(tileSize * 15, tileSize * 6)};
    private Point[] waypointsLevel4 = {new Point(0, tileSize * 4), new Point(tileSize * 2, tileSize * 4), new Point(tileSize * 2, tileSize * 1), new Point(tileSize * 13, tileSize * 1), new Point(tileSize * 13, tileSize * 3), new Point(tileSize * 4, tileSize * 5), new Point(tileSize * 3, tileSize * 6), new Point(tileSize * 6, tileSize * 5), new Point(tileSize * 8, tileSize * 6), new Point(tileSize * 10, tileSize * 5), new Point(tileSize * 14, tileSize * 5)};
    private final int FPS = 60;

    //Event Handler
    private MouseMotionHandler mouseMotionHandler;
    private MouseHandler mouseHandler;

    //Animation
    private BufferedImage[][] assasinAnimation = new BufferedImage[5][10];
    private BufferedImage[][] femaleGoblinAnimation = new BufferedImage[4][12];

    //Monster
    private ArrayList<FemaleGoblin> allEnemy = new ArrayList<>();
    private int enemyAmount = 0;
    private int spawnCounter = 0;
    private int maxEnemy = 10;

    //Tower
    private ArrayList<Assasin> allTower = new ArrayList<>();
    private int towerAmount = 0;

    //Tile
    private TileManager tileManager = new TileManager(this);

    public GamePanel() {

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        
        loadImages();
        
        gameThread = new Thread(this);
        gameThread.start();

        mouseMotionHandler = new MouseMotionHandler(this);
        mouseHandler = new MouseHandler(this);
        this.addMouseMotionListener(mouseMotionHandler);
        this.addMouseListener(mouseHandler);
        this.setFocusable(true);

    }

    @Override
    public void run() {
        while (gameThread != null) {
            spawnEnemy();
            update();
            repaint();
            try {
                Thread.sleep(1000 / FPS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        tileManager.draw(g2);
        for (FemaleGoblin e : allEnemy){
            e.draw(g2);
        }
        for (Assasin t : allTower) {
            t.draw(g2);
        }
        drawCharacterSelected(g2);
//        g2.dispose();
    }
    
    public void update() {
        for(FemaleGoblin e : allEnemy){
            e.update();
        }
    }

    public void loadImages() {
        try {
            //Import Assasin images to "assasinAnimation"
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 10; j++){
                    assasinAnimation[i][j] = ImageIO.read(getClass().getResourceAsStream("/source/Assasin/" + i + "/" + j + ".png"));
                }
            }
            //Import Female Goblin images to "femaleGoblinAnimation"
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 12; j++){
                    femaleGoblinAnimation[i][j] = ImageIO.read(getClass().getResourceAsStream("/source/Female Goblin/" + i + "/" + j + ".png"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void spawnEnemy(){
        spawnCounter++;
        if(spawnCounter == FPS && enemyAmount < maxEnemy){
            allEnemy.add(new FemaleGoblin(this, waypointsLevel4, 2));
            enemyAmount += 1;
            spawnCounter = 0;
        }
    }
    
    public void drawCharacterSelected(Graphics2D g2){
        if (!characterSelected.equals("")){
            BufferedImage image = null;
            switch(characterSelected){
                case "Assasin":
                    image = assasinAnimation[4][0];
                    break;
            }
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            g2.drawImage(image, mouseX - tileSize / 2, mouseY - tileSize / 2, tileSize, tileSize, null);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }
    }
    
    
    //------------------------- SETTER & GETTER -------------------------
    public void setMouseX(int x) {
        this.mouseX = x;
    }

    public int getMouseX() {
        return mouseX;
    }

    public void setMouseY(int y) {
        this.mouseY = y;
    }

    public int getMouseY() {
        return mouseY;
    }

    public int getTileSize() {
        return tileSize;
    }

    public int getLevel() {
        return level;
    }

    public int getMaxScreenCol() {
        return maxScreenCol;
    }

    public int getMaxScreenRow() {
        return maxScreenRow;
    }

    public int getTowerAmount() {
        return towerAmount;
    }

    public void setTowerAmount(int towerAmount) {
        this.towerAmount = towerAmount;
    }
    
    public String getCharacterSelected(){
        return characterSelected;
    }

    public ArrayList<Assasin> getAllTower() {
        return allTower;
    }

    public BufferedImage[][] getAssasinAnimation(){
        return assasinAnimation;
    }
    
    public BufferedImage[][] getFemaleGobinAnimation(){
        return femaleGoblinAnimation;
    }
    
}

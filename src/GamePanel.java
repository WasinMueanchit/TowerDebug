
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
    private Pointer pointer;
    private CollisionChecker collisionChecker;

    //Game Setting
    private int level = 4;
    private Point[] waypointsLevel1 = {new Point(0, tileSize * 3), new Point(tileSize * 3, tileSize * 5), new Point(tileSize * 6, tileSize * 1), new Point(tileSize * 9, tileSize * 6), new Point(tileSize * 12, tileSize * 3), new Point(tileSize * 15, tileSize * 3)};
    private Point[] waypointsLevel2 = {new Point(0, tileSize * 3), new Point(tileSize * 2, tileSize * 5), new Point(tileSize * 4, tileSize * 5), new Point(tileSize * 4, tileSize * 1), new Point(tileSize * 6, tileSize * 1), new Point(tileSize * 6, tileSize * 6), new Point(tileSize * 8, tileSize * 6), new Point(tileSize * 8, tileSize * 1), new Point(tileSize * 10, tileSize * 1), new Point(tileSize * 10, tileSize * 6), new Point(tileSize * 12, tileSize * 6), new Point(tileSize * 12, tileSize * 3), new Point(tileSize * 14, tileSize * 3)};
    private Point[] waypointsLevel3 = {new Point(0, 0), new Point(0, tileSize * 3), new Point(tileSize * 4, tileSize * 3), new Point(tileSize * 4, tileSize * 5), new Point(tileSize * 1, tileSize * 5), new Point(tileSize * 1, tileSize * 7), new Point(tileSize * 7, tileSize * 7), new Point(tileSize * 7, tileSize * 2), new Point(tileSize * 5, tileSize * 2), new Point(tileSize * 5, 0), new Point(tileSize * 11, 0), new Point(tileSize * 11, tileSize * 2), new Point(tileSize * 9, tileSize * 5), new Point(tileSize * 13, tileSize * 5), new Point(tileSize * 13, tileSize * 6), new Point(tileSize * 15, tileSize * 6)};
    private Point[] waypointsLevel4 = {new Point(0, tileSize * 4), new Point(tileSize * 2, tileSize * 4), new Point(tileSize * 2, tileSize * 1), new Point(tileSize * 13, tileSize * 1), new Point(tileSize * 13, tileSize * 3), new Point(tileSize * 4, tileSize * 5), new Point(tileSize * 3, tileSize * 6), new Point(tileSize * 6, tileSize * 5), new Point(tileSize * 8, tileSize * 6), new Point(tileSize * 10, tileSize * 5), new Point(tileSize * 14, tileSize * 5)};
    private final int FPS = 60;
    private double maxBaseHP = 1000;
    private double baseHP = 1000;
    private BufferedImage heartImage;

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

        pointer = new Pointer(this);
        collisionChecker = new CollisionChecker(this);
        mouseMotionHandler = new MouseMotionHandler(pointer);
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
        for (Assasin t : allTower) {
            t.draw(g2);
        }
        for (FemaleGoblin e : allEnemy){
            e.draw(g2);
        }
        pointer.drawCharacterSelected(g2);
//        g2.dispose();
        drawBaseHP(g2);
    }
    
    public void update() {
        for(int i = allEnemy.size()-1; i >= 0; i--){
            FemaleGoblin enemy = allEnemy.get(i);
            enemy.update();
            if(!enemy.getAlive()){
                allEnemy.remove(i);
            }
        }
        for(int i = allTower.size()-1; i >= 0; i--){
            Assasin tower = allTower.get(i);
            tower.update();
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
            heartImage = ImageIO.read(getClass().getResourceAsStream("/source/UI/Heart.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void spawnEnemy(){
        spawnCounter++;
        if(spawnCounter == FPS && enemyAmount < maxEnemy){
            allEnemy.add(new FemaleGoblin(this, waypointsLevel4, 1, 100, 100));
            enemyAmount += 1;
            spawnCounter = 0;
        }
    }

    public void drawBaseHP(Graphics2D g2){
        double oneScale = (double) 200/maxBaseHP;
        double baseHPBarValue = oneScale*baseHP;
        g2.setColor(new Color(18, 10, 7));
        g2.fillRoundRect(35, screenHeight - 50, 208, 30, 24, 24);
        g2.setColor(new Color(206, 67, 98));
        g2.fillRoundRect(39, screenHeight - 46, (int)baseHPBarValue, 22, 20, 20);
        g2.setColor(Color.white);
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.drawString((int) baseHP + "", 60,screenHeight - 29);
        g2.drawImage(heartImage, 20, screenHeight - 50, 32, 32, null);
    }
    
    //------------------------- SETTER & GETTER -------------------------

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

    public ArrayList<Assasin> getAllTower() {
        return allTower;
    }

    public BufferedImage[][] getAssasinAnimation(){
        return assasinAnimation;
    }
    
    public BufferedImage[][] getFemaleGobinAnimation(){
        return femaleGoblinAnimation;
    }
    
    public Pointer getPointer(){
        return pointer;
    }
    
    public TileManager getTileManager(){
        return tileManager;
    }
    
    public CollisionChecker getCollisionChecker(){
        return collisionChecker;
    }
    
    public double getBaseHP(){
        return baseHP;
    }
    
    public void setBaseHP(double baseHP){
        this.baseHP = baseHP;
    }
    
    public ArrayList<FemaleGoblin> getAllEnemy(){
        return allEnemy;
    }
}

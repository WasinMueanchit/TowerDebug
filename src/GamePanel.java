
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

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
    private int level = 3;
    private ArrayList<Point[]> allWaypoint = new ArrayList<>();
    private Point[] waypointsLevel1 = {new Point(0, tileSize * 3), new Point(tileSize * 3, tileSize * 5), new Point(tileSize * 6, tileSize * 1), new Point(tileSize * 9, tileSize * 6), new Point(tileSize * 12, tileSize * 3), new Point(tileSize * 15, tileSize * 3)};
    private Point[] waypointsLevel2 = {new Point(0, tileSize * 3), new Point(tileSize * 2, tileSize * 5), new Point(tileSize * 4, tileSize * 5), new Point(tileSize * 4, tileSize * 1), new Point(tileSize * 6, tileSize * 1), new Point(tileSize * 6, tileSize * 6), new Point(tileSize * 8, tileSize * 6), new Point(tileSize * 8, tileSize * 1), new Point(tileSize * 10, tileSize * 1), new Point(tileSize * 10, tileSize * 6), new Point(tileSize * 12, tileSize * 6), new Point(tileSize * 12, tileSize * 3), new Point(tileSize * 14, tileSize * 3)};
    private Point[] waypointsLevel3 = {new Point(0, 0), new Point(0, tileSize * 3), new Point(tileSize * 4, tileSize * 3), new Point(tileSize * 4, tileSize * 5), new Point(tileSize * 1, tileSize * 5), new Point(tileSize * 1, tileSize * 7), new Point(tileSize * 7, tileSize * 7), new Point(tileSize * 7, tileSize * 2), new Point(tileSize * 5, tileSize * 2), new Point(tileSize * 5, 0), new Point(tileSize * 11, 0), new Point(tileSize * 11, tileSize * 2), new Point(tileSize * 9, tileSize * 5), new Point(tileSize * 13, tileSize * 5), new Point(tileSize * 13, tileSize * 6), new Point(tileSize * 15, tileSize * 6)};
    private Point[] waypointsLevel4 = {new Point(0, tileSize * 4), new Point(tileSize * 2, tileSize * 4), new Point(tileSize * 2, tileSize * 1), new Point(tileSize * 13, tileSize * 1), new Point(tileSize * 13, tileSize * 3), new Point(tileSize * 4, tileSize * 5), new Point(tileSize * 3, tileSize * 6), new Point(tileSize * 6, tileSize * 5), new Point(tileSize * 8, tileSize * 6), new Point(tileSize * 10, tileSize * 5), new Point(tileSize * 14, tileSize * 5)};
    private final int FPS = 60;
    private double maxBaseHP = 1000;
    private double baseHP = 1000;

    //Monster
    private ArrayList<FemaleGoblin> allEnemy = new ArrayList<>();
    private int spawnCounter = 0;
    private WaveManager waveManager;
    private int waveCooldown = 0;
    //LLM
    private int currentWave = 0;
    ArrayList<String> spawnQueue = new ArrayList<>();

    //Tower
    private ArrayList<Assasin> allTower = new ArrayList<>();
    private int towerAmount = 0;

    //Tile
    private TileManager tileManager = new TileManager(this);

    //Animation
    private LoadAnimation loadAnimation;

    //Assets
    private LoadAsset loadAsset;

    //UI
    private String[] allCharacterSelected = {"Assasin", "Assasin", "", "", ""};
    private CharacterBox[] allCharacterBox;
    private GameEnd gameEnd;

    public GamePanel() {

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        loadAsset = new LoadAsset(this);
        loadAnimation = new LoadAnimation();
        waveManager = new WaveManager();
        gameEnd = new GameEnd(this);

        allWaypoint.add(waypointsLevel1);
        allWaypoint.add(waypointsLevel2);
        allWaypoint.add(waypointsLevel3);
        allWaypoint.add(waypointsLevel4);
        
        prepareWave();

        collisionChecker = new CollisionChecker(this);
        pointer = new Pointer(this, gameEnd);
        allCharacterBox = new CharacterBox[5];
        for (int i = 0; i < allCharacterSelected.length; i++) {
            String name = allCharacterSelected[i];
            BufferedImage image;
            switch (name) {
                case "Assasin":
                    image = loadAnimation.getAssasinAnimation()[4][0];
                    allCharacterBox[i] = new CharacterBox(this, tileSize * (5 + i), tileSize * 7, 50, 50, image, name);
                    break;
            }
        }
        this.addMouseMotionListener(pointer);
        this.addMouseListener(pointer);
        this.addKeyListener(pointer);
        this.setFocusable(true);

        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        while (gameThread != null) {
            spawnEnemy();
            checkWave();
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
        switch (level) {
            case 1:
                drawAssetLevel1(g2);
                break;
            case 2:
                drawAssetLevel2(g2);
                break;
            case 3:
                drawAssetLevel3(g2);
                break;
            case 4:
                drawAssetLevel4(g2);
                break;
        }
        for (Assasin t : allTower) {
            t.draw(g2);
        }
        for (FemaleGoblin e : allEnemy) {
            e.draw(g2);
        }
        pointer.draw(g2);
        for (CharacterBox c : allCharacterBox) {
            if (c != null) {
                c.drawCharacterBox(g2);
            }
        }
        drawBaseHP(g2);
        
//        gameEnd.drawGameEndUI(g2);
        if(gameEnd.getIsFinishing() == true){
            gameEnd.drawGameEndUI(g2);
        }
    }

    public void update() {
        pointer.update();
        for (int i = allEnemy.size() - 1; i >= 0; i--) {
            FemaleGoblin enemy = allEnemy.get(i);
            if (!enemy.getAlive()) {
                allEnemy.remove(i);
            } else {
                enemy.update();
            }
        }
        for (int i = allTower.size() - 1; i >= 0; i--) {
            Assasin tower = allTower.get(i);
            if (tower.getIsSold()) {
                allTower.remove(i);
            } else {
                tower.update();
            }
        }
        if(baseHP <= 0){
            gameEnd.setIsFinishing(true);
            gameEnd.setIsLose(true);
        }
    }

    //LLM
    public void prepareWave() {
        spawnQueue.clear();

        HashMap<String, Integer> waveData =  waveManager.getAllWaveAtLevel(level).get(currentWave);

        for (String enemyType : waveData.keySet()) {
            int amount = waveData.get(enemyType);
            for (int i = 0; i < amount; i++) {
                spawnQueue.add(enemyType);
            }
        }

    }

    //LLM
    public void spawnEnemy() {
        spawnCounter++;
        if (spawnCounter >= FPS && spawnQueue.size() > 0) {
            String enemyType = spawnQueue.remove(0);
            switch (enemyType) {
                case "FemaleGoblin":
                    allEnemy.add(new FemaleGoblin(this, allWaypoint.get(level - 1), 1, 100 + (25 * (currentWave - 1))));
                    break;
            }
            spawnCounter = 0;
        }
    }

    //LLM
    public void checkWave() {
        if (spawnQueue.size() == 0 && allEnemy.size() == 0) {
            if (waveCooldown >= FPS * 3) {
                waveCooldown = 0;
                if (currentWave < waveManager.getAllWaveAtLevel(level).get(currentWave).size()) {
                    currentWave++;
                    prepareWave();
                } else {
                    gameEnd.setIsWin(true);
                    gameEnd.setIsFinishing(true);
                }
            }
            waveCooldown++;
        }
    }

    public void drawBaseHP(Graphics2D g2) {
        double oneScale = (double) 200 / maxBaseHP;
        double baseHPBarValue = oneScale * baseHP;
        g2.setColor(new Color(18, 10, 7));
        g2.fillRoundRect(35, screenHeight - 50, 208, 30, 24, 24);
        g2.setColor(new Color(206, 67, 98));
        g2.fillRoundRect(39, screenHeight - 46, (int) baseHPBarValue, 22, 20, 20);
        g2.setColor(Color.white);
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.drawString((int) baseHP + "", 60, screenHeight - 29);
        g2.drawImage(loadAsset.getHeartImage(), 20, screenHeight - 50, 32, 32, null);
    }

    public void drawAssetLevel1(Graphics2D g2) {
        HashMap<String, Asset> allAssetLevel1 = loadAsset.getAllAssetLevel1();
        Asset house = allAssetLevel1.get("decor_2.png");
        Asset stone6 = allAssetLevel1.get("stone_6.png");
        Asset ruin = allAssetLevel1.get("decor_3.png");
        Asset bridge = allAssetLevel1.get("bridge.png");
        Asset magma = allAssetLevel1.get("decor_4.png");
        Asset lava = allAssetLevel1.get("decor_6.png");
        Asset dot = allAssetLevel1.get("dot.png");
        Asset bone = allAssetLevel1.get("decor_5.png");
        Asset stone1 = allAssetLevel1.get("stone_1.png");
        Asset stone2 = allAssetLevel1.get("stone_2.png");
        Asset stone3 = allAssetLevel1.get("stone_3.png");
        Asset stone4 = allAssetLevel1.get("stone_4.png");
        Asset stone5 = allAssetLevel1.get("stone_5.png");

        g2.drawImage(house.getImage(), 0, tileSize * 3 - 20, house.getWidth(), house.getHeight(), null);
        g2.drawImage(stone6.getImage(), tileSize * 2, tileSize + 20, stone6.getWidth(), stone6.getHeight(), null);
        g2.drawImage(stone6.getImage(), tileSize + 20, tileSize * 6, stone6.getWidth(), stone6.getHeight(), null);
        g2.drawImage(stone6.getImage(), tileSize * 7 + 20, tileSize * 3, stone6.getWidth(), stone6.getHeight() - 20, null);
        g2.drawImage(stone6.getImage(), tileSize * 12 + 30, tileSize * 6 + 30, stone6.getWidth() + 60, stone6.getHeight(), null);
        g2.drawImage(ruin.getImage(), tileSize * 4, tileSize * 6, ruin.getWidth(), ruin.getHeight(), null);
        g2.drawImage(ruin.getImage(), tileSize * 7, 20, ruin.getWidth(), ruin.getHeight(), null);
        g2.drawImage(ruin.getImage(), tileSize * 7 + 20, tileSize * 4 + 20, ruin.getWidth(), ruin.getHeight(), null);
        g2.drawImage(ruin.getImage(), tileSize * 9 + 20, tileSize * 7 + 10, ruin.getWidth(), ruin.getHeight(), null);
        g2.drawImage(ruin.getImage(), tileSize * 11, tileSize + 25, ruin.getWidth(), ruin.getHeight(), null);
        g2.drawImage(ruin.getImage(), tileSize * 13 + 10, tileSize * 5 + 10, ruin.getWidth(), ruin.getHeight(), null);
        g2.drawImage(magma.getImage(), tileSize - 10, tileSize, magma.getWidth(), magma.getHeight(), null);
        g2.drawImage(magma.getImage(), tileSize, tileSize * 6, magma.getWidth(), magma.getHeight(), null);
        g2.drawImage(magma.getImage(), tileSize * 4 + 20, tileSize * 4 + 40, magma.getWidth(), magma.getHeight(), null);
        g2.drawImage(magma.getImage(), tileSize * 5 + 20, tileSize * 6 + 40, magma.getWidth(), magma.getHeight(), null);
        g2.drawImage(magma.getImage(), tileSize * 5 + 30, 40, magma.getWidth(), magma.getHeight(), null);
        g2.drawImage(magma.getImage(), tileSize * 7 + 30, tileSize * 4, magma.getWidth(), magma.getHeight(), null);
        g2.drawImage(magma.getImage(), tileSize * 8 + 30, tileSize * 2 + 50, magma.getWidth(), magma.getHeight(), null);
        g2.drawImage(magma.getImage(), tileSize * 10, tileSize * 2, magma.getWidth(), magma.getHeight(), null);
        g2.drawImage(magma.getImage(), tileSize * 12 + 40, tileSize * 7 - 10, magma.getWidth(), magma.getHeight(), null);
        g2.drawImage(magma.getImage(), tileSize * 13 + 40, tileSize * 4, magma.getWidth(), magma.getHeight(), null);
        g2.drawImage(magma.getImage(), tileSize * 13 + 30, tileSize, magma.getWidth(), magma.getHeight(), null);
        g2.drawImage(lava.getImage(), tileSize * 4, tileSize * 3, lava.getWidth(), lava.getHeight(), null);
        g2.drawImage(lava.getImage(), tileSize * 10 + 30, tileSize * 3 + 30, lava.getWidth(), lava.getHeight(), null);
        g2.drawImage(dot.getImage(), tileSize * 6 + 20, tileSize * 7, dot.getWidth(), dot.getHeight(), null);
        g2.drawImage(dot.getImage(), tileSize * 12, tileSize, dot.getWidth(), dot.getHeight(), null);
        g2.drawImage(bone.getImage(), tileSize * 3, tileSize * 7, bone.getWidth(), bone.getHeight(), null);
        g2.drawImage(bone.getImage(), tileSize * 12, tileSize * 2, bone.getWidth(), bone.getHeight(), null);
        g2.drawImage(bone.getImage(), tileSize * 0, tileSize * 5, bone.getWidth(), bone.getHeight(), null);
        g2.drawImage(bone.getImage(), tileSize * 15, tileSize * 1, bone.getWidth(), bone.getHeight(), null);
        g2.drawImage(bone.getImage(), tileSize * 8, tileSize * 6, bone.getWidth(), bone.getHeight(), null);
        g2.drawImage(bone.getImage(), tileSize * 4, tileSize * 0, bone.getWidth(), bone.getHeight(), null);
        g2.drawImage(bone.getImage(), tileSize * 10, tileSize * 8, bone.getWidth(), bone.getHeight(), null);
        g2.drawImage(bone.getImage(), tileSize * 5, tileSize * 3, bone.getWidth(), bone.getHeight(), null);
        g2.drawImage(bone.getImage(), tileSize * 14, tileSize * 4, bone.getWidth(), bone.getHeight(), null);
        g2.drawImage(bone.getImage(), tileSize * 1, tileSize * 2, bone.getWidth(), bone.getHeight(), null);
        g2.drawImage(stone1.getImage(), tileSize * 2, tileSize * 1, stone1.getWidth(), stone1.getHeight(), null);
        g2.drawImage(stone2.getImage(), tileSize * 5, tileSize * 4, stone2.getWidth(), stone2.getHeight(), null);
        g2.drawImage(stone3.getImage(), tileSize * 7, tileSize * 0, stone3.getWidth(), stone3.getHeight(), null);
        g2.drawImage(stone4.getImage(), tileSize * 9, tileSize * 6, stone4.getWidth(), stone4.getHeight(), null);
        g2.drawImage(stone1.getImage(), tileSize * 11, tileSize * 3, stone1.getWidth(), stone1.getHeight(), null);
        g2.drawImage(stone2.getImage(), tileSize * 13, tileSize * 7, stone2.getWidth(), stone2.getHeight(), null);
        g2.drawImage(stone3.getImage(), tileSize * 4, tileSize * 8, stone3.getWidth(), stone3.getHeight(), null);
        g2.drawImage(stone4.getImage(), tileSize * 6, tileSize * 2, stone4.getWidth(), stone4.getHeight(), null);
        g2.drawImage(stone1.getImage(), tileSize * 0, tileSize * 6, stone1.getWidth(), stone1.getHeight(), null);
        g2.drawImage(stone2.getImage(), tileSize * 14, tileSize * 5, stone2.getWidth(), stone2.getHeight(), null);
        g2.drawImage(stone3.getImage(), tileSize * 3, tileSize * 7, stone3.getWidth(), stone3.getHeight(), null);
        g2.drawImage(stone4.getImage(), tileSize * 12, tileSize * 2, stone4.getWidth(), stone4.getHeight(), null);
        g2.drawImage(stone1.getImage(), tileSize * 8, tileSize * 6, stone1.getWidth(), stone1.getHeight(), null);
        g2.drawImage(stone2.getImage(), tileSize * 4, tileSize * 0, stone2.getWidth(), stone2.getHeight(), null);
        g2.drawImage(stone3.getImage(), tileSize * 10, tileSize * 8, stone3.getWidth(), stone3.getHeight(), null);
        g2.drawImage(stone4.getImage(), tileSize * 6, tileSize * 3, stone4.getWidth(), stone4.getHeight(), null);
        g2.drawImage(stone1.getImage(), tileSize * 14, tileSize * 4, stone1.getWidth(), stone1.getHeight(), null);
        g2.drawImage(stone2.getImage(), tileSize * 1, tileSize * 2, stone2.getWidth(), stone2.getHeight(), null);
        g2.drawImage(stone3.getImage(), tileSize * 9, tileSize * 1, stone3.getWidth(), stone3.getHeight(), null);
        g2.drawImage(stone4.getImage(), tileSize * 5, tileSize * 8, stone4.getWidth(), stone4.getHeight(), null);
        g2.drawImage(stone5.getImage(), tileSize * 3, tileSize * 5, stone5.getWidth(), stone5.getHeight(), null);
        g2.drawImage(stone5.getImage(), tileSize * 7, tileSize * 2, stone5.getWidth(), stone5.getHeight(), null);
        g2.drawImage(stone5.getImage(), tileSize * 10, tileSize * 4, stone5.getWidth(), stone5.getHeight(), null);
        g2.drawImage(stone5.getImage(), tileSize * 12, tileSize * 8, stone5.getWidth(), stone5.getHeight(), null);
        g2.drawImage(stone5.getImage(), tileSize * 1, tileSize * 7, stone5.getWidth(), stone5.getHeight(), null);

        drawRotate(g2, bridge.getImage(), tileSize * 6 + 2, tileSize * 2 + 6, bridge.getWidth() + 10, bridge.getHeight() + 14, 90);
        drawRotate(g2, bridge.getImage(), tileSize * 9 + 2, tileSize * 2 + 6, bridge.getWidth() + 10, bridge.getHeight() + 14, 90);
    }
    
    public void drawAssetLevel2(Graphics2D g2){
        HashMap<String, Asset> allAssetLevel2 = loadAsset.getAllAssetLevel2();
        Asset cart = allAssetLevel2.get("decor_2.png");
        Asset tunnel = allAssetLevel2.get("decor_7.png");
        Asset hole = allAssetLevel2.get("decor_5.png");
        Asset rail = allAssetLevel2.get("decor_6.png");
        Asset stone1 = allAssetLevel2.get("stone_1.png");
        Asset orange = allAssetLevel2.get("decor_3.png");
        Asset blue = allAssetLevel2.get("decor_4.png");
        Asset grass = allAssetLevel2.get("dot.png");
        Asset stonePile = allAssetLevel2.get("stone_1.png");
        Asset hole2 = allAssetLevel2.get("decor_8.png");
        Asset hole3 = allAssetLevel2.get("decor_9.png");
        Asset stone2 = allAssetLevel2.get("stone_2.png");
        Asset stone3 = allAssetLevel2.get("stone_3.png");
        Asset stone4 = allAssetLevel2.get("stone_4.png");
        Asset stone5 = allAssetLevel2.get("stone_5.png");
        Asset stone6 = allAssetLevel2.get("stone_6.png");
        Asset stone7 = allAssetLevel2.get("stone_7.png");
        Asset stone8 = allAssetLevel2.get("stone_8.png");
        Asset stone9 = allAssetLevel2.get("stone_9.png");
        
        
        g2.drawImage(tunnel.getImage(), tileSize * 0, tileSize * 0, tunnel.getWidth(), tunnel.getHeight(), null);
        g2.drawImage(rail.getImage(), tileSize * 2, tileSize * 1, rail.getWidth(), rail.getHeight(), null);
        g2.drawImage(hole.getImage(), tileSize * 0, tileSize * 1, hole.getWidth(), hole.getHeight(),null);
        g2.drawImage(cart.getImage(), tileSize * 0, tileSize * 1 + 1, cart.getWidth(), cart.getHeight(),  null);
        g2.drawImage(hole2.getImage(), tileSize * 0, tileSize * 7, hole2.getWidth(), hole2.getHeight(), null);
        g2.drawImage(hole3.getImage(), tileSize * 13, tileSize * 7, hole.getWidth(), hole.getHeight(),null);
        g2.drawImage(orange.getImage(), tileSize * 6, tileSize * 0 +1, orange.getWidth(), orange.getHeight(), null);
        g2.drawImage(orange.getImage(), tileSize * 11, tileSize * 0 + 1, orange.getWidth(), orange.getHeight(), null);
        g2.drawImage(orange.getImage(), tileSize * 13, tileSize * 0 + 1, orange.getWidth(), orange.getHeight(), null);
        g2.drawImage(blue.getImage(), tileSize * 1, tileSize * 5, blue.getWidth(), blue.getHeight(), null);
        g2.drawImage(stonePile.getImage(), tileSize * 0 , tileSize * 1, stonePile.getWidth(), stonePile.getHeight(), null);
        g2.drawImage(stonePile.getImage(), tileSize * 2 , tileSize * 6, stonePile.getWidth(), stonePile.getHeight(),null);
        g2.drawImage(stonePile.getImage(), tileSize * 5 , tileSize * 4, stonePile.getWidth(), stonePile.getHeight(), null);
        g2.drawImage(grass.getImage(), tileSize * 2, tileSize * 6, grass.getWidth(), grass.getHeight(), null);
        g2.drawImage(stone9.getImage(), tileSize * 1, tileSize * 1, stone9.getWidth(), stone9.getHeight(), null);
        g2.drawImage(stone7.getImage(), tileSize * 3, tileSize * 1, stone7.getWidth(), stone7.getHeight(), null);
        g2.drawImage(stone8.getImage(), tileSize * 6, tileSize * 1, stone8.getWidth(), stone8.getHeight(), null);
        g2.drawImage(stone9.getImage(), tileSize * 9, tileSize * 1, stone9.getWidth(), stone9.getHeight(), null);
        g2.drawImage(stone6.getImage(), tileSize * 13, tileSize * 1, stone6.getWidth(), stone6.getHeight(), null);
        g2.drawImage(stone8.getImage(), tileSize * 2, tileSize * 2, stone8.getWidth(), stone8.getHeight(), null);
        g2.drawImage(stone9.getImage(), tileSize * 5, tileSize * 2, stone9.getWidth(), stone9.getHeight(), null);
        g2.drawImage(stone7.getImage(), tileSize * 7, tileSize * 2, stone7.getWidth(), stone7.getHeight(), null);
        g2.drawImage(stone9.getImage(), tileSize * 11, tileSize * 2, stone9.getWidth(), stone9.getHeight(), null);
        g2.drawImage(stone5.getImage(), tileSize * 14, tileSize * 2, stone5.getWidth(), stone5.getHeight(), null);
        g2.drawImage(stone9.getImage(), tileSize * 1, tileSize * 3, stone9.getWidth(), stone9.getHeight(), null);
        g2.drawImage(stone8.getImage(), tileSize * 4, tileSize * 3, stone8.getWidth(), stone8.getHeight(), null);
        g2.drawImage(stone6.getImage(), tileSize * 6, tileSize * 3, stone6.getWidth(), stone6.getHeight(), null);
        g2.drawImage(stone9.getImage(), tileSize * 10, tileSize * 3, stone9.getWidth(), stone9.getHeight(), null);
        g2.drawImage(stone7.getImage(), tileSize * 13, tileSize * 3, stone7.getWidth(), stone7.getHeight(), null);
        g2.drawImage(stone8.getImage(), tileSize * 2, tileSize * 4, stone8.getWidth(), stone8.getHeight(), null);
        g2.drawImage(stone9.getImage(), tileSize * 5, tileSize * 4, stone9.getWidth(), stone9.getHeight(), null);
        g2.drawImage(stone4.getImage(), tileSize * 7, tileSize * 4, stone4.getWidth(), stone4.getHeight(), null);
        g2.drawImage(stone9.getImage(), tileSize * 9, tileSize * 4, stone9.getWidth(), stone9.getHeight(), null);
        g2.drawImage(stone6.getImage(), tileSize * 12, tileSize * 4, stone6.getWidth(), stone6.getHeight(), null);
        g2.drawImage(stone9.getImage(), tileSize * 1, tileSize * 5, stone9.getWidth(), stone9.getHeight(), null);
        g2.drawImage(stone7.getImage(), tileSize * 3, tileSize * 5, stone7.getWidth(), stone7.getHeight(), null);
        g2.drawImage(stone8.getImage(), tileSize * 6, tileSize * 5, stone8.getWidth(), stone8.getHeight(), null);
        g2.drawImage(stone9.getImage(), tileSize * 10, tileSize * 5, stone9.getWidth(), stone9.getHeight(), null);
        g2.drawImage(stone5.getImage(), tileSize * 14, tileSize * 5, stone5.getWidth(), stone5.getHeight(), null);
        g2.drawImage(stone8.getImage(), tileSize * 2, tileSize * 6, stone8.getWidth(), stone8.getHeight(), null);
        g2.drawImage(stone9.getImage(), tileSize * 4, tileSize * 6, stone9.getWidth(), stone9.getHeight(), null);
        g2.drawImage(stone7.getImage(), tileSize * 7, tileSize * 6, stone7.getWidth(), stone7.getHeight(), null);
        g2.drawImage(stone9.getImage(), tileSize * 11, tileSize * 6, stone9.getWidth(), stone9.getHeight(), null);
        g2.drawImage(stone3.getImage(), tileSize * 13, tileSize * 6, stone3.getWidth(), stone3.getHeight(), null);
        g2.drawImage(stone3.getImage(), tileSize * 2, tileSize * 1, stone3.getWidth(), stone3.getHeight(), null);
        g2.drawImage(stone2.getImage(), tileSize * 6, tileSize * 1, stone2.getWidth(), stone2.getHeight(), null);
        g2.drawImage(stone3.getImage(), tileSize * 11, tileSize * 1, stone3.getWidth(), stone3.getHeight(), null);
        g2.drawImage(stone1.getImage(), tileSize * 3, tileSize * 3, stone1.getWidth(), stone1.getHeight(), null); // rare big
        g2.drawImage(stone3.getImage(), tileSize * 8, tileSize * 2, stone3.getWidth(), stone3.getHeight(), null);
        g2.drawImage(stone2.getImage(), tileSize * 13, tileSize * 2, stone2.getWidth(), stone2.getHeight(), null);
        g2.drawImage(stone3.getImage(), tileSize * 1, tileSize * 3, stone3.getWidth(), stone3.getHeight(), null);
        g2.drawImage(stone2.getImage(), tileSize * 5, tileSize * 3, stone2.getWidth(), stone2.getHeight(), null);
        g2.drawImage(stone3.getImage(), tileSize * 10, tileSize * 3, stone3.getWidth(), stone3.getHeight(), null);
        g2.drawImage(stone3.getImage(), tileSize * 3, tileSize * 4, stone3.getWidth(), stone3.getHeight(), null);
        g2.drawImage(stone1.getImage(), tileSize * 7, tileSize * 4, stone1.getWidth(), stone1.getHeight(), null); // rare big
        g2.drawImage(stone2.getImage(), tileSize * 12, tileSize * 4, stone2.getWidth(), stone2.getHeight(), null);
        g2.drawImage(stone3.getImage(), tileSize * 2, tileSize * 5, stone3.getWidth(), stone3.getHeight(), null);
        g2.drawImage(stone2.getImage(), tileSize * 5, tileSize * 5, stone2.getWidth(), stone2.getHeight(), null);
        g2.drawImage(stone3.getImage(), tileSize * 11, tileSize * 5, stone3.getWidth(), stone3.getHeight(), null);
        g2.drawImage(stone3.getImage(), tileSize * 4, tileSize * 6, stone3.getWidth(), stone3.getHeight(), null);
        g2.drawImage(stone2.getImage(), tileSize * 10, tileSize * 6, stone2.getWidth(), stone2.getHeight(), null);
        g2.drawImage(stone3.getImage(), tileSize * 13, tileSize * 6, stone3.getWidth(), stone3.getHeight(), null);
        g2.drawImage(orange.getImage(), tileSize * 3, tileSize * 0, orange.getWidth(), orange.getHeight(), null);
        g2.drawImage(orange.getImage(), tileSize * 8, tileSize * 0, orange.getWidth(), orange.getHeight(), null);
        g2.drawImage(orange.getImage(), tileSize * 12, tileSize * 1, orange.getWidth(), orange.getHeight(), null);
        g2.drawImage(orange.getImage(), tileSize * 14, tileSize * 3, orange.getWidth(), orange.getHeight(), null);
        g2.drawImage(blue.getImage(), tileSize * 1, tileSize * 4, blue.getWidth(), blue.getHeight(), null);
        g2.drawImage(blue.getImage(), tileSize * 5, tileSize * 2, blue.getWidth(), blue.getHeight(), null);
        g2.drawImage(blue.getImage(), tileSize * 9, tileSize * 3, blue.getWidth(), blue.getHeight(), null);
        g2.drawImage(blue.getImage(), tileSize * 11, tileSize * 5, blue.getWidth(), blue.getHeight(), null);
            }
    public void drawAssetLevel3(Graphics2D g2){
        HashMap<String, Asset> allAssetLevel3 = loadAsset.getAllAssetLevel3();
        
        Asset lake = allAssetLevel3.get("lake.png");
        Asset gate = allAssetLevel3.get("decor_5.png");
        Asset ruins = allAssetLevel3.get("decor_6.png");
        Asset stone1 = allAssetLevel3.get("stone_1.png");
        Asset stone2 = allAssetLevel3.get("stone_2.png");
        Asset stone3 = allAssetLevel3.get("stone_3.png");
        Asset stone4 = allAssetLevel3.get("stone_4.png");
        Asset stone5 = allAssetLevel3.get("stone_5.png");
        Asset stone6 = allAssetLevel3.get("stone_6.png");
        Asset stone7 = allAssetLevel3.get("stone_7.png");
        Asset tree1 = allAssetLevel3.get("tree_1.png");
        Asset tree2 = allAssetLevel3.get("tree_2.png");
        Asset grave1 = allAssetLevel3.get("decor_3.png");
        Asset grave2 = allAssetLevel3.get("decor_4.png");
        Asset dot = allAssetLevel3.get("dot.png");
        
        g2.drawImage(lake.getImage(), tileSize * 9, tileSize * 6, lake.getWidth() * 3, lake.getHeight() * 3, null);
        g2.drawImage(lake.getImage(), tileSize * 2, tileSize * 2 - 20, lake.getWidth() * 2, lake.getHeight() * 2,null);
        g2.drawImage(gate.getImage(), tileSize * 10, tileSize * 3 - 10, gate.getWidth() * 3 + 20, gate.getHeight()  * 3 + 20, null);
        g2.drawImage(stone2.getImage(), tileSize * 2 + 10, tileSize * 1 + 2, stone2.getWidth(), stone2.getHeight(), null);
        g2.drawImage(stone2.getImage(), tileSize * 3 , tileSize * 1 - 45, stone2.getWidth() * 2 + + 10, stone2.getHeight() * 2,null);
        
    }
    
    
    public void drawAssetLevel4(Graphics2D g2){
        HashMap<String, Asset> allAssetLevel4 = loadAsset.getAllAssetLevel4();
    }

    public void drawRotate(Graphics2D g2, BufferedImage img, int x, int y, int width, int height, double angle) {

        AffineTransform old = g2.getTransform();

        g2.rotate(Math.toRadians(angle), x + width / 2, y + height / 2);
        g2.drawImage(img, x, y, width, height, null);

        g2.setTransform(old);
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

    public Pointer getPointer() {
        return pointer;
    }

    public TileManager getTileManager() {
        return tileManager;
    }

    public CollisionChecker getCollisionChecker() {
        return collisionChecker;
    }

    public double getBaseHP() {
        return baseHP;
    }

    public void setBaseHP(double baseHP) {
        this.baseHP = baseHP;
    }

    public ArrayList<FemaleGoblin> getAllEnemy() {
        return allEnemy;
    }

    public double getScale() {
        return scale;
    }

    public LoadAnimation getLoadAnimation() {
        return loadAnimation;
    }

    public CharacterBox[] getAllCharacterBox() {
        return allCharacterBox;
    }
    
    public void setLevel(int level){
        this.level = level;
    }
    
    public void nextLevel(){
        level += 1;
        tileManager.loadMap();
        tileManager.getTileImage();
        prepareWave();
        currentWave = 0;
    }
}

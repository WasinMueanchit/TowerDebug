package Game;

import All.Enemy.ArmoredOrg;
import All.Enemy.Cyclops;
import All.Enemy.EarthGolem;
import All.Enemy.Enemy;
import All.Enemy.FemaleGoblin;
import All.Enemy.MaleGoblin;
import All.Enemy.Skeleton;
import All.Enemy.Yeti;
import All.Tower.Tower;
import Menu.Controller;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

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
    private Controller controller;

    //Mouse
    private Pointer pointer;
    private CollisionChecker collisionChecker;

    //Game Setting
    private int level = 1;
    private ArrayList<Point[]> allWaypoint = new ArrayList<>();
    private Point[] waypointsLevel1 = {new Point(0, tileSize * 3), new Point(tileSize * 3, tileSize * 5), new Point(tileSize * 6, tileSize * 1), new Point(tileSize * 9, tileSize * 6), new Point(tileSize * 12, tileSize * 3), new Point(tileSize * 15, tileSize * 3)};
    private Point[] waypointsLevel2 = {new Point(0, tileSize * 3), new Point(tileSize * 2, tileSize * 5), new Point(tileSize * 4, tileSize * 5), new Point(tileSize * 4, tileSize * 1), new Point(tileSize * 6, tileSize * 1), new Point(tileSize * 6, tileSize * 6), new Point(tileSize * 8, tileSize * 6), new Point(tileSize * 8, tileSize * 1), new Point(tileSize * 10, tileSize * 1), new Point(tileSize * 10, tileSize * 6), new Point(tileSize * 12, tileSize * 6), new Point(tileSize * 12, tileSize * 3), new Point(tileSize * 14, tileSize * 3)};
    private Point[] waypointsLevel3 = {new Point(0, 0), new Point(0, tileSize * 3), new Point(tileSize * 4, tileSize * 3), new Point(tileSize * 4, tileSize * 5), new Point(tileSize * 1, tileSize * 5), new Point(tileSize * 1, tileSize * 7), new Point(tileSize * 7, tileSize * 7), new Point(tileSize * 7, tileSize * 2), new Point(tileSize * 5, tileSize * 2), new Point(tileSize * 5, 0), new Point(tileSize * 11, 0), new Point(tileSize * 11, tileSize * 2), new Point(tileSize * 9, tileSize * 5), new Point(tileSize * 13, tileSize * 5), new Point(tileSize * 13, tileSize * 6), new Point(tileSize * 15, tileSize * 6)};
    private Point[] waypointsLevel4 = {new Point(0, tileSize * 4), new Point(tileSize * 2, tileSize * 4), new Point(tileSize * 2, tileSize * 1), new Point(tileSize * 13, tileSize * 1), new Point(tileSize * 13, tileSize * 3), new Point(tileSize * 4, tileSize * 5), new Point(tileSize * 3, tileSize * 6), new Point(tileSize * 6, tileSize * 5), new Point(tileSize * 8, tileSize * 6), new Point(tileSize * 10, tileSize * 5), new Point(tileSize * 14, tileSize * 5)};
    private final int FPS = 60;
    private double maxBaseHP = 1000;
    private double baseHP = 1000;
    private int coin = 300;
    private boolean isEverLost = false;
    private boolean isEverWin = false;
    private int countStartWave = 0;
    private boolean isStartWave = false;

    //Monster
    private ArrayList<Enemy> allEnemy = new ArrayList<>();
    private int spawnCounter = 0;
    private WaveManager waveManager;
    private int waveCooldown = 0;
    private int currentWave = 0;
    ArrayList<String> spawnQueue = new ArrayList<>();

    //Tower
    private ArrayList<Tower> allTower = new ArrayList<>();
    private int towerAmount = 0;

    //Tile
    private TileManager tileManager = new TileManager(this);

    //Particle
    private ArrayList<Particle> allParticle = new ArrayList<>();

    //Assets
    private LoadData loadData;

    //Solid Area
    private ArrayList<SolidArea> allSolidArea;

    //UI
    private ArrayList<String> allCharacterSelected;
    private CharacterBox[] allCharacterBox;
    private GameEnd gameEnd;

    //Sound
    private Sound sound = new Sound();
    private Sound backGroundSound = new Sound();
    private int soundCount = 0;

    public GamePanel(Controller controller, ArrayList<String> allCharacterSelected) {
        this.controller = controller;
        this.allCharacterSelected = allCharacterSelected;
        loadData = new LoadData(this);
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        waveManager = new WaveManager();
        gameEnd = new GameEnd(this);
        allSolidArea = new ArrayList<>();

        allWaypoint.add(waypointsLevel1);
        allWaypoint.add(waypointsLevel2);
        allWaypoint.add(waypointsLevel3);
        allWaypoint.add(waypointsLevel4);

        collisionChecker = new CollisionChecker(this);
        pointer = new Pointer(this, gameEnd);
        allCharacterBox = new CharacterBox[5];
        for (int i = 0; i < allCharacterSelected.size(); i++) {
            String name = allCharacterSelected.get(i);
            if (!name.equals("")) {
                BufferedImage image = loadData.getAnimation(name).get("idle").get(0);
                allCharacterBox[i] = new CharacterBox(this, tileSize * (5 + i), tileSize * 7, 50, 50, image, name);
            }
        }
        this.addMouseMotionListener(pointer);
        this.addMouseListener(pointer);
        this.addKeyListener(pointer);
        this.setFocusable(true);

        gameThread = new Thread(this);
        gameThread.start();

        setAllSolidArea();

        backGroundSound.setSound("background");
        backGroundSound.play();
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
        for (Tower t : allTower) {
            t.draw(g2);
        }
        for (Enemy e : allEnemy) {
            e.draw(g2);
        }
        for (Particle p : allParticle) {
            p.draw(g2);
        }
        pointer.draw(g2);
        for (CharacterBox c : allCharacterBox) {
            if (c != null) {
                c.drawCharacterBox(g2);
            }
        }
        drawCoin(g2);
        drawBaseHP(g2);
        drawWaveCount(g2);

//        gameEnd.drawGameEndUI(g2);
        if (gameEnd.getIsFinishing() == true) {
            gameEnd.drawGameEndUI(g2);
        }
//        for (SolidArea solidAsset : allSolidArea) {
//            g2.setColor(Color.red);
//            g2.fill(solidAsset.getSolidArea());
//        }
    }

    public void update() {
        if (countStartWave >= FPS * 10 && isStartWave == false) { //Delay spawn enemy when start game
            isStartWave = true;
            prepareWave();
        }
        countStartWave++;

        pointer.update();
        for (int i = allEnemy.size() - 1; i >= 0; i--) {
            Enemy enemy = allEnemy.get(i);
            if (!enemy.getAlive()) {
                coin += enemy.getReward();
                allEnemy.remove(i);
            } else {
                enemy.update();
            }
        }
        for (int i = allTower.size() - 1; i >= 0; i--) {
            Tower tower = allTower.get(i);
            if (tower.getIsSold()) {
                coin += tower.getSellCost();
                allSolidArea.remove(tower.getSolidArea());
                allTower.remove(i);
            } else {
                tower.update();
            }
        }
        for (int i = allParticle.size() - 1; i >= 0; i--) {
            Particle particle = allParticle.get(i);
            if (!particle.getIsAlive()) {
                allParticle.remove(i);
            } else {
                particle.update();
            }
        }
        if (baseHP <= 0) {
            if (!isEverLost) {
                gameEnd.setIsFinishing(true);
                isEverLost = true;
                sound.setSound("explosion");
                sound.play();
            }
        }
        if (soundCount >= FPS * 250) {
            backGroundSound.setSound("background");
            backGroundSound.play();
            soundCount = 0;
        }
        soundCount++;
    }

    public void prepareWave() {
        spawnQueue.clear();

        HashMap<String, Integer> waveData = waveManager.getAllWaveAtLevel(level).get(currentWave);

        for (String enemyType : waveData.keySet()) {
            int amount = waveData.get(enemyType);
            for (int i = 0; i < amount; i++) {
                spawnQueue.add(enemyType);
            }
        }

    }

    public void spawnEnemy() {
        spawnCounter++;
        if (spawnCounter >= FPS * 2 && spawnQueue.size() > 0) {
            String enemyType = spawnQueue.remove(0);
            switch (enemyType) {
                case "Female Goblin":
                    allEnemy.add(new FemaleGoblin(this, allWaypoint.get(level - 1), false));
                    break;
                case "Ghost Female Goblin":
                    allEnemy.add(new FemaleGoblin(this, allWaypoint.get(level - 1), true));
                    break;
                case "Armored Org":
                    allEnemy.add(new ArmoredOrg(this, allWaypoint.get(level - 1), false));
                    break;
                case "Ghost Armored Org":
                    allEnemy.add(new ArmoredOrg(this, allWaypoint.get(level - 1), true));
                    break;
                case "Cyclops":
                    allEnemy.add(new Cyclops(this, allWaypoint.get(level - 1), false));
                    break;
                case "Ghost Cyclops":
                    allEnemy.add(new Cyclops(this, allWaypoint.get(level - 1), true));
                    break;
                case "Earth Golem":
                    allEnemy.add(new EarthGolem(this, allWaypoint.get(level - 1), false));
                    break;
                case "Ghost Earth Golem":
                    allEnemy.add(new EarthGolem(this, allWaypoint.get(level - 1), true));
                    break;
                case "Male Goblin":
                    allEnemy.add(new MaleGoblin(this, allWaypoint.get(level - 1), false));
                    break;
                case "Ghost Male Goblin":
                    allEnemy.add(new MaleGoblin(this, allWaypoint.get(level - 1), true));
                    break;
                case "Skeleton":
                    allEnemy.add(new Skeleton(this, allWaypoint.get(level - 1), false));
                    break;
                case "Ghost Skeleton":
                    allEnemy.add(new Skeleton(this, allWaypoint.get(level - 1), true));
                    break;
                case "Yeti":
                    allEnemy.add(new Yeti(this, allWaypoint.get(level - 1), false));
                    break;
                case "Ghost Yeti":
                    allEnemy.add(new Yeti(this, allWaypoint.get(level - 1), true));
                    break;
            }
            spawnCounter = 0;
        }
    }

    public void checkWave() {
        if (isEverLost) {
            return;
        }
        if (spawnQueue.size() == 0 /*&& allEnemy.size() == 0*/) {
            if (waveCooldown >= FPS * 15) {
                waveCooldown = 0;
                if (currentWave < waveManager.getAllWaveAtLevel(level).size() - 1) {
                    currentWave++;
                    for (Tower tower : allTower) {
                        if (tower.getCanCreateMoney() == true) {
                            coin += tower.getReward();
                            sound.setSound("pickupCoin");
                            sound.play();
                        }
                    }
                    prepareWave();
                }
            }
            waveCooldown++;
        }
        if (spawnQueue.size() == 0 && allEnemy.size() == 0 && currentWave == waveManager.getAllWaveAtLevel(level).size() - 1 && !isEverLost && baseHP > 0 && isEverWin == false && isStartWave == true) { ////asdasadadsdsa
            if (waveCooldown >= FPS * 5) {
                gameEnd.setIsWin(true);
                gameEnd.setIsFinishing(true);
                sound.setSound("victory");
                sound.play();
                isEverWin = true;
                if (level == controller.unlockedLevel) {
                    controller.unlockedLevel++;
                }
            }
        }
    }

    public void drawCoin(Graphics2D g2) {
        g2.setColor(Color.white);
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.drawString(coin + "", 60, screenHeight - 80);
        g2.drawImage(loadData.getCoinImage(), 20, screenHeight - 102, 32, 32, null);
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
        g2.drawImage(loadData.getHeartImage(), 20, screenHeight - 50, 32, 32, null);
    }

    public void drawWaveCount(Graphics2D g2) {
        g2.setColor(Color.white);
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.drawString(currentWave + 1 + "/" + waveManager.getAllWaveAtLevel(level).size(), 15, 30);
    }

    public void setAllSolidArea() {
        allSolidArea.clear();
        HashMap<String, Asset> allAssetLevel1 = loadData.getAllAssetLevel1();
        HashMap<String, Asset> allAssetLevel2 = loadData.getAllAssetLevel2();
        HashMap<String, Asset> allAssetLevel3 = loadData.getAllAssetLevel3();
        HashMap<String, Asset> allAssetLevel4 = loadData.getAllAssetLevel4();
        switch (level) {
            case 1:
                Asset lava = allAssetLevel1.get("decor_6.png");
                allSolidArea.add(new SolidArea(tileSize * 4, tileSize * 3, lava.getWidth(), lava.getHeight()));
                allSolidArea.add(new SolidArea(tileSize * 10 + 30, tileSize * 3 + 30, lava.getWidth(), lava.getHeight()));
                break;
            case 2:
                Asset hole2 = allAssetLevel2.get("decor_8.png");
                Asset hole3 = allAssetLevel2.get("decor_9.png");
                allSolidArea.add(new SolidArea(0, 0, 230, 170));
                allSolidArea.add(new SolidArea(780, 0, 180, 170));
                allSolidArea.add(new SolidArea(780, 0, 180, 170));
                allSolidArea.add(new SolidArea(tileSize * 0, tileSize * 7 + 20, hole2.getWidth(), hole2.getHeight()));
                allSolidArea.add(new SolidArea(tileSize * 14, tileSize * 7 + 40, hole3.getWidth(), hole3.getHeight()));
                break;
            case 3:
                Asset lake = allAssetLevel3.get("lake.png");
                Asset tree1 = allAssetLevel3.get("tree_1.png");
                Asset tree2 = allAssetLevel3.get("tree_2.png");
                Asset gate = allAssetLevel3.get("decor_5.png");
                Asset stone2 = allAssetLevel3.get("stone_2.png");
                allSolidArea.add(new SolidArea(tileSize * 9, tileSize * 6, lake.getWidth() * 3, lake.getHeight() * 3));
                allSolidArea.add(new SolidArea(tileSize * 1, tileSize * 0, 250, 200));
                allSolidArea.add(new SolidArea(tileSize * 5, tileSize * 3, tree1.getWidth() * 3, tree1.getHeight() * 3 - 60));
                allSolidArea.add(new SolidArea(tileSize * 2, tileSize * 6, tree2.getWidth() * 2, tree2.getHeight() * 2 - 40));
                allSolidArea.add(new SolidArea(tileSize * 2, tileSize * 6, tree2.getWidth() * 2, tree2.getHeight() * 2 - 40));
                allSolidArea.add(new SolidArea(tileSize * 0 - 20, tileSize * 4 - 20, tree1.getWidth() * 3, tree1.getHeight() * 3 - 60));
                allSolidArea.add(new SolidArea(tileSize * 1 + 10, tileSize * 0 + 20, tree2.getWidth() * 2, tree2.getHeight() * 2 - 40));
                allSolidArea.add(new SolidArea(tileSize * 10, tileSize * 3 - 10, gate.getWidth() * 3 - 20, gate.getHeight() * 3 - 20));
                allSolidArea.add(new SolidArea(tileSize * 12, tileSize * 2 - 20, stone2.getWidth() * 3, stone2.getHeight() * 3 - 50));
                break;
            case 4:
                Asset house = allAssetLevel4.get("decor_1.png");
                allSolidArea.add(new SolidArea(0, 0, 300, 80));
                allSolidArea.add(new SolidArea(0, 0, 30, 250));
                allSolidArea.add(new SolidArea(0, 280, 30, 500));
                allSolidArea.add(new SolidArea(tileSize * 11, 0, 400, 80));
                allSolidArea.add(new SolidArea(tileSize * 9, tileSize * 4 + 30, house.getWidth(), house.getHeight() - 40));
                return;
        }
    }

    public void drawAssetLevel1(Graphics2D g2) {
        HashMap<String, Asset> allAssetLevel1 = loadData.getAllAssetLevel1();
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

    public void drawAssetLevel2(Graphics2D g2) {
        HashMap<String, Asset> allAssetLevel2 = loadData.getAllAssetLevel2();
        Asset cart = allAssetLevel2.get("decor_2.png");
        Asset tunnel = allAssetLevel2.get("decor_7.png");
        Asset hole = allAssetLevel2.get("decor_5.png");
        Asset rail = allAssetLevel2.get("decor_6.png");
        Asset stone1 = allAssetLevel2.get("stone_1.png");
        Asset blue = allAssetLevel2.get("decor_3.png");
        Asset orange = allAssetLevel2.get("decor_4.png");
        Asset dot = allAssetLevel2.get("dot.png");
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
        g2.drawImage(rail.getImage(), tileSize * 2 - 20, tileSize * 1 - 10, rail.getWidth(), rail.getHeight(), null);
        g2.drawImage(rail.getImage(), tileSize * 13 + 43, tileSize * 1 + 10, rail.getWidth(), rail.getHeight(), null);
        g2.drawImage(rail.getImage(), tileSize * 12 + 20, tileSize * 0 - 10, rail.getWidth(), rail.getHeight(), null);
        g2.drawImage(hole.getImage(), tileSize * 0 + 30, tileSize * 1 + 8, hole.getWidth(), hole.getHeight(), null);
        g2.drawImage(cart.getImage(), tileSize * 2 + 25, tileSize * 2 + 10, cart.getWidth(), cart.getHeight(), null);
        g2.drawImage(cart.getImage(), tileSize * 2 + 35, tileSize * 1 - 5, cart.getWidth(), cart.getHeight(), null);
        g2.drawImage(cart.getImage(), tileSize * 14 - 40, tileSize * 2 - 30, cart.getWidth(), cart.getHeight(), null);
        g2.drawImage(cart.getImage(), tileSize * 14 - 50, tileSize * 0, cart.getWidth(), cart.getHeight(), null);
        g2.drawImage(hole2.getImage(), tileSize * 0, tileSize * 7 + 20, hole2.getWidth(), hole2.getHeight(), null);
        g2.drawImage(hole3.getImage(), tileSize * 14, tileSize * 7 + 40, hole3.getWidth(), hole3.getHeight(), null);
        g2.drawImage(stone1.getImage(), tileSize * 0 + 10, tileSize * 1, stone1.getWidth(), stone2.getHeight(), null);
        g2.drawImage(stone1.getImage(), tileSize * 0 + 20, tileSize * 1 + 20, stone1.getWidth(), stone2.getHeight(), null);
        g2.drawImage(stone1.getImage(), tileSize * 0 + 45, tileSize * 1 + 10, stone1.getWidth(), stone2.getHeight(), null);
        g2.drawImage(stone1.getImage(), tileSize * 1, tileSize * 1 + 15, stone1.getWidth(), stone2.getHeight(), null);
        g2.drawImage(stone1.getImage(), tileSize * 3, tileSize * 1 - 16, stone1.getWidth(), stone2.getHeight(), null);
        g2.drawImage(stone1.getImage(), tileSize * 3 - 30, tileSize * 1 - 26, stone1.getWidth(), stone2.getHeight(), null);
        g2.drawImage(stone1.getImage(), tileSize * 14, tileSize * 0, stone1.getWidth(), stone2.getHeight(), null);
        g2.drawImage(stone1.getImage(), tileSize * 14 - 20, tileSize * 0 + 25, stone1.getWidth(), stone2.getHeight(), null);
        g2.drawImage(stone1.getImage(), tileSize * 13, tileSize * 1 + 5, stone1.getWidth(), stone2.getHeight(), null);
        g2.drawImage(stone1.getImage(), tileSize * 13 - 20, tileSize * 1 + 35, stone1.getWidth(), stone2.getHeight(), null);
        g2.drawImage(stone1.getImage(), tileSize * 14 - 10, tileSize * 2, stone1.getWidth(), stone2.getHeight(), null);
        g2.drawImage(dot.getImage(), tileSize * 1, tileSize * 3 - 20, dot.getWidth(), dot.getHeight(), null);
        g2.drawImage(dot.getImage(), tileSize * 1 + 20, tileSize * 4, dot.getWidth(), dot.getHeight(), null);
        g2.drawImage(dot.getImage(), tileSize * 3, tileSize * 6, dot.getWidth(), dot.getHeight(), null);
        g2.drawImage(dot.getImage(), tileSize * 6 - 45, tileSize * 4 + 10, dot.getWidth(), dot.getHeight(), null);
        g2.drawImage(dot.getImage(), tileSize * 5 - 20, tileSize * 1 - 15, dot.getWidth(), dot.getHeight(), null);
        g2.drawImage(dot.getImage(), tileSize * 7 + 20, tileSize * 3 - 5, dot.getWidth(), dot.getHeight(), null);
        g2.drawImage(dot.getImage(), tileSize * 8 + 40, tileSize * 7, dot.getWidth(), dot.getHeight(), null);
        g2.drawImage(dot.getImage(), tileSize * 11 + 25, tileSize * 3 - 40, dot.getWidth(), dot.getHeight(), null);
        g2.drawImage(dot.getImage(), tileSize * 13, tileSize * 5 - 30, dot.getWidth(), dot.getHeight(), null);
        g2.drawImage(dot.getImage(), tileSize * 12 + 10, tileSize * 1 - 20, dot.getWidth(), dot.getHeight(), null);
        g2.drawImage(blue.getImage(), tileSize * 1, tileSize * 5, blue.getWidth(), blue.getHeight(), null);
        g2.drawImage(blue.getImage(), tileSize * 3 - 10, tileSize * 3 - 10, blue.getWidth(), blue.getHeight(), null);
        g2.drawImage(blue.getImage(), tileSize * 4 + 50, tileSize * 5 + 40, blue.getWidth(), blue.getHeight(), null);
        g2.drawImage(blue.getImage(), tileSize * 6 + 50, tileSize * 1 + 10, blue.getWidth(), blue.getHeight(), null);
        g2.drawImage(blue.getImage(), tileSize * 9 + 16, tileSize * 5 - 22, blue.getWidth(), blue.getHeight(), null);
        g2.drawImage(blue.getImage(), tileSize * 11 + 6, tileSize * 2 - 30, blue.getWidth(), blue.getHeight(), null);
        g2.drawImage(blue.getImage(), tileSize * 13, tileSize * 7 - 10, blue.getWidth(), blue.getHeight(), null);
        g2.drawImage(orange.getImage(), tileSize * 4 + 10, tileSize * 0 + 20, blue.getWidth(), blue.getHeight(), null);
        g2.drawImage(orange.getImage(), tileSize * 2 + 20, tileSize * 7, blue.getWidth(), blue.getHeight(), null);
        g2.drawImage(orange.getImage(), tileSize * 6 + 20, tileSize * 7 + 15, blue.getWidth(), blue.getHeight(), null);
        g2.drawImage(orange.getImage(), tileSize * 10 - 10, tileSize * 7 + 20, blue.getWidth(), blue.getHeight(), null);
        g2.drawImage(orange.getImage(), tileSize * 7 + 15, tileSize * 4 - 15, blue.getWidth(), blue.getHeight(), null);
        g2.drawImage(orange.getImage(), tileSize * 9, tileSize * 0 + 10, blue.getWidth(), blue.getHeight(), null);
        g2.drawImage(orange.getImage(), tileSize * 12 - 15, tileSize * 0 + 5, blue.getWidth(), blue.getHeight(), null);
        g2.drawImage(orange.getImage(), tileSize * 12 - 35, tileSize * 5 - 10, blue.getWidth(), blue.getHeight(), null);
        g2.drawImage(stone2.getImage(), tileSize * 1 - 6, tileSize * 1, stone2.getWidth(), stone2.getHeight(), null);
        g2.drawImage(stone3.getImage(), tileSize * 3 - 10, tileSize * 2, stone3.getWidth(), stone3.getHeight(), null);
        g2.drawImage(stone4.getImage(), tileSize * 5 - 4, tileSize * 1, stone4.getWidth(), stone4.getHeight(), null);
        g2.drawImage(stone5.getImage(), tileSize * 7 - 9, tileSize * 3, stone5.getWidth(), stone5.getHeight(), null);
        g2.drawImage(stone6.getImage(), tileSize * 9 - 3, tileSize * 2, stone6.getWidth(), stone6.getHeight(), null);
        g2.drawImage(stone7.getImage(), tileSize * 11 - 11, tileSize * 3, stone7.getWidth(), stone7.getHeight(), null);
        g2.drawImage(stone8.getImage(), tileSize * 13 - 7, tileSize * 4, stone8.getWidth(), stone8.getHeight(), null);
        g2.drawImage(stone9.getImage(), tileSize * 14 - 2, tileSize * 1, stone9.getWidth(), stone9.getHeight(), null);
        g2.drawImage(stone2.getImage(), tileSize * 2 - 10, tileSize * 5, stone2.getWidth(), stone2.getHeight(), null);
        g2.drawImage(stone3.getImage(), tileSize * 4 - 5, tileSize * 6, stone3.getWidth(), stone3.getHeight(), null);
        g2.drawImage(stone4.getImage(), tileSize * 6 - 8, tileSize * 5, stone4.getWidth(), stone4.getHeight(), null);
        g2.drawImage(stone5.getImage(), tileSize * 8 - 6, tileSize * 6, stone5.getWidth(), stone5.getHeight(), null);
        g2.drawImage(stone6.getImage(), tileSize * 10 - 10, tileSize * 5, stone6.getWidth(), stone6.getHeight(), null);
        g2.drawImage(stone7.getImage(), tileSize * 12 - 4, tileSize * 6, stone7.getWidth(), stone7.getHeight(), null);
        g2.drawImage(stone8.getImage(), tileSize * 14 - 9, tileSize * 7, stone8.getWidth(), stone8.getHeight(), null);
        g2.drawImage(stone9.getImage(), tileSize * 13 - 3, tileSize * 2, stone9.getWidth(), stone9.getHeight(), null);
        g2.drawImage(stone2.getImage(), tileSize * 5 - 7, tileSize * 7, stone2.getWidth(), stone2.getHeight(), null);
        g2.drawImage(stone3.getImage(), tileSize * 7 - 11, tileSize * 4, stone3.getWidth(), stone3.getHeight(), null);
        g2.drawImage(stone4.getImage(), tileSize * 9 - 2, tileSize * 7, stone4.getWidth(), stone4.getHeight(), null);
        g2.drawImage(stone5.getImage(), tileSize * 11 - 8, tileSize * 1, stone5.getWidth(), stone5.getHeight(), null);
        g2.drawImage(stone6.getImage(), tileSize * 13 - 5, tileSize * 3, stone6.getWidth(), stone6.getHeight(), null);
        g2.drawImage(stone2.getImage(), tileSize * 1 - 6, tileSize * 1, stone2.getWidth(), stone2.getHeight(), null);
        g2.drawImage(stone3.getImage(), tileSize * 3 - 10, tileSize * 2, stone3.getWidth(), stone3.getHeight(), null);
        g2.drawImage(stone4.getImage(), tileSize * 5 - 4, tileSize * 1, stone4.getWidth(), stone4.getHeight(), null);
        g2.drawImage(stone5.getImage(), tileSize * 7 - 9, tileSize * 3, stone5.getWidth(), stone5.getHeight(), null);
        g2.drawImage(stone6.getImage(), tileSize * 9 - 3, tileSize * 2, stone6.getWidth(), stone6.getHeight(), null);
        g2.drawImage(stone7.getImage(), tileSize * 11 - 11, tileSize * 3, stone7.getWidth(), stone7.getHeight(), null);
        g2.drawImage(stone8.getImage(), tileSize * 13 - 7, tileSize * 4, stone8.getWidth(), stone8.getHeight(), null);
        g2.drawImage(stone9.getImage(), tileSize * 14 - 2, tileSize * 1, stone9.getWidth(), stone9.getHeight(), null);
        g2.drawImage(stone2.getImage(), tileSize * 2 - 10, tileSize * 5, stone2.getWidth(), stone2.getHeight(), null);
        g2.drawImage(stone3.getImage(), tileSize * 4 - 5, tileSize * 6, stone3.getWidth(), stone3.getHeight(), null);
        g2.drawImage(stone4.getImage(), tileSize * 6 - 8, tileSize * 5, stone4.getWidth(), stone4.getHeight(), null);
        g2.drawImage(stone5.getImage(), tileSize * 8 - 6, tileSize * 6, stone5.getWidth(), stone5.getHeight(), null);
        g2.drawImage(stone6.getImage(), tileSize * 10 - 10, tileSize * 5, stone6.getWidth(), stone6.getHeight(), null);
        g2.drawImage(stone7.getImage(), tileSize * 12 - 4, tileSize * 6, stone7.getWidth(), stone7.getHeight(), null);
        g2.drawImage(stone8.getImage(), tileSize * 14 - 9, tileSize * 7, stone8.getWidth(), stone8.getHeight(), null);
        g2.drawImage(stone9.getImage(), tileSize * 13 - 3, tileSize * 2, stone9.getWidth(), stone9.getHeight(), null);
        g2.drawImage(stone2.getImage(), tileSize * 5 - 7, tileSize * 7, stone2.getWidth(), stone2.getHeight(), null);
        g2.drawImage(stone3.getImage(), tileSize * 7 - 11, tileSize * 4, stone3.getWidth(), stone3.getHeight(), null);
        g2.drawImage(stone4.getImage(), tileSize * 9 - 2, tileSize * 7, stone4.getWidth(), stone4.getHeight(), null);
        g2.drawImage(stone5.getImage(), tileSize * 11 - 8, tileSize * 1, stone5.getWidth(), stone5.getHeight(), null);
        g2.drawImage(stone6.getImage(), tileSize * 13 - 5, tileSize * 3, stone6.getWidth(), stone6.getHeight(), null);
        g2.drawImage(stone7.getImage(), tileSize * 1 - 8, tileSize * 3, stone7.getWidth(), stone7.getHeight(), null);
        g2.drawImage(stone8.getImage(), tileSize * 2 - 4, tileSize * 4, stone8.getWidth(), stone8.getHeight(), null);
        g2.drawImage(stone9.getImage(), tileSize * 3 - 11, tileSize * 5, stone9.getWidth(), stone9.getHeight(), null);
        g2.drawImage(stone2.getImage(), tileSize * 4 - 6, tileSize * 3, stone2.getWidth(), stone2.getHeight(), null);
        g2.drawImage(stone3.getImage(), tileSize * 6 - 9, tileSize * 2, stone3.getWidth(), stone3.getHeight(), null);
        g2.drawImage(stone4.getImage(), tileSize * 8 - 3, tileSize * 4, stone4.getWidth(), stone4.getHeight(), null);
        g2.drawImage(stone5.getImage(), tileSize * 10 - 10, tileSize * 3, stone5.getWidth(), stone5.getHeight(), null);
        g2.drawImage(stone6.getImage(), tileSize * 12 - 5, tileSize * 2, stone6.getWidth(), stone6.getHeight(), null);
        g2.drawImage(stone7.getImage(), tileSize * 14 - 7, tileSize * 4, stone7.getWidth(), stone7.getHeight(), null);
        g2.drawImage(stone8.getImage(), tileSize * 2 - 2, tileSize * 7, stone8.getWidth(), stone8.getHeight(), null);
        g2.drawImage(stone9.getImage(), tileSize * 4 - 12, tileSize * 7, stone9.getWidth(), stone9.getHeight(), null);
        g2.drawImage(stone2.getImage(), tileSize * 6 - 4, tileSize * 6, stone2.getWidth(), stone2.getHeight(), null);
        g2.drawImage(stone3.getImage(), tileSize * 8 - 8, tileSize * 7, stone3.getWidth(), stone3.getHeight(), null);
        g2.drawImage(stone4.getImage(), tileSize * 10 - 6, tileSize * 6, stone4.getWidth(), stone4.getHeight(), null);
        g2.drawImage(stone5.getImage(), tileSize * 12 - 9, tileSize * 7, stone5.getWidth(), stone5.getHeight(), null);
        g2.drawImage(stone6.getImage(), tileSize * 14 - 3, tileSize * 6, stone6.getWidth(), stone6.getHeight(), null);
        g2.drawImage(stone2.getImage(), tileSize * 1 - 3, tileSize * 6, stone2.getWidth(), stone2.getHeight(), null);
        g2.drawImage(stone3.getImage(), tileSize * 2 - 9, tileSize * 2, stone3.getWidth(), stone3.getHeight(), null);
        g2.drawImage(stone4.getImage(), tileSize * 3 - 5, tileSize * 4, stone4.getWidth(), stone4.getHeight(), null);
        g2.drawImage(stone5.getImage(), tileSize * 4 - 11, tileSize * 1, stone5.getWidth(), stone5.getHeight(), null);
        g2.drawImage(stone6.getImage(), tileSize * 5 - 6, tileSize * 3, stone6.getWidth(), stone6.getHeight(), null);
        g2.drawImage(stone7.getImage(), tileSize * 6 - 2, tileSize * 2, stone7.getWidth(), stone7.getHeight(), null);
        g2.drawImage(stone8.getImage(), tileSize * 7 - 10, tileSize * 5, stone8.getWidth(), stone8.getHeight(), null);
        g2.drawImage(stone9.getImage(), tileSize * 8 - 4, tileSize * 3, stone9.getWidth(), stone9.getHeight(), null);
        g2.drawImage(stone2.getImage(), tileSize * 9 - 8, tileSize * 1, stone2.getWidth(), stone2.getHeight(), null);
        g2.drawImage(stone3.getImage(), tileSize * 10 - 3, tileSize * 4, stone3.getWidth(), stone3.getHeight(), null);
        g2.drawImage(stone4.getImage(), tileSize * 11 - 7, tileSize * 2, stone4.getWidth(), stone4.getHeight(), null);
        g2.drawImage(stone5.getImage(), tileSize * 12 - 5, tileSize * 5, stone5.getWidth(), stone5.getHeight(), null);
        g2.drawImage(stone6.getImage(), tileSize * 13 - 9, tileSize * 3, stone6.getWidth(), stone6.getHeight(), null);
        g2.drawImage(stone7.getImage(), tileSize * 14 - 1, tileSize * 2, stone7.getWidth(), stone7.getHeight(), null);
        g2.drawImage(stone8.getImage(), tileSize * 15 - 6, tileSize * 7, stone8.getWidth(), stone8.getHeight(), null);
        g2.drawImage(stone9.getImage(), tileSize * 13 - 4, tileSize * 7, stone9.getWidth(), stone9.getHeight(), null);

        //        for (SolidArea solidAsset : allSolidArea){
//            g2.setColor(Color.red);
//            g2.fill(solidAsset.getSolidArea());
//        }
    }

    public void drawAssetLevel3(Graphics2D g2) {
        HashMap<String, Asset> allAssetLevel3 = loadData.getAllAssetLevel3();

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
        Asset grass = allAssetLevel3.get("dot.png");

        g2.drawImage(grass.getImage(), tileSize * 2, tileSize * 1, grass.getWidth(), grass.getHeight(), null);
        g2.drawImage(grass.getImage(), tileSize * 7, tileSize * 1, grass.getWidth(), grass.getHeight(), null);
        g2.drawImage(grass.getImage(), tileSize * 6, tileSize * 2, grass.getWidth(), grass.getHeight(), null);
        g2.drawImage(grass.getImage(), tileSize * 10, tileSize * 2, grass.getWidth(), grass.getHeight(), null);
        g2.drawImage(grass.getImage(), tileSize * 4, tileSize * 3, grass.getWidth(), grass.getHeight(), null);
        g2.drawImage(grass.getImage(), tileSize * 11, tileSize * 3, grass.getWidth(), grass.getHeight(), null);
        g2.drawImage(grass.getImage(), tileSize * 1, tileSize * 4, grass.getWidth(), grass.getHeight(), null);
        g2.drawImage(grass.getImage(), tileSize * 7, tileSize * 4, grass.getWidth(), grass.getHeight(), null);
        g2.drawImage(grass.getImage(), tileSize * 4, tileSize * 5, grass.getWidth(), grass.getHeight(), null);
        g2.drawImage(grass.getImage(), tileSize * 8, tileSize * 5, grass.getWidth(), grass.getHeight(), null);
        g2.drawImage(grass.getImage(), tileSize * 1, tileSize * 6, grass.getWidth(), grass.getHeight(), null);
        g2.drawImage(grass.getImage(), tileSize * 7, tileSize * 6, grass.getWidth(), grass.getHeight(), null);
        g2.drawImage(lake.getImage(), tileSize * 9, tileSize * 6, lake.getWidth() * 3, lake.getHeight() * 3, null);
        g2.drawImage(lake.getImage(), tileSize * 2, tileSize * 2 - 20, lake.getWidth() * 2, lake.getHeight() * 2, null);
        g2.drawImage(gate.getImage(), tileSize * 10, tileSize * 3 - 10, gate.getWidth() * 3 + 20, gate.getHeight() * 3 + 20, null);
        g2.drawImage(stone2.getImage(), tileSize * 2 + 10, tileSize * 1 + 2, stone2.getWidth(), stone2.getHeight(), null);
        g2.drawImage(stone2.getImage(), tileSize * 3, tileSize * 1 - 45, stone2.getWidth() * 2 + +10, stone2.getHeight() * 2, null);
        g2.drawImage(stone3.getImage(), tileSize * 1 + 50, tileSize * 2 - 45, stone3.getWidth(), stone3.getHeight(), null);
        g2.drawImage(tree2.getImage(), tileSize * 1 + 10, tileSize * 0 + 20, tree2.getWidth() * 2, tree2.getHeight() * 2, null);
        g2.drawImage(tree2.getImage(), tileSize * 4 + 20, tileSize * 1 - 20, tree2.getWidth() * 2, tree2.getHeight() * 2, null);
        g2.drawImage(tree2.getImage(), tileSize * 1 + 10, tileSize * 3 + 40, tree2.getWidth() * 2, tree2.getHeight() * 2, null);
        g2.drawImage(tree1.getImage(), tileSize * 0 - 20, tileSize * 4 - 20, tree1.getWidth() * 3, tree1.getHeight() * 3, null);
        g2.drawImage(gate.getImage(), tileSize * 6 - 10, tileSize * 3 + 20, gate.getWidth() + 10, gate.getHeight() + 10, null);
        g2.drawImage(ruins.getImage(), tileSize * 7 - 20, tileSize * 4 - 30, ruins.getWidth(), ruins.getHeight(), null);
        g2.drawImage(tree1.getImage(), tileSize * 5, tileSize * 3, tree1.getWidth() * 3, tree1.getHeight() * 3, null);
        g2.drawImage(tree2.getImage(), tileSize * 2, tileSize * 6, tree2.getWidth() * 2, tree2.getHeight() * 2, null);
        g2.drawImage(ruins.getImage(), tileSize * 4, tileSize * 6 - 20, ruins.getWidth() * 2, ruins.getHeight() * 2, null);
        g2.drawImage(stone3.getImage(), tileSize * 3 + 20, tileSize * 6, stone3.getWidth(), stone3.getHeight(), null);
        g2.drawImage(stone3.getImage(), tileSize * 5, tileSize * 6, stone3.getWidth(), stone3.getHeight(), null);
        g2.drawImage(stone2.getImage(), tileSize * 12, tileSize * 2 - 20, stone2.getWidth() * 3, stone2.getHeight() * 3, null);
        g2.drawImage(stone2.getImage(), tileSize * 7, tileSize * 1, stone3.getWidth() * 2, stone3.getHeight() * 2, null);
        g2.drawImage(stone2.getImage(), tileSize * 8 - 20, tileSize * 1, stone3.getWidth() * 2, stone3.getHeight() * 2, null);

        g2.drawImage(stone4.getImage(), tileSize * 1, tileSize * 1, stone4.getWidth(), stone4.getHeight(), null);
        g2.drawImage(stone5.getImage(), tileSize * 3, tileSize * 1, stone5.getWidth(), stone5.getHeight(), null);
        g2.drawImage(stone6.getImage(), tileSize * 5, tileSize * 1, stone6.getWidth(), stone6.getHeight(), null);

        g2.drawImage(stone5.getImage(), tileSize * 1, tileSize * 2, stone5.getWidth(), stone5.getHeight(), null);
        g2.drawImage(stone4.getImage(), tileSize * 4, tileSize * 2, stone4.getWidth(), stone4.getHeight(), null);
        g2.drawImage(stone6.getImage(), tileSize * 8, tileSize * 2, stone6.getWidth(), stone6.getHeight(), null);

        g2.drawImage(stone6.getImage(), tileSize * 2, tileSize * 3, stone6.getWidth(), stone6.getHeight(), null);
        g2.drawImage(stone4.getImage(), tileSize * 6, tileSize * 3, stone4.getWidth(), stone4.getHeight(), null);
        g2.drawImage(stone5.getImage(), tileSize * 9, tileSize * 3, stone5.getWidth(), stone5.getHeight(), null);

        g2.drawImage(stone4.getImage(), tileSize * 3, tileSize * 4, stone4.getWidth(), stone4.getHeight(), null);
        g2.drawImage(stone5.getImage(), tileSize * 5, tileSize * 4, stone5.getWidth(), stone5.getHeight(), null);
        g2.drawImage(stone6.getImage(), tileSize * 10, tileSize * 4, stone6.getWidth(), stone6.getHeight(), null);

        g2.drawImage(stone5.getImage(), tileSize * 2, tileSize * 5, stone5.getWidth(), stone5.getHeight(), null);
        g2.drawImage(stone4.getImage(), tileSize * 6, tileSize * 5, stone4.getWidth(), stone4.getHeight(), null);
        g2.drawImage(stone6.getImage(), tileSize * 11, tileSize * 5, stone6.getWidth(), stone6.getHeight(), null);

        g2.drawImage(stone6.getImage(), tileSize * 3, tileSize * 6, stone6.getWidth(), stone6.getHeight(), null);
        g2.drawImage(stone5.getImage(), tileSize * 5, tileSize * 6, stone5.getWidth(), stone5.getHeight(), null);
        g2.drawImage(stone4.getImage(), tileSize * 9, tileSize * 6, stone4.getWidth(), stone4.getHeight(), null);
        g2.drawImage(stone4.getImage(), tileSize * 11, tileSize * 2, stone4.getWidth(), stone4.getHeight(), null);
        g2.drawImage(stone5.getImage(), tileSize * 12, tileSize * 3, stone5.getWidth(), stone5.getHeight(), null);
        g2.drawImage(stone6.getImage(), tileSize * 13, tileSize * 4, stone6.getWidth(), stone6.getHeight(), null);

        g2.drawImage(stone5.getImage(), tileSize * 10, tileSize * 5, stone5.getWidth(), stone5.getHeight(), null);
        g2.drawImage(stone4.getImage(), tileSize * 12, tileSize * 6, stone4.getWidth(), stone4.getHeight(), null);
        g2.drawImage(stone6.getImage(), tileSize * 14, tileSize * 5, stone6.getWidth(), stone6.getHeight(), null);

//        for (SolidArea solidAsset : allSolidArea) {
//            g2.setColor(Color.red);
//            g2.fill(solidAsset.getSolidArea());
//        }
    }

    public void drawAssetLevel4(Graphics2D g2) {
        HashMap<String, Asset> allAssetLevel4 = loadData.getAllAssetLevel4();

        Asset bridge = allAssetLevel4.get("bridge.png");
        Asset bush1 = allAssetLevel4.get("bush_1.png");
        Asset bush2 = allAssetLevel4.get("bush_2.png");
        Asset bush3 = allAssetLevel4.get("bush_3.png");
        Asset bush4 = allAssetLevel4.get("bush_4.png");
        Asset bush5 = allAssetLevel4.get("bush_5.png");
        Asset house = allAssetLevel4.get("decor_1.png");
        Asset skull = allAssetLevel4.get("decor_2.png");
        Asset grass = allAssetLevel4.get("dot.png");
        Asset stone1 = allAssetLevel4.get("stone_1.png");
        Asset stone2 = allAssetLevel4.get("stone_2.png");
        Asset stone3 = allAssetLevel4.get("stone_3.png");
        Asset stone4 = allAssetLevel4.get("stone_4.png");
        Asset stone5 = allAssetLevel4.get("stone_5.png");
        Asset tree1 = allAssetLevel4.get("tree_1.png");
        Asset tree2 = allAssetLevel4.get("tree_2.png");
        Asset tree3 = allAssetLevel4.get("tree_3.png");
        Asset tree4 = allAssetLevel4.get("tree_4.png");

        g2.drawImage(stone1.getImage(), tileSize * 0, tileSize * 6, stone1.getWidth(), stone1.getHeight(), null);
        g2.drawImage(stone2.getImage(), tileSize * 1, tileSize * 6 + 10, stone2.getWidth(), stone2.getHeight(), null);
        g2.drawImage(stone3.getImage(), tileSize * 2, tileSize * 7, stone3.getWidth(), stone3.getHeight(), null);
        g2.drawImage(stone4.getImage(), tileSize * 0 + 15, tileSize * 7 + 5, stone4.getWidth(), stone4.getHeight(), null);
        g2.drawImage(stone5.getImage(), tileSize * 1 + 20, tileSize * 7 + 15, stone5.getWidth(), stone5.getHeight(), null);
        g2.drawImage(bush1.getImage(), tileSize * 0 + 5, tileSize * 6 + 12, bush1.getWidth(), bush1.getHeight(), null);
        g2.drawImage(bush2.getImage(), tileSize * 1 + 20, tileSize * 7 + 3, bush2.getWidth(), bush2.getHeight(), null);
        g2.drawImage(bush3.getImage(), tileSize * 2 + 10, tileSize * 8 - 5, bush3.getWidth(), bush3.getHeight(), null);
        g2.drawImage(bush4.getImage(), tileSize * 1 + 30, tileSize * 6 + 25, bush4.getWidth(), bush4.getHeight(), null);
        g2.drawImage(bush5.getImage(), tileSize * 3 + 15, tileSize * 7 + 18, bush5.getWidth(), bush5.getHeight(), null);
        g2.drawImage(bush1.getImage(), tileSize * 12 + 8, tileSize * 0 + 5, bush1.getWidth(), bush1.getHeight(), null);
        g2.drawImage(bush2.getImage(), tileSize * 13 + 25, tileSize * 1 + 18, bush2.getWidth(), bush2.getHeight(), null);
        g2.drawImage(bush3.getImage(), tileSize * 14 + 12, tileSize * 0 + 30, bush3.getWidth(), bush3.getHeight(), null);
        g2.drawImage(bush4.getImage(), tileSize * 15 - 10, tileSize * 2 + 7, bush4.getWidth(), bush4.getHeight(), null);
        g2.drawImage(bush5.getImage(), tileSize * 13 + 5, tileSize * 2 + 25, bush5.getWidth(), bush5.getHeight(), null);
        g2.drawImage(bush1.getImage(), tileSize * 7 + 10, tileSize * 2 + 5, bush1.getWidth(), bush1.getHeight(), null);
        g2.drawImage(bush2.getImage(), tileSize * 7 - 12, tileSize * 3 + 22, bush2.getWidth(), bush2.getHeight(), null);
        g2.drawImage(bush3.getImage(), tileSize * 7 + 18, tileSize * 4 + 15, bush3.getWidth(), bush3.getHeight(), null);
        g2.drawImage(bush4.getImage(), tileSize * 7 + 5, tileSize * 5 + 28, bush4.getWidth(), bush4.getHeight(), null);
        g2.drawImage(bush5.getImage(), tileSize * 7 - 8, tileSize * 6 + 12, bush5.getWidth(), bush5.getHeight(), null);
        g2.drawImage(bush1.getImage(), tileSize * 7 + 20, tileSize * 7 + 5, bush1.getWidth(), bush1.getHeight(), null);
        g2.drawImage(bush2.getImage(), tileSize * 2 + 10, tileSize * 2 + 10, bush2.getWidth(), bush2.getHeight(), null);
        g2.drawImage(bush3.getImage(), tileSize * 4 + 15, tileSize * 3 + 20, bush3.getWidth(), bush3.getHeight(), null);
        g2.drawImage(bush4.getImage(), tileSize * 6 + 5, tileSize * 4 + 25, bush4.getWidth(), bush4.getHeight(), null);
        g2.drawImage(bush5.getImage(), tileSize * 8 + 12, tileSize * 5 + 18, bush5.getWidth(), bush5.getHeight(), null);
        g2.drawImage(bush1.getImage(), tileSize * 10 + 20, tileSize * 6 + 30, bush1.getWidth(), bush1.getHeight(), null);
        g2.drawImage(bush2.getImage(), tileSize * 12 + 8, tileSize * 7 + 12, bush2.getWidth(), bush2.getHeight(), null);
        g2.drawImage(stone1.getImage(), tileSize * 12, tileSize * 0, stone1.getWidth(), stone1.getHeight(), null);
        g2.drawImage(stone2.getImage(), tileSize * 13, tileSize * 0 + 10, stone2.getWidth(), stone2.getHeight(), null);
        g2.drawImage(stone3.getImage(), tileSize * 14, tileSize * 1, stone3.getWidth(), stone3.getHeight(), null);
        g2.drawImage(stone4.getImage(), tileSize * 13 + 15, tileSize * 1 + 5, stone4.getWidth(), stone4.getHeight(), null);
        g2.drawImage(stone5.getImage(), tileSize * 14 + 20, tileSize * 1 + 15, stone5.getWidth(), stone5.getHeight(), null);
        g2.drawImage(stone1.getImage(), tileSize * 7, tileSize * 2, stone1.getWidth(), stone1.getHeight(), null);
        g2.drawImage(stone2.getImage(), tileSize * 7, tileSize * 3 + 10, stone2.getWidth(), stone2.getHeight(), null);
        g2.drawImage(stone3.getImage(), tileSize * 7, tileSize * 4 + 5, stone3.getWidth(), stone3.getHeight(), null);
        g2.drawImage(stone4.getImage(), tileSize * 7, tileSize * 5 + 15, stone4.getWidth(), stone4.getHeight(), null);
        g2.drawImage(stone5.getImage(), tileSize * 7, tileSize * 6 + 20, stone5.getWidth(), stone5.getHeight(), null);
        g2.drawImage(stone1.getImage(), tileSize * 0 + 5, tileSize * 6 + 12, stone1.getWidth(), stone1.getHeight(), null);
        g2.drawImage(stone2.getImage(), tileSize * 2 + 18, tileSize * 7 + 3, stone2.getWidth(), stone2.getHeight(), null);
        g2.drawImage(stone3.getImage(), tileSize * 1 + 25, tileSize * 6 + 30, stone3.getWidth(), stone3.getHeight(), null);
        g2.drawImage(stone4.getImage(), tileSize * 3 + 10, tileSize * 7 + 22, stone4.getWidth(), stone4.getHeight(), null);
        g2.drawImage(stone5.getImage(), tileSize * 2 + 5, tileSize * 8 - 10, stone5.getWidth(), stone5.getHeight(), null);
        g2.drawImage(stone1.getImage(), tileSize * 12 + 8, tileSize * 0 + 5, stone1.getWidth(), stone1.getHeight(), null);
        g2.drawImage(stone2.getImage(), tileSize * 13 + 25, tileSize * 1 + 18, stone2.getWidth(), stone2.getHeight(), null);
        g2.drawImage(stone3.getImage(), tileSize * 14 + 12, tileSize * 0 + 30, stone3.getWidth(), stone3.getHeight(), null);
        g2.drawImage(stone4.getImage(), tileSize * 15 - 10, tileSize * 2 + 7, stone4.getWidth(), stone4.getHeight(), null);
        g2.drawImage(stone5.getImage(), tileSize * 13 + 5, tileSize * 2 + 25, stone5.getWidth(), stone5.getHeight(), null);
        g2.drawImage(stone1.getImage(), tileSize * 7 + 10, tileSize * 2 + 5, stone1.getWidth(), stone1.getHeight(), null);
        g2.drawImage(stone2.getImage(), tileSize * 7 - 12, tileSize * 3 + 22, stone2.getWidth(), stone2.getHeight(), null);
        g2.drawImage(stone3.getImage(), tileSize * 7 + 18, tileSize * 4 + 15, stone3.getWidth(), stone3.getHeight(), null);
        g2.drawImage(stone4.getImage(), tileSize * 7 + 5, tileSize * 5 + 28, stone4.getWidth(), stone4.getHeight(), null);
        g2.drawImage(stone5.getImage(), tileSize * 7 - 8, tileSize * 6 + 12, stone5.getWidth(), stone5.getHeight(), null);
        g2.drawImage(stone1.getImage(), tileSize * 7 + 20, tileSize * 7 + 5, stone1.getWidth(), stone1.getHeight(), null);
        g2.drawImage(bush1.getImage(), tileSize * 1, tileSize * 1, bush1.getWidth(), bush1.getHeight(), null);
        g2.drawImage(bush2.getImage(), tileSize * 1 + 20, tileSize * 1 + 10, bush2.getWidth(), bush2.getHeight(), null);
        g2.drawImage(bush3.getImage(), tileSize * 2, tileSize * 1, bush3.getWidth(), bush3.getHeight(), null);
        g2.drawImage(bush4.getImage(), tileSize * 2 + 15, tileSize * 1 + 5, bush4.getWidth(), bush4.getHeight(), null);
        g2.drawImage(bush5.getImage(), tileSize * 3, tileSize * 1, bush5.getWidth(), bush5.getHeight(), null);
        g2.drawImage(bush1.getImage(), tileSize * 4, tileSize * 2, bush1.getWidth(), bush1.getHeight(), null);
        g2.drawImage(bush2.getImage(), tileSize * 4 + 10, tileSize * 2 + 15, bush2.getWidth(), bush2.getHeight(), null);
        g2.drawImage(bush3.getImage(), tileSize * 5, tileSize * 2, bush3.getWidth(), bush3.getHeight(), null);
        g2.drawImage(bush4.getImage(), tileSize * 5 + 20, tileSize * 2 + 10, bush4.getWidth(), bush4.getHeight(), null);
        g2.drawImage(bush5.getImage(), tileSize * 6, tileSize * 2, bush5.getWidth(), bush5.getHeight(), null);
        g2.drawImage(bush1.getImage(), tileSize * 7, tileSize * 3, bush1.getWidth(), bush1.getHeight(), null);
        g2.drawImage(bush2.getImage(), tileSize * 7 + 15, tileSize * 3 + 5, bush2.getWidth(), bush2.getHeight(), null);
        g2.drawImage(bush3.getImage(), tileSize * 8, tileSize * 3, bush3.getWidth(), bush3.getHeight(), null);
        g2.drawImage(bush4.getImage(), tileSize * 8 + 20, tileSize * 3 + 10, bush4.getWidth(), bush4.getHeight(), null);
        g2.drawImage(bush5.getImage(), tileSize * 9, tileSize * 3, bush5.getWidth(), bush5.getHeight(), null);
        g2.drawImage(bush1.getImage(), tileSize * 10, tileSize * 4, bush1.getWidth(), bush1.getHeight(), null);
        g2.drawImage(bush2.getImage(), tileSize * 10 + 15, tileSize * 4 + 5, bush2.getWidth(), bush2.getHeight(), null);
        g2.drawImage(bush3.getImage(), tileSize * 11, tileSize * 4, bush3.getWidth(), bush3.getHeight(), null);
        g2.drawImage(bush4.getImage(), tileSize * 11 + 20, tileSize * 4 + 10, bush4.getWidth(), bush4.getHeight(), null);
        g2.drawImage(bush5.getImage(), tileSize * 12, tileSize * 4, bush5.getWidth(), bush5.getHeight(), null);
        g2.drawImage(bush1.getImage(), tileSize * 13, tileSize * 5, bush1.getWidth(), bush1.getHeight(), null);
        g2.drawImage(bush2.getImage(), tileSize * 13 + 15, tileSize * 5 + 5, bush2.getWidth(), bush2.getHeight(), null);
        g2.drawImage(bush3.getImage(), tileSize * 14, tileSize * 5, bush3.getWidth(), bush3.getHeight(), null);
        g2.drawImage(bush4.getImage(), tileSize * 14 + 20, tileSize * 5 + 10, bush4.getWidth(), bush4.getHeight(), null);
        g2.drawImage(bush5.getImage(), tileSize * 15, tileSize * 5, bush5.getWidth(), bush5.getHeight(), null);
        g2.drawImage(bush1.getImage(), tileSize * 0, tileSize * 6, bush1.getWidth(), bush1.getHeight(), null);
        g2.drawImage(bush2.getImage(), tileSize * 1, tileSize * 6, bush2.getWidth(), bush2.getHeight(), null);
        g2.drawImage(bush3.getImage(), tileSize * 0, tileSize * 7, bush3.getWidth(), bush3.getHeight(), null);
        g2.drawImage(bush4.getImage(), tileSize * 1, tileSize * 7, bush4.getWidth(), bush4.getHeight(), null);
        g2.drawImage(bush5.getImage(), tileSize * 2, tileSize * 7, bush5.getWidth(), bush5.getHeight(), null);
        g2.drawImage(bush1.getImage(), tileSize * 9 - 15, tileSize * 3 - 20, bush1.getWidth(), bush1.getHeight(), null);
        g2.drawImage(bush2.getImage(), tileSize * 10, tileSize * 3 - 25, bush2.getWidth(), bush2.getHeight(), null);
        g2.drawImage(bush3.getImage(), tileSize * 9 + 30, tileSize * 2 - 15, bush3.getWidth(), bush3.getHeight(), null);
        g2.drawImage(bush4.getImage(), tileSize * 11 - 10, tileSize * 2 - 5, bush4.getWidth(), bush4.getHeight(), null);
        g2.drawImage(bush5.getImage(), tileSize * 10 + 20, tileSize * 2 - 30, bush5.getWidth(), bush5.getHeight(), null);
        g2.drawImage(bush1.getImage(), tileSize * 9 - 25, tileSize * 1 - 20, bush1.getWidth(), bush1.getHeight(), null);
        g2.drawImage(bush2.getImage(), tileSize * 10 - 5, tileSize * 1 - 15, bush2.getWidth(), bush2.getHeight(), null);
        g2.drawImage(bush3.getImage(), tileSize * 9 + 35, tileSize * 0 - 10, bush3.getWidth(), bush3.getHeight(), null);
        g2.drawImage(bush4.getImage(), tileSize * 11 - 20, tileSize * 0 - 5, bush4.getWidth(), bush4.getHeight(), null);
        g2.drawImage(bush5.getImage(), tileSize * 10 + 25, tileSize * 0 - 25, bush5.getWidth(), bush5.getHeight(), null);
        g2.drawImage(bush1.getImage(), tileSize * 9 - 10, tileSize * 5 + 15, bush1.getWidth(), bush1.getHeight(), null);
        g2.drawImage(bush2.getImage(), tileSize * 10 + 20, tileSize * 5 + 5, bush2.getWidth(), bush2.getHeight(), null);
        g2.drawImage(bush3.getImage(), tileSize * 9 + 30, tileSize * 6 + 10, bush3.getWidth(), bush3.getHeight(), null);
        g2.drawImage(bush4.getImage(), tileSize * 11 - 15, tileSize * 6 + 25, bush4.getWidth(), bush4.getHeight(), null);
        g2.drawImage(bush5.getImage(), tileSize * 10 + 5, tileSize * 7 + 12, bush5.getWidth(), bush5.getHeight(), null);
        g2.drawImage(skull.getImage(), tileSize * 9 - 20, tileSize * 4 - 15, skull.getWidth(), skull.getHeight(), null);
        g2.drawImage(skull.getImage(), tileSize * 10 + 25, tileSize * 4 - 10, skull.getWidth(), skull.getHeight(), null);
        g2.drawImage(skull.getImage(), tileSize * 9 + 10, tileSize * 6 + 5, skull.getWidth(), skull.getHeight(), null);
        g2.drawImage(skull.getImage(), tileSize * 11 - 15, tileSize * 6 + 20, skull.getWidth(), skull.getHeight(), null);
        g2.drawImage(skull.getImage(), tileSize * 5 + 12, tileSize * 1 + 8, skull.getWidth(), skull.getHeight(), null);
        g2.drawImage(skull.getImage(), tileSize * 12 + 18, tileSize * 3 + 5, skull.getWidth(), skull.getHeight(), null);
        g2.drawImage(skull.getImage(), tileSize * 7 + 25, tileSize * 5 + 15, skull.getWidth(), skull.getHeight(), null);
        g2.drawImage(skull.getImage(), tileSize * 2 + 30, tileSize * 7 + 12, skull.getWidth(), skull.getHeight(), null);
        g2.drawImage(skull.getImage(), tileSize * 13 + 10, tileSize * 1 + 25, skull.getWidth(), skull.getHeight(), null);
        g2.drawImage(tree3.getImage(), tileSize * 0 + 12, tileSize * 6 + 5, tree3.getWidth() * 85 / 100, tree3.getHeight() * 85 / 100, null);
        g2.drawImage(tree4.getImage(), tileSize * 2 + 25, tileSize * 7 + 18, tree4.getWidth() * 85 / 100, tree4.getHeight() * 85 / 100, null);
        g2.drawImage(tree3.getImage(), tileSize * 12 + 8, tileSize * 0 + 5, tree3.getWidth() * 85 / 100, tree3.getHeight() * 85 / 100, null);
        g2.drawImage(tree4.getImage(), tileSize * 14 + 12, tileSize * 1 + 25, tree4.getWidth() * 85 / 100, tree4.getHeight() * 85 / 100, null);
        g2.drawImage(tree3.getImage(), tileSize * 7 + 10, tileSize * 2 + 5, tree3.getWidth() * 85 / 100, tree3.getHeight() * 85 / 100, null);
        g2.drawImage(tree4.getImage(), tileSize * 7 - 12, tileSize * 4 + 22, tree4.getWidth() * 85 / 100, tree4.getHeight() * 85 / 100, null);
        g2.drawImage(tree3.getImage(), tileSize * 7 + 18, tileSize * 6 + 15, tree3.getWidth() * 85 / 100, tree3.getHeight() * 85 / 100, null);
        g2.drawImage(tree3.getImage(), tileSize * 9 - 20, tileSize * 4 - 10, tree3.getWidth() * 9 / 10, tree3.getHeight() * 9 / 10, null);
        g2.drawImage(tree4.getImage(), tileSize * 9 - 5, tileSize * 4 - 15, tree4.getWidth() * 9 / 10, tree4.getHeight() * 9 / 10, null);
        g2.drawImage(tree3.getImage(), tileSize * 10 + 15, tileSize * 4 - 5, tree3.getWidth() * 9 / 10, tree3.getHeight() * 9 / 10, null);
        g2.drawImage(tree4.getImage(), tileSize * 10 + 25, tileSize * 5 + 8, tree4.getWidth() * 9 / 10, tree4.getHeight() * 9 / 10, null);
        g2.drawImage(tree3.getImage(), tileSize * 11 - 10, tileSize * 5 + 20, tree3.getWidth() * 9 / 10, tree3.getHeight() * 9 / 10, null);
        g2.drawImage(tree4.getImage(), tileSize * 9 - 15, tileSize * 2 - 20, tree4.getWidth() * 9 / 10, tree4.getHeight() * 9 / 10, null);
        g2.drawImage(tree3.getImage(), tileSize * 9 + 10, tileSize * 2 - 25, tree3.getWidth() * 9 / 10, tree3.getHeight() * 9 / 10, null);
        g2.drawImage(tree4.getImage(), tileSize * 10 + 20, tileSize * 1 - 15, tree4.getWidth() * 9 / 10, tree4.getHeight() * 9 / 10, null);
        g2.drawImage(tree3.getImage(), tileSize * 11 - 5, tileSize * 1 - 5, tree3.getWidth() * 9 / 10, tree3.getHeight() * 9 / 10, null);
        g2.drawImage(tree4.getImage(), tileSize * 11 + 15, tileSize * 2 - 10, tree4.getWidth() * 9 / 10, tree4.getHeight() * 9 / 10, null);
        g2.drawImage(tree3.getImage(), tileSize * 9 - 10, tileSize * 6 + 15, tree3.getWidth() * 9 / 10, tree3.getHeight() * 9 / 10, null);
        g2.drawImage(tree4.getImage(), tileSize * 9 + 20, tileSize * 6 + 25, tree4.getWidth() * 9 / 10, tree4.getHeight() * 9 / 10, null);
        g2.drawImage(tree3.getImage(), tileSize * 10 + 5, tileSize * 7 + 12, tree3.getWidth() * 9 / 10, tree3.getHeight() * 9 / 10, null);
        g2.drawImage(tree4.getImage(), tileSize * 11 - 15, tileSize * 7 + 20, tree4.getWidth() * 9 / 10, tree4.getHeight() * 9 / 10, null);
        g2.drawImage(tree3.getImage(), tileSize * 11 + 25, tileSize * 6 + 30, tree3.getWidth() * 9 / 10, tree3.getHeight() * 9 / 10, null);
        g2.drawImage(bush1.getImage(), tileSize * 12, tileSize * 0, bush1.getWidth(), bush1.getHeight(), null);
        g2.drawImage(bush2.getImage(), tileSize * 13, tileSize * 0, bush2.getWidth(), bush2.getHeight(), null);
        g2.drawImage(bush3.getImage(), tileSize * 12, tileSize * 1, bush3.getWidth(), bush3.getHeight(), null);
        g2.drawImage(bush4.getImage(), tileSize * 13, tileSize * 1, bush4.getWidth(), bush4.getHeight(), null);
        g2.drawImage(bush5.getImage(), tileSize * 14, tileSize * 1, bush5.getWidth(), bush5.getHeight(), null);
        g2.drawImage(bush1.getImage(), tileSize * 7, tileSize * 2, bush1.getWidth(), bush1.getHeight(), null);
        g2.drawImage(bush2.getImage(), tileSize * 7, tileSize * 3, bush2.getWidth(), bush2.getHeight(), null);
        g2.drawImage(bush3.getImage(), tileSize * 7, tileSize * 4, bush3.getWidth(), bush3.getHeight(), null);
        g2.drawImage(bush4.getImage(), tileSize * 7, tileSize * 5, bush4.getWidth(), bush4.getHeight(), null);
        g2.drawImage(bush5.getImage(), tileSize * 7, tileSize * 6, bush5.getWidth(), bush5.getHeight(), null);
        g2.drawImage(bush1.getImage(), tileSize * 7, tileSize * 7, bush1.getWidth(), bush1.getHeight(), null);
        g2.drawImage(tree1.getImage(), tileSize * 0, tileSize * 0 - 40, tree1.getWidth(), tree1.getHeight(), null);
        g2.drawImage(tree1.getImage(), tileSize * 1, tileSize * 0 - 30, tree1.getWidth(), tree1.getHeight(), null);
        g2.drawImage(tree1.getImage(), tileSize * 1 + 20, tileSize * 0 - 20, tree1.getWidth(), tree1.getHeight(), null);
        g2.drawImage(tree1.getImage(), tileSize * 2, tileSize * 0 - 35, tree1.getWidth(), tree1.getHeight(), null);
        g2.drawImage(tree1.getImage(), tileSize * 2 + 20, tileSize * 0 - 40, tree1.getWidth(), tree1.getHeight(), null);
        g2.drawImage(tree1.getImage(), tileSize * 3, tileSize * 0 - 50, tree1.getWidth(), tree1.getHeight(), null);
        g2.drawImage(tree1.getImage(), tileSize * 4, tileSize * 0 - 70, tree1.getWidth(), tree1.getHeight(), null);
        g2.drawImage(tree1.getImage(), tileSize * 0 - 20, tileSize * 1, tree1.getWidth(), tree1.getHeight(), null);
        g2.drawImage(tree1.getImage(), tileSize * 0 - 20, tileSize * 1 - 30, tree1.getWidth(), tree1.getHeight(), null);
        g2.drawImage(tree1.getImage(), tileSize * 0 - 20, tileSize * 5, tree1.getWidth(), tree1.getHeight(), null);
        g2.drawImage(tree1.getImage(), tileSize * 0 - 30, tileSize * 6 + 30, tree1.getWidth(), tree1.getHeight(), null);
        g2.drawImage(tree1.getImage(), tileSize * 0 - 10, tileSize * 6, tree1.getWidth(), tree1.getHeight(), null);
        g2.drawImage(tree1.getImage(), tileSize * 0 - 5, tileSize * 7, tree1.getWidth(), tree1.getHeight(), null);
        g2.drawImage(tree1.getImage(), tileSize * 0 - 10, tileSize * 7 - 30, tree1.getWidth(), tree1.getHeight(), null);
        g2.drawImage(tree1.getImage(), tileSize * 0 - 30, tileSize * 7 + 30, tree1.getWidth(), tree1.getHeight(), null);
        g2.drawImage(bush1.getImage(), tileSize * 1, tileSize * 4, bush1.getWidth(), bush1.getHeight(), null);
        g2.drawImage(bush2.getImage(), tileSize * 3, tileSize * 5, bush2.getWidth(), bush2.getHeight(), null);
        g2.drawImage(bush3.getImage(), tileSize * 2, tileSize * 7, bush3.getWidth(), bush3.getHeight(), null);
        g2.drawImage(bush4.getImage(), tileSize * 2, tileSize * 8, bush4.getWidth(), bush4.getHeight(), null);
        g2.drawImage(bush5.getImage(), tileSize * 2, tileSize * 7, bush5.getWidth(), bush5.getHeight(), null);
        g2.drawImage(bush1.getImage(), tileSize * 0, tileSize * 6, bush1.getWidth(), bush1.getHeight(), null);
        g2.drawImage(bush2.getImage(), tileSize * 1, tileSize * 6, bush2.getWidth(), bush2.getHeight(), null);
        g2.drawImage(bush3.getImage(), tileSize * 0, tileSize * 7, bush3.getWidth(), bush3.getHeight(), null);
        g2.drawImage(bush4.getImage(), tileSize * 1, tileSize * 7, bush4.getWidth(), bush4.getHeight(), null);
        g2.drawImage(bush5.getImage(), tileSize * 2, tileSize * 7, bush5.getWidth(), bush5.getHeight(), null);
        g2.drawImage(bush1.getImage(), tileSize * 9 - 30, tileSize * 4, bush1.getWidth(), bush1.getHeight(), null);
        g2.drawImage(bush2.getImage(), tileSize * 9 - 20, tileSize * 4 + 20, bush2.getWidth(), bush2.getHeight(), null);
        g2.drawImage(bush3.getImage(), tileSize * 8, tileSize * 4 + 30, bush3.getWidth(), bush3.getHeight(), null);
        g2.drawImage(bush4.getImage(), tileSize * 9 + 40, tileSize * 4, bush4.getWidth(), bush4.getHeight(), null);
        g2.drawImage(bush5.getImage(), tileSize * 9 + 50, tileSize * 4 + 50, bush5.getWidth(), bush5.getHeight(), null);

        g2.drawImage(tree1.getImage(), tileSize * 0 - 20, tileSize * 2 + 10, tree1.getWidth() * 1, tree1.getHeight() * 2, null);
        g2.drawImage(house.getImage(), tileSize * 0 - 20, tileSize * 3 + 30, house.getWidth() * 1 + 20, house.getHeight() * 1 + 20, null);
        g2.drawImage(bridge.getImage(), tileSize * 5 - 10, tileSize * 1 + 3, bridge.getWidth() + 20, bridge.getHeight() + 10, null);
        g2.drawImage(bridge.getImage(), tileSize * 12 - 10, tileSize * 1 + 3, bridge.getWidth() + 20, bridge.getHeight() + 10, null);
        g2.drawImage(bridge.getImage(), tileSize * 12 - 10, tileSize * 3 + 3, bridge.getWidth() + 20, bridge.getHeight() + 10, null);
        g2.drawImage(bridge.getImage(), tileSize * 6 - 10, tileSize * 3 + 3, bridge.getWidth() + 20, bridge.getHeight() + 10, null);
        g2.drawImage(bridge.getImage(), tileSize * 7 - 10, tileSize * 5 + 3, bridge.getWidth() + 20, bridge.getHeight() + 10, null);
        g2.drawImage(bridge.getImage(), tileSize * 12 - 10, tileSize * 5 + 3, bridge.getWidth() + 20, bridge.getHeight() + 10, null);
        g2.drawImage(house.getImage(), tileSize * 9, tileSize * 4 + 30, house.getWidth(), house.getHeight(), null);
        g2.drawImage(tree1.getImage(), tileSize * 0 - 20, tileSize * 3 + 10, tree1.getWidth() * 1, tree1.getHeight() * 2, null);
        g2.drawImage(tree1.getImage(), tileSize * 14 - 40, tileSize * 0 - 40, tree1.getWidth(), tree1.getHeight(), null);
        g2.drawImage(tree1.getImage(), tileSize * 12 + 30, tileSize * 0 - 15, tree1.getWidth(), tree1.getHeight(), null);
        g2.drawImage(tree1.getImage(), tileSize * 14 - 10, tileSize * 0 - 10, tree1.getWidth(), tree1.getHeight(), null);
        g2.drawImage(tree1.getImage(), tileSize * 11 - 15, tileSize * 0 - 40, tree1.getWidth(), tree1.getHeight(), null);
        g2.drawImage(bush5.getImage(), tileSize * 13, tileSize * 4, bush5.getWidth(), bush5.getHeight(), null);
        g2.drawImage(bush1.getImage(), tileSize * 2, tileSize * 8, bush1.getWidth(), bush1.getHeight(), null);
        g2.drawImage(bush2.getImage(), tileSize * 5, tileSize * 6, bush2.getWidth(), bush2.getHeight(), null);
        g2.drawImage(bush3.getImage(), tileSize * 11, tileSize * 7, bush3.getWidth(), bush3.getHeight(), null);
        g2.drawImage(bush4.getImage(), tileSize * 4, tileSize * 7, bush4.getWidth(), bush4.getHeight(), null);
        g2.drawImage(bush1.getImage(), tileSize * 3, tileSize * 4, bush1.getWidth(), bush1.getHeight(), null);
        g2.drawImage(bush2.getImage(), tileSize * 8, tileSize * 2, bush2.getWidth(), bush2.getHeight(), null);
        g2.drawImage(bush3.getImage(), tileSize * 2, tileSize * 7, bush3.getWidth(), bush3.getHeight(), null);
        g2.drawImage(bush4.getImage(), tileSize * 1, tileSize * 9, bush4.getWidth(), bush4.getHeight(), null);
        g2.drawImage(bush1.getImage(), tileSize * 5, tileSize * 0, bush1.getWidth(), bush1.getHeight(), null);
        g2.drawImage(bush2.getImage(), tileSize * 7, tileSize * 0, bush2.getWidth(), bush2.getHeight(), null);
        g2.drawImage(bush3.getImage(), tileSize * 4, tileSize * 0, bush3.getWidth(), bush3.getHeight(), null);
        g2.drawImage(bush4.getImage(), tileSize * 8, tileSize * 0, bush4.getWidth(), bush4.getHeight(), null);
        g2.drawImage(bush1.getImage(), tileSize * 3, tileSize * 4, bush1.getWidth(), bush1.getHeight(), null);
        g2.drawImage(bush2.getImage(), tileSize * 8, tileSize * 2, bush2.getWidth(), bush2.getHeight(), null);
        g2.drawImage(bush3.getImage(), tileSize * 2, tileSize * 7, bush3.getWidth(), bush3.getHeight(), null);
        g2.drawImage(bush4.getImage(), tileSize * 1, tileSize * 1, bush4.getWidth(), bush4.getHeight(), null);
        g2.drawImage(tree1.getImage(), tileSize * 3 - 100, tileSize * 5 + 40, tree1.getWidth(), tree1.getHeight(), null);
        g2.drawImage(tree1.getImage(), tileSize * 4, tileSize * 0 - 30, tree1.getWidth(), tree1.getHeight(), null);
        g2.drawImage(tree1.getImage(), tileSize * 2, tileSize * 0, tree1.getWidth(), tree1.getHeight(), null);
        g2.drawImage(tree1.getImage(), tileSize * 1, tileSize * 9, tree1.getWidth(), tree1.getHeight(), null);
        g2.drawImage(tree2.getImage(), tileSize * 5, tileSize * 4 - 20, tree2.getWidth(), tree2.getHeight(), null);
        g2.drawImage(tree2.getImage(), tileSize * 7, tileSize * 4 - 20, tree2.getWidth(), tree2.getHeight(), null);
        g2.drawImage(tree2.getImage(), tileSize * 4, tileSize * 2, tree2.getWidth(), tree2.getHeight(), null);
        g2.drawImage(tree2.getImage(), tileSize * 10, tileSize * 4 - 20, tree2.getWidth(), tree2.getHeight(), null);
        g2.drawImage(tree2.getImage(), tileSize * 15, tileSize * 5 - 20, tree2.getWidth(), tree2.getHeight(), null);
        g2.drawImage(tree2.getImage(), tileSize * 14 + 30, tileSize * 4 - 20, tree2.getWidth(), tree2.getHeight(), null);
        g2.drawImage(tree2.getImage(), tileSize * 10, tileSize * -20, tree2.getWidth(), tree2.getHeight(), null);
        g2.drawImage(tree2.getImage(), tileSize * 14 + 30, tileSize * 7 - 20, tree2.getWidth(), tree2.getHeight(), null);
        g2.drawImage(bush2.getImage(), tileSize * 14 + 30, tileSize * 7 + 50, bush2.getWidth(), bush2.getHeight(), null);
        g2.drawImage(bush2.getImage(), tileSize * 14 + 10, tileSize * 7 - 50, bush2.getWidth(), bush2.getHeight(), null);

//        for (SolidArea solidAsset : allSolidArea) {
//            g2.setColor(Color.red);
//            g2.fill(solidAsset.getSolidArea());
//        }
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

    public ArrayList<Tower> getAllTower() {
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

    public ArrayList<Enemy> getAllEnemy() {
        return allEnemy;
    }

    public double getScale() {
        return scale;
    }

    public LoadData getLoadData() {
        return loadData;
    }

    public CharacterBox[] getAllCharacterBox() {
        return allCharacterBox;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void nextLevel() {
        if (level < 4) {
            level += 1;
            currentWave = 0;
            allTower.clear();
            allEnemy.clear();
            isEverLost = false;
            isEverWin = false;
            gameEnd.setIsWin(false);
            gameEnd.setIsFinishing(false);
            coin = 300;
            baseHP = 1000;
            tileManager.loadMap();
            tileManager.getTileImage();
            isStartWave = false;
            countStartWave = 0;
            setAllSolidArea();
        } else {
            gameThread = null;
            controller.returnToMenu();
        }
    }

    public void replay() {
        currentWave = 0;
        allTower.clear();
        allEnemy.clear();
        coin = 300;
        baseHP = 1000;
        isEverLost = false;
        isEverWin = false;
        isStartWave = false;
        countStartWave = 0;
        gameEnd.setIsWin(false);
        gameEnd.setIsFinishing(false);
        setAllSolidArea();
    }

    public ArrayList<SolidArea> getAllSolidAsset() {
        return allSolidArea;
    }

    public int getFPS() {
        return FPS;
    }

    public ArrayList<Particle> getAllParticle() {
        return allParticle;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public Sound getSound() {
        return sound;
    }

    public Controller getController() {
        return controller;
    }

    public void stopGameAndReturnToMenu() {
        gameThread = null;
        controller.returnToMenu();
        backGroundSound.stop();
    }

    public ArrayList<SolidArea> getAllSolidArea() {
        return allSolidArea;
    }
}

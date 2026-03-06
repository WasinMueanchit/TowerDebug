import javax.swing.*;
import java.awt.*;
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
    
    //Level Setting
    private int level = 1;
    private Point[] waypointsLevel1 = { new Point(0, tileSize * 3), new Point(tileSize * 3, tileSize * 5), new Point(tileSize * 6, tileSize * 1), new Point(tileSize * 9, tileSize * 6), new Point(tileSize * 12, tileSize * 3), new Point(tileSize * 15, tileSize * 3)};
    private Point[] waypointsLevel2 = { new Point(0, 0), new Point(100, 0), new Point(100, -100) };
    private Point[] waypointsLevel3 = { new Point(0, 0), new Point(100, 0), new Point(100, -100) };
    private Point[] waypointsLevel4 = { new Point(0, 0), new Point(100, 0), new Point(100, -100) };
    
    //Event Handler
    private MouseHandler mouseMotionHandler;
    
    //Monster
    private EnemyRun enemyRun = new EnemyRun(this, waypointsLevel1, 2);
    
    //Tile
    private TileManager tileManager = new TileManager(this);
    
    public GamePanel(){

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        gameThread = new Thread(this);
        gameThread.start();
        
        mouseMotionHandler = new MouseHandler(this);
        this.addMouseMotionListener(mouseMotionHandler);
        this.setFocusable(true);
        
    }

    @Override
    public void run() {
        while (gameThread != null){
            update();
            repaint();
            try{
                Thread.sleep(1000/60);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    public void update(){
        enemyRun.update();
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        tileManager.draw(g2);
        enemyRun.draw(g2);
//        g2.setColor(Color.RED);
//        g2.fillRect(mouseX, mouseY, tileSize, tileSize);
        g2.dispose();
    }
    
    public void setMouseX(int x){
        this.mouseX = x; 
    }
    
    public int getMouseX(){
        return mouseX;
    }
    
    public void setMouseY(int y){
        this.mouseY = y; 
    }
    
    public int getMouseY(){
        return mouseY; 
    }
    
    public int getTileSize(){
        return tileSize;
    }
    
    public int getLevel(){
        return level;
    }
    
    public int getMaxScreenCol(){
        return maxScreenCol;
    }
    
    public int getMaxScreenRow(){
        return maxScreenRow;
    }
    
}

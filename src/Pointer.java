
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Pointer implements MouseMotionListener, MouseListener, KeyListener {

    private GamePanel gamePanel;
    private int x = 0;
    private int y = 0;
    private String characterSelected = "";
    private boolean collisionOn = false;
    private String holdCharacterBox = "";
    private Rectangle pointerArea;
    private Rectangle solidArea;
    private CollisionChecker collisionChecker;
    private Assasin towerOnHold;
    private Assasin towerOnShowUpgrade;
    private UpgradeUI upgradeUI;       
    private boolean holdOnUpgradeUI = false;
    private boolean holdOnUpgradeButton = false;
    private boolean holdOnSellButton = false;
    private GameEnd gameEnd;
    private boolean holdOnToMenuButton = false;
    private boolean holdOnNextLevelButton = false;

    public Pointer(GamePanel gamePanel, GameEnd gameEnd) {
        this.gamePanel = gamePanel;
        this.pointerArea = new Rectangle(x, y, 10, 10);
        this.collisionChecker = gamePanel.getCollisionChecker();
        this.upgradeUI = new UpgradeUI();
        this.gameEnd = gameEnd;
    }

    public void update() {
        this.pointerArea = new Rectangle(x, y, 10, 10);
        collisionChecker.checkCharacterBox(this); //Check is pointer on characterbox
        collisionChecker.checkCharacter(this); //Check is pointer on any character
        collisionChecker.checkUpgradeUI(this); //Check is pointer on upgradeUI;
        collisionChecker.checkGameEnd(this); //Check is pointer on game end ui;
    }

    public void draw(Graphics2D g2){
        drawCharacterSelected(g2);
        if(towerOnShowUpgrade != null){
            upgradeUI.setCharacter(towerOnShowUpgrade);
            upgradeUI.draw(g2);
        }
    }
    
    public void drawCharacterSelected(Graphics2D g2) {
        if (!characterSelected.equals("")) {
            int tileSize = gamePanel.getTileSize();
            int baseRange = 0, solidWidth = 0, solidHeight = 0;
            BufferedImage image = null;
            switch (characterSelected) {
                case "Assasin":
                    baseRange = Assasin.getBaseRange();
                    solidWidth = Assasin.getSolidWidth();
                    solidHeight = Assasin.getSolidHeight();
                    image = gamePanel.getLoadAnimation().getAssasinAnimation()[4][0];
                    break;
            }
            solidArea = new Rectangle(-solidWidth / 2, -solidHeight / 2, solidWidth, solidHeight);
            collisionChecker.checkTile(this);

            //Draw Range
            Ellipse2D attackArea = new Ellipse2D.Double(x - baseRange / 2, y - baseRange / 2, baseRange, baseRange);
            //Draw Color Background
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            if (collisionOn == false) {
                g2.setColor(new Color(59, 188, 122));
                g2.fill(attackArea);
            } else {
                g2.setColor(new Color(206, 67, 98));
                g2.fill(attackArea);
            }
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            //Draw Animation & Image
            g2.drawImage(image, x - tileSize / 2, y - tileSize / 2, tileSize, tileSize, null);
        }
    }
    
    //Setter & Getter
    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public String getCharacterSelected() {
        return characterSelected;
    }

    public Rectangle getSolidArea() {
        return solidArea;
    }

    public boolean getCollisionOn() {
        return collisionOn;
    }

    public void setCollisionOn(boolean collisionOn) {
        this.collisionOn = collisionOn;
    }

    public Rectangle getPointerArea() {
        return pointerArea;
    }

    public void setHoldCharacterBox(String holdCharacterBox) {
        this.holdCharacterBox = holdCharacterBox;
    }

    public void setCharacterSelected(String characterSelected) {
        this.characterSelected = characterSelected;
    }
    
    public void setTowerOnHold(Assasin tower){
        this.towerOnHold = tower;
    }

    //Get coordinates of mouse
    @Override
    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();
    }
    //Place Character && Select CharacterBox
    @Override
    public void mousePressed(MouseEvent e) {
        if(!holdCharacterBox.equals("")){ //When select character on characterbox
            characterSelected = holdCharacterBox;
            towerOnShowUpgrade = null;
        }
        else if(!characterSelected.equals("")){ //When already character and want to place
            if(collisionOn == false){
                ArrayList<Assasin> allTower = gamePanel.getAllTower();
                int towerAmount = gamePanel.getTowerAmount();
                switch(characterSelected){
                    case "Assasin":
                        allTower.add(new Assasin(gamePanel, "Assasin", x, y, 20, 20, 150, "Single"));
                        break;
                }
                gamePanel.setTowerAmount(towerAmount + 1);
                characterSelected = "";
            }
        }else if(towerOnHold != null){
            towerOnShowUpgrade = towerOnHold;
        }else if(towerOnShowUpgrade != null && towerOnHold == null && holdOnUpgradeUI == false){
            towerOnShowUpgrade = null;
        }else if(holdOnUpgradeButton == true){
            towerOnShowUpgrade.levelUp();
        }else if(holdOnSellButton == true && towerOnShowUpgrade != null){
            towerOnShowUpgrade.sell();
            towerOnShowUpgrade = null;
        }else if(holdOnToMenuButton == true){
            gameEnd.setIsFinishing(false);
            gameEnd.setIsFinishing(false);
            holdOnToMenuButton = false;
        }else if(holdOnNextLevelButton == true){
            gameEnd.setIsFinishing(false);
            gamePanel.nextLevel();
            holdOnNextLevelButton = false;
        }
    }
    //Cancle placea charcter
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            characterSelected = "";
        }
    }
    
    //Don't use
    @Override
    public void mouseClicked(MouseEvent e) {
    }
    @Override
    public void mouseReleased(MouseEvent e) {
    }
    @Override
    public void mouseEntered(MouseEvent e) {  
    }
    @Override
    public void mouseExited(MouseEvent e) {
    }
    @Override
    public void mouseDragged(MouseEvent e) {
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyReleased(KeyEvent e) {
    }
    
    public Assasin getTowerOnShowUpgrade(){
        return towerOnShowUpgrade;
    }
    
    public UpgradeUI getUpgradeUI(){
        return upgradeUI;
    }
    
    public void setHoldOnUpgradeUI(boolean holdOnUpgradeUI){
        this.holdOnUpgradeUI = holdOnUpgradeUI;
    }
    
    public void setHoldOnUpgradeButton(boolean holdOnUpgradeButton){
        this.holdOnUpgradeButton = holdOnUpgradeButton;
    }
    
    public void setHoldOnSellButton(boolean holdOnSellButton){
        this.holdOnSellButton = holdOnSellButton;
    }
    
    public void setHoldOnToMenuButton(boolean holdOnToMenuButton){
        this.holdOnToMenuButton = holdOnToMenuButton;
    }
    
    public void setHoldOnNextLevelButton(boolean holdOnNextLevelButton){
        this.holdOnNextLevelButton = holdOnNextLevelButton;
    }
    
    public GameEnd getGameEnd(){
        return gameEnd;
    }
}

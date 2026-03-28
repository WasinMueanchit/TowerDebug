package Game;

import All.Tower.*;
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
    private Tower towerOnHold;
    private Tower towerOnShowUpgrade;
    private UpgradeUI upgradeUI;
    private boolean holdOnUpgradeUI = false;
    private boolean holdOnUpgradeButton = false;
    private boolean holdOnSellButton = false;
    private GameEnd gameEnd;
    private boolean holdOnToMenuButton = false;
    private boolean holdOnNextLevelOrRetryButton = false;

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

    public void draw(Graphics2D g2) {
        drawCharacterSelected(g2);
        if (towerOnShowUpgrade != null) {
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
                case "Gladiator":
                    baseRange = Gladiator.getBaseRange();
                    solidWidth = Gladiator.getSolidWidth();
                    solidHeight = Gladiator.getSolidHeight();
                    image = gamePanel.getLoadData().getAnimation("Gladiator").get("idle").get(0);
                    break;
                case "Valkyrie":
                    baseRange = Valkyrie.getBaseRange();
                    solidWidth = Valkyrie.getSolidWidth();
                    solidHeight = Valkyrie.getSolidHeight();
                    image = gamePanel.getLoadData().getAnimation("Valkyrie").get("idle").get(0);
                    break;
                case "Saolin":
                    baseRange = Saolin.getBaseRange();
                    solidWidth = Saolin.getSolidWidth();
                    solidHeight = Saolin.getSolidHeight();
                    image = gamePanel.getLoadData().getAnimation("Saolin").get("idle").get(0);
                    break;
                case "Ninja":
                    baseRange = Ninja.getBaseRange();
                    solidWidth = Ninja.getSolidWidth();
                    solidHeight = Ninja.getSolidHeight();
                    image = gamePanel.getLoadData().getAnimation("Ninja").get("idle").get(0);
                    break;
                case "Pirate":
                    baseRange = Pirate.getBaseRange();
                    solidWidth = Pirate.getSolidWidth();
                    solidHeight = Pirate.getSolidHeight();
                    image = gamePanel.getLoadData().getAnimation("Pirate").get("idle").get(0);
                    break;
                case "Pharaoh":
                    baseRange = Pharaoh.getBaseRange();
                    solidWidth = Pharaoh.getSolidWidth();
                    solidHeight = Pharaoh.getSolidHeight();
                    image = gamePanel.getLoadData().getAnimation("Pharaoh").get("idle").get(0);
                    break;
            }
            solidArea = new Rectangle(-solidWidth / 2, -solidHeight / 2, solidWidth, solidHeight);
            collisionOn = false;
            collisionChecker.checkTile(this);
            collisionChecker.checkSolidAsset(this);

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

//            g2.fillRect(x - (solidWidth / 2), y - (solidHeight / 2), solidWidth, solidHeight);
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

    public void setTowerOnHold(Tower tower) {
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
        if (!holdCharacterBox.equals("")) { //When select character on characterbox
            characterSelected = holdCharacterBox;
            towerOnShowUpgrade = null;
        } else if (!characterSelected.equals("")) { //When already character and want to place
            if (collisionOn == false) {
                ArrayList<Tower> allTower = gamePanel.getAllTower();
                int towerAmount = gamePanel.getTowerAmount();
                int cost = 0;
                switch (characterSelected) {
                    case "Gladiator":
                        cost = 50;
                        if (gamePanel.getCoin() - cost >= 0) {
                            allTower.add(new Gladiator(gamePanel, x, y));
                            gamePanel.setCoin(gamePanel.getCoin() - cost);
                        }
                        break;
                    case "Valkyrie":
                        cost = 100;
                        if (gamePanel.getCoin() - cost >= 0) {
                            allTower.add(new Valkyrie(gamePanel, x, y));
                            gamePanel.setCoin(gamePanel.getCoin() - cost);
                        }
                        break;
                    case "Saolin":
                        cost = 130;
                        if (gamePanel.getCoin() - cost >= 0) {
                            allTower.add(new Saolin(gamePanel, x, y));
                            gamePanel.setCoin(gamePanel.getCoin() - cost);
                        }
                        break;
                    case "Ninja":
                        cost = 120;
                        if (gamePanel.getCoin() - cost >= 0) {
                            allTower.add(new Ninja(gamePanel, x, y));
                            gamePanel.setCoin(gamePanel.getCoin() - cost);
                        }
                        break;
                    case "Pirate":
                        cost = 150;
                        if (gamePanel.getCoin() - cost >= 0) {
                            allTower.add(new Pirate(gamePanel, x, y));
                            gamePanel.setCoin(gamePanel.getCoin() - cost);
                        }
                        break;
                    case "Pharaoh":
                        cost = 150;
                        if (gamePanel.getCoin() - cost >= 0) {
                            allTower.add(new Pharaoh(gamePanel, x, y));
                            gamePanel.setCoin(gamePanel.getCoin() - cost);
                        }
                        break;
                }
                gamePanel.setTowerAmount(towerAmount + 1);
                characterSelected = "";
                gamePanel.getSound().setSound("placeCharacter");
                gamePanel.getSound().play();
            }
        } else if (towerOnHold != null) {
            gamePanel.getSound().setSound("buttonClick");
            gamePanel.getSound().play();
            towerOnShowUpgrade = towerOnHold;
        } else if (towerOnShowUpgrade != null && towerOnHold == null && holdOnUpgradeUI == false) {
            towerOnShowUpgrade = null;
        } else if (holdOnUpgradeButton == true) {
            gamePanel.getSound().setSound("buttonClick");
            gamePanel.getSound().play();
            towerOnShowUpgrade.levelUp();
        } else if (holdOnSellButton == true && towerOnShowUpgrade != null) {
            gamePanel.getSound().setSound("pickupCoin");
            gamePanel.getSound().play();
            towerOnShowUpgrade.sell();
            towerOnShowUpgrade = null;
        } else if (holdOnToMenuButton == true) {
            gamePanel.getSound().setSound("buttonClick");
            gamePanel.getSound().play();
            gameEnd.setIsFinishing(false);
            gameEnd.setIsFinishing(false);
            holdOnToMenuButton = false;
            gamePanel.stopGameAndReturnToMenu();
        } else if (holdOnNextLevelOrRetryButton == true) {
            if (gameEnd.getIsWin() == true) {
                gameEnd.setIsFinishing(false);
                gamePanel.nextLevel();
                holdOnNextLevelOrRetryButton = false;
            } else {
                gameEnd.setIsFinishing(false);
                gamePanel.replay();
                holdOnNextLevelOrRetryButton = false;
            }
            gamePanel.getSound().setSound("buttonClick");
            gamePanel.getSound().play();
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

    public Tower getTowerOnShowUpgrade() {
        return towerOnShowUpgrade;
    }

    public UpgradeUI getUpgradeUI() {
        return upgradeUI;
    }

    public void setHoldOnUpgradeUI(boolean holdOnUpgradeUI) {
        this.holdOnUpgradeUI = holdOnUpgradeUI;
    }

    public void setHoldOnUpgradeButton(boolean holdOnUpgradeButton) {
        this.holdOnUpgradeButton = holdOnUpgradeButton;
    }

    public void setHoldOnSellButton(boolean holdOnSellButton) {
        this.holdOnSellButton = holdOnSellButton;
    }

    public void setHoldOnToMenuButton(boolean holdOnToMenuButton) {
        this.holdOnToMenuButton = holdOnToMenuButton;
    }

    public void setHoldOnNextLevelOrRetryButton(boolean holdOnNextLevelButton) {
        this.holdOnNextLevelOrRetryButton = holdOnNextLevelButton;
    }

    public GameEnd getGameEnd() {
        return gameEnd;
    }
}

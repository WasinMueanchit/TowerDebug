package Game;


import All.Enemy.Enemy;
import All.Enemy.FemaleGoblin;
import All.Tower.Tower;
import java.awt.Rectangle;
import java.util.ArrayList;

public class CollisionChecker {

    GamePanel gamePanel;

    public CollisionChecker(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    //check is pointer on any road;
    public void checkTile(Pointer pointer) {
        if(pointer.getSolidArea() != null){
            int pointerLeft = pointer.getX() + pointer.getSolidArea().x;
            int pointerRight = pointer.getX() + pointer.getSolidArea().x + pointer.getSolidArea().width;
            int pointerTop = pointer.getY() + pointer.getSolidArea().y;
            int pointerBottom = pointer.getY() + pointer.getSolidArea().y + pointer.getSolidArea().height;

            int tileSize = gamePanel.getTileSize();
            int pointerLeftCol = pointerLeft / tileSize;
            int pointerRightCol = pointerRight / tileSize;
            int pointerTopRow = pointerTop / tileSize;
            int pointerBottomRow = pointerBottom / tileSize;

            int tileNum1, tileNum2, tileNum3, tileNum4;
            int[][] mapTileNum = gamePanel.getTileManager().getMapTileNum();
            if (pointerLeftCol < gamePanel.getMaxScreenCol() && pointerRightCol < gamePanel.getMaxScreenCol()
                    && pointerTopRow < gamePanel.getMaxScreenRow() && pointerBottomRow < gamePanel.getMaxScreenRow()) {

                tileNum1 = mapTileNum[pointerLeftCol][pointerTopRow];
                tileNum2 = mapTileNum[pointerRightCol][pointerTopRow];
                tileNum3 = mapTileNum[pointerLeftCol][pointerBottomRow];
                tileNum4 = mapTileNum[pointerRightCol][pointerBottomRow];

                Tile[] tile = gamePanel.getTileManager().getTile();

                if (tile[tileNum1].getCollision() == true
                        || tile[tileNum2].getCollision() == true
                        || tile[tileNum3].getCollision() == true
                        || tile[tileNum4].getCollision() == true) {

                    pointer.setCollisionOn(true);

                }
            }
        }
    }

    //check is enemy on any tower;
    public void checkEntity(Tower tower) {
        tower.getEnemyInArea().clear();
        ArrayList<Enemy> allEnemy = gamePanel.getAllEnemy();
        for (int i = 0; i < allEnemy.size(); i++) {
            Enemy enemy = allEnemy.get(i);
            if(enemy.getIsGhost() == true){
                if (tower.getAttackArea().intersects(allEnemy.get(i).getSolidArea()) && tower.getIsGhostBuster() == true) {
                    if (!tower.getEnemyInArea().contains(enemy)) {
                        tower.getEnemyInArea().add(enemy);
                    }
                }
            }else{                
                if (tower.getAttackArea().intersects(allEnemy.get(i).getSolidArea())) {
                    if (!tower.getEnemyInArea().contains(enemy)) {
                        tower.getEnemyInArea().add(enemy);
                    }
                }
            }
        }
    }

    //check is pointer on characterbox;
    public void checkCharacterBox(Pointer pointer) {
        CharacterBox[] allCharacterBox = gamePanel.getAllCharacterBox();
        pointer.setHoldCharacterBox("");
        for (int i = 0; i < allCharacterBox.length; i++) {
            if (allCharacterBox[i] != null) {
                CharacterBox characterBox = allCharacterBox[i];
                if (characterBox.getArea().intersects(pointer.getPointerArea())) {
                    String name = characterBox.getName();
                    pointer.setHoldCharacterBox(name);
                }
            }
        }
    }

    //check is pointer on character;
    public void checkCharacter(Pointer pointer) {
        ArrayList<Tower> allTower = gamePanel.getAllTower();
        pointer.setTowerOnHold(null);

        for (Tower tower : allTower) {
            if (tower.getPlacedSolidArea().intersects(pointer.getPointerArea())) {
                pointer.setTowerOnHold(tower);
                break;
            }
        }
    }

    public void checkUpgradeUI(Pointer pointer) {
        if (pointer.getTowerOnShowUpgrade() != null) {
            if (pointer.getUpgradeUI().getUpgradeUI().intersects(pointer.getPointerArea())) {
                pointer.setHoldOnUpgradeUI(true);
                if (pointer.getUpgradeUI().getUpgradeButton().intersects(pointer.getPointerArea())) {
                    pointer.setHoldOnUpgradeButton(true);
                } else {
                    pointer.setHoldOnUpgradeButton(false);
                }
                if (pointer.getUpgradeUI().getSellButton().intersects(pointer.getPointerArea())) {
                    pointer.setHoldOnSellButton(true);
                } else {
                    pointer.setHoldOnSellButton(false);
                }
            } else {
                pointer.setHoldOnUpgradeUI(false);
            }
        }
    }

    public void checkGameEnd(Pointer pointer) {
        if (pointer.getGameEnd().getIsFinishing() == true) {
            if (pointer.getGameEnd().getReturnToMenuButton().intersects(pointer.getPointerArea())) {
                pointer.setHoldOnToMenuButton(true);
            } else {
                pointer.setHoldOnToMenuButton(false);
            }
            if (pointer.getGameEnd().getNextLevelOrRetryButton().intersects(pointer.getPointerArea())) {
                pointer.setHoldOnNextLevelOrRetryButton(true);
            } else {
                pointer.setHoldOnNextLevelOrRetryButton(false);
            }
        }
    }

    public void checkSolidAsset(Pointer pointer) {
        if (!pointer.getCharacterSelected().equals("") && pointer.getSolidArea() != null) {
            ArrayList<SolidArea> allSolidAsset = gamePanel.getAllSolidAsset();
            for (SolidArea solidAsset : allSolidAsset) {
                int width = (int) pointer.getSolidArea().getWidth();
                int height = (int) pointer.getSolidArea().getHeight();
                Rectangle characterSolidArea = new Rectangle(pointer.getX() - (width / 2), pointer.getY() - (height / 2), width, height);
                if (characterSolidArea.intersects(solidAsset.getSolidArea())) {
                    pointer.setCollisionOn(true);
                    break;
                }
            }
        }
    }
}


import java.util.ArrayList;

public class CollisionChecker {

    GamePanel gamePanel;

    public CollisionChecker(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    //check is pointer on any road;
    public void checkTile(Pointer pointer) {
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

            } else {
                pointer.setCollisionOn(false);
            }
        }
    }

    //check is enemy on any hero;
    public void checkEntity(Assasin assasin) {
        assasin.getEnemyInArea().clear();
        ArrayList<FemaleGoblin> allEnemy = gamePanel.getAllEnemy();
        for (int i = 0; i < allEnemy.size(); i++) {
            FemaleGoblin enemy = allEnemy.get(i);
            if (assasin.getAttackArea().intersects(allEnemy.get(i).getSolidArea())) {
                if (!assasin.getEnemyInArea().contains(enemy)) {
                    assasin.getEnemyInArea().add(enemy);
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
        ArrayList<Assasin> allTower = gamePanel.getAllTower();
        pointer.setTowerOnHold(null);

        for (Assasin tower : allTower) {
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
                if(pointer.getUpgradeUI().getUpgradeButton().intersects(pointer.getPointerArea())){
                    pointer.setHoldOnUpgradeButton(true);
                }else{
                    pointer.setHoldOnUpgradeButton(false);
                }
                if(pointer.getUpgradeUI().getSellButton().intersects(pointer.getPointerArea())){
                    pointer.setHoldOnSellButton(true);
                }else{
                    pointer.setHoldOnSellButton(false);
                }
            } else {
                pointer.setHoldOnUpgradeUI(false);
            }
        }
    }
}

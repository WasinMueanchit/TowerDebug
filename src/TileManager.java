
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;


public class TileManager {
    GamePanel gamePanel;
    Tile[] tile;
    int[][] mapTileNum;
    
    public TileManager(GamePanel gamePanel){
        this.gamePanel = gamePanel;
        mapTileNum = new int[gamePanel.getMaxScreenCol()][gamePanel.getMaxScreenRow()];
        getTileImage();
        loadMap();
    }
    
    // Import code for tileset
    public void loadMap(){
        try{
            int level = gamePanel.getLevel();
            InputStream iS = getClass().getResourceAsStream("/source/Tiles/0" + level + "/code.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(iS));
            
            int col = 0;
            int row = 0;
            
            while (col < gamePanel.getMaxScreenCol() && row < gamePanel.getMaxScreenRow()){
                String line = br.readLine();
                while (col < gamePanel.getMaxScreenCol()){
                    String[] numbers = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[col][row] = num;
                    col++;
                }
                if(col == gamePanel.getMaxScreenCol()){
                    col = 0;
                    row++;
                }
            }
            br.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    // Import tileset
    public void getTileImage(){
        try{
            int level = gamePanel.getLevel();
            switch(level){
                case 1:
                    tile = new Tile[13];
                    for (int i = 0; i < 13; i++){
                        BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/source/Tiles/01/Images/" + i + ".png"));
                        if(i == 0){
                            tile[i] = new Tile(image, false);
                        }else{
                            tile[i] = new Tile(image, true);
                        }
                    }
                    break;
                case 2:
                    tile = new Tile[7];
                    for (int i = 0; i < 7; i++){
                        BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/source/Tiles/02/Images/" + i + ".png"));
                        if(i == 0){
                            tile[i] = new Tile(image, false);
                        }else{
                            tile[i] = new Tile(image, true);
                        }
                    }
                    break;
                case 3:
                    tile = new Tile[7];
                    for (int i = 0; i < 7; i++){
                        BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/source/Tiles/03/Images/" + i + ".png"));
                        if(i == 0){
                            tile[i] = new Tile(image, false);
                        }else{
                            tile[i] = new Tile(image, true);
                        }
                    }
                    break;
                case 4:
                    tile = new Tile[13];
                    for (int i = 0; i < 13; i++){
                        BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/source/Tiles/04/Images/" + i + ".png"));
                        if(i == 0){
                            tile[i] = new Tile(image, false);
                        }else{
                            tile[i] = new Tile(image, true);
                        }
                    }
                    break;
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public void draw(Graphics2D g2){
        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;
        
        while (col < gamePanel.getMaxScreenCol() && row < gamePanel.getMaxScreenRow()){
            int tileNum = mapTileNum[col][row];
            g2.drawImage(tile[tileNum].getImage(), x, y, gamePanel.getTileSize(), gamePanel.getTileSize(), null);
            col++;
            x += gamePanel.getTileSize();
            if (col == gamePanel.getMaxScreenCol()){
                col = 0;
                x = 0;
                row++;
                y += gamePanel.getTileSize();
            }
        }
    }
    
    public int[][] getMapTileNum(){
        return mapTileNum;
    }
    
    public Tile[] getTile(){
        return tile;
    }
}

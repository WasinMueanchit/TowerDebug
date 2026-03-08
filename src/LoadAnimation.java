
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class LoadAnimation {
    //Animation
    private BufferedImage[][] assasinAnimation = new BufferedImage[5][10];
    private BufferedImage[][] femaleGoblinAnimation = new BufferedImage[4][12];

    public LoadAnimation() {
        loadAllAnimation();
    }

    //Load Animation
    public void loadAllAnimation() {
        try {
            //Import Assasin images to "assasinAnimation"
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 10; j++) {
                    assasinAnimation[i][j] = ImageIO.read(getClass().getResourceAsStream("/source/Assasin/" + i + "/" + j + ".png"));
                }
            }
            //Import Female Goblin images to "femaleGoblinAnimation"
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 12; j++) {
                    femaleGoblinAnimation[i][j] = ImageIO.read(getClass().getResourceAsStream("/source/Female Goblin/" + i + "/" + j + ".png"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Setter & getter
    public BufferedImage[][] getAssasinAnimation() {
        return assasinAnimation;
    }

    public BufferedImage[][] getFemaleGobinAnimation() {
        return femaleGoblinAnimation;
    }
}

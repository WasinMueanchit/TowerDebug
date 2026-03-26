
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import javax.imageio.ImageIO;

public class LoadAnimation {

    //Animation
    private static HashMap<String, HashMap> allAnimation = new HashMap();

    public LoadAnimation() {
        loadAllAnimation();
    }

    //Load Animation
    public void loadAllAnimation() {
        File Animationfolder = new File("src/source/Animation"); //Animation
        File[] allAnimationFolder = Animationfolder.listFiles(); // Animation/....
        for (File characterFolder : allAnimationFolder) { //Assasin
            HashMap<String, ArrayList<BufferedImage>> characterAnimation = new HashMap<>();
            allAnimation.put(characterFolder.getName(), characterAnimation);
            File[] typeAnimation = characterFolder.listFiles(); //Assasin/....
            for (File typeAnimationFolder : typeAnimation) { //Assasin/idle
                ArrayList<BufferedImage> animation = new ArrayList<>();
                File[] imageAnimation = typeAnimationFolder.listFiles();
                Arrays.sort(imageAnimation, Comparator.comparingInt(file -> {
                    String name = file.getName().replace(".png", "");
                    return Integer.parseInt(name);
                }));
                for (File imageFile : imageAnimation) { //Assasin/idle/01.png
                    try {
                        BufferedImage image = ImageIO.read(imageFile);
                        animation.add(image);
                    }catch(IOException ex){
                        ex.printStackTrace();
                    }
                }
                characterAnimation.put(typeAnimationFolder.getName(), animation);
            }
        }
    }
    
    public static HashMap<String, ArrayList<BufferedImage>> getAnimation(String object){
        return allAnimation.get(object);
    }
}

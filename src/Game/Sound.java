package Game;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {

    private Clip clip;
    private FloatControl vol;
    private HashMap<String, URL> allSound = new HashMap<>();

    public Sound() {
        allSound.put("pickupCoin", getClass().getResource("/source/Sound/pickupCoin.wav"));
        allSound.put("placeCharacter", getClass().getResource("/source/Sound/placeCharacter.wav"));
        allSound.put("victory", getClass().getResource("/source/Sound/victory.wav"));
        allSound.put("explosion", getClass().getResource("/source/Sound/explosion.wav"));
        allSound.put("buttonClick", getClass().getResource("/source/Sound/buttonClick.wav"));
        allSound.put("background", getClass().getResource("/source/Sound/background.wav"));
    }

    public void setSound(String sound) {
        try (AudioInputStream aIs = AudioSystem.getAudioInputStream(allSound.get(sound));) {
            clip = AudioSystem.getClip();
            clip.open(aIs);
            vol = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            vol.setValue(-30);
        } catch (UnsupportedAudioFileException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        }
    }

    public void play() {
        clip.start();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
    }
}

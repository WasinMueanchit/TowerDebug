
import java.util.ArrayList;
import java.util.HashMap;

public class WaveManager {
    private ArrayList<HashMap<String, Integer>> allWaveLevel1 = new ArrayList<>();
    public WaveManager(){
        setWaveInLevel1();  
    }
    
    public void setWaveInLevel1(){
        HashMap<String, Integer> wave1 = new HashMap<>();
        wave1.put("FemaleGoblin", 5);
        wave1.put("Tank", 2);
        
        HashMap<String, Integer> wave2 = new HashMap<>();
        wave2.put("FemaleGoblin", 3);
        wave2.put("Tank", 5);
        
        HashMap<String, Integer> wave3 = new HashMap<>();
        wave3.put("FemaleGoblin", 1);
        wave3.put("Tank", 5);
        
        allWaveLevel1.add(wave1);
        allWaveLevel1.add(wave2);
        allWaveLevel1.add(wave3);
    }
    
    public ArrayList<HashMap<String, Integer>> getAllWaveLevel1(){
        return allWaveLevel1;
    }
}

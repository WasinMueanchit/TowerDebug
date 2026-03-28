package Game;


import java.util.ArrayList;
import java.util.HashMap;

public class WaveManager {

    private ArrayList<HashMap<String, Integer>> allWaveLevel1 = new ArrayList<>();
    private ArrayList<HashMap<String, Integer>> allWaveLevel2 = new ArrayList<>();
    private ArrayList<HashMap<String, Integer>> allWaveLevel3 = new ArrayList<>();
    private ArrayList<HashMap<String, Integer>> allWaveLevel4 = new ArrayList<>();
    private ArrayList<ArrayList<HashMap<String, Integer>>> allWave = new ArrayList<>();

    public WaveManager() {
        setWaveInLevel1();
        setWaveInLevel2();
        setWaveInLevel3();
        setWaveInLevel4();
        allWave.add(allWaveLevel1);
        allWave.add(allWaveLevel2);
        allWave.add(allWaveLevel3);
        allWave.add(allWaveLevel4);
    }

    public void setWaveInLevel1() {
        HashMap<String, Integer> wave1 = new HashMap<>();
        wave1.put("Female Goblin", 10);
        wave1.put("Armored Org", 3);
        wave1.put("Ghost Female Goblin", 3);

        HashMap<String, Integer> wave2 = new HashMap<>();
        wave2.put("Female Goblin", 5);
        wave2.put("Armored Org", 5);

        HashMap<String, Integer> wave3 = new HashMap<>();
        wave3.put("Female Goblin", 1);
        wave3.put("Armored Org", 5);

        allWaveLevel1.add(wave1);
        allWaveLevel1.add(wave2);
        allWaveLevel1.add(wave3);
    }

    public void setWaveInLevel2()   {
        HashMap<String, Integer> wave1 = new HashMap<>();
        wave1.put("Female Goblin", 1);
        wave1.put("Tank", 2);

        HashMap<String, Integer> wave2 = new HashMap<>();
        wave2.put("Female Goblin", 5);
        wave2.put("Tank", 5);

        HashMap<String, Integer> wave3 = new HashMap<>();
        wave3.put("Female Goblin", 1);
        wave3.put("Tank", 5);

        allWaveLevel2.add(wave1);
        allWaveLevel2.add(wave2);
        allWaveLevel2.add(wave3);
    }

    public void setWaveInLevel3() {
        HashMap<String, Integer> wave1 = new HashMap<>();
        wave1.put("Female Goblin", 5);
        wave1.put("Tank", 2);

        HashMap<String, Integer> wave2 = new HashMap<>();
        wave2.put("Female Goblin", 3);
        wave2.put("Tank", 5);

        HashMap<String, Integer> wave3 = new HashMap<>();
        wave3.put("Female Goblin", 1);
        wave3.put("Tank", 5);

        allWaveLevel3.add(wave1);
        allWaveLevel3.add(wave2);
        allWaveLevel3.add(wave3);
    }

    public void setWaveInLevel4() {
        HashMap<String, Integer> wave1 = new HashMap<>();
        wave1.put("Female Goblin", 5);
        wave1.put("Tank", 2);

        HashMap<String, Integer> wave2 = new HashMap<>();
        wave2.put("Female Goblin", 3);
        wave2.put("Tank", 5);

        HashMap<String, Integer> wave3 = new HashMap<>();
        wave3.put("Female Goblin", 1);
        wave3.put("Tank", 5);

        allWaveLevel4.add(wave1);
        allWaveLevel4.add(wave2);
        allWaveLevel4.add(wave3);
    }
    
    public ArrayList<HashMap<String, Integer>> getAllWaveAtLevel(int level){
        return allWave.get(level - 1);
    }
}

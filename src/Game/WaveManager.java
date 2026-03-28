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
        wave1.put("Female Goblin", 5);

        HashMap<String, Integer> wave2 = new HashMap<>();
        wave2.put("Female Goblin", 8);

        HashMap<String, Integer> wave3 = new HashMap<>();
        wave3.put("Female Goblin", 5);
        wave3.put("Armored Org", 2);
        
        HashMap<String, Integer> wave4 = new HashMap<>();
        wave4.put("Female Goblin", 10);
        wave4.put("Armored Org", 3);
        
        HashMap<String, Integer> wave5 = new HashMap<>();
        wave5.put("Female Goblin", 5);
        wave5.put("Armored Org", 2);
        wave5.put("Cyclops", 1);

        allWaveLevel1.add(wave1);
//        allWaveLevel1.add(wave2);
//        allWaveLevel1.add(wave3);
//        allWaveLevel1.add(wave4);
//        allWaveLevel1.add(wave5);
    }

    public void setWaveInLevel2()   {
        HashMap<String, Integer> wave1 = new HashMap<>();
        wave1.put("Female Goblin", 10);
        wave1.put("MaleGoblin", 2);
        wave1.put("ArmoredOrg", 2);

        HashMap<String, Integer> wave2 = new HashMap<>();
        wave2.put("Female Goblin", 9);
        wave2.put("MaleGoblin", 3);
        wave2.put("ArmoredOrg", 2);

        HashMap<String, Integer> wave3 = new HashMap<>();
        wave3.put("Female Goblin", 8);
        wave3.put("MaleGoblin", 5);
        wave3.put("ArmoredOrg", 2);
        
        HashMap<String, Integer> wave4 = new HashMap<>();
        wave4.put("Female Goblin", 7);
        wave4.put("MaleGoblin", 7);
        wave4.put("ArmoredOrg", 3);

        HashMap<String, Integer> wave5 = new HashMap<>();
        wave5.put("MaleGoblin", 5);
        wave5.put("Cyclops", 1);

        HashMap<String, Integer> wave6 = new HashMap<>();
        wave6.put("Female Goblin", 3);
        wave6.put("MaleGoblin", 2);
        wave6.put("ArmoredOrg", 8);
        
        HashMap<String, Integer> wave7 = new HashMap<>();
        wave7.put("Female Goblin", 5);
        wave7.put("MaleGoblin", 12);
        wave7.put("ArmoredOrg", 5);

        HashMap<String, Integer> wave8 = new HashMap<>();
        wave8.put("Female Goblin", 8);
        wave8.put("MaleGoblin", 12);
        wave8.put("ArmoredOrg", 5);

        HashMap<String, Integer> wave9 = new HashMap<>();
        wave9.put("MaleGoblin", 15);
        wave9.put("ArmoredOrg", 8);

        HashMap<String, Integer> wave10 = new HashMap<>();
        wave2.put("Yeti", 1);


        allWaveLevel2.add(wave1);
        allWaveLevel2.add(wave2);
        allWaveLevel2.add(wave3);
        allWaveLevel2.add(wave4);
        allWaveLevel2.add(wave5);
        allWaveLevel2.add(wave6);
        allWaveLevel2.add(wave7);
        allWaveLevel2.add(wave8);
        allWaveLevel2.add(wave9);
        allWaveLevel2.add(wave10);
    }

    public void setWaveInLevel3() {
        HashMap<String, Integer> wave1 = new HashMap<>();
        wave1.put("Female Goblin", 10);
        wave1.put("MaleGoblin", 5);
        wave1.put("ArmoredOrg", 3);
        wave1.put("Ghost Female Goblin", 2);

        HashMap<String, Integer> wave2 = new HashMap<>();
        wave2.put("Female Goblin", 7);
        wave2.put("MaleGoblin", 7);
        wave2.put("ArmoredOrg", 4);
        wave2.put("Ghost Female Goblin", 4);

        HashMap<String, Integer> wave3 = new HashMap<>();
        wave3.put("Female Goblin", 5);
        wave3.put("MaleGoblin", 8);
        wave3.put("ArmoredOrg", 5);
        
        HashMap<String, Integer> wave4 = new HashMap<>();
        wave4.put("Female Goblin", 4);
        wave4.put("MaleGoblin", 9);
        wave4.put("ArmoredOrg", 6);
        wave4.put("Ghost Female Goblin", 6);

        HashMap<String, Integer> wave5 = new HashMap<>();
        wave5.put("Female Goblin", 3);
        wave5.put("MaleGoblin", 10);
        wave5.put("ArmoredOrg", 8);
        wave5.put("Ghost Female Goblin", 7);

        HashMap<String, Integer> wave6 = new HashMap<>();
        wave6.put("Female Goblin", 2);
        wave6.put("MaleGoblin", 10);
        wave6.put("ArmoredOrg", 9);
        wave6.put("Ghost Female Goblin", 9);
        
        HashMap<String, Integer> wave7 = new HashMap<>();
        wave7.put("Female Goblin", 2);
        wave7.put("MaleGoblin", 10);
        wave7.put("ArmoredOrg", 9);
        wave7.put("Ghost Female Goblin", 9);

        HashMap<String, Integer> wave8 = new HashMap<>();
        wave8.put("MaleGoblin", 10);
        wave8.put("ArmoredOrg", 9);
        wave8.put("Ghost Female Goblin", 14);

        HashMap<String, Integer> wave9 = new HashMap<>();
        wave8.put("MaleGoblin", 12);
        wave8.put("ArmoredOrg", 10);
        wave8.put("Ghost Female Goblin", 15);

        HashMap<String, Integer> wave10 = new HashMap<>();
        wave2.put("EathGolem", 1);


        allWaveLevel3.add(wave1);
        allWaveLevel3.add(wave2);
        allWaveLevel3.add(wave3);
        allWaveLevel3.add(wave4);
        allWaveLevel3.add(wave5);
        allWaveLevel3.add(wave6);
        allWaveLevel3.add(wave7);
        allWaveLevel3.add(wave8);
        allWaveLevel3.add(wave9);
        allWaveLevel3.add(wave10);
    }

    public void setWaveInLevel4() {
        HashMap<String, Integer> wave1 = new HashMap<>();
        wave1.put("Female Goblin", 15);
        wave1.put("MaleGoblin", 10);
        wave1.put("Ghost Female Goblin", 5);

        HashMap<String, Integer> wave2 = new HashMap<>();
        wave2.put("MaleGoblin", 15);
        wave2.put("ArmoredOrg", 5);
        wave2.put("Ghost Female Goblin",8);

        HashMap<String, Integer> wave3 = new HashMap<>();
        wave3.put("Female Goblin", 10);
        wave3.put("Ghost Female Goblin", 12);

        
        HashMap<String, Integer> wave4 = new HashMap<>();
        wave4.put("Female Goblin", 20);
        wave4.put("MaleGoblin", 20);

        HashMap<String, Integer> wave5 = new HashMap<>();
        wave5.put("Cyclops", 2);
        wave5.put("Ghost Female Goblin", 10);

        HashMap<String, Integer> wave6 = new HashMap<>();
        wave6.put("MaleGoblin", 10);
        wave6.put("ArmoredOrg", 15);
        wave6.put("Ghost Female Goblin", 15);
        
        HashMap<String, Integer> wave7 = new HashMap<>();
        wave7.put("Female Goblin", 30);
        wave7.put("ArmoredOrg", 20);

        HashMap<String, Integer> wave8 = new HashMap<>();        
        wave8.put("EartGolem", 12);
        wave8.put("Ghost Female Goblin", 12);

        HashMap<String, Integer> wave9 = new HashMap<>();
        wave9.put("MaleGoblin", 30);


        HashMap<String, Integer> wave10 = new HashMap<>();
        wave10.put("Yeti", 2);
        wave10.put("ArmoredOrg", 2);
        
        HashMap<String, Integer> wave11 = new HashMap<>();
        wave11.put("ArmoredOrg", 25);
        wave11.put("Ghost Female Goblin", 20);
        
        HashMap<String, Integer> wave12 = new HashMap<>();
        wave12.put("MaleGoblin", 5);

        HashMap<String, Integer> wave13 = new HashMap<>();
        wave13.put("ArmoredOrg", 40);

        HashMap<String, Integer> wave14 = new HashMap<>();
        wave14.put("Ghost Female Goblin", 30);

        HashMap<String, Integer> wave15 = new HashMap<>();
        wave15.put("Skeleton", 1);
        wave15.put("EathGolem", 1);
        wave15.put("Yeti", 1);
        wave15.put("ArmoredOrg", 10);
        wave15.put("Ghost Female Goblin", 10);


        allWaveLevel4.add(wave1);
        allWaveLevel4.add(wave2);
        allWaveLevel4.add(wave3);
        allWaveLevel4.add(wave4);
        allWaveLevel4.add(wave5);
        allWaveLevel4.add(wave6);
        allWaveLevel4.add(wave7);
        allWaveLevel4.add(wave8);
        allWaveLevel4.add(wave9);
        allWaveLevel4.add(wave10);
        allWaveLevel4.add(wave11);
        allWaveLevel4.add(wave12);
        allWaveLevel4.add(wave13);
        allWaveLevel4.add(wave14);
        allWaveLevel4.add(wave15);
    }
    
    public ArrayList<HashMap<String, Integer>> getAllWaveAtLevel(int level){
        return allWave.get(level - 1);
    }
}

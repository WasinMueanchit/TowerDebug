package Menu;

import Game.GamePanel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class Controller implements ActionListener {

    private JFrame mainFrame;
    private Menu menuscreen;
    private SelectHero selectHero;
    private Selectmap selectmap;
    public static int unlockedLevel = 1;

    public Controller() {
        mainFrame = new JFrame("Tower Debug");
        mainFrame.getContentPane().setPreferredSize(new Dimension(960, 512));
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainFrame.setResizable(false);
        menuscreen = new Menu();
        selectHero = new SelectHero();
        selectmap = new Selectmap();

        init();

        switchScreen(menuscreen.getPanel());
        mainFrame.setVisible(true);
    }

    public void init() {
        menuscreen.startBut().addActionListener(this);
        menuscreen.HeroBut().addActionListener(this);
        menuscreen.exittBut().addActionListener(this);
        
        selectHero.getBackBtn().addActionListener(this);
        
        selectmap.backBut().addActionListener(this);
        selectmap.getLevel1Btn().addActionListener(this);
        selectmap.getLevel2Btn().addActionListener(this);
        selectmap.getLevel3Btn().addActionListener(this);
        selectmap.getLevel4Btn().addActionListener(this);
        
        
    }

    private void switchScreen(JPanel newPanel) {
        mainFrame.setContentPane(newPanel);
        mainFrame.revalidate();
        mainFrame.repaint(); 
    }
    public void returnToMenu() {
        selectmap.updateUnlocks(unlockedLevel);
        selectHero.updateUnlocksHero(unlockedLevel);
        switchScreen(menuscreen.getPanel());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == menuscreen.startBut()) {
            switchScreen(selectmap.getPanel());

        } else if (e.getSource() == selectHero.getBackBtn()) {
            switchScreen(menuscreen.getPanel());

        } else if (e.getSource() == menuscreen.HeroBut()){
            switchScreen(selectHero.getPanel());
            
        } else if (e.getSource() == selectmap.backBut()) {
            switchScreen(menuscreen.getPanel());

        } else if (e.getSource() == menuscreen.exittBut()) {
            System.exit(0);
        }
        else if (e.getSource() == selectmap.getLevel1Btn()) {
            startGame(1);
        } else if (e.getSource() == selectmap.getLevel2Btn()) {
            startGame(2);
        } else if (e.getSource() == selectmap.getLevel3Btn()) {
            startGame(3);
        } else if (e.getSource() == selectmap.getLevel4Btn()) {
            startGame(4);
        }
    }
    
    private void startGame(int level) {
        ArrayList<Integer> selectedHeroIds = selectHero.getSelectedHeroes();
        String[] heroDatabase = { "Gladiator","Valkyrie", "Pirate","Ninja","Saolin","Pharaoh"};
        
       ArrayList<String> chosenHeroNames = new ArrayList<>();
       for (int id : selectedHeroIds) {
           chosenHeroNames.add(heroDatabase[id]);
       }
        
        GamePanel gamePanel = new GamePanel(this, chosenHeroNames);

        if (level != 1) {
            gamePanel.setLevel(level - 1);
            gamePanel.nextLevel(); 
        }

        switchScreen(gamePanel);
        gamePanel.requestFocus();
    }
    
}



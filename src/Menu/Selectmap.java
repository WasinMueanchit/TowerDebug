package Menu;

import java.awt.*;
import javax.swing.*;

public class Selectmap {

    private JPanel bgPanel;
    private JButton back, level1, level2, level3, level4;

    public Selectmap() {

        ImageIcon bgIcon = new ImageIcon(getClass().getResource("../source/GUI_IMAGE/BG.png"));
        final Image bgImage = bgIcon.getImage();

        bgPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            }
        };

        bgPanel.setLayout(null);

        back = new JButton();
        setIconToButton(back, "../source/GUI_IMAGE/BACK.png", 100, 70);
        back.setBounds(20, 20, 100, 70);
        level1 = new JButton("1");
        level2 = new JButton("2");
        level3 = new JButton("3");
        level4 = new JButton("4");

        back.setBounds(20, 20, 80, 40);

        int btnWidth = 80;
        int btnHeight = 80;
        int startY = 168;
        setIconToButton(level1, "../source/GUI_IMAGE/Level1.png", 175, 175);
        setIconToButton(level2, "../source/GUI_IMAGE/Level2.png", 175, 175);
        setIconToButton(level3, "../source/GUI_IMAGE/Level3.png", 175, 175);
        setIconToButton(level4, "../source/GUI_IMAGE/Level4.png", 175, 175);
        level1.setBounds(150, startY, 175, 175);
        level2.setBounds(318, startY, 175, 175);
        level3.setBounds(485, startY, 175, 175);
        level4.setBounds(650, startY, 175, 175);

        bgPanel.add(back);
        bgPanel.add(level1);
        bgPanel.add(level2);
        bgPanel.add(level3);
        bgPanel.add(level4);
        updateUnlocks(1);
    }
    
    public void updateUnlocks(int unlockLevel) {
        setIconToButton(level1, "../source/GUI_IMAGE/Level1.png", 175, 175);
        level1.setEnabled(true);
        
        if (unlockLevel >= 2) {
            setIconToButton(level2, "../source/GUI_IMAGE/Level2.png", 175, 175);
            level2.setEnabled(true);
        } else {
            setIconToButton(level2, "../source/GUI_IMAGE/LockLevel2.png", 175, 175);
            level2.setEnabled(false);
        }
        
        if (unlockLevel >= 3) {
            setIconToButton(level3, "../source/GUI_IMAGE/Level3.png", 175, 175);
            level3.setEnabled(true);
        } else {
            setIconToButton(level3, "../source/GUI_IMAGE/LockLevel3.png", 175, 175);
            level3.setEnabled(false);
        }
        
        if (unlockLevel >= 4) {
            setIconToButton(level4, "../source/GUI_IMAGE/Level4.png", 175, 175);
            level4.setEnabled(true);
        } else {
            setIconToButton(level4, "../source/GUI_IMAGE/LockLevel4.png", 175, 175);
            level4.setEnabled(false);
        }
    }

    private void setIconToButton(JButton button, String imagePath, int width, int height) {
        java.net.URL imgURL = getClass().getResource(imagePath);
        try {
            if (imgURL != null) {
                ImageIcon icon = new ImageIcon(imgURL);
                Image img = icon.getImage();
                Image newImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                button.setIcon(new ImageIcon(newImg));
                ImageIcon finalIcon = new ImageIcon(newImg);
                button.setIcon(finalIcon);
                button.setDisabledIcon(finalIcon);
            }
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            button.setFocusPainted(false);
        } catch (Exception e) {
            button.setBackground(Color.DARK_GRAY);
        }
    }

    public JButton backBut() {
        return back;
    }
    
    public JButton getLevel1Btn() { return level1; }
    public JButton getLevel2Btn() { return level2; }
    public JButton getLevel3Btn() { return level3; }
    public JButton getLevel4Btn() { return level4; }

    public JPanel getPanel() {
        return bgPanel;
    }

}

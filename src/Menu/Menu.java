package Menu;

import java.awt.*;
import javax.swing.*;

public class Menu {

    private JLabel logo;
    private JButton start, hero, exit;
    private JPanel bgPanel;

    public Menu() {

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
        logo = new JLabel(new ImageIcon(getClass().getResource("Logo.png")));
        start = new JButton();
        hero = new JButton();
        exit = new JButton();

        setIconToButton(start, "../source/GUI_IMAGE/START.png", 250, 175);
        setIconToButton(hero, "../source/GUI_IMAGE/HERO.png", 250, 175);
        setIconToButton(exit, "../source/GUI_IMAGE/EXIT.png", 250, 175);
        
        int newLogoW = 450;
        int newLogoH = 250;
        logo = new JLabel(getScaledIcon("Logo.png", newLogoW, newLogoH));
        logo.setBounds(255, 0, newLogoW, newLogoH);
        start.setBounds(380, 250, 200, 70);
        hero.setBounds(380, 330, 200, 70);
        exit.setBounds(380, 410, 200, 70);

        bgPanel.add(logo);
        bgPanel.add(start);
        bgPanel.add(hero);
        bgPanel.add(exit);
    }

    private void setIconToButton(JButton button, String imagePath, int width, int height) {
        java.net.URL imgURL = getClass().getResource(imagePath);
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            Image img = icon.getImage();
            Image newImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(newImg));
        }
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
    }

    public JButton startBut() {
        return start;
    }
    
    public JButton HeroBut(){
        return hero;
    }

    public JButton exittBut() {
        return exit;
    }

    public JPanel getPanel() {
        return bgPanel;
    }

    public ImageIcon getScaledIcon(String path, int width, int height) {
        ImageIcon originalIcon = new ImageIcon(getClass().getResource(path)); // หรือ new ImageIcon(path) ตามการจัดเก็บไฟล์ของคุณ
        Image originalImage = originalIcon.getImage();
        // ใช้ SCALE_SMOOTH เพื่อคุณภาพที่ดีที่สุดตอนย่อ
        Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

}

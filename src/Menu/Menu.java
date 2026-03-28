package Menu;

import java.awt.*;
import javax.swing.*;

public class Menu {

    private JLabel name;
    private JButton start, setting, exit;
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
        name = new JLabel("Tower Debug", SwingConstants.CENTER);
        start = new JButton();
        setting = new JButton();
        exit = new JButton();

        setIconToButton(start, "../source/GUI_IMAGE/START.png", 250, 175);
        setIconToButton(setting, "../source/GUI_IMAGE/SETTING.png", 250, 175);
        setIconToButton(exit, "../source/GUI_IMAGE/EXIT.png", 250, 175);
        name.setFont(new Font("Arial", Font.BOLD, 55));

        name.setBounds(0, 30, 960, 60);
        start.setBounds(355, 150, 225, 90);
        setting.setBounds(355, 250, 225, 90);
        exit.setBounds(355, 360, 225, 90);

        bgPanel.add(name);
        bgPanel.add(start);
        bgPanel.add(setting);
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

    public JButton exittBut() {
        return exit;
    }

    public JPanel getPanel() {
        return bgPanel;
    }
}

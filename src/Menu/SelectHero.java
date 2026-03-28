package Menu;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;

public class SelectHero {

    private JPanel bgPanel;
    private JButton back, nextBtn;
    private JLabel topLabel, bottomLabel;
    private JButton[] topGrid = new JButton[6];
    private JButton[] bottomGrid = new JButton[5];

    private String[] heroIcons = {
        "../source/GUI_IMAGE/Hero1.png", "../source/GUI_IMAGE/Hero2.png", "../source/GUI_IMAGE/Hero3.png",
        "../source/GUI_IMAGE/Hero4.png", "../source/GUI_IMAGE/Hero5.png", "../source/GUI_IMAGE/Hero6.png",
    };

    private ArrayList<Integer> selectedHeroes = new ArrayList<>();
    
    private int size = 100; 

    public SelectHero() {
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
        setIconToButton(back, "../source/GUI_IMAGE/BACK.png", 110, 70);
        back.setBounds(20, 20, 120, 80);
        bgPanel.add(back);

        topLabel = createOutlineLabel("selected", 30);
        topLabel.setBounds(150, 20, 200, 40);
        bgPanel.add(topLabel);

        int startX = 220, startY = 80, gap = 40;

        for (int i = 0; i < 6; i++) {
            topGrid[i] = new JButton();
            int row = i / 3;
            int col = i % 3;
            topGrid[i].setBounds(startX + (col * (size + gap)), startY + (row * (size + 20)), size, size);
            topGrid[i].setOpaque(false);

            setIconToButton(topGrid[i], heroIcons[i], size, size);
            bgPanel.add(topGrid[i]);

            final int heroIndex = i;
            topGrid[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (selectedHeroes.size() < 5 && !selectedHeroes.contains(heroIndex)) {
                        selectedHeroes.add(heroIndex);
                        updateBottomGrid();
                    }
                }
            });
        }

        bottomLabel = createOutlineLabel("YOUR HERO:", 35);
        bottomLabel.setBounds(60, 310, 300, 50);
        bgPanel.add(bottomLabel);

        int bottomStartX = 60, bottomStartY = 360;

        for (int i = 0; i < 5; i++) {
            bottomGrid[i] = new JButton();
            bottomGrid[i].setBounds(bottomStartX + (i * (size + 30)), bottomStartY, size, size);
            
            bottomGrid[i].setOpaque(false);

            setIconToButton(bottomGrid[i], "../source/GUI_IMAGE/Cbox.png", size, size);
            bgPanel.add(bottomGrid[i]);

            final int slotIndex = i;
            bottomGrid[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (slotIndex < selectedHeroes.size()) {
                        selectedHeroes.remove(slotIndex);
                        updateBottomGrid();
                    }
                }
            });
        }

        nextBtn = new JButton("NEXT");
        nextBtn.setFont(new Font("Arial", Font.BOLD, 28));
        nextBtn.setForeground(Color.WHITE);
        nextBtn.setBackground(new Color(30, 45, 55));
        nextBtn.setBounds(740, 350, 160, 55);
        bgPanel.add(nextBtn);
    }

    private void updateBottomGrid() {
        for (int i = 0; i < 5; i++) {
            if (i < selectedHeroes.size()) {
                int heroId = selectedHeroes.get(i);
                setIconToButton(bottomGrid[i], heroIcons[heroId], size, size);
            } else {
                setIconToButton(bottomGrid[i],"../source/GUI_IMAGE/Cbox.png", size, size);
            }
        }
    }

    private JLabel createOutlineLabel(String text, int fontSize) {
        JLabel label = new JLabel(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                String t = getText();
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = 0;
                int y = fm.getAscent();
                int outlineSize = 2;

                g2.setColor(Color.BLACK);
                for (int i = -outlineSize; i <= outlineSize; i++) {
                    for (int j = -outlineSize; j <= outlineSize; j++) {
                        if (i != 0 || j != 0) {
                            g2.drawString(t, x + i, y + j);
                        }
                    }
                }
                g2.setColor(Color.WHITE);
                g2.drawString(t, x, y);
            }
        };
        label.setFont(new Font("Arial", Font.BOLD, fontSize));
        return label;
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

    public JButton getBackBtn() { return back; }
    public JButton getNextBtn() { return nextBtn; }
    public JPanel getPanel() { return bgPanel; }
    
    public ArrayList<Integer> getSelectedHeroes() {
        return selectedHeroes;
    }
}
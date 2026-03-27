package All;


import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {

        JFrame window = new JFrame();

        //Character UI
//        JPanel panel = new JPanel();
//        panel.setPreferredSize(new Dimension(400, 50));
//        JButton btn = new JButton("Click");
//        panel.add(btn);
        //Main game
        GamePanel gamePanel = new GamePanel();

        window.add(gamePanel, BorderLayout.CENTER);
//        window.add(panel, BorderLayout.SOUTH);

        //Monitor
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        window.setResizable(false);
        window.setTitle("Tower Debug");
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}

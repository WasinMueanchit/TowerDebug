import javax.swing.JFrame;
public class Main {
    public static void main(String[] args) {
        //Initialize
        JFrame window = new JFrame();
        GamePanel gamePanel = new GamePanel();
        
        window.add(gamePanel);
        
        //Monitor
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        window.pack();
        window.setResizable(false);
        window.setTitle("Tower Debug");
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}

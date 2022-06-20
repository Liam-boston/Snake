import javax.swing.*;

public class GameFrame extends JFrame {
    /**
     * Empty-constructor
     */
    GameFrame() {
        // Add new panel to GameFrame
        this.add(new GamePanel());

        // Initial Swing setup
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}

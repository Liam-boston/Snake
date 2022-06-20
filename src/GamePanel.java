import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    // Static final class variables
    static final Integer SCREEN_WIDTH = 600;
    static final Integer SCREEN_HEIGHT = 600;
    static final Integer UNIT_SIZE = 25;
    static final Integer GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final Integer DELAY = 75;

    // Position of the snake
    final int row[] = new int[GAME_UNITS];
    final int col[] = new int[GAME_UNITS];

    // Snake length
    private Integer bodyParts;

    // Apple count & position (row, col)
    private Integer applesEaten;
    private Integer appleRow;
    private Integer appleCol;

    // Direction (Right, Left, Up, Down)
    Character direction;

    // Snake moving?
    private Boolean inMotion;

    // Game time
    private Timer timer;

    // Random position variable
    private Random random;

    /**
     * Empty-constructor
     */
    GamePanel() {
        // Initial snake length
        this.bodyParts = 6;

        // Initial apple count
        this.applesEaten = 0;

        // Initial direction (Right, Left, Up, Down)
        direction = 'R';

        // Snake isn't set in-motion until game starts
        this.inMotion = false;

        // Initialize the Random variable to place the apple
        this.random = new Random();

        // Initial Swing setup
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

        // Start the game
        startGame();
    }

    /**
     * To start the game a new apple is created and the timer is started
     */
    public void startGame() {
        newApple();
        this.inMotion = true;
        this.timer = new Timer(DELAY, this);
        this.timer.start();
    }

    /**
     * 1) Draw an apple on the panel
     * 2) Draw the head & body of the snake
     * 3) Display the score
     */
    public void draw(Graphics g) {
        // While the game is being played
        if (this.inMotion) {
            // 1) Draw an apple on the panel
            g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            g.fillOval(this.appleRow, this.appleCol, UNIT_SIZE, UNIT_SIZE);

            // 2) Draw the head & body of the snake
            for (int j = 0; j < this.bodyParts; j++) {
                // Head = purple
                if (j == 0) {
                    g.setColor(new Color(79, 66, 181));
                    g.fillRect(this.row[j], this.col[j], UNIT_SIZE, UNIT_SIZE);
                }

                // Body = blue
                else {
                    g.setColor(new Color(1, 120, 189));
                    g.fillRect(this.row[j], this.col[j], UNIT_SIZE, UNIT_SIZE);
                }
            }

            // 3) Display the score
            g.setColor(new Color(1, 120, 189));
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString(("Score: " + this.applesEaten), (SCREEN_WIDTH - metrics.stringWidth("Score: " + this.applesEaten)) / 2, g.getFont().getSize());
        }

        // When the game ends
        else {
            gameOver(g);
        }
    }

    /**
     * Moves the snake by adjusting row[] and col[] values
     */
    public void move() {
        // Adjusts the snake's position one coordinate at a time
        for (int i = this.bodyParts; i > 0; i--) {
            this.row[i] = this.row[i - 1];
            this.col[i] = this.col[i - 1];
        }

        // Switch-statement to handle changes in direction
        switch (direction) {
            case 'R':
                this.row[0] = this.row[0] + UNIT_SIZE;
                break;
            case 'L':
                this.row[0] = this.row[0] - UNIT_SIZE;
                break;
            case 'U':
                this.col[0] = this.col[0] - UNIT_SIZE;
                break;
            case 'D':
                this.col[0] = this.col[0] + UNIT_SIZE;
                break;
        }
    }

    /**
     * Move the apple to a new random location
     */
    public void newApple() {
        this.appleRow = random.nextInt((Integer) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        this.appleCol = random.nextInt((Integer) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    /**
     * When the snake eats an apple, increase it's size by 1
     * and create a new apple
     */
    public void checkApple() {
        if ((this.row[0] == appleRow) && (this.col[0] == appleCol)) {
            this.bodyParts++;
            this.applesEaten++;
            newApple();
        }
    }

    /**
     * 1) Snake collides with itself
     * 2) Snake collides with left-wall
     * 3) Snake collides with right-wall
     * 4) Snake collides with top-wall
     * 5) Snake collides with bottom-wall
     */
    public void checkCollisions() {
        // 1) Snake collides with itself
        for (int i = this.bodyParts; i > 0; i--) {
            if ((this.row[0] == this.row[i]) && (this.col[0] == this.col[i])) {
                this.inMotion = false;
            }
        }

        // 2) Snake collides with left-wall
        if (this.row[0] < 0) {
            this.inMotion = false;
        }

        // 3) Snake collides with right-wall
        if (this.row[0] >= SCREEN_WIDTH) {
            this.inMotion = false;
        }

        // 4) Snake collides with top-wall
        if (this.col[0] < 0) {
            this.inMotion = false;
        }

        // 5) Snake collides with bottom-wall
        if (this.col[0] >= SCREEN_HEIGHT) {
            this.inMotion = false;
        }

        // Stop the timer if any of these collisions occurred
        if (!this.inMotion) {
            this.timer.stop();
        }
    }

    /**
     * When the game ends, display the final score and 'Game Over...'
     */
    public void gameOver(Graphics g) {
        // Final score
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metricsOne = getFontMetrics(g.getFont());
        g.drawString(("Final Score: " + this.applesEaten), (SCREEN_WIDTH - metricsOne.stringWidth("Final Score: " + this.applesEaten)) / 2, g.getFont().getSize());

        // Game over...
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metricsTwo = getFontMetrics(g.getFont());
        g.drawString("Game Over...", (SCREEN_WIDTH - metricsTwo.stringWidth("Game Over...")) / 2, (SCREEN_HEIGHT / 2));
    }

    /**
     * This is a Swing method invoked automatically when an action
     * is performed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (this.inMotion) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    /**
     * This is a Swing method called internally
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    /**
     * KeyAdapter is an abstract class used for receiving keyboard events
     * (Takes input from the arrow keys)
     */
    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                // Left-arrow key
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;

                // Right-arrow key
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;

                // Up-arrow key
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;

                // Down-arrow key
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
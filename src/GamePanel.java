import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {
    
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 75;
    final int BORDER_THICKNESS = 10; 
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten = 0;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    int highScore = 0;
    
    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(new Color(173, 216, 230)); 
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    
    public void startGame() {
        bodyParts = 6;
        applesEaten = 0;
        direction = 'R';
        running = true;

        for (int i = 0; i < bodyParts; i++) {
            x[i] = BORDER_THICKNESS;
            y[i] = BORDER_THICKNESS;
        }

        newApple();
        timer = new Timer(DELAY, this);
        timer.start();
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    
    public void draw(Graphics g) {
        if (running) {
            
            g.setColor(Color.YELLOW);
            g.fillRect(0, 0, SCREEN_WIDTH, BORDER_THICKNESS);
            g.fillRect(0, SCREEN_HEIGHT - BORDER_THICKNESS, SCREEN_WIDTH, BORDER_THICKNESS);
            g.fillRect(0, 0, BORDER_THICKNESS, SCREEN_HEIGHT);
            g.fillRect(SCREEN_WIDTH - BORDER_THICKNESS, 0, BORDER_THICKNESS, SCREEN_HEIGHT);

            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                g.setColor(Color.GREEN);
                g.fillOval(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }

            g.setColor(Color.BLUE);
            g.setFont(new Font("Times New Roman", Font.BOLD, 40));
            g.drawString("Score: " + applesEaten, 30, 50); 
            g.drawString("High Score: " + highScore, SCREEN_WIDTH - 300, 50); 
        } else {
            gameOver(g);
        }
    }
    
    public void newApple() {
        appleX = random.nextInt((SCREEN_WIDTH - 2 * BORDER_THICKNESS) / UNIT_SIZE) * UNIT_SIZE + BORDER_THICKNESS;
        appleY = random.nextInt((SCREEN_HEIGHT - 2 * BORDER_THICKNESS) / UNIT_SIZE) * UNIT_SIZE + BORDER_THICKNESS;
    }
    
    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;

            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;

            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;

            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }
    
    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }
    
    public void checkCollisions() {
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i] && y[0] == y[i])) {
                running = false;
            }
        }
        if (x[0] < BORDER_THICKNESS || x[0] >= SCREEN_WIDTH - BORDER_THICKNESS || 
            y[0] < BORDER_THICKNESS || y[0] >= SCREEN_HEIGHT - BORDER_THICKNESS) {
            running = false;
        }
        if (!running) {
            if (applesEaten > highScore) {
                highScore = applesEaten; 
            }
            timer.stop();
        }
    }
    
    public void gameOver(Graphics g) {
        g.setColor(Color.BLUE);
        g.setFont(new Font("Times New Roman", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, SCREEN_HEIGHT / 10);

        g.drawString("High Score: " + highScore, (SCREEN_WIDTH - metrics.stringWidth("High Score: " + highScore)) / 2, SCREEN_HEIGHT / 5);

        g.setColor(Color.BLUE);
        g.setFont(new Font("Times New Roman", Font.BOLD, 75));
        g.drawString("Game Over", SCREEN_WIDTH / 5, SCREEN_HEIGHT / 2);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }
    
    public class MyKeyAdapter extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_ENTER:
                    if (!running) {
                        startGame();
                        repaint();
                    }
                    break;
            }
        }
    }
}

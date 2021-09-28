import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GameField extends JPanel implements ActionListener {
    private final int SIZE = 320;
    private final int DOT_SIZE = 16;
    private final int ALL_DOTS = 400;
    private Image dot;
    private Image apple;
    private int appleX;
    private int appleY;
    private int [] x = new int[ALL_DOTS];
    private int [] y = new int[ALL_DOTS];
    private int dots;
    private Timer timer;
    private boolean left = false;
    private boolean right = true;
    private boolean up = true;
    private boolean down = false;
    private boolean inGame = true;

    public GameField(){
        setBackground(Color.BLACK);
        this.loadImages();

        this.initGame();

        addKeyListener(new FieldKeyListener());
        setFocusable(true);
    }

    public void initGame(){
        dots = 3;
        for(int i=0; i < dots; i++){
            x[i] = 48 - i*DOT_SIZE;
            y[i] = 48;
        }

        Timer timer = new Timer(250, this);
        timer.start();

        this.createApple();
    }

    public void createApple(){
        this.appleX = new Random().nextInt(20) * DOT_SIZE;
        this.appleY = new Random().nextInt(20) * DOT_SIZE;
    }

    public void loadImages(){
        ImageIcon iia = new ImageIcon("apple.png");
        ImageIcon iid = new ImageIcon("dot.png");

        apple = iia.getImage();
        dot = iid.getImage();
    }

    public void move(){
        for (int i = dots; i > 0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        if(left){
            x[0] -= DOT_SIZE;
        }else if(right){
            x[0] += DOT_SIZE;
        }else if(up){
            y[0] -= DOT_SIZE;
        }else if(down){
            y[0] += DOT_SIZE;
        }
    }

    public void checkApple(){
        if(this.x[0] == this.appleX && this.y[0] == this.appleY){
            this.dots++;
            createApple();
        }
    }

    public void checkCollisions(){
        for(int i = dots; i > 0; i--){
            if(i > 4 && x[0] == x[i] && y[0] == y[i]) {
                this.inGame = false;
            }
        }

        if(x[0] > SIZE || x[0] < 0 || y[0] < 0 || y[0] > SIZE){
            this.inGame = false;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(!inGame){
            String gameOver = "Game over";
            g.setColor(Color.WHITE);
            g.drawString(gameOver, 120, SIZE/2);
        }

        g.drawImage(apple, appleX, appleY, this);
        for(int i = 0; i < dots; i++){
            g.drawImage(dot, x[i], y[i], this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(!inGame){
            // end
            return;
        }

        this.checkApple();
        this.checkCollisions();
        this.move();
        repaint();
    }

    class FieldKeyListener extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);

            int key = e.getKeyCode();

            if(key == KeyEvent.VK_LEFT && !right){
                left = true;
                up = down = false;
            } else if(key == KeyEvent.VK_RIGHT && !left){
                right = true;
                up = down = false;
            } else if(key == KeyEvent.VK_UP && !down){
                up = true;
                right = left = false;
            } else if(key == KeyEvent.VK_DOWN && !up){
                down = true;
                right = left = false;
            } else{
                System.out.println("Block this button");
            }
        }
    }
}


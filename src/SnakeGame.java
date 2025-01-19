import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    
    private class Tile {
        int x;
        int y;

        Tile (int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    int boardHeight;
    int boardWidth;

    // for tile size
    int tileSize;

    //score
    int score;

    /*
    //high score
    File scoreFile;
    BufferedWriter writer;
    ArrayList<Integer> HIGHSCORE;
    //Scanner scanScore;
    */

    //snake 
    Tile snakeHead;
    ArrayList<Tile> snakeBody;    

    //food
    Tile food;
    Random random;

    // logic
    int velocityX;
    int velocityY;
    Timer gameLoop;

    //game over
    boolean gameOver;
    boolean isPaused;

    Color retro;




    SnakeGame(int boardHeight, int boardWidth) throws IOException {
        
        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;
        this.setBackground(Color.white);
        this.setFocusable(true);
        this.setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        addKeyListener(this);

        tileSize = 25;

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10, 10);
        random = new Random();

        velocityX = 0;
        velocityY = 0;

        placeFood();

        /* 
        score = 0;
        highScore = 0;
        HIGHSCORE = new ArrayList<Integer>();
        scoreFile = new File("highscores.txt");
        writer = new BufferedWriter(new FileWriter(scoreFile, true));
        //scanScore = new Scanner(scoreFile);
        */

        gameLoop = new Timer(100, this);
        gameLoop.start();

        retro = new Color(110, 135, 10);
        isPaused = false;


        
    }

    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void pause() {
        if (!gameOver) {
            isPaused = true;
            gameLoop.stop();
        }
    }

    public void resume() {
        if ((!gameOver) && isPaused == true) {
            gameLoop.start();
            isPaused = false;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        //grid lines
        /* 
        for (int i = 0; i < boardWidth/tileSize; i++) {
            g.setColor(Color.gray);
            g.drawLine(i*tileSize,0, i*tileSize, boardHeight);
            g.drawLine(0, i*tileSize, boardWidth, i*tileSize);
        }
            */

        //drawing snake's head
        g.setColor(Color.green);
        g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);
        

        //drawing snake's body
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            g.setColor(Color.green);
            g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, true);
        }


        //drawing the food
        g.setColor(Color.red);
        g.fill3DRect(food.x*tileSize, food.y*tileSize, tileSize, tileSize, true);

        //drawing the score
        g.setColor(Color.lightGray);
        g.setFont(new Font("Menlo", Font.PLAIN , 25));
        g.drawString("" + score, 8, 25);


        g.setColor(Color.lightGray);
        g.setFont(new Font("Menlo", Font.PLAIN , 25));
        //g.drawString("High Score: " + highScore, 8, 50);

     
        g.setColor(Color.lightGray);
        g.setFont(new Font("Menlo", Font.PLAIN, 10));
        g.drawString("Space to pause", 10 , 45);
        g.drawString("Enter to resume", 10, 60);
        g.drawString("1 for black", 10, 90);
        g.drawString("2 for white", 10, 105);
        g.drawString("3 for retro", 10, 120);

    }


    public void move() {

    if (collision(snakeHead, food)) {
        snakeBody.add(new Tile(food.x, food.y));
        placeFood();
        score += 1;
    }

        //moving snake head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;


        // moving snake body
        for (int i = snakeBody.size()-1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else {
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y; 
            }
        }

        for (int i = 2; i < snakeBody.size(); i++) {
            Tile snakepart = snakeBody.get(i);
            if (collision(snakepart, snakeHead)) {
                gameOver = true;           
             }
        }

        if (snakeHead.x * tileSize == boardWidth || snakeHead.y * tileSize == boardHeight || snakeHead.x == -1 || snakeHead.y == -1) {
            gameOver = true;
        } 

    }

    public void placeFood() {
        food.x = random.nextInt(boardHeight/tileSize);
        food.y = random.nextInt(boardWidth/tileSize);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if ((!gameOver) && (isPaused == false)){
            move();
            repaint();
        }
       
        if (gameOver == true) {
            gameLoop.stop();
        }
    
        /* //Failed highscore system
        if (gameOver == true) {
            HIGHSCORE.add(score);

            for (int i : HIGHSCORE) {
                try {
                    writer.write(HIGHSCORE.toString());
                    writer.newLine();
                    writer.close();
                    //System.out.println(HIGHSCORE);
                    System.out.println("Sucessfullly wrote to file");

                    BufferedReader reader = new BufferedReader(new FileReader(scoreFile));
                    String line;

                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                
            }


            
        }
    }

    public int getMax(int[] list) {
        int max = Integer.MIN_VALUE;

        for (int i = 0; i < HIGHSCORE.size(); i++) {
            if (HIGHSCORE.get(i) > max) {
                max = HIGHSCORE.get(i);
            }
        }
        return max;
        */
    }

    @Override
    public void keyPressed(KeyEvent e) {

        //arrow keys
        if (e.getKeyCode() == (KeyEvent.VK_LEFT) && (velocityX) != 1) {
            velocityX = -1;
            velocityY = 0;
        }
        else if (e.getKeyCode() == (KeyEvent.VK_RIGHT) && (velocityX) != -1) {
            velocityX = 1;
            velocityY = 0;
        }
        else if (e.getKeyCode() == (KeyEvent.VK_UP) && (velocityY) != 1) {
            velocityX = 0;
            velocityY = -1;
        }
        else if (e.getKeyCode() == (KeyEvent.VK_DOWN) && (velocityY) != -1) {
            velocityX = 0;
            velocityY = 1;
        }


        //wasd
        if (e.getKeyCode() == (KeyEvent.VK_A) && (velocityX) != 1) {
            velocityX = -1;
            velocityY = 0;

        }
        else if (e.getKeyCode() == (KeyEvent.VK_D) && (velocityX) != -1) {
            velocityX = 1;
            velocityY = 0;
        }
        else if (e.getKeyCode() == (KeyEvent.VK_W) && (velocityY) != 1) {
            velocityX = 0;
            velocityY = -1;
        }
        else if (e.getKeyCode() == (KeyEvent.VK_S) && (velocityY) != -1) {
            velocityX = 0;
            velocityY = 1;
        }

        if (e.getKeyCode() == (KeyEvent.VK_SPACE) && (!gameOver)) {
            //isPaused = true;
            //gameLoop.stop();
            pause();
        }

        else if (e.getKeyCode() == (KeyEvent.VK_ENTER) && isPaused == true) {
            //isPaused = false;
            //gameLoop.start();
            resume(); 
        }


        if (e.getKeyCode() == (KeyEvent.VK_1) && (!gameOver)) {
            this.setBackground(Color.black);
        }
        
        if (e.getKeyCode() == (KeyEvent.VK_2) && (!gameOver)) {
            this.setBackground(Color.white);
        }

       if (e.getKeyCode() == (KeyEvent.VK_3) && (!gameOver)) {
        this.setBackground(retro);
       }
        
        
    }


    // not needed
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}

    
}

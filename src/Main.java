import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) throws Exception {

       
        int boardHeight = 600;
        int boardWidth = 600;   

       JFrame frame = new JFrame("Snake");
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       frame.setResizable(false);
       frame.setSize(boardWidth, boardHeight);
       frame.setLocationRelativeTo(null);

       SnakeGame snakeGame = new SnakeGame(boardHeight, boardWidth);
       //ColorChoice bgColor = new ColorChoice();
       //frame.add(bgColor);
       frame.add(snakeGame);
       frame.pack();
       frame.setVisible(true);
       
       
    }
}

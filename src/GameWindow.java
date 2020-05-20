import javax.swing.*;

public class GameWindow extends JFrame{
    public static JFrame window;

    public GameWindow(){
        initWindow();
    }

    private void initWindow(){
        add(new GameField());
        setResizable(false);
        //pack sets size of window
        pack();
        setTitle("Snake-Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void restartGame(){
        window.dispose();
        window = new GameWindow();
        window.setVisible(true);
    }

    public static void main(String[] args){
        window = new GameWindow();
        window.setVisible(true);
    }
}

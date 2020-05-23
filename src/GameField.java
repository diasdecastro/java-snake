import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;

public class GameField extends JPanel implements ActionListener {

    private boolean gameRunning;

    //window properties
    private final int fieldWidth = 500;
    private final int fieldHeight = 600;
    private final int pixelSize = 10;

    LinkedList<SnakeBodyPart> snakeBody = new LinkedList<SnakeBodyPart>();

    //snake starting state
    private int xCoordHead = 250;
    private int yCoordHead = 400;
    private int snakeSize = 3;

    //apple coordinates
    int [] appleCoords;

    //movement direction
    private String direction = "";

    int score = 0;

    private Timer timer;

    public GameField() {
        initField();
    }

    private void initField() {
        addKeyListener(new keyEvents());
        setBackground(Color.BLACK);
        //for key-listener
        setFocusable(true);
        setPreferredSize(new Dimension(fieldWidth, fieldHeight));
        initSnake();
        initGame();
    }

    private void initSnake(){
        snakeBody.add(new SnakeBodyPart(xCoordHead, yCoordHead));
        snakeBody.add(new SnakeBodyPart(xCoordHead + pixelSize, yCoordHead));
        snakeBody.add(new SnakeBodyPart(xCoordHead + 2*pixelSize, yCoordHead));
        snakeBody.add(new SnakeBodyPart(xCoordHead + 3*pixelSize, yCoordHead));
    }

    private void initGame(){
        gameRunning = true;
        appleCoords = generateRandCoord();
        timer = new Timer(50, this);
        timer.start();
    }

    private void move(){
        if(direction != ""){
            for(int i = snakeBody.size() - 1; i > 0; i--){
                snakeBody.get(i).setX(snakeBody.get(i - 1).getX());
                snakeBody.get(i).setY(snakeBody.get(i - 1).getY());
            }
            switch(direction){
                case "up":
                    snakeBody.get(0).setY(snakeBody.get(0).getY() - pixelSize);
                    break;
                case "down":
                    snakeBody.get(0).setY(snakeBody.get(0).getY() + pixelSize);
                    break;
                case "left":
                    snakeBody.get(0).setX(snakeBody.get(0).getX() - pixelSize);
                    break;
                case "right":
                    snakeBody.get(0).setX(snakeBody.get(0).getX() + pixelSize);
                    break;
            }
        }
    }

    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        drawBorders(g);
        drawSnake(g);
        drawApple(g, appleCoords[0], appleCoords[1]);
    }

    private void drawSnake(Graphics g){
        checkCollision(g);
        if(gameRunning){
            for(int i = 0; i < snakeSize; i++){
                if(i == 0){
                    g.setColor(Color.GREEN);
                }
                else{
                    g.setColor(Color.WHITE);
                }
                g.fillOval(snakeBody.get(i).getX(), snakeBody.get(i).getY(), pixelSize, pixelSize);
            }
        }
    }

    private void drawApple(Graphics g, int x, int y){
        int xCoord = x;
        int yCoord = y;
        g.setColor(Color.RED);
        g.fillOval(xCoord, yCoord, pixelSize, pixelSize);
    }

    private void drawBorders(Graphics g){
        g.setColor(Color.white);
        g.fillRect(0,500, 500, 5);
        //score text
        String scoreTxt = "Score";
        Font scoreFnt = new Font("Helvetica", Font.BOLD, 20);
        FontMetrics metrics = g.getFontMetrics(scoreFnt);
        g.setFont(scoreFnt);
        g.setColor(Color.white);
        g.drawString(scoreTxt, 40 - (metrics.stringWidth(scoreTxt))/2, 540);
        g.drawString(Integer.toString(score), 40 - (metrics.stringWidth(Integer.toString(score)))/2, 570);
    }

    //generate coordnates for the apple
    private int[] generateRandCoord(){
        Random rand = new Random();
        int x = rand.nextInt(501/10) * 10; //multiple of 10
        int pHolder = -1;
        do {
            pHolder = rand.nextInt(501/10) * 10; //multiple of 10
        }while (pHolder == x || pHolder < 0);
        int y = pHolder;
        int [] arr = {x, y};
        return arr;
    }

    private void checkApple(){
        if((snakeBody.get(0).getX() == appleCoords[0] && snakeBody.get(0).getY() == appleCoords[1])
                && (snakeBody.get(0).getX() == appleCoords[0] && snakeBody.get(0).getY() == appleCoords[1])){
            appleCoords = generateRandCoord();
            snakeBody.add(new SnakeBodyPart(snakeBody.getLast().getX() + pixelSize, snakeBody.getLast().getY() + pixelSize));
            snakeSize++;
            score++;
        }
    }

    private void endGame(Graphics g){
//        timer.stop();
        gameRunning = false;
        String txt1 = "Game Over";
        String txt2 = "Press Space to play again";
        Font fnt = new Font("Helvetica", Font.BOLD, 20);
        FontMetrics metrics = g.getFontMetrics(fnt);
        g.setFont(fnt);
        g.setColor(Color.white);
        g.drawString(txt1, (fieldWidth - metrics.stringWidth(txt1))/2, 500/2 - 20);
        g.drawString(txt2, (fieldWidth - metrics.stringWidth(txt2))/2, 500/2 + 20);
    }

    private void checkCollision(Graphics g){
        for(int i = 1; i < snakeSize; i++){
            if((snakeBody.get(0).getX() == snakeBody.get(i).getX()
                    && snakeBody.get(0).getY() == snakeBody.get(i).getY())
                    || (snakeBody.get(0).getX() < 0 || snakeBody.get(0).getX() > 500)
                    || (snakeBody.get(0).getY() < 0 || snakeBody.get(0).getY() > 500)
            ){
                endGame(g);
            }
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameRunning) {
            checkApple();
            move();
        }
        //calls paintComponent
        repaint();
    }

    private class keyEvents extends KeyAdapter {
        public void keyPressed(KeyEvent e){
            int key = e.getKeyCode();

            if(key == KeyEvent.VK_W && !(direction.equals("down"))){
                direction = "up";
            }
            if(key == KeyEvent.VK_S && !(direction.equals("up"))){
                direction = "down";
            }
            if(key == KeyEvent.VK_A && !(direction.equals("right"))){
                direction = "left";
            }
            if(key == KeyEvent.VK_D && !(direction.equals("left") || direction.equals(""))){
                direction = "right";
            }
            if(key == KeyEvent.VK_SPACE && !gameRunning){
                GameWindow.restartGame();
            }
        }
    }
}

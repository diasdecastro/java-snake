public class SnakeBodyPart {
    private int _xCoord;
    private int _yCoord;

    public SnakeBodyPart(int xCoord, int yCoord){
        _xCoord = xCoord;
        _yCoord = yCoord;
    }

    public int getX(){
        return _xCoord;
    }

    public int getY(){
        return _yCoord;
    }

    public void setX(int x){
        _xCoord = x;
    }

    public void setY(int y){
        _yCoord = y;
    }
}

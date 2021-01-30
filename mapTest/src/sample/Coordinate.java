package sample;

public class Coordinate
{
    private int y;
    private int x;

    //x and y reversed to account for 2D array indexing
    public Coordinate(int y, int x)
    {
        this.y = y;
        this.x = x;
    }
    public int getX()
    {
        return x;
    }
    public int getY()
    {
        return y;
    }
    public String toString()
    {
        return "(" + y +","+ x+")";
    }

    public double getDistance(Coordinate coordinate)
    {
        //returns the distance between coordinates
        return Math.sqrt(Math.pow(y - coordinate.getY(),2) + Math.pow(x - coordinate.getX(),2));
    }



}

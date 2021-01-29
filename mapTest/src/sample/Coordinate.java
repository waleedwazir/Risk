package sample;

public class Coordinate
{
    private int y;
    private int x;

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

}

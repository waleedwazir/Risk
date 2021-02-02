package sample;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return y == that.y &&
                x == that.x;
    }

}

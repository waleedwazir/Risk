package Map;

public class Coordinate
{
    private int y;
    private int x;

    //x and y reversed to account for 2D array indexing
    //constructor for Coordinate
    public Coordinate(int y, int x)
    {
        this.y = y;
        this.x = x;
    }

    //getters for coordinate's instance variables
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

    //returns the distance between two coordinates using the distance formula
    public double getDistance(Coordinate coordinate)
    {
        //returns the distance between coordinates
        return Math.sqrt(Math.pow(y - coordinate.getY(),2) + Math.pow(x - coordinate.getX(),2));
    }

    //returns true if the instance variable of two coordinates are equal
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return y == that.y &&
                x == that.x;
    }

}

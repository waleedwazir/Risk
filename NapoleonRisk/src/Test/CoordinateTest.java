package Test;

import Map.Coordinate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CoordinateTest {
    @Test
    void testInitialisation(){
        Coordinate c = new Coordinate(10, 5);
        assertEquals(c.getY(), 10);
        assertEquals(c.getX(), 5);
    }

    @Test
    void testToString(){
        Coordinate c = new Coordinate(10, 5);
        assertEquals(c.toString(), "(10,5)");
    }

    @Test
    void testEquals(){
        Coordinate x = new Coordinate(10, 5);
        Coordinate y = new Coordinate(10, 5);
        assertEquals(x.equals(y), true);
    }

    @Test
    void testGetDistance(){
        Coordinate x = new Coordinate(5, 5);
        Coordinate y = new Coordinate(10, 5);
        assertEquals(x.getDistance(y), 5); //should be distance formula
    }

}

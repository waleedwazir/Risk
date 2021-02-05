package sample;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CountryTest {
    @Test
    void testName(){
        Country country = new Country();
        country.setName("Ireland");
        assertEquals(country.getName(), "Ireland");
    }

    @Test
    void testAdding(){
        Country country = new Country();
        Coordinate a = new Coordinate(10, 10);
        Coordinate b = new Coordinate(10, 11);
        Coordinate c = new Coordinate(10, 12);
        country.addCoordinate(a);
        country.addCoordinate(b);
        country.addCoordinate(c);
        ArrayList<Coordinate> test = new ArrayList<Coordinate>();
        test.add(a);
        test.add(b);
        test.add(c);
        assertEquals(country.getCoordinates(), test);
    }
}

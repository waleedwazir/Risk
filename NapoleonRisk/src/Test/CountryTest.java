package Test;

import Map.Coordinate;
import Map.Country;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CountryTest {
    @Test
    void testSetName(){
        Country country = new Country();
        country.setName("Ireland");
        assertEquals("Ireland", country.getName());
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

    @Test
    void testGetCountryFromIndex(){
        String countryName = Country.getCountryNameFromIndex(5);
        assertEquals("Congo", countryName);
    }

    @Test
    void testGetIndex(){
        Country country = new Country();
        country.setName("Congo");
        assertEquals(5, country.getIndex());
    }

    @Test
    void testGetIndexFromCountryName(){
        int index = Country.getIndexFromCountryName("Indonesia");
        assertEquals(0, index);
    }

    @Test
    void testCalculateLevenshtein(){
        int levenshteinDistance1 = Country.calculateLevenshtein("Congo", "Congi");
        int levenshteinDistance2 = Country.calculateLevenshtein("Eastern Australia", "Eastern Aus");
        assertEquals(1, levenshteinDistance1);
        assertEquals(6, levenshteinDistance2);
    }

    @Test
    void testCoordinateIteration(){
        Country country = new Country();
        country.addCoordinate(new Coordinate(10, 10));
        country.addCoordinate(new Coordinate(10, 11));
        country.addCoordinate(new Coordinate(10, 12));
        int size = 0;
        for(Coordinate c:country){
            size++;
        }
        assertEquals(3, size);
    }
}

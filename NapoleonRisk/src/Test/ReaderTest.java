package Test;

import Map.Coordinate;
import Map.Country;
import Map.Reader;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ReaderTest
{

    @Test
    void testRun()
    {
        File file = new File("src/Map/countries.txt");
        Reader reader = new Reader(file);
        reader.run();
        //49 is total size of the countries when all information has been correctly read in
        if(reader.getCountries().size() != 49)
        {
            fail("File was not read correctly");
        }

    }

    /*
    @Test
    void testSetCoordinate()
    {
        File file = new File("src/Map/countries.txt");
        Reader reader = new Reader(file);
        Coordinate a = new Coordinate(1, 1);
        Coordinate b = new Coordinate(1, 2);
        Coordinate c = new Coordinate(1, 3);

        reader.setCoordinate(1,1);
        assertEquals(a,reader.getCoordinate());

        reader.setCoordinate(1,2);
        assertEquals(b,reader.getCoordinate());

        reader.setCoordinate(1,3);
        assertEquals(c,reader.getCoordinate());

    }
     */

    @Test
    void testGetCountries()
    {
        File file = new File("src/Map/countries.txt");
        Reader reader = new Reader(file);
        if(!(reader.getCountries() instanceof ArrayList))
        {
            fail("Return type invalid");
        }
    }
    @Test
    void testGetCountryName()
    {
        String[] countryNames = {"Indonesia", "New Guinea", "Western Australia", "Eastern Australia", "North Africa", "Congo",
                "Egypt", "East Africa", "South Africa", "Madagascar", "Argentina", "Venezuela", "Peru", "Brazil", "UK & Ireland",
                "Iceland", "Western Europe", "Southern Europe", "Northern Europe", "Ukraine", "Scandinavia", "Greenland", "Quebec",
                "Western USA", "Alberta", "Ontario", "North West Territory", "Eastern USA", "Central America", "Alaska",
                "Middle East", "India", "China", "Siam", "Mongolia", "Japan", "Afghanistan", "Ural", "Siberia", "Irkutsk", "Yakutsk",
                "Kamchatka"};
        File file = new File("src/Map/countries.txt");
        Reader reader = new Reader(file);
        for(int i=0;i < countryNames.length;i++)
        {
            assertEquals(countryNames[i],reader.getCountryName(i));
        }


    }
}
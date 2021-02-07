package Map;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CountriesTest {



    @Test
    void getCountries() {
        Countries set = new Countries();
        ArrayList<Country> countries = new ArrayList<Country>();


        assertEquals(set.getCountries() , countries );
    }

    @Test
    void size() {
        Countries test = new Countries();
        ArrayList<Country> countries = new ArrayList<Country>();

        assertEquals(49,test.size());
    }
}
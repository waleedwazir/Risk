
package Map;

import java.io.File;
import java.util.ArrayList;

public class Countries
{
    File file; //file used for retreiving countries
    private ArrayList<Country> countries; //countries array list

    //reads and stores all all coordinates in the countries array
    public Countries()
    {
        this.file = new File("Map/countries.txt");
        readFile();
    }
    //reader object for handling file reading
    public void readFile() {
        Reader reader = new Reader(file);
        reader.run();
        countries = reader.getCountries();
    }

    //Countries getter
    public ArrayList<Country> getCountries()
    {
        return countries;
    }

    //returns the size of the countries array list.
    public int size(){
        return countries.size();
    }

}
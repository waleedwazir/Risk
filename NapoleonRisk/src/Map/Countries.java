
package Map;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

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

    private final static int[][] continentIndices ={
            {0,1,2,3},
            {4,5,6,7,8,9},
            {10,11,12,13},
            {14,15,16,17,18,19,20},
            {21,22,23,24,25,26,27,28,29},
            {30,31,32,33,34,35,36,37,38,39,40,41}
    };

    public static int[] getContinentIndexes(int continentIndex)
    {
        return continentIndices[continentIndex];
    }

}

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

    public ArrayList<Integer> getContinentIndexes(String continentName)
    {
        ArrayList<Integer> list = new ArrayList();

        if(continentName.equals("Australia"))
        {
            Integer[] index = new Integer[] {0,1,2,3};
            list.addAll(Arrays.asList(index));

        }else if(continentName.equals("Africa"))
        {
            Integer[] index = new Integer[] {4,5,6,7,8,9};
            list.addAll(Arrays.asList(index));

        }else if(continentName.equals("South America"))
        {
            Integer[] index = new Integer[] {10,11,12,13};
            list.addAll(Arrays.asList(index));

        }else if(continentName.equals("Europe"))
        {
            Integer[] index = new Integer[] {14,15,16,17,18,19,20};
            list.addAll(Arrays.asList(index));

        }else if(continentName.equals("North America"))
        {
            Integer[] index = new Integer[] {21,22,23,24,25,26,27,28,29};
            list.addAll(Arrays.asList(index));

        }else if(continentName.equals("Asia"))
        {
            Integer[] index = new Integer[] {30,31,32,33,34,35,36,37,38,39,40,41};
            list.addAll(Arrays.asList(index));

        }else
        {
            throw new IllegalArgumentException("Error");
        }
        return list;
    }

}
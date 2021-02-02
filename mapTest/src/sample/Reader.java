package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class Reader
{

    private File file;
    private Coordinate coordinate;
    private ArrayList<Coordinate>  country;
    private ArrayList<Country> countries;

    public Reader(File test)
    {
        this.file = test;
        country  = new ArrayList<Coordinate>();
        countries = new ArrayList<Country>();
    }
    public void run()
    {
        try
        {
            //increments each to loop to append the arraylist of coordinates to the 2D arraylist
            int index = 0;
            Scanner scan = new Scanner(file);
            while(scan.hasNextLine())
            {
                String line = scan.nextLine();
                if(line.equals("XXX"))
                {
                    //lets scanner know we have reached the end of a country
                    //adds the country to continent ArrayList and clears the country ArrayList

                    appendCountry(countries, country, index);
                    index++;
                    country.clear();
                }
                if(!line.matches("[0-9]*.\t[0-9]*"))
                {
                    //scanner will skip any line that isn't in coordinate format
                    continue;
                }

                String[] split = line.split("\t");
                //stores the coordinate in reverse order to account for 2D array indexing
                setCoordinate(Integer.valueOf(split[0]), Integer.valueOf(split[1]));
                country.add(coordinate);
            }
        } catch (FileNotFoundException e)
        {
            System.out.println("Error reading file: " + e.getStackTrace());
        }

    }
    public void setCoordinate(int x, int y)
    {
        coordinate = new Coordinate(x,y);
    }
    public ArrayList<Country> getCountries()
    {
        return countries;
    }
    private void appendCountry(ArrayList<Country> countries, ArrayList<Coordinate> country, int index){

        //adds a newly initialised ArrayList to countries and fills it with Coordinate objects
        countries.add(new Country());
        if(index<42) {
            countries.get(index).setName(getCountryName(index));
        }
        for(Coordinate c:country){
            countries.get(index).addCoordinate(c);
        }
    }

    private String getCountryName(int index){
        String[] countryNames = {"Indonesia", "New Guinea", "Western Australia", "Eastern Australia", "North Africa", "Congo",
        "Egypt", "East Africa", "South Africa", "Madagascar", "Argentina", "Venezuela", "Peru", "Brazil", "UK & Ireland",
        "Iceland", "Western Europe", "Southern Europe", "Northern Europe", "Ukraine", "Scandinavia", "Greenland", "Quebec",
        "Western USA", "Alberta", "Ontario", "North West Territory", "Eastern USA", "Central America", "Alaska",
        "Middle East", "India", "China", "Siam", "Mongolia", "Japan", "Afghanistan", "Ural", "Siberia", "Irkutsk", "Yakutsk",
        "Kamchatka"};
        return countryNames[index];
    }



}

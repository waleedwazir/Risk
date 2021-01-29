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
    private ArrayList<ArrayList<Coordinate>> countries;

    public Reader(File test)
    {
        this.file = test;
        country  = new ArrayList<Coordinate>();
        countries = new ArrayList<ArrayList<Coordinate>>();
    }

    public void run()
    {
        try
        {
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
                setCoordinate(Integer.valueOf(split[0]),Integer.valueOf(split[1]));
                country.add(coordinate);
            }
        } catch (FileNotFoundException e)
        {
            System.out.println("Error reading file: " + e.getStackTrace());
        }

    }
    public void setCoordinate(int x, int y)
    {
        coordinate = new Coordinate(y,x);
    }
    public ArrayList<ArrayList<Coordinate>> getCountries()
    {
        return countries;
    }

    public void printCoordinates()
    {
        int i = 0;
        for(ArrayList<Coordinate> countryArray: countries)
        {
            System.out.println(i++);
            for(Coordinate countryCoordinate: countryArray)
            {
                System.out.println(countryCoordinate.toString());
            }
        }
    }

    private void appendCountry(ArrayList<ArrayList<Coordinate>> countries, ArrayList<Coordinate> country, int index){
        countries.add(new ArrayList<Coordinate>());
        for(Coordinate c:country){
            countries.get(index).add(c);
        }
    }

}

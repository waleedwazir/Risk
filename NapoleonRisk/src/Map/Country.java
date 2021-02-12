package Map;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Country implements Iterable<Coordinate>{
    private String name;
    private ArrayList<Coordinate> coordinates= new ArrayList<Coordinate>();
    private static String[] countryNames = {"Indonesia", "New Guinea", "Western Australia", "Eastern Australia", "North Africa", "Congo",
            "Egypt", "East Africa", "South Africa", "Madagascar", "Argentina", "Venezuela", "Peru", "Brazil", "UK & Ireland",
            "Iceland", "Western Europe", "Southern Europe", "Northern Europe", "Ukraine", "Scandinavia", "Greenland", "Quebec",
            "Western USA", "Alberta", "Ontario", "North West Territory", "Eastern USA", "Central America", "Alaska",
            "Middle East", "India", "China", "Siam", "Mongolia", "Japan", "Afghanistan", "Ural", "Siberia", "Irkutsk", "Yakutsk",
            "Kamchatka"};

    //adds a coordinate to the country's coordinate arraylist
    public void addCoordinate(Coordinate c){
        coordinates.add(c);
    }

    //getter for coordinates of a country
    public ArrayList<Coordinate> getCoordinates(){
        return coordinates;
    }

    //setter for country's name
    public void setName(String name) {
        this.name = name;
    }

    //getter for country's name
    public String getName(){
        return name;
    }


    public int getIndex()
    {
        for(int i=0;i<42;i++)
        {
            if(countryNames[i].equals(name))
            {
                return i;
            }
        }
        return -1;
    }
    public static int getIndexFromCountryName(String name)
    {
        for(int i=0;i<42;i++)
        {
            if(countryNames[i].equals(name))
            {
                return i;
            }
        }
        return -1;
    }



    //Iterator for iterating through country's coordinates
    private class CountryIterator<Coordinate> implements Iterator<Coordinate>{
        int index = 0;

        @Override
        public boolean hasNext() {
            return index < coordinates.size();
        }

        @Override
        public Coordinate next() {
            if(hasNext()){
                Coordinate ret = (Coordinate) coordinates.get(index);
                index++;
                return ret;
            }else{
                throw new NoSuchElementException();
            }
        }
    }

    //initialise Country iterator
    @Override
    public Iterator<Coordinate> iterator() {
        return new CountryIterator<Coordinate>();
    }
}

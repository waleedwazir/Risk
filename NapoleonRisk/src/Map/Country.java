package Map;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Country implements Iterable<Coordinate>{
    private String name;
    private ArrayList<Coordinate> coordinates= new ArrayList<Coordinate>();

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

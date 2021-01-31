package sample;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Country implements Iterable<Coordinate>{
    private String name;
    private ArrayList<Coordinate> coordinates= new ArrayList<Coordinate>();

    public void addCoordinate(Coordinate c){
        coordinates.add(c);
    }

    public ArrayList<Coordinate> getCoordinates(){
        return coordinates;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }

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


    @Override
    public Iterator<Coordinate> iterator() {
        return new CountryIterator<Coordinate>();
    }
}

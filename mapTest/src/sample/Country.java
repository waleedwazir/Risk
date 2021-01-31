package sample;

import java.util.ArrayList;

public class Country {
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
}

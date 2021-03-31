package Cards;

import Map.Country;
import java.util.ArrayList;

public  class Card
{

    private boolean cond;
    private  Country country;
    private  String type;

    private ArrayList<String[]> exchangeCardTypes;

    public Card(String type, Country country ) {
        this.type = type;
        this.country = country;
    }
    public void setType(String type)
    {
        this.type = type;
    }

    public Country getCountry() {
        return country;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return country.getName();
    }

    @Override
    public String toString(){
        return getName() + " "+ getType();
    }

}

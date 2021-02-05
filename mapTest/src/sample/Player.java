package sample;

import java.util.HashMap;


public class Player {

    private String name;
    private int totalCards;
    private int totalArmies;
    private Colors PlayerColor;
    private HashMap<String, Country> AssignedCountries;

    public Player(){};

    // colors for the players
    enum Colors {
        GREEN,
        YELLOW,
        BLUE,
        PURPLE,
        ORANGE
    }
    
    public Player(String name){
        
        this.name = name;
        
        AssignedCountries = new HashMap<String, Country>();
        
    }


    public void setColors (Colors PlayerColor){
        this.PlayerColor = PlayerColor;
    }

    public Colors getColors(){
        return PlayerColor;
    }


    public void getName(String name){
        this.name = name;
    }

    public void setAssignedCountries(HashMap<String, Country>AssignedCountries){
        this.AssignedCountries = AssignedCountries;

    }

    public HashMap<String, Country> getAssignedCountries() {
        return AssignedCountries;
    }

    // to see the amount of total cards
    public int getTotalCards() {
        return totalCards;
    }

    public void setTotalCards(int totalCards) {
        this.totalCards = totalCards;
    }

    //to see the amount of total armies 1 player has
    public int getTotalArmies() {
        return totalArmies;
    }

    public void setTotalArmies(int totalArmies) {
        this.totalArmies = totalArmies;
    }

    /*
    function that adds a captured country to the arraylist
    used hashmap because of string
    
     */
    public void addCountry(Country country){
        
        
        getAssignedCountries().put(country.getName(), country);

    }
    
    //similar to the function above but can add multiple countries at once
    public void addCountry(ArrayList<Country> countryList){
        for(int i = 0; i < countryList.size(); i++){
            getAssignedCountries().put(countryList.get(i).getName(), countryList.get(i));
        }
    }
    
   


    //removes country when a player loses it
    public void removeCountry(String Country){
        getAssignedCountries().remove(Country);
    }

   



        // toString shows the player and the amount of total assets it has
    public String toString(){
        return name + "Owns: " + getAssignedCountries() + " Color is: " + getColors() + " Total Cards: " + getTotalCards() + " Total Armies: " +  getTotalArmies();
    }



}

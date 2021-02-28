package Player;

import Cards.Card;
import Map.Countries;
import Map.Country;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.HashMap;


public class Player {

    private String name;
    private int totalCards;
    private int totalArmies;
    private Color PlayerColor;
    private HashMap<String, Country> AssignedCountries = new HashMap<String, Country>();
    private int troops;
    private Card hand;
    private int turnInCount;

    public Player(){
    }

    //neutral constructor
    public Player(Color colour){
        this.PlayerColor = colour;
        this.troops = 24;
        hand = new Card();
    }

    public Player(Color colour, int num){
        this.PlayerColor = colour;
        troops = num;
        hand = new Card();
    }

    public void setColors (Color PlayerColor){
        this.PlayerColor = PlayerColor;
    }

    public Color getColour(){
        return PlayerColor;
    }

    public void setName(String name){
        this.name = name;
        this.troops = 36;
    }

    public String getName() {
        return name;
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

    //removes country when a player loses it, account for when removing a country by two different methods
    public void removeCountry(String Country){
        getAssignedCountries().remove(Country);
    }

    public void removeCountry(Country country){
        getAssignedCountries().remove(country.getName());
    }

    // toString shows the player and the amount of total assets it has
    public String toString(){
        return name + "Owns: " + getAssignedCountries() + " Color is: " + getColour() + " Total Cards: " + getTotalCards() + " Total Armies: " +  getTotalArmies();
    }

    public void setTroops(int num){
        troops = num;
    }

    public void addTroops(int num){
        troops+=num;
    }

    public void decrementTroops(int num){
        troops-=num;
    }

    public int getTroops(){
        return troops;
    }

    public void addCard(Card card){
        hand.add(card);
    }

    //removes cards to from players hand to reflect cards being turned in
    public void removeCards(int[] cardsTurnedInIndex) {
        hand.removeCards(cardsTurnedInIndex[0], cardsTurnedInIndex[1], cardsTurnedInIndex[2]);
    }

    public int getTurnInCount() {
        turnInCount++;
        return turnInCount;
    }

    public ArrayList<Card> getHand() {
        return hand.getCards();
    }

    public Card getHandObject() {
        return hand;
    }

    public boolean mustTurnInCards() {
        return hand.mustTurnIn();
    }

    public boolean isEliminated(){
        return getAssignedCountries().isEmpty();
    }

    public int getExtraTroops(){
        int sum = 0;
        int base = (getAssignedCountries().size()/3);
        if(base < 3){
            base = 3;
        }
        for(int i=0;i<6;i++) {
            boolean check = true;
            int[] countries = Countries.getContinentIndexes(i);
            for(Integer index:countries){
                if(!getAssignedCountries().containsKey(Country.getCountryNameFromIndex(index))){
                    check = false;
                }
            }
            if(check){
                if(i == 0 || i == 2){
                    sum+=2;
                }else if(i == 1){
                    sum+=3;
                }else if(i == 3 || i == 4){
                    sum+=5;
                }else if(i == 5){
                    sum+=7;
                }
            }
        }
        return sum + base;
    }

}

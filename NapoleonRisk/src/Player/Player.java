package Player;

import Cards.CardHandle;
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
    private CardHandle hand;
    private int turnInCount;

    public Player(){
    }

    //neutral constructor
    public Player(Color colour){
        this.PlayerColor = colour;
        this.troops = 24;
    }

    public Player(Color colour, int num){
        this.PlayerColor = colour;
        troops = num;
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

    //removes country when a player loses it
    public void removeCountry(String Country){
        getAssignedCountries().remove(Country);
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

    public void addCard(CardHandle card){
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

    public ArrayList<CardHandle> getHand() {

        return hand.getCards();
    }

    public CardHandle getHandObject() {

        return hand;
    }

    public boolean mustTurnInCards() {

        return hand.mustTurnIn();
    }


}

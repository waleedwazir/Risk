package sample;

import java.util.ArrayList;


public class Player {

    private String name;
    private String PlayerColor;
    private int totalCards;
    private int totalArmies;
    private ArrayList<Country> AssignedCountries;

    // colors for the players
    enum Colors {
        GREEN,
        YELLOW,
        BLUE,
        PURPLE,
        ORANGE
    }

    public void setPlayerColor(String PlayerColor){
        this.PlayerColor = PlayerColor;
    }

    public String getPlayerColor(){
        return PlayerColor;
    }


    public void setName(String name){
        this.name = name;
    }

    public void setAssignedCountries(ArrayList<Country>AssignedCountries){
        this.AssignedCountries = AssignedCountries;

    }

    public ArrayList<Country> getAssignedCountries() {
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

    public Player(String name) {
        super();
        this.name = name;
    }



        // shows the player and the amount of total assets it has
    public String toString(){
        return name + "Owns: " + getAssignedCountries() + " Color is: " + getPlayerColor() + " Total Cards: " + getTotalCards() + " Total Armies: " +  getTotalArmies();
    }



}
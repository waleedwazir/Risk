package Player;

import Map.Country;
import Player.Player;

public class Army {
    //Instance variables
    private int armySize;
    private Player player;
    private Country country;

    //constructor
    public Army(Player player, int armySize, Country country){
        this.player = player;
        this.armySize = armySize;
        this.country = country;
    }



    //getters and setters
    public int getArmySize() {
        return armySize;
    }

    public void setArmySize(int armySize) {
        this.armySize = armySize;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public void incrementSize(int add){
        this.armySize+=add;
    }
}

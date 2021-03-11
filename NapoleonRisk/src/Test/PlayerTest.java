package Test;

import Player.Player;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;
import Map.Country;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {


    @Test
    void getColors() {
        Player color = new Player();
        color.setColors(Color.GREEN);
        assertEquals(color.getColour(), Color.GREEN);

    }

    @Test
    void getName() {
        Player test = new Player();
        test.setName("Waleed");
        assertEquals(test.getName(), "Waleed");
    }

    @Test
    void getAssignedCountries() {
        Player Assigned = new Player();
        HashMap<Integer, Country> test = new HashMap<Integer, Country>();
        Country Ireland = new Country();

        test.put(1, Ireland);
        Assigned.setAssignedCountries(test);

        assertEquals(Assigned.getAssignedCountries(), test);
    }



    @Test
    void getTotalCards() {
        Player cards = new Player();
        cards.setTotalCards(10);
        assertEquals(cards.getTotalCards(), 10);
    }

    @Test
    void getTotalArmies() {
        Player army = new Player();
        army.setTotalArmies(90);
        assertEquals(army.getTotalArmies(), 90);
    }

    @Test
    void testToString() {
        Player list = new Player();
        list.setName("Tom");
        list.setTotalArmies(90);
        list.setTotalCards(10);
        list.setColors(Color.GREEN);

        HashMap<Integer, Country> man = new HashMap<Integer, Country>() ;
        list.setAssignedCountries(man);
        String output = list.getName() + "Owns: " + list.getAssignedCountries() + " Color is: " + list.getColour() + " Total Cards: " + list.getTotalCards() + " Total Armies: " +  list.getTotalArmies();

        assertEquals(list.toString(), output);

    }

    @Test
    void testCountryRemoveAndAdd(){
        Player napoleon = new Player();
        Country corsica = new Country();
        corsica.setName("Corsica");
        napoleon.addCountry(corsica);
        assertEquals(false, napoleon.getAssignedCountries().isEmpty());
        napoleon.removeCountry(corsica);
        assertEquals(true, napoleon.getAssignedCountries().isEmpty());
    }

    @Test
    void testIsEliminated(){
        Player napoleon = new Player();
        Country corsica = new Country();
        corsica.setName("Corsica");
        napoleon.addCountry(corsica);
        assertEquals(false, napoleon.isEliminated());
        napoleon.removeCountry(corsica);
        assertEquals(true, napoleon.isEliminated());
    }

    @Test
    void testGetExtraTroops(){
        Player napoleon = new Player();
        Country indonesia = new Country();
        Country newGuinea = new Country();
        Country westernAustralia = new Country();
        Country easternAustralia = new Country();
        indonesia.setName("Indonesia");
        newGuinea.setName("New Guinea");
        westernAustralia.setName("Western Australia");
        easternAustralia.setName("Eastern Australia");
        napoleon.addCountry(indonesia);
        napoleon.addCountry(newGuinea);
        napoleon.addCountry(westernAustralia);
        napoleon.addCountry(easternAustralia);
        assertEquals(5, napoleon.getExtraTroops());
    }
}
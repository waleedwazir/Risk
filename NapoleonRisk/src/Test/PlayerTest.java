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
        assertEquals(color.getColors(), Color.GREEN);

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
        HashMap<String, Country> test = new HashMap<String, Country>();
        Country Ireland = new Country();

        test.put("aye", Ireland);
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

        HashMap<String, Country> man = new HashMap<String, Country>() ;
        list.setAssignedCountries(man);
        String output = list.getName() + "Owns: " + list.getAssignedCountries() + " Color is: " + list.getColors() + " Total Cards: " + list.getTotalCards() + " Total Armies: " +  list.getTotalArmies();

        assertEquals(list.toString(), output);

    }
}
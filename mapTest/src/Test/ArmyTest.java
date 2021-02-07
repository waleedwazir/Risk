package sample;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArmyTest {



    @Test
    void incrementSize() {
        Army test = new Army(new Player(), 50, new Country());
        int output = test.incrementSize(5);
        int output1 = test.incrementSize(6);
        int output2 = test.incrementSize(10);

        assertEquals(55, output);
        assertEquals(61, output1);
        assertEquals(71, output2);

    }

    @Test
    void getArmySize() {
        Army size = new Army(new Player(), 90, new Country());
        size.setArmySize(90);
        assertEquals(size.getArmySize(), 90);
    }

    @Test
    void getCountry(){
        Army size = new Army(new Player(), 90, new Country());
        Country Ireland = new Country();
        size.setCountry(Ireland);

        assertEquals(Ireland, size.getCountry());
    }

    @Test
    void getPlayer(){
        Army size = new Army(new Player(), 90, new Country());
        Player plays = new Player();
        size.setPlayer(plays);
        assertEquals(plays, size.getPlayer());
    }


}
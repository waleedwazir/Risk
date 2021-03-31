package Test;

import Cards.Card;
import Map.Country;
import org.junit.jupiter.api.Test;
import Cards.Hand;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HandTest {
    @Test
    void testValidCommands(){
        Hand testHand = new Hand();
        assertEquals(false, testHand.validExchangeCommand("sdf"));
        assertEquals(false, testHand.validExchangeCommand("1231"));
        assertEquals(true, testHand.validExchangeCommand("iii"));
        assertEquals(true, testHand.validExchangeCommand("ccc"));
        assertEquals(true, testHand.validExchangeCommand("aaa"));
        assertEquals(true, testHand.validExchangeCommand("ica"));
        assertEquals(true, testHand.validExchangeCommand("aci"));
        assertEquals(true, testHand.validExchangeCommand("cia"));
    }

    @Test
    void testExchange(){
        Country country = new Country();
        Hand handTest1 = new Hand();
        handTest1.add(new Card("Infantry",country));
        handTest1.add(new Card("Infantry",country));
        handTest1.add(new Card("Infantry",country));
        assertEquals(3, handTest1.getCards().size());

        assertEquals(true,handTest1.exchangeCards("iii"));
        assertEquals(0, handTest1.getCards().size());

        handTest1.add(new Card("Cavalry",country));
        handTest1.add(new Card("Cavalry",country));
        handTest1.add(new Card("Cavalry",country));
        assertEquals(3, handTest1.getCards().size());

        assertEquals(true,handTest1.exchangeCards("ccc"));
        assertEquals(0, handTest1.getCards().size());

        handTest1.add(new Card("Infantry", country));
        handTest1.add(new Card("Cavalry", country));
        handTest1.add(new Card("Artillery", country));
        handTest1.add(new Card("Infantry", country));
        assertEquals(4, handTest1.getCards().size());

        assertEquals(true, handTest1.exchangeCards("cia"));
        assertEquals(1, handTest1.getCards().size());

        handTest1.add(new Card("Infantry",country));
        handTest1.add(new Card("Infantry",country));
        handTest1.add(new Card("Infantry",country));
        assertEquals(4, handTest1.getCards().size());

        assertEquals(false,handTest1.exchangeCards("garbage"));
        assertEquals(4, handTest1.getCards().size());
    }
}

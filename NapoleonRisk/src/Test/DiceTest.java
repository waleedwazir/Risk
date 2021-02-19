package Test;

import GameLogic.Dice;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DiceTest
{

    @Test
    void testThrowDice()
    {
        Dice dice = new Dice();
        int roll;

        for(int i=0;i<100000;i++)
        {
            roll = dice.throwDice();
            if(roll < 1 || roll > 6)
            {
                fail("Number is outside the range of a dice!");
            }
        }

    }

    @Test
    void testGetRolls()
    {
        Dice dice = new Dice();
        dice.rollXDice(5);

        if(!(dice.getRolls() instanceof ArrayList))
        {
            fail("Type returned is incorrect");
        }
    }

    //method not implemented yet
    /*
    @Test
    void determineRollWinner()
    {
    }
     */

    @Test
    void testRollXDice()
    {
        Dice dice = new Dice();
        for(int i=0;i<100;i++)
        {
            dice.rollXDice(i);
            if(dice.getRolls().size() != i)
            {
                fail("Method returning incorrect number of rolls");
            }
        }
    }
}
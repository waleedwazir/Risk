package GameLogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Dice
{
    private Random random;
    private ArrayList<Integer> rolls;

    public Dice(){
        random = new Random();
        rolls = new ArrayList<Integer>();
    }

    public int throwDice(){
        int dice = random.nextInt(6) + 1;
        return dice;
    }
    //rolls dice and sorts them in descending order
    //risk only takes the top roles so this will allow us to take the top rolls
    public ArrayList<Integer> rollXDice(int x)
    {
        for(int i=0 ; i<x; i++)
        {
            rolls.add(throwDice());
        }
        Collections.sort(rolls, Collections.reverseOrder());
        return rolls;
    }
    public String toString()//for debugging
    {
        String ret = "Rolls: ";
        for(int roll: rolls)
        {
            ret += roll + "\t";
        }
        return ret;
    }

}

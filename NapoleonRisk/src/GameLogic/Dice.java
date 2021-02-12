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

    //generates a random number between 1 and 6 (dice)
    public int throwDice(){
        int dice = random.nextInt(6) + 1;
        return dice;
    }

    public ArrayList<Integer> getRolls()
    {
        return rolls;
    }


    //pass in defenders dice and it will update armies appropriately
    public void determineRollWinner(Dice defender)
    {
        ArrayList<Integer> defenderRolls = defender.getRolls();
        if(rolls.size() > defenderRolls.size())
        {
            for(int i=0;i<defenderRolls.size();i++)
            {
                if(rolls.get(i) <= defenderRolls.get(i))
                {
                    //attacker loses piece
                }else
                {
                    //defender losses piece
                }
            }
        }else
        {
            for(int i=0;i<rolls.size();i++)
            {
                if(rolls.get(i) <= defenderRolls.get(i))
                {
                    //attacker loses piece
                }else
                {
                    //defender losses piece
                }
            }
        }

    }

    //rolls X number of dice and stores them in the rolls instance variable
    //every call clears the old rolls
    public ArrayList<Integer> rollXDice(int x)
    {
        rolls.clear();
        for(int i=0 ; i<x; i++)
        {
            rolls.add(throwDice());
        }
        Collections.sort(rolls, Collections.reverseOrder());
        return rolls;
    }

}

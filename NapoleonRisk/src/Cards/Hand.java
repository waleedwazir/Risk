package Cards;

import Map.Country;

import java.util.ArrayList;
import java.util.Arrays;

public class Hand {
    private ArrayList<Card> hand;
    private String[] exchangeCardCommands = {
            "iii",
            "ica",
            "iac",
            "ccc",
            "cia",
            "cai",
            "aaa",
            "aic",
            "aci"};
    
    private ArrayList<String[]> exchangeCardTypes = new ArrayList<>(Arrays.asList(new String[] {"Infantry","Infantry","Infantry"},
            new String[] {"Cavalry","Cavalry","Cavalry"},
            new String[] {"Artillery","Artillery","Artillery"},
            new String[] {"Infantry","Cavalry","Artillery"}));
    
    
    
    public Hand(){
        hand = new ArrayList<Card>();
    }

    public void add(Card card){
        hand.add(card);
    }

    //when you turn in cards this will remove the cards from the hand
    public void removeCards(ArrayList<Integer> indices){
        ArrayList<Card> toRemove = new ArrayList<>();
        for(Integer n:indices){
            toRemove.add(hand.get(n));
        }
        hand.removeAll(toRemove);
    }

    //if check is set to true then cards wont be removed only a boolean will be returned
    private boolean hasCardsInHand(String[] types, boolean check) {
        Card blankCard = new Card("", new Country());

        //creates a copy of the hand to alter
        ArrayList<Card> tempHand = (ArrayList<Card>) hand.clone();
        //arraylist to store indexes of cards to be removed
        ArrayList<Integer> indexes = new ArrayList<>();
        //counter for how many cards match the types
        int counter = 0;

        //compares types we're looking for with the cards in the hand
        //counter reaches 3 if all cards are present
        //they will be removed and the function returns true
        //otherwise it returns false
        for (int i = 0; i < types.length; i++) {
            for (int j = 0; j < tempHand.size(); j++) {
                if (tempHand.get(j).getType().equals(types[i])) {
                    counter++;
                    tempHand.set(j, blankCard);
                    indexes.add(j);
                    break;
                }
            }
            if (counter == 3)
                break;
        }
        if (counter == 3) {
            if (check == false)
                removeCards(indexes);
            return true;
        }
        return false;
    }

    //returns true if a player must turn in cards
    public boolean mustTurnIn() {
        return hand.size()>=5;
    }

    //returns the hand
    public ArrayList<Card> getCards() {
        return hand;
    }

    public boolean validExchangeCommand(String input)
    {
        for(String command: exchangeCardCommands)
        {
            if(command.equalsIgnoreCase(input))
            {
                return true;
            }
        }
        return false;
    }

    public boolean exchangeCards(String input)
    {
        if(validExchangeCommand(input))
        {
            if(input.equalsIgnoreCase("iii"))
            {
                return hasCardsInHand(exchangeCardTypes.get(0), false);
            }
            else if(input.equalsIgnoreCase("ccc"))
            {
                return hasCardsInHand(exchangeCardTypes.get(1), false);
            }
            else if(input.equalsIgnoreCase("aaa"))
            {
                return hasCardsInHand(exchangeCardTypes.get(2),false);
            }
            else
            {
                return hasCardsInHand(exchangeCardTypes.get(3), false);
            }
        }
        return false;
    }

    public boolean canExchange(){
        for(String[] types:exchangeCardTypes){
            if(hasCardsInHand(types, true)){
                return true;
            }
        }
        return false;
    }
}

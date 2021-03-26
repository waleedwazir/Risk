package Cards;

import Map.Country;
import java.util.ArrayList;

public  class Card
{

    private boolean cond;
    private ArrayList<Card> hand;
    private  Country country;
    private  String type;
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
    private ArrayList<String[]> exchangeCardTypes;

    public Card(){

        //instantiate deck
        hand = new ArrayList<Card>();
        initialiseExchange();
    }

    public Card(String type, Country country ) {
        this.type = type;
        this.country = country;
        initialiseExchange();
    }
    public void setType(String type)
    {
        this.type = type;
    }

    //initializes the exchangeCardType array with array list of the different type combinations
    public void initialiseExchange()
    {
        exchangeCardTypes = new ArrayList<>();
        String[] allInfantry = {"Infantry","Infantry","Infantry"};
        String[] allCavalry = {"Cavalry","Cavalry","Cavalry"};
        String[] allArtillery = {"Artillery","Artillery","Artillery"};
        String[] differentCards = {"Infantry","Cavalry","Artillery"};
        exchangeCardTypes.add(allInfantry);
        exchangeCardTypes.add(allCavalry);
        exchangeCardTypes.add(allArtillery);
        exchangeCardTypes.add(differentCards);
    }
    public Country getCountry() {
        return country;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return country.getName();
    }

    //adds card
    public void add(Card card){
        hand.add(card);
    }

    //when you turn in cards this will remove the cards from the hand
    public void removeCards(int c1, int c2, int c3){
        hand.remove(c3);
        hand.remove(c2);
        hand.remove(c1);
    }
      //returns true if a player must turn in cards
    public boolean mustTurnIn() {

        cond = false;

        if (hand.size() > 6) {
            cond = true;
        }
        return cond;
    }

    @Override
    public String toString(){
        return getName() + " "+ getType();
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
                return hasCardsInHand(exchangeCardTypes.get(0));
            }
            else if(input.equalsIgnoreCase("ccc"))
            {
                return hasCardsInHand(exchangeCardTypes.get(1));
            }
            else if(input.equalsIgnoreCase("aaa"))
            {
                return hasCardsInHand(exchangeCardTypes.get(2));
            }
            else if(input.equalsIgnoreCase("ica") || input.equalsIgnoreCase("iac") || input.equalsIgnoreCase("cia")
            || input.equalsIgnoreCase("cai") || input.equalsIgnoreCase("aic") || input.equalsIgnoreCase("aci"))
            {
                return hasCardsInHand(exchangeCardTypes.get(3));
            }
        }
        return false;
    }
    private boolean hasCardsInHand(String[] types)
    {
        Card blankCard = new Card();
        blankCard.setType("");

        //creates a copy of the hand to alter
        ArrayList<Card> tempHand = hand;
        //arraylist to store indexes of cards to be removed
        ArrayList<Integer> indexes = new ArrayList<>();
        //counter for how many cards match the types
        int counter = 0;


        //compares types we're looking for with the cards in the hand
        //counter reaches 3 if all cards are present
        //they will be removed and the function returns true
        //otherwise it returns false
        for(int i=0;i<types.length;i++)
        {
            for(int j=0;j<tempHand.size();j++)
            {
                if(tempHand.get(j).getType().equals(types[i]))
                {
                    counter++;
                    tempHand.set(j,blankCard);
                    indexes.add(j);
                }
            }
        }
        if(counter == 3)
        {
            removeCards(indexes.get(0),indexes.get(1),indexes.get(2));
            return true;
        }
        return false;
    }



}

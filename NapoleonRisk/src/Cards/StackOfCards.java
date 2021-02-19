package Cards;

import java.util.ArrayList;
import java.util.Collections;
import Map.Country;

public class StackOfCards {

    private String input;
    private String name;
    private String[] typesArr;
    private ArrayList<CardHandle> stack;
    private ArrayList<Country> countries;
    private int index;
    private CardHandle getCard;

    /*
    create 42 cards, one for each country

    each country will have a one of each infantry, Cavalry and Artillery
     */
    public StackOfCards(ArrayList<Country> countries) {

        Collections.shuffle(countries);

        //Type of cards
        typesArr = new String[]{ "Infantry", "Cavalry", "Artillery"};

        stack = new ArrayList<CardHandle>();

        for (index = 0; index < countries.size(); index++) {
            //adds new cards to deck
            stack.add(new CardHandle(typesArr[index / 14], countries.get(index)));
            System.out.println("New Card added to Deck: " + stack.get(index).getName());
        }

        Collections.shuffle(stack);
    }

    //shuffles Deck of cards
    public void shuffle() {

        Collections.shuffle(stack);
    }


    //removes a card from the deck and return it
    public CardHandle draw() {

        getCard = stack.get(0);
        stack.remove(0);
        return getCard;
    }

    //adds a card to the deck
    public void add(CardHandle card) {

        stack.add(card);
    }



}





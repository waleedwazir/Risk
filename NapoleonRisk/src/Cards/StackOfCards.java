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
    private String[] wild;
    private int index;
    private CardHandle getCard;

    /*
    create cards for each country
     */
    public StackOfCards(ArrayList<Country> countries) {

        //Type of cards
        typesArr = new String[]{ "Infantry", "Cavalry", "Artillery"};

        //not implemented yet
        wild = new String[]{"WildCard"};


        stack = new ArrayList<CardHandle>();

        for (index = 0; index < countries.size(); index++) {
            stack.add(new CardHandle(typesArr[index / 14], countries.get(index)));
            System.out.println("New Card added to Deck: " + stack.get(index).getName());
        }

        Collections.shuffle(stack);
    }

    public void shuffleDeck() {

        Collections.shuffle(stack);
    }

    public CardHandle draw() {

        getCard = stack.get(0);
        stack.remove(0);
        return getCard;
    }

    public void add(CardHandle card) {

        stack.add(card);
    }

}



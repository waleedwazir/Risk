package Cards;

import java.util.ArrayList;
import java.util.Collections;

import Map.Countries;
import Map.Country;

public class Deck
{

    private String[] typesArr;
    private ArrayList<Card> stack;
    private ArrayList<Country> countries;
    private int index;
    private Card getCard;

    /*
    create 42 cards, one for each country
    each country will have a one of each infantry, Cavalry and Artillery
     */
    public Deck(Countries countries) {

        this.countries = countries.getCountries();
        //Collections.shuffle(this.countries);

        //Type of cards
        typesArr = new String[]{ "Infantry", "Cavalry", "Artillery"};

        stack = new ArrayList<Card>();

        for (index = 0; index < 42; index++) {
            //adds new cards to deck
            stack.add(new Card(typesArr[index / 14], this.countries.get(index)));
            //System.out.println("New Card added to Deck: " + stack.get(index).getName());
        }

        Collections.shuffle(stack);
    }

    //removes a card from the deck and return it
    public Card draw() {

        getCard = stack.get(0);
        stack.remove(0);
        return getCard;
    }

    //adds a card to the deck
    public void add(Card card) {

        stack.add(card);
    }



}





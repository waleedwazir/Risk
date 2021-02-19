package Cards;

import Map.Country;
import java.util.ArrayList;

public  class CardHandle {

    private boolean cond;
    private ArrayList<CardHandle> hand;
    private  Country country;
    private  String type;

    public CardHandle(){

        //instantiate deck
        hand = new ArrayList<CardHandle>();
    }

    public CardHandle(String type, Country country ) {
        this.type = type;
        this.country = country;
    }

    public Country getCountry() {
        return country;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return country.getName() + " " + type;
    }

    //returns true if the player can turn in cards
    public boolean can(int c1, int c2, int c3){
        cond = false;

        if (hand.size() >= 3) {
            if (hand.get(c1).getType().equals(hand.get(c2).getType()) && hand.get(c1).getType().equals(hand.get(c3).getType())) {
               //if the three cards are the same type
                cond = true;

            } else if (
                    !hand.get(c1).getType().equals(hand.get(c2).getType()) && !hand.get(c1).getType().equals(hand.get(c3).getType()) && !hand.get(c2).getType().equals(hand.get(c3).getType())) {
                //if the three cards have different types
                cond = true;
            }
        }
        return cond;
    }

    //adds card
    public void add(CardHandle card){
        hand.add(card);
    }


    //when you turn in cards this will remove the cards from the hand
    public void removeCards(int c1, int c2, int c3){
        if(can(c1, c2, c3) == true){
            hand.remove(c3);
            hand.remove(c2);
            hand.remove(c1);
        }else{
            System.out.println("Cards must be three of the same type or one of each type to trade in.");

        }
    }
      //returns true if a player must turn in cards
    public boolean mustTurnIn() {

        cond = false;

        if (hand.size() > 6) {
            cond = true;
        }
        return cond;
    }

    public String toString(){
        return getName() + getType() + getCountry();
    }


    //returns the hand
    public ArrayList<CardHandle> getCards() {
        return hand;
    }




}

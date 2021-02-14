package Cards;

import Map.Country;
import java.util.ArrayList;

public  class CardHandle {

    private boolean cond;
    private ArrayList<CardHandle> hand;
    private  Country country;
    private  String type;

    public CardHandle(){
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

    public boolean can(int c1, int c2, int c3){
        cond = false;

        if (hand.size() >= 3) {
            if (hand.get(c1).getType().equals(hand.get(c2).getType()) && hand.get(c1).getType().equals(hand.get(c3).getType())) {
                cond = true;

            } else if (
                    !hand.get(c1).getType().equals(hand.get(c2).getType()) && !hand.get(c1).getType().equals(hand.get(c3).getType()) && !hand.get(c2).getType().equals(hand.get(c3).getType())) {
                cond = true;
            }
        }
        return cond;
    }

    //adds card
    public void add(CardHandle card){
        hand.add(card);
    }

    public void removeCards(int c1, int c2, int c3){
        if(can(c1, c2, c3) == true){
            hand.remove(c3);
            hand.remove(c2);
            hand.remove(c1);
        }else{
            System.out.println("Cards must be three of the same type or one of each type to trade in.");

        }
    }

    public ArrayList<CardHandle> getCards() {
        return hand;
    }




}
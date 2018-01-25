package programming2_assignment2.classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import programming2_assignment2.classes.Card.Rank;
import programming2_assignment2.classes.Card.Suit;

/**
 *
 * @author Hakeem
 */
public final class Hand implements Serializable, Iterable<Card>{
    
    static final long serialVersionUID = 102L;
    
    private List<Card> addedOrderHandSnapshot;
    private List<Card> hand;
    private HashMap<Card.Rank, Integer> rankCounter;
    private List<Integer> possibleValues;
    
    public List<Integer> getPossibleValues(){
        //ensure scores are up to date
        scoreHand();
        return this.possibleValues;
    }
    
    public List<Card> getCards(){
        return this.hand;
    }
    
    private void initialise(){
        hand = new ArrayList();
        addedOrderHandSnapshot = new ArrayList();
        
        rankCounter = new HashMap();
        possibleValues = new ArrayList();
        
        for (Card.Rank r : Card.Rank.values()){
            rankCounter.put(r ,0);
        }
        
        
    }
    
    public Hand(){
        initialise();
    }
    
    public Hand(List<Card> cards){
        initialise();
        
        for (Card card : cards){
            System.out.println(card);
            addCardToHand(new Card(card.getRank(), card.getSuit()));
        }
    }
    
    public Hand(Hand other){
        initialise();
        
        for (Card card : other.getCards()){
            addCardToHand(new Card(card.getRank(), card.getSuit()));
        }
        
    }
    
    
    public void addCardToHand(Card card){
        if (hand == null) hand = new ArrayList();
        
        int index = hand.size();
        hand.add(index, card);
        addedOrderHandSnapshot.add(card);
        
        rankCounter.put(card.getRank(), rankCounter.get(card.getRank())+1);
        scoreHand();        
    }
    
    public void addCardsToHand(Collection<Card> cards){
        for (Card card : cards){
            addCardToHand(card);
        }
    }
    
    public void addCardsToHand(Hand hand){
        for (Card card : hand.getCards()){
            addCardToHand(card);
        }
    }
    
    public boolean removeCardFromHand(Card card){
        if (hand != null && hand.size() >= 1){
            
            int index = hand.indexOf(card);
            
            boolean removed = hand.remove(card);
            
            
            if (removed) {
                addedOrderHandSnapshot.remove(index);
                scoreHand();
                rankCounter.put(card.getRank(), rankCounter.get(card.getRank())-1);
                return true;
            }
        }
        return false;
    }
    
    public boolean remove(Hand givenHand, boolean removeAllOrDoNothing){
        
        if (removeAllOrDoNothing){
            for (Card card : givenHand.getCards()){
                if (!hand.contains(card)) return false;
            }
        }
        
        boolean removedAll = true;
        for (Card card : givenHand.getCards()){
            removedAll = removedAll && removeCardFromHand(card);
        }
        return removedAll;
    }
    
    public Card removeCardAt(int position){
        if (hand.size() > position){
            Card removedCard = hand.get(position);
            if (removeCardFromHand(removedCard)){
                return removedCard;
            }
        }
        return null;
    }
    
    private void scoreHand(){
        
        possibleValues.clear();
        
        List<Integer> scores = new ArrayList();
        scores.add(0);
        
        for (Card card : hand){
            
            int value1 = card.getRank().getValue();
            int value2 = card.getRank().getAltValue();
            
            if (value2 == -1){
                for (int i = 0; i < scores.size(); i++){
                   scores.set(i, scores.get(i) + value1);
                }
            }else{
                Integer[] scoreCopy = scores.toArray(new Integer[]{});
                for (int i = 0; i < scoreCopy.length; i++){
                   scores.set(i, scoreCopy[i] + value1);
                   scores.add(scoreCopy[i] + value2);
                }
            }
        }
        
        for (int score : scores){
            if (!possibleValues.contains(score)) possibleValues.add(score);
        }
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Hand\n");
        sb.append("\tCards:\n");
        for (Card card : hand){
            sb.append("\t\t");
            sb.append(card.toString());
            sb.append("\n");
        }
        sb.append("\tValue(s):\n");
        for (int val : possibleValues){
            sb.append("\t\t");
            sb.append(val);
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public Iterator<Card> iterator() {
        return new AddedOrderIterator(addedOrderHandSnapshot);
    }
    
    private class AddedOrderIterator implements Iterator<Card>{

        int current;
        List<Card> originalOrder;
        
        public AddedOrderIterator(List<Card> addedOrderHand){
            this.originalOrder = addedOrderHand;
            this.current = 0;
        }

        @Override
        public boolean hasNext() {
            return (current < this.originalOrder.size());
        }

        @Override
        public Card next() {
            return this.originalOrder.get(current++);
        }
        
    }
    
    //sorting
    
    public void sortDescending(){
        Collections.sort(hand);
        
    }
    
    public void sortAscending(){
        Collections.sort(hand, new Card.CompareAscending());
        
    }
    
    public int countSuit(Suit suit){
        int output = 0;
        
        for (Card card : hand){
            if (card.getSuit().equals(suit)) output++;
        }
        
        return output;
    }
    
    public int countRank(Rank rank){
        int output = 0;
        
        for (Card card : hand){
            if (card.getRank().equals(rank)) output++;
        }
        
        return output;
    }
    
    public boolean isOver(int givenVal){
        int over = 0;
        for(int val : possibleValues){
            if (val > givenVal) over++;
        }
        return (over==possibleValues.size());
    }
    
    public Hand reverseHand(){
        
        //hack, abuses behaviour from constructor in an obscure way
        //in order to maintain the "added order" across the new, reversed hand
        Hand newHand = new Hand(addedOrderHandSnapshot);
        newHand.hand = hand;
        newHand.reverseCardsInHand();
        
        return newHand;
    }
    
    public void reverseCardsInHand(){
        
        List<Card> reversedCards = new ArrayList();
        for (Card card : hand){
            //adding to index 0 (and shifting existing elements) reverses the order
            reversedCards.add(0, card);
        }
        hand = reversedCards;
        
    }
    
}

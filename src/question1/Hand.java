package question1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import question1.Card.Rank;
import question1.Card.Suit;
import static question1.CardGameUtils.*;
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
            //populate the counter with every possible card having a count of 0
            rankCounter.put(r ,0);
        }
        
        
    }
    
    public Hand(){
        initialise();
    }
    
    public Hand(List<Card> cards){
        initialise();
        
        for (Card card : cards){
            
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
    
    
    
    public static void main(String[] args){
       
        Hand test = new Hand();
        Deck deck = new Deck();

        for (int i = 0; i < 5; i++){
            test.addCardToHand(deck.deal());
        }
        
        String exp0 = "Hand\n" +
                        "	Cards:\n" +
                        "		two of diamonds\n" +
                        "		three of diamonds\n" +
                        "		four of diamonds\n" +
                        "		five of diamonds\n" +
                        "		six of diamonds\n" +
                        "	Value(s):\n" +
                        "		20\n";

        String exp1 = "Hand\n" +
                        "	Cards:\n" +
                        "		queen of clubs\n" +
                        "		seven of clubs\n" +
                        "		ace of diamonds\n" +
                        "		jack of hearts\n" +
                        "		ace of hearts\n" +
                        "	Value(s):\n" +
                        "		49\n" +
                        "		39\n" +
                        "		29\n";

        assertEquals(exp0.replace("\n", " ").replace("\t", " "), test.toString().replace("\n", " ").replace("\t", " "), "Testing toString() when Hand is populated from a new deck:\n\t");
        
        test = new Hand();
        test.addCardToHand(new Card(Rank.QUEEN, Suit.CLUBS));
        test.addCardToHand(new Card(Rank.SEVEN, Suit.CLUBS));
        test.addCardToHand(new Card(Rank.ACE, Suit.DIAMONDS));
        test.addCardToHand(new Card(Rank.JACK, Suit.HEARTS));
        test.addCardToHand(new Card(Rank.ACE, Suit.HEARTS));
        
        assertEquals(exp1.replace("\n", " ").replace("\t", " "), test.toString().replace("\n", " ").replace("\t", " "), "Testing toString() when Hand is populated given specific cards:\n\t");
        
        
        test = new Hand();
        deck = new Deck();
        
        System.out.println("\nAdding 5 diamonds to hand.");
        for (int i = 0; i < 5; i++){
            test.addCardToHand(deck.deal());
        }
        
        assertEquals(0, test.countSuit(Suit.CLUBS), "Testing countSuit() determines no clubs:\n\t");
        assertEquals(0, test.countSuit(Suit.HEARTS), "Testing countSuit() determines no hearts:\n\t");
        assertEquals(0, test.countSuit(Suit.SPADES), "Testing countSuit() determines no spades:\n\t");
        assertEquals(5, test.countSuit(Suit.DIAMONDS), "Testing countSuit() determines 5 diamonds:\n\t");
        
        
        System.out.println("\nAdding 6 more diamonds to hand.");
        
        for (int i = 0; i < 6; i++){
            test.addCardToHand(deck.deal());
        }
        
        assertEquals(0, test.countSuit(Suit.CLUBS), "Testing countSuit() determines no clubs:\n\t");
        assertEquals(0, test.countSuit(Suit.HEARTS), "Testing countSuit() determines no hearts:\n\t");
        assertEquals(0, test.countSuit(Suit.SPADES), "Testing countSuit() determines no spades:\n\t");
        assertEquals(11, test.countSuit(Suit.DIAMONDS), "Testing countSuit() determines 11 diamonds:\n\t");
        
        
        System.out.println("\nAdding 2 diamonds and 2 hearts to hand.");
        
        for (int i = 0; i < 4; i++){
            test.addCardToHand(deck.deal());
        }
        
        assertEquals(0, test.countSuit(Suit.CLUBS), "Testing countSuit() determines no clubs:\n\t");
        assertEquals(2, test.countSuit(Suit.HEARTS), "Testing countSuit() determines 2 hearts:\n\t");
        assertEquals(0, test.countSuit(Suit.SPADES), "Testing countSuit() determines no spades:\n\t");
        assertEquals(13, test.countSuit(Suit.DIAMONDS), "Testing countSuit() determines 13 diamonds:\n\t");
        
        
        System.out.println("\nNew hand: adding first 5 cards from ordered deck.");
        
        test = new Hand();
        deck = new Deck();
        
        for (int i = 0; i < 5; i++){
            test.addCardToHand(deck.deal());
        }
        
        assertEquals(0, test.countRank(Rank.ACE), "Testing countRank() determines no aces:\n\t");
        assertEquals(1, test.countRank(Rank.TWO), "Testing countRank() determines 1 two:\n\t");
        assertEquals(0, test.countRank(Rank.TEN), "Testing countRank() determines no tens:\n\t");
        
        
        System.out.println("\nAdding next 5 cards from ordered deck.");
        
        for (int i = 0; i < 5; i++){
            test.addCardToHand(deck.deal());
        }
        
        assertEquals(0, test.countRank(Rank.ACE), "Testing countRank() determines no aces:\n\t");
        assertEquals(1, test.countRank(Rank.TWO), "Testing countRank() determines 1 two:\n\t");
        assertEquals(1, test.countRank(Rank.TEN), "Testing countRank() determines 1 ten:\n\t");
        
        
        System.out.println("\nAdding remaining 42 cards.");
        for (int i = 0; i < 42; i++){
            test.addCardToHand(deck.deal());
        }
        
        assertEquals(4, test.countRank(Rank.ACE), "Testing countRank() determines 4 aces:\n\t");
        assertEquals(4, test.countRank(Rank.TWO), "Testing countRank() determines 4 twos:\n\t");
        
        
        test = new Hand();
        deck = new Deck();
        
        System.out.println("\nNew hand: adding QUEEN_CLUBS, SEVEN_CLUBS, ACE_DIAMONDS, JACK_SPADES, ACE_HEARTS.");
        
        test = new Hand();
        Card c1 = new Card(Rank.QUEEN, Suit.CLUBS);
        Card c2 = new Card(Rank.SEVEN, Suit.CLUBS);
        Card c3 = new Card(Rank.ACE, Suit.DIAMONDS);
        Card c4 = new Card(Rank.JACK, Suit.HEARTS);
        Card c5 = new Card(Rank.ACE, Suit.HEARTS);
        
        test.addCardToHand(c1);
        test.addCardToHand(c2);
        test.addCardToHand(c3);
        test.addCardToHand(c4);
        test.addCardToHand(c5);
        
        
        System.out.println("Sorting cards descending.\n\t");
        
        test.sortDescending();
        
        System.out.println("Testing iteratable behaviour still iterates in 'added order', even after sorting:\n\t");
        //Test helper iterates over hand in order added
        assertCardsAppearInOrder(test, c1, c2, c3, c4, c5);
        
        
        System.out.println("Testing cards are actually sorted into descending order:\n\t");
        //Test helper iterates over list of cards in list natural order [i.e. according to their indexes, which will be in sorted order after sortDescending()]
        assertCardsAppearInOrder(test.getCards(), c3, c5, c1, c4, c2);
        
		
        System.out.println("Testing sortAscending() sorts by rank, then by suit as expected:\n");
        test.sortAscending();
        //Test helper iterates over list of cards in list natural order [i.e. according to their indexes, which will be in sorted order after sortAscending()]
        assertCardsAppearInOrder(test.getCards(), c2, c1, c4, c3, c5);
        
        System.out.println("Showing reverseHand():\n");
        System.out.println("Hand:\n\t"+test.toString().replace("\n", " ").replace("\t", " "));
        test = test.reverseHand();
        System.out.println("Reversed hand:\n\t"+test.toString().replace("\n", " ").replace("\t", " "));
        
        
        System.out.println("Testing iteratable behaviour still iterates in 'added order', even after reverse:\n\t");
        //added order will be maintained
        assertCardsAppearInOrder(test, c1, c2, c3, c4, c5);
        
        System.out.println("Testing cards are actually sorted into reverse order:\n\t");
        //but reverse the actual order
        assertCardsAppearInOrder(test.getCards(), c5, c3, c4, c1, c2);
        
	
        test = new Hand();
        deck = new Deck();
        
        
        System.out.println("\nNew hand: adding first 5 cards, 2+3+4+5+6 = 20.");
        
        
        for (int i = 0; i < 5; i++){
            test.addCardToHand(deck.deal());
        }
        
        //2+3+4+5+6
        assertFalse(test.isOver(22), "Testing isOver(), given 22, determines false:\n\t");
        assertFalse(test.isOver(21), "Testing isOver(), given 21, determines false:\n\t");
        assertFalse(test.isOver(20), "Testing isOver(), given 20, determines false:\n\t");
        assertTrue(test.isOver(19), "Testing isOver(), given 19, determines true:\n\t");
		
}
    
    //test helper function
    private static void assertCardsAppearInOrder(Hand hand, Card exp1, Card exp2, Card exp3, Card exp4, Card exp5){
        for (Card card : hand){
            //test appears in added order
            int index = hand.addedOrderHandSnapshot.indexOf(card);
            switch(index){
                case(0):
                    assertEquals(exp1, card);
                    break;
                case(1):
                    assertEquals(exp2, card);
                    break;
                case(2):
                    assertEquals(exp3, card);
                    break;
                case(3):
                    assertEquals(exp4, card);
                    break;
                case(4):
                    assertEquals(exp5, card);
                    break;
            }
        }
    }
    
    private static void assertCardsAppearInOrder(List<Card> hand, Card exp1, Card exp2, Card exp3, Card exp4, Card exp5){
        for (Card card : hand){
            //test appears in added order
            int index = hand.indexOf(card);
            switch(index){
                case(0):
                    assertEquals( exp1, card);
                    break;
                case(1):
                    assertEquals(exp2, card);
                    break;
                case(2):
                    assertEquals(exp3, card);
                    break;
                case(3):
                    assertEquals(exp4, card);
                    break;
                case(4):
                    assertEquals(exp5, card);
                    break;
            }
        }
    }
    
}

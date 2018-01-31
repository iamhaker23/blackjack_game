package question2;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.Collections;
import question2.Card.Rank;
import question2.Card.Suit;
import static question2.CardGameUtils.*;
import static question2.FileUtils.readDeckFromFile;
import static question2.FileUtils.writeDeckToFile;
/**
 *
 * @author Hakeem
 */
public class Deck implements Iterable<Card>, Serializable{
    
    static final long serialVersionUID = 112L;
    
    private List<Card> deck;
    private transient final int DEFAULT_DECK_SIZE = 52;
    private transient final int SHUFFLE_DEPTH = 10;
    private transient boolean saveAll;
    
    //allows choosing of save behaviour by setting state
    
    public void setSaveAllFlag(boolean saveAll){
        this.saveAll = saveAll;
    }
    
    public Deck(){
        deck = new ArrayList(DEFAULT_DECK_SIZE);
        saveAll = false;
        newDeck();
        
    }
    
    public int size(){
        return this.deck.size();
    }
    
    public void shuffle(){
        
        List<Card> shuffled = new ArrayList();
        
        for (int i = 0; i < SHUFFLE_DEPTH; i++){
            for (Card c : this.deck){
                boolean swap = (Math.random() * Math.random() > 0.5f);
                
                if (swap){
                    //to the beginning
                    shuffled.add(0, c);
                }else{
                    //random place
                    shuffled.add(c);
                }
            }
            this.deck.clear();
            this.deck.addAll(shuffled);
            shuffled.clear();
        }
        
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        int last = this.deck.size() - 1;
        
        for (Card c : this.deck){
            sb.append(c.toString());
            if (this.deck.indexOf(c) != last) sb.append(", ");
        }
        return sb.toString();
    }
    
    public Card deal(){
        return (deck.size() > 0) ? deck.remove(0) : null;
    }
    
    public void newDeck(){
        deck.clear();
        for (Suit s : Suit.values()){
            for (Rank r : Rank.values()){
                deck.add(new Card(r, s));
            }
        }
    }

    @Override
    public Iterator<Card> iterator() {
        return new DealOrderIterator();
    }
    
    public Iterator<Card> secondCardIterator() {
        return new SecondCardIterator();
    }
    
    public class SecondCardIterator implements Iterator<Card>{

        private int current = 0;
        
        @Override
        public boolean hasNext() {
            return (deck.size() > (this.current) && deck.get(current) != null);
        }

        @Override
        public Card next() {
            Card output = deck.get(current);
            current += 2;
            return output;
        }
    }
    
    public class DealOrderIterator implements Iterator<Card>{

        private int current = 0;
        
        @Override
        public boolean hasNext() {
            return (deck.size() > (current) && deck.get(current) != null);
        }

        @Override
        public Card next() {
            return deck.get(current++);
        }
    }
    
     //Serializable code
    
    private void writeObject(ObjectOutputStream out) throws IOException{
        
        if (this.saveAll){
            System.out.println("Saving [Actual game, so writing every card in deck.]\n");
            
            out.putFields().put("deck", this.deck);
        }else{
            System.out.println("Saving [Writing every other card in deck.]\n");
            
            List<Card> onlySecondCards = new ArrayList();

            Iterator<Card> iter = secondCardIterator();
            while (iter.hasNext()){
                Card current = iter.next();
                System.out.println("writing "+current.toString());
                onlySecondCards.add(current);
            }

            out.putFields().put("deck", onlySecondCards);

        }
        
        out.writeFields();
        out.flush();
    }
    
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
        in.defaultReadObject();
        
    }
    
    public void setDeck(List<Card> deck){
        this.deck = deck;
    }
    
    //Test helpers
    
    public List<Card> copyOfDeck(){
        List<Card> copy = new ArrayList();
        for (Card current : deck){
            copy.add(current);
        }
        return copy;
    }
    
    public int indexOfCard(Card card){
        return deck.indexOf(card);
    }
    
    public void sortAscending(){
        //sorts by rank ascending, then by suit ascending
        Collections.sort(deck, new Card.CompareAscending());
        Collections.sort(deck, new Card.CompareSuit());
        
    }
    
    public void sortDescending(){
        //use natural ordering (i.e. compareTo) to sort into descending order
        
        Collections.sort(deck);
        
    }
    
    public static void main(String[] args){
        Deck test;
    
        test = new Deck();
    
        assertEquals(52, test.size(), "Testing Deck() and Deck.deal():\nInitial deck size is 52:\n\t");
        test.deal();
        assertEquals(51, test.size(), "Testing deck size is 51 after deal():\n\t");
        test.deal();
        assertEquals(50, test.size(), "Testing deck size is 50 after deal():\n\t");
        test.deal();
        assertEquals(49, test.size(), "Testing deck size is 49 after deal():\n\t");
        
    
    
        test = new Deck();
    
        
        List<Card> beforeShuffleDeck = test.copyOfDeck();
        String beforeShuffle = test.toString();
        test.shuffle();
        String afterShuffle = test.toString();
        
        
        assertEquals(afterShuffle.length(), beforeShuffle.length(), "Testing shuffle(), after shuffle toString() contains same number of characters:\n\t");
        assertNotEquals(afterShuffle, beforeShuffle, "Testing shuffle(), after shuffle toString() values should be different.\n\t");
    
        int cardsInDifferentPlace = 0;
        System.out.println("\nTesting all cards are present before and after shuffle():\n");
        
        for (Card card : test){
            //Current card must exist in deck, and might be moved
            assertTrue(beforeShuffleDeck.contains(card), "Testing deck contains "+card.toString()+"\n\t");
            cardsInDifferentPlace += (beforeShuffleDeck.get(test.indexOfCard(card)).equals(card)) ? 0 : 1;
        }
        assertNotEquals(0, cardsInDifferentPlace, "Testing that more than 0 cards have moved place after shuffle\n\t");
    
	
        test = new Deck();
        
        File testFile = new File("test-deck.ser");
        
        System.out.println("Start READ/WRITE\n*************\n");
        writeDeckToFile(testFile, test);
        
        Deck test2 = readDeckFromFile(testFile);
        
        System.out.println("*************\nEnd READ/WRITE\n");
        
        //Assignment brief specifies writing and reading only "every second card" so size is halved.
        assertEquals(test.size()/2, test2.size(), "Testing write only stored every other card:\n\t");
        
        Iterator<Card> everyOther = test.secondCardIterator();
        while (everyOther.hasNext()){
            assertEquals(everyOther.next().toString(), test2.deal().toString(), "Testing reconstructed deck contains 'every other' card:\n\t");
        }
        
        test = new Deck();
        
        
        List<Card> testDeck = new ArrayList();
        testDeck.add(new Card(Card.Rank.SIX, Card.Suit.HEARTS));
        testDeck.add(new Card(Card.Rank.TEN, Card.Suit.DIAMONDS));
        testDeck.add(new Card(Card.Rank.TEN, Card.Suit.SPADES));
        testDeck.add(new Card(Card.Rank.TWO, Card.Suit.CLUBS));
        
        test.setDeck(testDeck);
        
        test.sortAscending();
        
        System.out.println(test.toString());
        assertEquals("two of clubs, six of hearts, ten of diamonds, ten of spades", test.toString(), "Testing Deck.sortAscending():\n\t");

        test.sortDescending();
        
        System.out.println(test.toString());
        assertEquals("ten of diamonds, ten of spades, six of hearts, two of clubs", test.toString(), "Testing Deck.sortDescending():\n\t");
        

        test.newDeck();
        
        //TODO: bugfix - sort produces different results depending on the initial order of the deck
        
        System.out.println("\n****************\nNO MORE TESTS TO PERFORM, OUTPUT DECK SORTING SCENARIOS:");
        
        test.sortAscending();
        System.out.println("Asc[new deck]");
        System.out.println(test.toString());

        test.sortDescending();
        System.out.println("Desc[new deck]");
        System.out.println(test.toString());
        
        
        test.shuffle();
        
        test.sortAscending();
        System.out.println("Asc[after shuffle()]");
        System.out.println(test.toString());

        test.sortDescending();
        System.out.println("Desc[after shuffle()]");
        System.out.println(test.toString());
    }
    
}

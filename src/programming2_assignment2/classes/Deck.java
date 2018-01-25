package programming2_assignment2.classes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.Collections;
import programming2_assignment2.classes.Card.Rank;
import programming2_assignment2.classes.Card.Suit;

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
            System.out.println("Writing every card in deck.\n*************\n");
            
            out.putFields().put("deck", this.deck);
        }else{
            System.out.println("Writing every other card in deck.\n************\n");
            
            List<Card> onlySecondCards = new ArrayList();

            Iterator<Card> iter = secondCardIterator();
            while (iter.hasNext()){
                Card current = iter.next();
                System.out.println("writing "+current.toString()+"\n");
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
    
}

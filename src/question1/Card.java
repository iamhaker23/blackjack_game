package question1;

import java.io.IOException;
import java.io.Serializable;
import java.util.Comparator;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import static question1.CardGameUtils.*;

/**
 *
 * @author Hakeem
 */
public class Card implements Serializable, Comparable<Card>{
    
    private static final int BLACKJACK = 21;
    
    static final long serialVersionUID = 111L;
    
    private Rank rank;
    private Suit suit;
    
    //constructor and accessors
    
    public Card(Rank rank, Suit suit){
        this.rank = rank;
        this.suit = suit;
        
    }
    
    public Suit getSuit(){
        return this.suit;
    }
    
    public Rank getRank(){
        return this.rank;
    }
    
    //added for testing reverse hand in Hand.java, which creates copies of cards in a hand 
    //in which case the default "compare by ref" is not sufficient to determine equality
    //note: it's the only use case of deep-equals functionality
    @Override
    public boolean equals(Object c){
        if (c == null) return false;
        try{
            Card tmp = (Card)c;
            return ((tmp.getRank().equals(this.rank)) && (tmp.getSuit().equals(this.suit)));
        }catch(Exception e){
            return false;
        }
    }
    
    @Override
    public String toString(){
        return (this.rank.getName() + " of " + this.suit.getName());
    }
    
    public static int sum(Card a, Card b){
        return a.getRank().value + b.getRank().value;
    }
    
    public static boolean isBlackjack(Card a, Card b){
        return (sum(a, b) == BLACKJACK);
    }
    
    //Comparable code
    
    @Override
    public int compareTo(Card other){
        //according to example:
        //largest rank first, but then lowest suit first
        
        int output = 0;
        
        //descending rank
        //output -= (this.rank.getValue() - other.rank.getValue());
        
        //ascending suit
        //output += ((this.suit.getValue() -  other.suit.getValue()));
        
        output += -1 * (new CompareAscending()).compare(this, other);
        //output += -1* (new CompareSuit()).compare(this, other);
        
        return output;
    }
    
    //Comparators
    
    public static class CompareAscending implements Comparator<Card>{

        //if a > b, return 1
        //if a == b, return 0
        //if a < b, return -1
        @Override
        public int compare(Card a, Card b) {
            int output = 0;
            int diff = b.rank.getValue() - a.rank.getValue();
            if (diff > 0){
                //b is greater than a, so it goes earlier
                output = -1;
            }else if (diff < 0){
                output = 1;
            }
            return output;
        }
        
    }
    
    public static class CompareSuit implements Comparator<Card>{

        //if a > b, return 1
        //if a == b, return 0
        //if a < b, return -1
        @Override
        public int compare(Card a, Card b) {
            int output = 0;
        
            output += (new CompareAscending()).compare(a, b);
            
            int diff = b.suit.getValue() - a.suit.getValue();
            if (diff > 0){
                //b is larger, so it goes earlier
                output += -1;
            }else if (diff < 0){
                output += 1;
            }
            return output;
        }
        
    }
    
    //Serializable code
    
    private void writeObject(ObjectOutputStream out) throws IOException{
        out.defaultWriteObject();
        out.flush();
    }
    
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
        in.defaultReadObject();
    }
    
    //Enumerations
    
    public enum Rank{
        
        TWO(2, "two"){
                @Override
                public Rank getPrevious(){

                        return ACE;
                }
        }
        , THREE(3, "three"){
                @Override
                public Rank getPrevious(){

                        return TWO;
                }
        }
        , FOUR(4, "four"){
                @Override
                public Rank getPrevious(){

                        return THREE;
                }
        }
        , FIVE(5, "five"){
                @Override
                public Rank getPrevious(){

                        return FOUR;
                }
        }
        , SIX(6, "six"){
                @Override
                public Rank getPrevious(){

                        return FIVE;
                }
        }
        , SEVEN(7, "seven"){
                @Override
                public Rank getPrevious(){

                        return SIX;
                }
        }
        , EIGHT(8, "eight"){
                @Override
                public Rank getPrevious(){

                        return SEVEN;
                }
        }
        , NINE(9, "nine"){
                @Override
                public Rank getPrevious(){

                        return EIGHT;
                }
        }
        , TEN(10, "ten"){
                @Override
                public Rank getPrevious(){

                        return NINE;
                }
        }
        , JACK(10, "jack"){
                @Override
                public Rank getPrevious(){

                        return TEN;
                }
        }
        , KING(10, "king"){
                @Override
                public Rank getPrevious(){

                        return JACK;
                }
        }
        , QUEEN(10, "queen"){
                @Override
                public Rank getPrevious(){

                        return KING;
                }
        }
        , ACE(11, "ace", 1){
                @Override
                public Rank getPrevious(){

                        return QUEEN;
                }
        };
        
        public abstract Rank getPrevious();
        
        private final int value;
        private final String name;
        private final int altValue;
        
        Rank(int value, String name){
            this.value = value;
            this.name = name;
            this.altValue = -1;
        }
        
        Rank(int value, String name, int altValue){
            this.value = value;
            this.name = name;
            this.altValue = altValue;
        }
        
        public String getName(){
            return this.name;
        }
        
        int getAltValue(){
            return this.altValue;
        }
        
        int getValue(){
            return this.value;
        }
        
    }
    
    public enum Suit{
        DIAMONDS(2, "diamonds"), HEARTS(3, "hearts"), CLUBS(1, "clubs"), SPADES(4, "spades");
        
        private final String name;
        private final int value;
        
        Suit(int value, String name){
            this.name = name;
            this.value = value;
        }
        
        public String getName(){
            return this.name;
        }
        
        public int getValue(){
            return this.value;
        }
    }
    
    public static void main(String[] args){
        
        assertEquals(Rank.QUEEN, Rank.ACE.getPrevious(), "Testing Rank.getPrevious():\nBefore ACE is QUEEN:\n\t");
        assertEquals(Rank.ACE, Rank.TWO.getPrevious(), "Before TWO is ACE:\n\t");
        assertEquals(Rank.TWO, Rank.THREE.getPrevious(), "Before THREE is TWO:\n\t");
        assertEquals(Rank.THREE, Rank.FOUR.getPrevious(), "Before FOUR is THREE:\n\t");
        assertEquals(Rank.FOUR, Rank.FIVE.getPrevious(), "Before FIVE is FOUR:\n\t");
        assertEquals(Rank.FIVE, Rank.SIX.getPrevious(), "Before SIX is FIVE:\n\t");
        assertEquals(Rank.SIX, Rank.SEVEN.getPrevious(), "Before SEVEN is SIX:\n\t");
        assertEquals(Rank.SEVEN, Rank.EIGHT.getPrevious(), "Before EIGHT is SEVEN:\n\t");
        assertEquals(Rank.EIGHT, Rank.NINE.getPrevious(), "Before NINE is EIGHT:\n\t");
        assertEquals(Rank.NINE, Rank.TEN.getPrevious(), "Before TEN is NINE:\n\t");
        assertEquals(Rank.TEN, Rank.JACK.getPrevious(), "Before JACK is TEN:\n\t");
        assertEquals(Rank.JACK, Rank.KING.getPrevious(), "Before KING is JACK:\n\t");
        assertEquals(Rank.KING, Rank.QUEEN.getPrevious(), "Before QUEEN is KING:\n\t");

        assertEquals(1, Rank.ACE.getAltValue(), "\nTesting Rank.getValue() and Rank.getAltValue():\nACE alt value is 1:\n\t");
        assertEquals(11, Rank.ACE.getValue(), "ACE value is 1\n\t");
        assertEquals(2, Rank.TWO.getValue(), "TWO value is 2\n\t");
        assertEquals(3, Rank.THREE.getValue(), "THREE value is 3\n\t");
        assertEquals(4, Rank.FOUR.getValue(), "FOUR value is 4\n\t");
        assertEquals(5, Rank.FIVE.getValue(), "FIVE value is 5\n\t");
        assertEquals(6, Rank.SIX.getValue(), "SIX value is 6\n\t");
        assertEquals(7, Rank.SEVEN.getValue(), "SEVEN value is 7\n\t");
        assertEquals(8, Rank.EIGHT.getValue(), "EIGHT value is 8\n\t");
        assertEquals(9, Rank.NINE.getValue(), "NINE value is 9\n\t");
        assertEquals(10, Rank.TEN.getValue(), "TEN value is 10\n\t");
        assertEquals(10, Rank.JACK.getValue(), "JACK value is 10\n\t");
        assertEquals(10, Rank.KING.getValue(), "KING value is 10\n\t");
        assertEquals(10, Rank.QUEEN.getValue(), "QUEEN value is 10\n\t");
		
        assertEquals(-1, Rank.TWO.getAltValue(), "TWO alt value is nothing so defaults to -1\n\t");
        assertEquals(-1, Rank.THREE.getAltValue(), "THREE alt value is nothing so defaults to -1\n\t");
        assertEquals(-1, Rank.FOUR.getAltValue(), "FOUR alt value is nothing so defaults to -1\n\t");
        assertEquals(-1, Rank.FIVE.getAltValue(), "FIVE alt value is nothing so defaults to -1\n\t");
        assertEquals(-1, Rank.SIX.getAltValue(), "SIX alt value is nothing so defaults to -1\n\t");
        assertEquals(-1, Rank.SEVEN.getAltValue(), "SEVEN alt value is nothing so defaults to -1\n\t");
        assertEquals(-1, Rank.EIGHT.getAltValue(), "EIGHT alt value is nothing so defaults to -1\n\t");
        assertEquals(-1, Rank.NINE.getAltValue(), "NINE alt value is nothing so defaults to -1\n\t");
        assertEquals(-1, Rank.TEN.getAltValue(), "TEN alt value is nothing so defaults to -1\n\t");
        assertEquals(-1, Rank.JACK.getAltValue(), "JACK alt value is nothing so defaults to -1\n\t");
        assertEquals(-1, Rank.KING.getAltValue(), "KING alt value is nothing so defaults to -1\n\t");
        assertEquals(-1, Rank.QUEEN.getAltValue(), "QUEEN alt value is nothing so defaults to -1\n\t");

        assertEquals(18, Card.sum(new Card(Rank.EIGHT, Suit.CLUBS), new Card(Rank.QUEEN, Suit.DIAMONDS)), "Testing Card.sum():\nEIGHT and QUEEN:\n\t");

        assertTrue(Card.isBlackjack(new Card(Rank.ACE, Suit.CLUBS), new Card(Rank.QUEEN, Suit.CLUBS)), "Testing Card.isBlackjack():\nTesting ACE and QUEEN:\n\t");
        assertTrue(Card.isBlackjack(new Card(Rank.ACE, Suit.CLUBS), new Card(Rank.TEN, Suit.CLUBS)), "Testing ACE and TEN:\n\t");
        assertTrue(Card.isBlackjack(new Card(Rank.ACE, Suit.CLUBS), new Card(Rank.KING, Suit.CLUBS)), "Testing ACE and KING:\n\t");
        assertTrue(Card.isBlackjack(new Card(Rank.ACE, Suit.CLUBS), new Card(Rank.JACK, Suit.CLUBS)), "Testing ACE and JACK:\n\t");
        assertFalse(Card.isBlackjack(new Card(Rank.ACE, Suit.CLUBS), new Card(Rank.NINE, Suit.CLUBS)), "Testing ACE and NINE:\n\t");
    }
    
}

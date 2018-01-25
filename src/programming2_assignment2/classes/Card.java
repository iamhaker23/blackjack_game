/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package programming2_assignment2.classes;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Comparator;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

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
    
    
    
}

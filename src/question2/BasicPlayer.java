package question2;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import static question2.BlackjackDealer.determineBlackjack;
import question2.Card.Rank;
import static question1.CardGameUtils.randomName;
import interfaces.Player;

/**
 *
 * @author Hakeem
 */
public class BasicPlayer implements Player, Serializable{
    
    private int balance = 200;
    private final int baseBet = 10;
    private String name;
    private Hand hand;
    
    
    
    public BasicPlayer(){
        this.name = randomName();
        this.hand = new Hand();
    }
    
    @Override
    public Hand newHand() {
        Hand oldHand = hand;
        hand = new Hand();
        return oldHand;
    }

    @Override
    public int makeBet() {
        
        return baseBet;
    }

    @Override
    public int getBet(int minBet, int maxBet) {
        //returns the basebet (or all in) or the maxbet if the basebet is too big or the minbet if the basebet is too small
        return Math.min(balance, Math.max(Math.min(maxBet, makeBet()), minBet));
    }

    @Override
    public int getBalance() {
        return this.balance;
    }

    @Override
    public boolean hit() {
        return (getHandTotal() < 17);
    }

    @Override
    public void takeCard(Card c) {
        this.hand.addCardToHand(c);
    }

    @Override
    public boolean settleBet(int p) {
        this.balance += p;
        return this.balance > 0;
    }

    @Override
    public int getHandTotal() {
        //ask the dealer for the best possible score
        //violates encapsulation to improve consistency and reduce repetition
        return BlackjackDealer.getBestScore(hand.getPossibleValues());
    }

    @Override
    public boolean blackjack() {
        return determineBlackjack(hand.getCards());
    }

    @Override
    public boolean isBust() {
        List<Integer> values = this.hand.getPossibleValues();
        
        if (values.isEmpty()) return false;
        for (int score : values){
            if (score <= 21) return false;
        }
        return true;
    }

    @Override
    public Hand getHand() {
        return this.hand;
    }

    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public void viewDealerCard(Card c) {
        //basic player don't got strategum
    }

    @Override
    public void viewCards(List<Card> cards) {
        //basic player don't know how to count cards
    }

    @Override
    public void newDeck() {
        //take notice of a new deck by doing nothing
        //basic player don't cheat
        //basic player can't cheat
    }
    
    public final void setName(String name){
        this.name = name;
    }
    
}

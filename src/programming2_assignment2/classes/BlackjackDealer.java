package programming2_assignment2.classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import programming2_assignment2.interfaces.Dealer;
import programming2_assignment2.interfaces.Player;

/**
 *
 * @author Hakeem
 */
public class BlackjackDealer implements Dealer, Serializable{
    
    private List<Player> playersDealingTo;
    
    private int dealerWinnings;
    
    private int dealerScore;
    private transient List<Bet> bets;
    
    private Deck deck;
    private Hand hand;
    private final int TIME_LIMIT = 20000;
    private int initialSize;
    
    private int minBet;
    private int maxBet;
    
    private int deckCount;
    
    private boolean printOut;
    
    private List<Card> cardsPlayedDuringHand;
    
    //test helper
    public Deck getDeck(){
        return this.deck;
    }
    
    public Hand getHand(){
        return this.hand;
    }
    
    public int getDealerScore(){
        return this.dealerScore;
    }
    
    @Override
    public int getDealerWinnings(){
        return this.dealerWinnings;
    }
    
    public List<Player> getPlayersDealingTo(){
        return this.playersDealingTo;
    }
    
    
    
    private class Bet{
        Player player;
        int bet;
        int score;
        
        Bet(Player player, int bet){
            this.player = player;
            this.bet = bet;
            this.score = -1;
        }
        
        void setScore(int score){
            this.score = score;
        }
    }
    
    public BlackjackDealer(){
        this.playersDealingTo = new ArrayList();
        
        dealerWinnings = 0;
        bets = new ArrayList();
        
        deck = new Deck();
        initialSize = deck.size();
        deck.shuffle();
        
        hand = new Hand();
        dealerScore = 0;
        
        //by default allow all bets
        minBet = Integer.MIN_VALUE;
        maxBet = Integer.MAX_VALUE;
        
        cardsPlayedDuringHand = new ArrayList();
        
        deckCount = 0;
        
        printOut = false;
        
    }

    @Override
    public int getDecksUsed(){
        return this.deckCount;
    }
    
    @Override
    public void assignPlayers(List<Player> p) {
        this.playersDealingTo = p;
    }

    @Override
    public void takeBets() {
        bets.clear();
        
        for (Player p : playersDealingTo){
            int bet = p.getBet(minBet, maxBet);
            if (printOut) System.out.println(p.getName()+": BETS{"+ bet+"}");
            bets.add(new Bet(p, bet));
        }
        
    }
    
    @Override
    public void setSaveAll(boolean flag){
        this.deck.setSaveAllFlag(flag);
    }

    @Override
    public boolean beatAllPlayers(){
        for (Player p : this.playersDealingTo){
            if (p.getBalance() > 0) return false;
        }
        return true;
    }
    
    @Override
    public void dealFirstCards() throws Exception{
        if (printOut) System.out.println("\nLet's play!\n\n");
        
        //players get first card in a round
        for (Player p : this.playersDealingTo){
            Card c = dealCard();
            if (printOut) System.out.println(p.getName() + ": +" + c);
            p.takeCard(c);
            
            c = dealCard();
            if (printOut) System.out.println(p.getName() + ": +" + c);
            p.takeCard(c);
        }
        
        //dealer takes one
        Card c = dealCard();
        
        for (Player p : playersDealingTo){
            p.viewDealerCard(c);
        }
        
        if (printOut) System.out.println("Dealer: +" + c + "\n\n");
        takeCard(c);
    }
    
    private Card dealCard() throws Exception{
        Card c = deck.deal();
        if (c == null) throw new Exception("Deck has no cards remaining.");
        cardsPlayedDuringHand.add(c);
        return c;
    }

    @Override
    public int play(Player p) throws Exception{
        long starttime = System.currentTimeMillis();
        
        //make sure player cannot take too long
        long timeTaken = (System.currentTimeMillis() - starttime);
        
        while (!p.isBust() && !p.blackjack() && p.hit()){
            //within time limit, not bust and hitting
            
            if (printOut) System.out.println(p.getName() + ": HIT ME!");
            Card c = dealCard();
            if (printOut) System.out.println(p.getName() + ": +"+ c.toString());
            p.takeCard(c);
        }
        
        int score = scoreHand(p.getHand());
        
        //Novel timeout mechanism, abandoned
        /*if (timeTaken >= TIME_LIMIT){
            if (printOut) System.out.println(p.getName() + " went to the toilet!? \n"+ p.getName()+ ": STICK[" + score + "]\n");
        }else */
        
        if (p.isBust()){
            if (printOut) System.out.println(p.getName() + ": BUST(" + score + ")\n");
        }else if (p.blackjack()){
            if (printOut) System.out.println(p.getName() + ": BLACKJACK!\n");
        }else{
            if (printOut) System.out.println(p.getName() + ": STICK[" + score + "]\n");
        }
        
        return score;
    }

    @Override
    public int playDealer() throws Exception{
        int score = 0;
        while (score < 17 ){
            //hit until greater than 17
            Card c = dealCard();
            if (printOut) System.out.println("Dealer: +" + c);
            hand.addCardToHand(c);
            score = scoreHand(hand);
        }
        
        if (dealerScore <= 21){
            if (printOut) System.out.println("Dealer: STICK["+score+"]\n");
        }else{
            if (printOut) System.out.println("Dealer: BUST("+score+")\n");
        }
        
        return score;
    }

    @Override
    public int scoreHand(Hand h) {
        //the hand might have no cards
        if (h.getCards().isEmpty()) return 0;
        
        return getBestScore(h.getPossibleValues());
    }
    
    public static int getBestScore(List<Integer> h){
         //get the highest value under 21
        int best = -1;
        int min = Integer.MAX_VALUE;
        
        for (Integer val : h){
            if (val > best && val <= 21) best = val;
            if (val < min) min = val;
        }
        
        //if no value is 21 or less, return the smallest soft value
        if (best == -1) return min;
        return best;
    }
    
    public static boolean determineBlackjack(List<Card> cards){
        int blackCount = 0;
        int jackCount = 0;
        Card.Rank[] jacks = new Card.Rank[]{Card.Rank.TEN, Card.Rank.JACK, Card.Rank.KING, Card.Rank.QUEEN};
        for (Card c : cards){
            if (c.getRank().equals(Card.Rank.ACE)) blackCount++;
            if (Arrays.asList(jacks).contains(c.getRank())) jackCount++;
        }
        return (blackCount == 1 && jackCount == 1 && cards.size() == 2);
    }

    @Override
    public void settleBets() {
        boolean dealerBlackjack = determineBlackjack(hand.getCards());
        for (Bet b : bets){
            int multiplier = (b.player.blackjack() && !(dealerBlackjack)) ? 2 : 1;
            if (b.score > 21){
                losesBet(b.bet, b.player);
            }else if (dealerBlackjack){
                //dealer has a blackjack, player loses even if total is 21
                //i.e. even if they have a blackjack
                losesBet(b.bet, b.player);
            }else if (dealerScore > 21){
                //dealer bust and player sticks
                winsBet(b.bet, b.player);
            }else{
                //both stick
                if (b.score > dealerScore){
                    winsBet(b.bet,b.player);
                }else if (b.score <= dealerScore){
                    losesBet(b.bet, b.player);
                }
            }
        }
        if (printOut) System.out.println("Dealer: WINNINGS{" + dealerWinnings + "}\n");
        
    }
    private void winsBet(int amount, Player p){
        p.settleBet(amount);
        if (printOut) System.out.println(p.getName()  + ": WINS BET {"+amount+"}");
        if (printOut) System.out.println(p.getName()  + ": BALANCE{"+p.getBalance()+"}\n");
        dealerWinnings -= amount;
    }
    private void losesBet(int amount, Player p){
        if(!p.settleBet(-1*amount)){
            if (printOut) System.out.println(p.getName()  + ": BROKE, LEAVES.");
            playersDealingTo.remove(p);
        }else{
            if (printOut) System.out.println(p.getName()  + ": LOSES BET {"+amount+"}");
            if (printOut) System.out.println(p.getName()  + ": BALANCE{"+p.getBalance()+"}\n");
        }
        dealerWinnings += amount;
    }
    
    private void takeCard(Card card){
        this.hand.addCardToHand(card);
    }

    private void initRound(){
        this.dealerScore = 0;
        this.bets = new ArrayList();
        this.hand = new Hand();
        for (Player p : playersDealingTo){
            p.newHand();
        }
        this.cardsPlayedDuringHand.clear();
    }
    
    @Override
    public void playRound(boolean printOut) {
        
        this.printOut = printOut;
        
        initRound();
        
        if (deck.size() < initialSize/4){
            deck = new Deck();
            deck.shuffle();
            for (Player p : playersDealingTo){
                p.newDeck();
            }
            initialSize = deck.size();
        }
        
        takeBets();
        try{
            dealFirstCards();

            for (Player p : playersDealingTo){

                //if the player is betting

                int index = -1;
                for (Bet b : bets){
                    if (b.player.equals(p)) index = bets.indexOf(b);
                }
                if (index != -1) bets.get(index).setScore(play(p));
            }

            dealerScore = playDealer();
            
            settleBets();
            
            for (Player p : playersDealingTo){
                p.viewCards(cardsPlayedDuringHand);
            }
            
        }catch(Exception e){
            //Only settle bets if game resolves correctly
            //Must change deck on next round to escape DECK EMPTY condition
            
            //profit per deck
            deckCount++;
            
            if (printOut) System.out.println("DECK EMPTY: PREMATURE ROUND END.");
        }
    }
    
    public void capBets(int minBet, int maxBet){
        this.maxBet = maxBet;
        this.minBet = minBet;
    }
    
}

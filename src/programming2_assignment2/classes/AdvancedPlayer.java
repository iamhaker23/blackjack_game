package programming2_assignment2.classes;

import java.util.List;
import programming2_assignment2.classes.Card.Rank;

/**
 *
 * @author Hakeem
 */
public class AdvancedPlayer extends IntermediatePlayer {

    private Card dealersCard;

    private int cardCounter;

    public AdvancedPlayer() {
        super();
        this.cardCounter = 0;
    }

    @Override
    public int makeBet() {
        if (cardCounter < 0){
            return super.makeBet();
        }else{
            return super.makeBet() * cardCounter;
        }
    }

    @Override
    public void viewCards(List<Card> cards) {
        //update counts
        for (Card c : cards) {
            if (!Rank.ACE.equals(c.getRank()) && c.getRank().getValue() <= 6) {
                //not an ace, less than 6
                cardCounter++;
            } else if (c.getRank().getValue() >= 10 || c.getRank().getAltValue() >= 10) {
                cardCounter--;
            }
        }
    }

    @Override
    public void newDeck() {
        //reset counts
        this.cardCounter = 0;
    }

}

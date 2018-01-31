package question2;

import java.util.List;
import question2.Card.Rank;

/**
 *
 * @author Hakeem
 */
public class IntermediatePlayer extends BasicPlayer{

    private Card dealersCard;
    private List<Card> cardsPlayedSoFar;
    private HitBehaviour hb;
    
    private enum HitBehaviour{
        HITUNTILEXCEED17, HITUNTILEXCEED12
    }
    
    public IntermediatePlayer(){
        super();
        this.dealersCard = null;
    }
    
    @Override
    public void viewDealerCard(Card c) {
        this.dealersCard = c;
    }

    @Override
    public boolean hit(){
        
        Hand h = getHand();
        int score = BlackjackDealer.getBestScore(h.getPossibleValues());
        
        if (hb != null){
            if (HitBehaviour.HITUNTILEXCEED12.equals(hb)){
                if (score <= 12){
                    return true;
                }else{
                    return false;
                }
            }else if (HitBehaviour.HITUNTILEXCEED17.equals(hb)){
                if (score <= 17){
                    return true;
                }else{
                    return false;
                }
            }
        }
        
        if (dealersCard != null){
            
            boolean hasAce = false;
            for (Card c : h.getCards()){
                if (Rank.ACE.equals(c.getRank())){
                    hasAce = true;
                }
            }
            if (hasAce && (h.getPossibleValues().contains(9) || h.getPossibleValues().contains(10))){
                //if there's an ace and a soft value of 9 or 10
                //NOTE: not explicitly checking that the value is not a hard value
                //Because the lowest hard value is 11
                return false;
            }else if (hasAce){
                //always take a card if there is a soft total of 8 or less
                for (int value : h.getPossibleValues()){
                    if (value <= 8) return true;
                }
            }
            
            if (dealersCard.getRank().getValue() >= 7 || dealersCard.getRank().getAltValue() >= 7){
                //7+ (INCLUDING ACES)
                if (score < 17){
                    hb = HitBehaviour.HITUNTILEXCEED17;
                    return true;
                }else{
                    return false;
                }
            }else if (dealersCard.getRank().getValue() <= 6 && dealersCard.getRank().getAltValue() <= 6 ){
                //six or lower, excluding ace
                if (score < 12){
                    hb = HitBehaviour.HITUNTILEXCEED17;
                    return true;
                }else{
                    return false;
                }
            }
        }
        return super.hit();
    }

}

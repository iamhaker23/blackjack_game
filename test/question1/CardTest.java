package question1;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import question1.Card;
import question1.Card.Rank;
import question1.Card.Suit;

/**
 *
 * @author rjx16equ
 */
public class CardTest {
    
    static Card test;

    public CardTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        
        test = new Card(Rank.ACE, Suit.SPADES);
        
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testRankGetPrevious() {
        assertEquals(Rank.QUEEN, Rank.ACE.getPrevious());
        assertEquals(Rank.ACE, Rank.TWO.getPrevious());
        assertEquals(Rank.TWO, Rank.THREE.getPrevious());
        assertEquals(Rank.THREE, Rank.FOUR.getPrevious());
        assertEquals(Rank.FOUR, Rank.FIVE.getPrevious());
        assertEquals(Rank.FIVE, Rank.SIX.getPrevious());
        assertEquals(Rank.SIX, Rank.SEVEN.getPrevious());
        assertEquals(Rank.SEVEN, Rank.EIGHT.getPrevious());
        assertEquals(Rank.EIGHT, Rank.NINE.getPrevious());
        assertEquals(Rank.NINE, Rank.TEN.getPrevious());
        assertEquals(Rank.TEN, Rank.JACK.getPrevious());
        assertEquals(Rank.JACK, Rank.KING.getPrevious());
        assertEquals(Rank.KING, Rank.QUEEN.getPrevious());
    }
    
    @Test
    public void testGetValue(){
        assertEquals(1, Rank.ACE.getAltValue());
        assertEquals(11, Rank.ACE.getValue());
        assertEquals(2, Rank.TWO.getValue());
        assertEquals(3, Rank.THREE.getValue());
        assertEquals(4, Rank.FOUR.getValue());
        assertEquals(5, Rank.FIVE.getValue());
        assertEquals(6, Rank.SIX.getValue());
        assertEquals(7, Rank.SEVEN.getValue());
        assertEquals(8, Rank.EIGHT.getValue());
        assertEquals(9, Rank.NINE.getValue());
        assertEquals(10, Rank.TEN.getValue());
        assertEquals(10, Rank.JACK.getValue());
        assertEquals(10, Rank.KING.getValue());
        assertEquals(10, Rank.QUEEN.getValue());
    }
    @Test
    public void testSum(){
        assertEquals(18, Card.sum(new Card(Rank.EIGHT, Suit.CLUBS), new Card(Rank.QUEEN, Suit.DIAMONDS)));
    }
    
    @Test
    public void testBlackJack(){
        assertTrue(Card.isBlackjack(new Card(Rank.ACE, Suit.CLUBS), new Card(Rank.QUEEN, Suit.CLUBS)));
        assertTrue(Card.isBlackjack(new Card(Rank.ACE, Suit.CLUBS), new Card(Rank.TEN, Suit.CLUBS)));
        assertTrue(Card.isBlackjack(new Card(Rank.ACE, Suit.CLUBS), new Card(Rank.KING, Suit.CLUBS)));
        assertTrue(Card.isBlackjack(new Card(Rank.ACE, Suit.CLUBS), new Card(Rank.JACK, Suit.CLUBS)));
        
        assertFalse(Card.isBlackjack(new Card(Rank.ACE, Suit.CLUBS), new Card(Rank.NINE, Suit.CLUBS)));
        
    }
    
}

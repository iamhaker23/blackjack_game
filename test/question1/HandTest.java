package question1;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import question1.Card.Rank;
import question1.Card.Suit;

/**
 *
 * @author Hakeem
 */
public class HandTest {
    
    Hand test;
    Deck deck;
    
    public HandTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        test = new Hand();
        deck = new Deck();
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testHandToString() {
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

        assertEquals(exp0, test.toString());
        
        test = new Hand();
        test.addCardToHand(new Card(Rank.QUEEN, Suit.CLUBS));
        test.addCardToHand(new Card(Rank.SEVEN, Suit.CLUBS));
        test.addCardToHand(new Card(Rank.ACE, Suit.DIAMONDS));
        test.addCardToHand(new Card(Rank.JACK, Suit.HEARTS));
        test.addCardToHand(new Card(Rank.ACE, Suit.HEARTS));
        
        assertEquals(exp1, test.toString());
        
        
    }
    
    
    @Test
    public void testCountSuit() {
        
        for (int i = 0; i < 5; i++){
            test.addCardToHand(deck.deal());
        }
        
        assertEquals(0, test.countSuit(Suit.CLUBS));
        assertEquals(0, test.countSuit(Suit.HEARTS));
        assertEquals(0, test.countSuit(Suit.SPADES));
        assertEquals(5, test.countSuit(Suit.DIAMONDS));
        
        for (int i = 0; i < 6; i++){
            test.addCardToHand(deck.deal());
        }
        
        assertEquals(0, test.countSuit(Suit.CLUBS));
        assertEquals(0, test.countSuit(Suit.HEARTS));
        assertEquals(0, test.countSuit(Suit.SPADES));
        assertEquals(11, test.countSuit(Suit.DIAMONDS));
        
        
        for (int i = 0; i < 4; i++){
            test.addCardToHand(deck.deal());
        }
        
        assertEquals(0, test.countSuit(Suit.CLUBS));
        assertEquals(2, test.countSuit(Suit.HEARTS));
        assertEquals(0, test.countSuit(Suit.SPADES));
        assertEquals(13, test.countSuit(Suit.DIAMONDS));
        
        
    }
    
    @Test
    public void testCountRank() {
        
        for (int i = 0; i < 5; i++){
            test.addCardToHand(deck.deal());
        }
        
        assertEquals(0, test.countRank(Rank.ACE));
        assertEquals(1, test.countRank(Rank.TWO));
        assertEquals(0, test.countRank(Rank.TEN));
        
        for (int i = 0; i < 5; i++){
            test.addCardToHand(deck.deal());
        }
        
        assertEquals(0, test.countRank(Rank.ACE));
        assertEquals(1, test.countRank(Rank.TWO));
        assertEquals(1, test.countRank(Rank.TEN));
        
        
        for (int i = 0; i < 42; i++){
            test.addCardToHand(deck.deal());
        }
        
        assertEquals(4, test.countRank(Rank.ACE));
        assertEquals(4, test.countRank(Rank.TWO));
        
        
    }
    
    @Test
    public void testHandAddedOrderIteratorAndSortingAndCountSuitAndReverseOrder() {
        
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
        
        //Test helper iterates over hand in order added
        assertCardsAppearInOrder(test, c1, c2, c3, c4, c5);
        
        test.sortDescending();
        //Test helper iterates over list of cards in list natural order [i.e. according to their indexes, which will be in sorted order after sortDescending()]
        assertCardsAppearInOrder(test.getCards(), c3, c5, c1, c4, c2);
        
        test.sortAscending();
        //Test helper iterates over list of cards in list natural order [i.e. according to their indexes, which will be in sorted order after sortAscending()]
        assertCardsAppearInOrder(test.getCards(), c2, c1, c4, c3, c5);
        
        for(Card card : test.getCards()){
            System.out.println(card);
        }
        
        test = test.reverseHand();
        System.out.println(test.toString());
        
        for(Card card : test){
            System.out.println(card);
        }
        
        //added order will be maintained
        assertCardsAppearInOrder(test, c1, c2, c3, c4, c5);
        //but reverse the actual order
        assertCardsAppearInOrder(test.getCards(), c5, c3, c4, c1, c2);
        
    }
    
    //test helper function
    private void assertCardsAppearInOrder(Hand hand, Card exp1, Card exp2, Card exp3, Card exp4, Card exp5){
        for (Card card : hand){
            //test appears in added order
            int index = hand.getCards().indexOf(card);
            switch(index){
                case(0):
                    assertEquals("Expected first card to be exp1", exp1, card);
                    break;
                case(1):
                    assertEquals("Expected second card to be exp2", exp2, card);
                    break;
                case(2):
                    assertEquals("Expected third card to be exp3", exp3, card);
                    break;
                case(3):
                    assertEquals("Expected fourth card to be exp4", exp4, card);
                    break;
                case(4):
                    assertEquals("Expected fifth card to be exp5", exp5, card);
                    break;
            }
        }
    }
    
    private void assertCardsAppearInOrder(List<Card> hand, Card exp1, Card exp2, Card exp3, Card exp4, Card exp5){
        for (Card card : hand){
            //test appears in added order
            int index = hand.indexOf(card);
            switch(index){
                case(0):
                    assertEquals("Expected first card to be exp1", exp1, card);
                    break;
                case(1):
                    assertEquals("Expected second card to be exp2", exp2, card);
                    break;
                case(2):
                    assertEquals("Expected third card to be exp3", exp3, card);
                    break;
                case(3):
                    assertEquals("Expected fourth card to be exp4", exp4, card);
                    break;
                case(4):
                    assertEquals("Expected fifth card to be exp5", exp5, card);
                    break;
            }
        }
    }
    
    
    @Test
    public void testIsOver(){
        //TODO
        
        for (int i = 0; i < 5; i++){
            test.addCardToHand(deck.deal());
        }
        
        //two, three, four, five, six
        assertFalse(test.isOver(22));
        assertFalse(test.isOver(21));
        assertFalse(test.isOver(20));
        assertTrue(test.isOver(19));
        
        
        
    }
    
    
    
    
}

package question1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import question1.Card;
import question1.Deck;
import static question1.FileUtils.readDeckFromFile;
import static question1.FileUtils.writeDeckToFile;

/**
 *
 * @author Hakeem
 */
public class DeckTest {
    
    Deck test;
    
    public DeckTest() {
        
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        test = new Deck();
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testConstructorAndDeal() {
    
        assertEquals("Expected deck of 52\n", 52, test.size());
        test.deal();
        assertEquals("Expected deck of 51\n", 51, test.size());
        test.deal();
        assertEquals("Expected deck of 50\n", 50, test.size());
        test.deal();
        assertEquals("Expected deck of 49\n", 49, test.size());
        
    
    }
    
    @Test
    public void testShuffle() {
    
        
        List<Card> beforeShuffleDeck = test.copyOfDeck();
        String beforeShuffle = test.toString();
        test.shuffle();
        String afterShuffle = test.toString();
        
        
        assertEquals("Expected string length match\n", afterShuffle.length(), beforeShuffle.length());
        assertNotEquals("Expected different strings values\n", afterShuffle, beforeShuffle);
    
        int cardsInDifferentPlace = 0;
        for (Card card : test){
            //Current card must exist in deck, and might be moved
            assertTrue(beforeShuffleDeck.contains(card));
            cardsInDifferentPlace += (beforeShuffleDeck.get(test.indexOfCard(card)).equals(card)) ? 0 : 1;
        }
        assertNotEquals("Expected at least 1 card to have been shuffled out of original place.\n", 0, cardsInDifferentPlace);
    }
    
    @Test
    public void testSerialization(){
        
        File testFile = new File("test-deck.ser");
        
        writeDeckToFile(testFile, test);
        
        Deck test2 = readDeckFromFile(testFile);
        
        //Usual behaviour expected would be same after reading
        //assertEquals("Expected read to contain same number of cards as written.\n", test.size(), test2.size());
        //Assignment brief specifies writing and reading only "every second card" so size is halved.
        assertEquals("Expected read to contain same number of cards as written.\n", test.size()/2, test2.size());
        
        
        Iterator<Card> everyOther = test.secondCardIterator();
        while (everyOther.hasNext()){
            assertEquals("Expected every other card from written deck in read deck.", everyOther.next().toString(), test2.deal().toString());
        }
        
    }
    
    @Test
    public void testSorting(){
        
        List<Card> testDeck = new ArrayList();
        testDeck.add(new Card(Card.Rank.SIX, Card.Suit.HEARTS));
        testDeck.add(new Card(Card.Rank.TEN, Card.Suit.DIAMONDS));
        testDeck.add(new Card(Card.Rank.TEN, Card.Suit.SPADES));
        testDeck.add(new Card(Card.Rank.TWO, Card.Suit.CLUBS));
        
        test.setDeck(testDeck);
        
        test.sortAscending();
        System.out.println("Asc");
        System.out.println(test.toString());
        assertEquals("Expected ouput to match ascending example\n", "two of clubs, six of hearts, ten of diamonds, ten of spades", test.toString());

        test.sortDescending();
        System.out.println("Desc");
        System.out.println(test.toString());
        assertEquals("Expected output to match descending example\n", "ten of diamonds, ten of spades, six of hearts, two of clubs", test.toString());
        
        test.newDeck();
        
        test.sortAscending();
        System.out.println("Asc");
        System.out.println(test.toString());

        test.sortDescending();
        System.out.println("Desc");
        System.out.println(test.toString());
        
        
        test.shuffle();
        
        test.sortAscending();
        System.out.println("Asc");
        System.out.println(test.toString());

        test.sortDescending();
        System.out.println("Desc");
        System.out.println(test.toString());
    }
}

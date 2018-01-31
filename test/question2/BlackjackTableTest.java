package question2;

import java.io.File;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static question2.FileUtils.readBlackjackTableFromFile;
import static question2.FileUtils.writeBlackjackTableToFile;

/**
 *
 * @author Hakeem
 */
public class BlackjackTableTest {
    
    BlackjackTable test;
    
    public BlackjackTableTest() {
        
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        test = new BlackjackTable();
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testSerialization(){
        
        File testFile = new File("test-blackjack-table.ser");
        
        //avoid the "every other card" behaviour which is only included to display understanding of serialization
        //setting the saveAll flag to true will save every card the next time the deck is saved
        test.getDealer().setSaveAll(true);
        
        //modify the state somewhat
        test.initialiseBasicGame();
        
        try{
            test.getDealer().dealFirstCards();
        }catch(Exception e){
            fail("Could not perform first deal: "+e.getMessage());
        }
        
        //do the write and read back
        writeBlackjackTableToFile(testFile, test);
        
        BlackjackTable test2 = readBlackjackTableFromFile(testFile);
        
        
        assertEquals(4, test.getPlayers().size());
        assertEquals(3, test.getLimits().length);
        assertEquals(8, test.getLimits()[0]);
        assertEquals(1, test.getLimits()[1]);
        assertEquals(500, test.getLimits()[2]);
        
        assertEquals(0, test.getBlackjackDealer().getDealerScore());
        assertEquals(0, test.getBlackjackDealer().getDealerWinnings());
        assertEquals(4, test.getBlackjackDealer().getPlayersDealingTo().size());
        //after the first play has been called there are 43 cards remaining
        assertEquals(43, test.getBlackjackDealer().getDeck().size());
        assertEquals(1, test.getBlackjackDealer().getHand().getCards().size());
        
        
        assertNotNull(test2);
        assertEquals(test2.getPlayers().size(), test.getPlayers().size());
        assertEquals(test2.getLimits().length, test.getLimits().length);
        assertEquals(test.getLimits()[0],test2.getLimits()[0]);
        assertEquals(test.getLimits()[1],test2.getLimits()[1]);
        assertEquals(test.getLimits()[2],test2.getLimits()[2]);
        assertEquals(test.getBlackjackDealer().getDealerScore(), test2.getBlackjackDealer().getDealerScore());
        assertEquals(test.getBlackjackDealer().getDealerWinnings(), test2.getBlackjackDealer().getDealerWinnings());
        assertEquals(test.getBlackjackDealer().getPlayersDealingTo().size(), test2.getBlackjackDealer().getPlayersDealingTo().size());
        assertEquals(test.getBlackjackDealer().getDeck().size(), test2.getBlackjackDealer().getDeck().size());
        assertEquals(test.getBlackjackDealer().getHand().getCards().size(), test2.getBlackjackDealer().getHand().getCards().size());
        
        List<Card> c1 = test.getBlackjackDealer().getDeck().copyOfDeck();
        List<Card> c2 = test2.getBlackjackDealer().getDeck().copyOfDeck();
        assertTrue(c1.size() > 0);
        
        for(int i = 0; i < c1.size(); i++){
            assertEquals(c1.get(i).getRank(), c2.get(i).getRank());
            assertEquals(c1.get(i).getSuit(), c2.get(i).getSuit());
        }
        
        try{
            assertEquals(test.getBlackjackDealer().play(test.getPlayers().get(0)), test2.getBlackjackDealer().play(test2.getPlayers().get(0)));
        }catch(Exception e){
            fail("Could not process play: " + e.getMessage());
        }
        
        
    }
    
}

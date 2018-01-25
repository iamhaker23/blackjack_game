/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package programming2_assignment2;

import java.io.File;
import java.io.IOException;
import programming2_assignment2.classes.*;
import static programming2_assignment2.classes.FileUtils.readDeckFromFile;
import static programming2_assignment2.classes.FileUtils.writeDeckToFile;

/**
 *
 * @author Hakeem
 */
public class main {

    private static Deck deck;
    private static Deck deck2;
            
    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchFieldException{
        
        BlackjackTable table = new BlackjackTable();
        table.chooseGame();
        
        
        /*deck = new Deck();
        
        System.out.println("I'm Flapjack, the blapflack--blajcka--blackfla--- gah. gahhhh. Card games program.\nSo, for my first trick...");
        System.out.println("Behold, the deck of " + deck.size() + "\n\t" + deck.toString());
        deck.shuffle();
        System.out.println("Behold, the deck of " + deck.size() + ", but shuffled\n\t" + deck.toString());
        printDeal();
        printIteratedCards();
        
        File file = new File("deck.ser");
        
        writeDeckToFile(file, deck);
        
        deck2 = readDeckFromFile(file);
        
        System.out.println("Behold, the deck of " + deck2.size() + ", back from the file!\n\t" + deck2.toString());
        */
        
    }
    
    private static void printDeal(){
        System.out.println("I deal the top card!\n\tPlayer-san, you got a "+deck.deal().toString()+"!?");
        System.out.println("Oh no, the deck only has "+deck.size() + " cards remaining! *gravely* The end draws near.");
    }
    
    private static void printIteratedCards(){
        int count = 1;
        for (Card current : deck){
            System.out.println("Card "+count+" is "+current.toString());
            count += 1;
        }
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package programming2_assignment2.classes;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Hakeem
 */
public class CardGameUtils {
    
    private static final String[] titles = new String[]{
        "Mr.", "Dr.", "Miss", "Ms.", "Mrs.", "Sir", "Count", "Lord", "Dame", "Master"
    };
    
    private static final String[] forenames = new String[]{
        "Tyler", "Rick", "Morty", "Sterling", "Luke", "Ben", "Charles", "Peter", "Bruce", "Tony", "StarLord"
    };
    private static final String[] middlenames = new String[]{
        "Mallory", "Nancy", "Ray", "Lazer"
    };
    private static final String[] surnames = new String[]{
        "Archer", "Durden", "Sanchez", "Skywalker", "Solo", "Xavier", "Parker", "Wayne", "Stark"
    };
    
    public static String randomName(){
        StringBuilder sb = new StringBuilder();
        sb.append(titles[(int)Math.rint(Math.random() * (titles.length-1))]);
        sb.append(" ");
        sb.append(forenames[(int)Math.rint(Math.random() * (forenames.length-1))]);
        sb.append(" ");
        sb.append(((Math.random() > 0.95))? middlenames[(int)Math.rint(Math.random() * (middlenames.length-1))]+" " : "");
        sb.append(surnames[(int)Math.rint(Math.random() * (surnames.length-1))]);
        return sb.toString();
        
    }
    
    public static String readLine(){
        InputStreamReader isr = new InputStreamReader(System.in);
        
        char[] charBuffer = new char[512];
        int c;
        int index = 0;
        try{
            
            do {
                c = (int)isr.read();
                charBuffer[index++] = (char)c;
            }
            while (c != '\r' && c != '\n' && c != -1);
            
        }catch(IOException e){
            System.out.println("Couldn't read input.");
            return null;
        }
        
        return String.valueOf(charBuffer).trim();
    }
}

package question1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author Hakeem
 */
public class FileUtils {
    public static boolean writeDeckToFile(File file, Deck deck){
        try{
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            
            oos.writeObject(deck);

            fos.flush();
            oos.close();
            fos.close();
            return true;
        }catch(IOException e){
            System.out.println("Failed to serialize deck to file.\n"+e.getMessage());
        }
        return false;
    }
    
    public static Deck readDeckFromFile(File file){
        
        try{
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            
            Deck output  = (Deck)ois.readObject();
            
            ois.close();
            fis.close();
            return output;
        }catch(IOException e){
            System.out.println("Failed to reconstruct deck from file.\n"+e.getMessage());
        }catch (ClassNotFoundException e){
            System.out.println("Could not reconstruct deck due to missing class.\n"+e.getMessage());
        }
        return null;
    }
    
 
    
    
}

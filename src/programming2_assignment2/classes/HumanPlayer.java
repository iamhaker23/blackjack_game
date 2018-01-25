package programming2_assignment2.classes;

/**
 *
 * @author Hakeem
 */

import static programming2_assignment2.classes.CardGameUtils.readLine;

public class HumanPlayer extends BasicPlayer{

public HumanPlayer(){
    super();
    
    boolean accepted = false;
    String name = "";
    while (!accepted){
        
        System.out.println("Please enter your name:");
        name = readLine();
        System.out.printf("Your name will be %s.\n", name);
        System.out.println("Accept?\n[Yes=Y, No=N]");
        String accept = readLine();
        if (accept.contains("Y") || accept.contains("y")){
            accepted = true;
        }
    }
    
    setName(name);
    
}


//Bet amount
@Override
public int makeBet(){
    System.out.println("Balance: "+ getBalance());
    System.out.println("How much do you wish to bet?");
    
    int bet = -1;
    
    while (bet < 0){
        String input = readLine();
        try{
            if (input.contains("q") || input.contains("Q")) return 10;
            bet = Integer.parseInt(input);
        }catch(NumberFormatException e){
            System.out.println("Couldn't read bet. Please enter a positive number or Q to quit.");
        }
    }
    return bet;
}
    
//Hit or stick
@Override
public boolean hit(){
    String hit = "";
    boolean output = false;
    
    while ("".equals(hit)){
        
        System.out.println("Do you wish to hit?\n[Yes=Y, No=N]");
        hit = readLine();
        if (hit.contains("Y") || hit.contains("y")){
            output = true;
        }else if (hit.contains("N") || hit.contains("n")){
            output = false;
        }
        else{
            //force while loop to reiterate
            hit="";
        }
    }
    
    return output;
}


}

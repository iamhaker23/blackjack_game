package question2;

/**
 *
 * @author Hakeem
 */

import static question1.CardGameUtils.readLine;

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
        if (accept.charAt(0) == 'Y' || accept.charAt(0) == 'y'){
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
            if (input.charAt(0) == 'Q' || input.charAt(0) == 'q') return 0;
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
        if (hit.charAt(0) == 'Y' || hit.charAt(0) == 'y'){
            output = true;
        }else if (hit.charAt(0) == 'N' || hit.charAt(0) == 'n'){
            output = false;
        }
        else{
            //force while loop to reiterate given any other input
            System.out.println("Sorry, your input was not recognised.");
            hit="";
        }
    }
    
    return output;
}


}

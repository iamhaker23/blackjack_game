package question2;

import question1.FileUtils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static question2.FileUtils.readBlackjackTableFromFile;
import static question2.FileUtils.writeBlackjackTableToFile;
import interfaces.Dealer;
import interfaces.Player;
import static question2.CardGameUtils.readLine;

/**
 *
 * @author Hakeem
 */
public class BlackjackTable implements Serializable{
    
    private final int DEFAULT_PLAYER_MAX = 8;
    private final int DEFAULT_MIN_BET = 1;
    private final int DEFAULT_MAX_BET = 500;
    
    private List<Player> players;
    private Dealer dealer;
    
    private int playerLimit;
    private int minBet;
    private int maxBet;
    
    private boolean printOut;
    
    private transient boolean autosave;
    private GameType gameType;
    
    private enum GameType{
        INTERMEDIATE,
        ADVANCED,
        BASIC,
        NONE,
        INTERACTIVE,
        PERFORMANCE
    }
    
    public List<Player> getPlayers(){
        return this.players;
    }
    
    public Dealer getDealer(){
        return this.dealer;
    }
    
    //test helper
    public BlackjackDealer getBlackjackDealer(){
        //For test use
        return (BlackjackDealer)this.dealer;
    }
    
    public int[] getLimits(){
        return new int[]{playerLimit, minBet, maxBet};
    }
    
    public BlackjackTable(){
        this.players = new ArrayList();
        this.dealer = new BlackjackDealer();
        
        this.printOut = true;
        
        //use fields to allow modifications from default
        playerLimit = DEFAULT_PLAYER_MAX;
        minBet = DEFAULT_MIN_BET;
        maxBet = DEFAULT_MAX_BET;
        
        autosave = true;
        gameType = GameType.NONE;
    }
    
    
    
    public void chooseGame(){

        //Failsafe, default value will cause safe quit instead of crash
        String input = "q";
        
        //if called after a benchmark, or it's a new game.
        if (this.gameType == null || GameType.NONE.equals(this.gameType)){
            System.out.println("Please select a game type.\n[Quit=Q, Load Game=L, Advanced Game=A, Intermediate Game=I, Basic Game=B, Interactive Game=H, Performance Benchmark=P]");
            input = readLine();
        }else{
            System.out.println("Starting game...\n");
        }
        
        if (GameType.ADVANCED==this.gameType || (input.charAt(0)=='A'  || input.charAt(0)=='a' )){
            this.gameType = GameType.ADVANCED;
            advancedGame();
        }
        else if (GameType.INTERMEDIATE==this.gameType || (input.charAt(0)=='I'  || input.charAt(0)=='i' )){
            this.gameType = GameType.INTERMEDIATE;
            intermediateGame();
        }
        else if (GameType.BASIC==this.gameType || (input.charAt(0)=='B'  || input.charAt(0)=='b' )){
            this.gameType = GameType.BASIC;
            basicGame();
        }
        else if (GameType.INTERACTIVE==this.gameType || (input.charAt(0)=='H'  || input.charAt(0)=='h' )){
            this.gameType = GameType.INTERACTIVE;
            humanGame();
        }else if (GameType.PERFORMANCE==this.gameType || (input.charAt(0)=='P'  || input.charAt(0)=='p' )){
            this.gameType = GameType.PERFORMANCE;
            performanceBenchmark();
            //After benchamark completes, allow player to choose another option.
            chooseGame();
        }else if ((input.charAt(0)=='L'  || input.charAt(0)=='l' )){
            if (!handleLoad()){
                chooseGame();
            }
        }else{
            chooseGame();
        }
        
        
        if (input.charAt(0)=='Q'  || input.charAt(0)=='q' ) return;

        
    }
    
    public void basicGame(){
        
        
        printOut = true;
        if (players.isEmpty()) initialiseBasicGame();
        
        gameLoop();
        
    }
    
    public void intermediateGame(){
        
        printOut = true;
        if (players.isEmpty()) initialiseIntermediateGame();
        
        gameLoop();
        
    }
    
    public void humanGame(){
        
        
        
        printOut = true;
        if (players.isEmpty()) initialiseHumanGame();
        
        gameLoop();
    }
    
    public void advancedGame(){
        
        printOut = true;
        if (players.isEmpty()) initialiseAdvancedGame();
        
        gameLoop();
        
    }
    
    public void performanceBenchmark(){
        
        printOut = false;
        if (players.isEmpty()) initialiseAdvancedGame();
        
        benchMarkGameLoop();
        
        resetGame();
        
    }
    
    
    public void resetGame(){
        this.gameType = GameType.NONE;
        players.clear();
        dealer = new BlackjackDealer();
    }
    
    public void initialiseBasicGame(){
        
        
        players.clear();
        //add 4 players
        for (int i = 0; i < 4; i++){
            players.add(new BasicPlayer());
        }
        //connect to dealer
        this.dealer.assignPlayers(this.players);
        
        this.dealer.capBets(minBet, maxBet);
        
        printWelcomeMessage();
    }
    
    public void initialiseIntermediateGame(){
        
        
        players.clear();
        //add 4 players
        for (int i = 0; i < 4; i++){
            players.add(new IntermediatePlayer());
        }
        //connect to dealer
        this.dealer.assignPlayers(this.players);
        
        this.dealer.capBets(minBet, maxBet);
        
        printWelcomeMessage();
    }
    
    public void initialiseAdvancedGame(){
        
        
        players.clear();
        players.add(new IntermediatePlayer());
        
        players.add(new BasicPlayer());
        
        players.add(new AdvancedPlayer());
        
        //connect to dealer
        this.dealer.assignPlayers(this.players);
        
        this.dealer.capBets(minBet, maxBet);
        
        if (printOut) printWelcomeMessage();
    }
    
    public void initialiseHumanGame(){
        
        
        players.add(new HumanPlayer());
        players.add(new BasicPlayer());
        
        //connect to dealer
        this.dealer.assignPlayers(this.players);
        
        this.dealer.capBets(minBet, maxBet);
        
        printWelcomeMessage();
    }
    
    
    public void gameLoop(){
        boolean endGame = false;
        
        while (!endGame){
            
            if (dealer.beatAllPlayers()){
               endGame = noPlayersLeft();
               autosave = !endGame;
            }
            //if game was killed by noPlayersLeft(), just continue out without saving.
            if (!endGame){
                System.out.println("Continue game?\n[Continue=<Enter number of hands to play>, Save game=S, Load game=L, Quit=Q]");

                String input = readLine();
                if (input != null){

                    if (input.charAt(0)=='Q' || input.charAt(0)=='q' || input.charAt(0)=='n' || input.charAt(0)=='N'){
                        endGame = true;
                    }else if (input.charAt(0)=='L' || input.charAt(0)=='l'){

                        endGame = handleLoad();
                        autosave = !endGame;

                    }else if (input.charAt(0)=='S' || input.charAt(0)=='s'){

                        handleSave();

                    }else{
                        playHands(input);
                    }
                }
            }
        }
        
        if (autosave){
            getDealer().setSaveAll(true);
            save("AUTOSAVE.ser", this);
        }
    }
    
    public void benchMarkGameLoop(){
        int games = 0;
        String logFileName = "stats_" + (new SimpleDateFormat("yyyy-MM-dd_HHmm")).format(new Date()) + ".txt";
        
        int handsPlayed = 0;
        int defeatCount = 0;
        
        //Column names matching output as logged in printDeckStats
        logBenchmark("Game_Type, Profit_Per_Deck, Dealer_Winnings, Decks_Used, Hands_Played, Times_Defeated_All, Players_Defeated, Players_Per_Game\n", logFileName);
        
        int decksUsed = 0;
        
        while (games++ < 1000){
            
            if (dealer.beatAllPlayers()){
                defeatCount++;
                initialiseAdvancedGame();
            }
            
            decksUsed = printDeckStats(decksUsed, handsPlayed, defeatCount, logFileName);
            handsPlayed += playHands(1000);
            
            if (games == 1000){
                System.out.printf("Total dealer winnings: %d, Decks played: %d, Mean winnings per deck: %f, Players per game: 4, Hands played: %d, Players beaten: %d\n", dealer.getDealerWinnings(), dealer.getDecksUsed(), (float)dealer.getDealerWinnings()/(float)dealer.getDecksUsed(), handsPlayed, defeatCount*4);
            }
        }
        System.out.println("Benchmark stats saved in "+logFileName);
    }
    
    private void logBenchmark(String stats, String fileName){
        File logFile = new File(fileName);
        
        try{
            FileWriter fw = new FileWriter(logFile, true);
            
            fw.append(stats);
            fw.flush();
            fw.close();
        }catch(IOException e){
            System.out.println("Failed to write stats:\n" + stats+"\n");
        }
            
    }
    
    private int printDeckStats(int previousDecksUsed, int handsPlayed, int defeatCount, String logFileName){
        int newDecksUsed = previousDecksUsed;
        
        if (dealer.getDecksUsed() > previousDecksUsed){
           newDecksUsed = dealer.getDecksUsed();

           String stats = String.format("ADVANCED_GAME, %f, %d, %d, %d, %d, %d, %d\n", (float)dealer.getDealerWinnings()/(float)previousDecksUsed, dealer.getDealerWinnings(), previousDecksUsed, handsPlayed, defeatCount, defeatCount*4, 4);

           logBenchmark(stats, logFileName);

        }
        return newDecksUsed;
    }
    
    private boolean noPlayersLeft(){
        System.out.println("No players at table.\nInvite new players to table and continue this game?\n[Add new players=Y, New Game=N]");

        String input = readLine();
        if (input != null && (input.charAt(0)=='Y' || input.charAt(0)=='y')){
            //will reinit same game type
            if (gameType == GameType.ADVANCED){
                initialiseAdvancedGame();
            }else if (gameType == GameType.INTERACTIVE){
                initialiseHumanGame();
            }else if (gameType == GameType.INTERMEDIATE){
                initialiseIntermediateGame();
            }else if (gameType == GameType.BASIC){
                initialiseBasicGame();
            }else{
                System.out.println("Oops. Cannot invite table to this type of game.\n");
                resetGame();
                chooseGame();
            }
        }else if (input != null && (input.charAt(0)=='N' || input.charAt(0)=='n')){
            //will reset game and ask user to choose
            resetGame();
            chooseGame();
            //when passed back to gameloop, this will kill the game which started the new game.
            return true;
        }
        return false;
    }
    
    private void playHands(String input){
        playHands(getNumberOfHandsToPlay(input));
    }
    
    private int playHands(int hands){
        int handsPlayed = 0;
        
        if( hands > 0 ){
            int initHands = hands;
            while(hands-- > 0){
                if (dealer.beatAllPlayers()){
                    //Otherwise the loop continues with no players (i.e. the dealer deals to themselves for the remaining games).
                    handsPlayed = initHands-hands;
                    if(printOut){
                        System.out.println("The house always wins. This time after only "+(handsPlayed)+".");
                        System.out.println((hands == 0)? "All requested games were played.\n" : (hands)+" out of "+(initHands)+" requested games were not played.\n");
                    } 
                    hands = -1;
                }else{
                    dealer.playRound(printOut);
                }
            }
        }
        return handsPlayed;
    }
    
    private int getNumberOfHandsToPlay(String input){
        int hands = -1;
        try{
            hands = Integer.parseInt(input);
            if (hands < 0){
                throw new NumberFormatException();
            }
        }catch(NumberFormatException e){
            System.out.println("Couldn't read number of hands to play. Please enter a positive number.");
        }
        return hands;
    }
    
    private boolean handleLoad(){
        
        boolean endGame = false;
        
        System.out.println("Please enter a filename to load from:");
        String loadName = readLine();

        if (loadName != null){
            BlackjackTable bjt = load(loadName);
            if (bjt != null){
                
                endGame = true;

                System.out.println("Successfully loaded game: "+loadName);
                bjt.chooseGame();
            }else{
                System.out.println("Failed to load "+loadName+". Please check it's a valid file and try again.");
            }
        }
        
        return endGame;
    }
    
    private void handleSave(){
        //save
        String name = getSaveName();

        getDealer().setSaveAll(true);

        save(name, this);
        System.out.println("Saved game as "+name+"\n");
    }
    
    private String getSaveName(){
        return "Blackjack_" + (new SimpleDateFormat("yyyy-MM-dd_HHmm")).format(new Date()) + ".ser";
    }
    
    private static boolean save(String filename, BlackjackTable table){
        return writeBlackjackTableToFile(new File(filename), table);
    }
    
    private static BlackjackTable load(String filename){
        return readBlackjackTableFromFile(new File(filename));
    }
    
    private void printWelcomeMessage(){
        System.out.println("Welcome to our new players:\n");
        for (Player player : players){
            System.out.println(player.getName());
        }
        System.out.println("\n");
    }
    
    //Serializable code
    
    private void writeObject(ObjectOutputStream out) throws IOException{
            out.putFields().put("players", this.players);
            out.putFields().put("playerLimit", this.playerLimit);
            out.putFields().put("minBet", this.minBet);
            out.putFields().put("maxBet", this.maxBet);
            out.putFields().put("dealer", this.dealer);
            out.putFields().put("gameType", this.gameType);
            out.writeFields();
            out.flush();
    }
    
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
        in.defaultReadObject();
    }
    
    public static void main(String[] args){
        BlackjackTable table = new BlackjackTable();
        table.chooseGame();
    }
    
}

package programming2_assignment2.classes;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import static programming2_assignment2.classes.FileUtils.readBlackjackTableFromFile;
import programming2_assignment2.interfaces.Dealer;
import programming2_assignment2.interfaces.Player;
import static programming2_assignment2.classes.CardGameUtils.readLine;

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
        INTERMEDIATE, ADVANCED, BASIC, NONE, INTERACTIVE, PERFORMANCE
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
        if (this.gameType==GameType.PERFORMANCE || this.gameType == null || GameType.NONE.equals(this.gameType)){
            System.out.println("Please select a game type.\n[Quit=Q, Load Game=L, Advanced Game=A, Intermediate Game=I, Basic Game=B, Interactive Game=H, Performance Benchmark=P]");
            input = readLine();
        }else{
            System.out.println("Starting game...\n");
        }
        
        if (input.charAt(0)=='Q'  || input.charAt(0)=='q' ) return;

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
        }
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
        
        players.clear();
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
               noPlayersLeft();
            }
            
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
        
        if (autosave){
            getDealer().setSaveAll(true);
            save("AUTOSAVE.ser", this);
        }
    }
    
    public void benchMarkGameLoop(){
        int games = 0;
        String logFileName = "stats_" + (new SimpleDateFormat("yyyy-MM-dd_HHmm")).format(new Date()) + ".txt";
        
        logBenchmark("Game_Type, Profit_Per_Deck, Dealer_Winnings, Decks_Used\n", logFileName);
        int decksUsed = 0;
        
        while (games++ < 1000){
            
            if (dealer.beatAllPlayers()){
                initialiseAdvancedGame();
            }
            
            decksUsed = printDeckStats(decksUsed, logFileName);
            playHands(1000);
            
            if (games == 1000){
                System.out.printf("Total dealer winnings: %d, Decks played: %d, Mean winnings per deck: %f\n", dealer.getDealerWinnings(), dealer.getDecksUsed(), (float)dealer.getDealerWinnings()/(float)dealer.getDecksUsed());
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
    
    private int printDeckStats(int previousDecksUsed, String logFileName){
        int newDecksUsed = previousDecksUsed;
        
        if (dealer.getDecksUsed() > previousDecksUsed){
           newDecksUsed = dealer.getDecksUsed();

           String stats = String.format("ADVANCED_GAME, %f, %d, %d\n", (float)dealer.getDealerWinnings()/(float)previousDecksUsed, dealer.getDealerWinnings(), previousDecksUsed);

           logBenchmark(stats, logFileName);

        }
        return newDecksUsed;
    }
    
    private void noPlayersLeft(){
        System.out.println("No players at table.\nInvite new players to table?\n[Add new players=Y, Do not add new players=N]");

        String input = readLine();
        if (input != null && (input.charAt(0)=='Y' || input.charAt(0)=='y')){
            chooseGame();
        }
    }
    
    private void playHands(String input){
        playHands(getNumberOfHandsToPlay(input));
    }
    
    private void playHands(int hands){
        if( hands > 0 ){
            int initHands = hands;
            while(hands-- > 0){
                if (dealer.beatAllPlayers()){
                    //Otherwise the loop continues with no players (i.e. the dealer deals to themselves for the remaining games).
                    if(printOut){
                        System.out.println("The house always wins. This time after only "+(initHands-hands)+".");
                        System.out.println((hands)+" out of "+(initHands)+" requested games were not played.\n");
                    } 
                    hands = -1;
                }else{
                    dealer.playRound(printOut);
                }
            }
        }
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
        return FileUtils.writeBlackjackTableToFile(new File(filename), table);
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

package com.orangelight.monopolyserver.Main.Game;

import com.orangelight.monopolyserver.Main.Game.Player.PlayerProperty;
import com.orangelight.monopolyserver.Main.Game.Player.Player;
import com.orangelight.monopolyserver.Main.Game.Board.Board;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Alex
 */
public class GameInstance {
    private ArrayList<Player> players;
    private int[] currentDiceRoll;
    private ArrayList<PlayerProperty> properties;
    private final Board board;
    
    public GameInstance(Board b) throws IOException {
        properties = new ArrayList<>();
        populatePlayerProperties();
        this.board = b;
    }
    
    public void newGame(ArrayList<Player> players, int startingCash) {
        this.players = choosePlayerRotation(players);
        this.players.get(this.players.size() -1).setCurretTurn(true); //Set it to last player so when we call nextTurn it circles around to the first player
        this.currentDiceRoll = new int[] {-1, -1}; 
        for(Player p : players) p.addCash(1500);
        this.nextTurn();
    }

    public void loadGame(int gameID) {
        
    }
    
    private static ArrayList<Player> choosePlayerRotation(ArrayList<Player>  p) {
        Collections.shuffle(p);
        return p;
    }
    
    public void nextTurn() {
        Player lastPlayer = players.get(getCurrentPlayerIndex());
        lastPlayer.endTurn();        
        Player currentPlayer = players.get(getNextPlayerIndex());
        lastPlayer.setCurretTurn(false);
        currentPlayer.setCurretTurn(true);
        setCurrentDiceRoll(Board.rollDice());
        currentPlayer.move(getCurrentDiceRollSum(), this);
        board.getTileFromID(currentPlayer.getCurrentTileID()).action(this, currentPlayer);
       //Wait for player to end turn
        if(isWinner()) {
            
        }
        
    }
    
    private int getNextPlayerIndex() {
        for(int index = getCurrentPlayerIndex()+1; index < players.size(); index++) {
            if(!players.get(index).isBankrupt()) return index;
        }
        return getFirstNotBankruptPlayerIndex();
    }
    
    private int getFirstNotBankruptPlayerIndex() {
        for(int index = 0; index < players.size(); index++) {
            if(!players.get(index).isBankrupt()) return index;
        }
        return -1; //This means everyone is bankrupt
    }
    
    public int getCurrentPlayerIndex() {
        for(int index = 0; index < players.size(); index++) {
            if(players.get(index).isCurrentTurn()) return index;
        }
        return -1;//No player has current turn? Something is messed up
    }
    
    private boolean isWinner() {
        int isAlive = 0;
        for(Player p : players) {
            if(!p.isBankrupt()) isAlive++;
            if(isAlive <= 2) return false;
        }
        return true;
    }
    
    
    private void setCurrentDiceRoll(int[] a) {
        this.currentDiceRoll = a;
    }
    
    public Board getBoard() {
        return board;
    }
    
    public int[] getCurrentDiceRoll() {return currentDiceRoll;}
    public int getCurrentDiceRollSum() {return currentDiceRoll[0]+currentDiceRoll[1];}
    
    public PlayerProperty getPlayerProperty(int id) {
        return properties.get(id);
    }
    
    private void populatePlayerProperties() throws FileNotFoundException , IOException{
         try (BufferedReader br = new BufferedReader(new FileReader( getClass().getClassLoader().getResource("PropertyData.csv").getFile()))) {
            for (String line; (line = br.readLine()) != null;) {
                String[] lineData=  line.split(",");
                properties.add(new PlayerProperty(Integer.parseInt(lineData[0]), Integer.parseInt(lineData[2]), Integer.parseInt(lineData[3]), Integer.parseInt(lineData[4]), Integer.parseInt(lineData[5]), Integer.parseInt(lineData[6]), Integer.parseInt(lineData[7]), Integer.parseInt(lineData[8]), Integer.parseInt(lineData[0]), Integer.parseInt(lineData[10]), Boolean.parseBoolean(lineData[12]), Boolean.parseBoolean(lineData[11])));
            }
         }
    }
    
    public Player getCurrentPlayer() {
        return players.get(getCurrentPlayerIndex());
    }
}

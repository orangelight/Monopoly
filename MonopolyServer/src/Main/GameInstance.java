package Main;

import java.util.ArrayList;
import java.util.Collections;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alex
 */
public class GameInstance {
    private Board board;
    private ArrayList<Player> players;
    private ArrayList<Trade> trades;
    private int[] currentDiceRoll;
    
    public GameInstance(ArrayList<Player> players, Board b, int startingCash) {
        this.players = choosePlayerRotation(players);
        this.players.get(this.players.size() -1).setCurretTurn(true); //Set it to last player so when we call nextTurn it circles around to the first player
        this.currentDiceRoll = new int[] {-1, -1};
    }
    
    private static ArrayList<Player> choosePlayerRotation(ArrayList<Player>  p) {
        Collections.shuffle(p);
        return p;
    }
    
    public void nextTurn() {
        Player lastPlayer = players.get(getCurrentPlayerIndex());
        Player currentPlayer = players.get(getNextPlayerIndex());
        lastPlayer.setCurretTurn(false);
        currentPlayer.setCurretTurn(true);
        setCurrentDiceRoll(Board.rollDice());
        currentPlayer.move(getCurrentDiceRollSum());
        board.getTileFromID(currentPlayer.getCurrentTileID()).action(this);
        //Wait for player to end turn
        if(isWinner()) {
            
        } else {
            nextTurn();
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
    
    public int[] getCurrentDiceRoll() {return currentDiceRoll;}
    public int getCurrentDiceRollSum() {return currentDiceRoll[0]+currentDiceRoll[1];}
}

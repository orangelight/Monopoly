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
    private int doubleDiceNum = 0;
    private boolean eligibleForRollAgain, noRollJail = false;
    
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
        eligibleForRollAgain = false;
        this.nextTurn();
    }

    public void loadGame(int gameID) {
        
    }
    
    private static ArrayList<Player> choosePlayerRotation(ArrayList<Player>  p) {
        Collections.shuffle(p);
        return p;
    }
    
    public void setEligibleForRollAgain(boolean b) {
        this.eligibleForRollAgain = b;
    }
    
    public Player getNextPlayer() {
        Player lastPlayer = players.get(getCurrentPlayerIndex());
        lastPlayer.endTurn();  
        if(eligibleForRollAgain && !lastPlayer.isJailed()) {
            return lastPlayer;
        } else {
            Player p = players.get(getNextPlayerIndex());;
            lastPlayer.setCurretTurn(false);
            return p;
        }
    }
    
    public void regularTurn(Player p) {
        p.move(getCurrentDiceRollSum(), this);
        board.getTileFromID(p.getCurrentTileID()).action(this, p);
    }
    /**
     * ONLY FOR IF YOU PAY OR USE A CARD DO NOT USE FOR DOUBLES
     */
    public void outOfJailTurn(boolean roll) {
        Player currentPlayer = getCurrentPlayer();
        if(roll) setCurrentDiceRoll(Board.rollDice());
        if (isDiceDouble()) { //If last roll was double
            doubleDiceNum++;
            if (doubleDiceNum == 3) { //Speeding
                currentPlayer.jailPlayer(this);
                nextTurn();//End turn right after you get jailed
                return;
            } else {
                eligibleForRollAgain = true;
            }
        } else {
            doubleDiceNum = 0;
            eligibleForRollAgain = false;
        }
        regularTurn(currentPlayer);
    }
    
    public void nextTurn() {
        Player currentPlayer = getNextPlayer();
        currentPlayer.setCurretTurn(true);
        
        if (!currentPlayer.isJailed()) {
            if(!noRollJail) {
                setCurrentDiceRoll(Board.rollDice());
                
            } else {
                setNotRollJail(false);
            }
            if (isDiceDouble()) { //If last roll was double
                doubleDiceNum++;
                if (doubleDiceNum == 3) { //Speeding
                    currentPlayer.jailPlayer(this);
                    nextTurn();//End turn right after you get jailed
                    return;
                } else {
                    eligibleForRollAgain = true;
                }
            } else {
                doubleDiceNum = 0;
                eligibleForRollAgain = false;
            }
            regularTurn(currentPlayer);
        } else {
            currentPlayer.addJailTurn();
        }
        
        
        
//        //Jail
//        if(currentPlayer.isJailed()) {
//            
//            if(isDiceDouble() && doubleDiceNum != 3) {
//                currentPlayer.unJailPlayer();
//                currentPlayer.move(getCurrentDiceRollSum(), this);
//                board.getTileFromID(currentPlayer.getCurrentTileID()).action(this, currentPlayer);
//                eligibleForRollAgain = false;
//            } else {
//                if(currentPlayer.turnsInJail() > 2) {//Get out of jail after 3 turns
//                    if(currentPlayer.canSubCash(50)) {
//                        currentPlayer.addCash(-50);
//                        currentPlayer.unJailPlayer();;
//                        currentPlayer.move(getCurrentDiceRollSum(), this);
//                        board.getTileFromID(currentPlayer.getCurrentTileID()).action(this, currentPlayer);
//                    } else {
//                        throw new UnsupportedOperationException();
//                        //Ummm you don't have enough money to get out of jail
//                    }
//                }else {
//                   doubleDiceNum = 0;
//                   eligibleForRollAgain = false;
//                }
//            }
//        } else {//Regular turn
//            
//        }   
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
    
    
    public void setCurrentDiceRoll(int[] a) {
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
    
    public void setNotRollJail(boolean b) {
        this.noRollJail = b;
    }
    
    public Player getPlayerFromID(String id) {
        for(Player p : this.players) {
            if(p.getPlayerID().equals(id)) {
                return p;
            }
        }
        System.err.println("Could not find player in ID look up");
        return null;
    }
    
    public int getPlayerRRNumberForRent(String id) {
        int numOfRR = 0;
        for(PlayerProperty prop : properties) {
            if(prop.isRailroad()) {
                if(prop.getOwnerID()!= null && prop.getOwnerID().equals(id)) numOfRR++;
            }
        }
        return numOfRR;
    }
    
     public int getPlayerUtilNumberForRent(String id) {
        int numOfUtil = 0;
        for(PlayerProperty prop : properties) {
            if(prop.isUtilitie()) {
                if(prop.getOwnerID()!= null && prop.getOwnerID().equals(id)) numOfUtil++;
            }
        }
        return numOfUtil;
    }
     
     public boolean doesPlayerOwnAllColor(String id, PlayerProperty propColor) {
          for(PlayerProperty prop : properties) {
              if(!prop.isRailroad() && !prop.isUtilitie() && prop.getColorID() == propColor.getColorID()) {
                  if(prop.getOwnerID()== null) return false;
                  else if(!prop.getOwnerID().equals(id)) return false;
              }
          }
          return true;
     }
     
     public boolean isDiceDouble() {
        return (currentDiceRoll[0]==currentDiceRoll[1]);
     }
}

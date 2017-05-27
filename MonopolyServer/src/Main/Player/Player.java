/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main.Player;

import java.util.ArrayList;
import Main.Board.Property;
import Main.GameInstance;

/**
 *
 * @author Alex
 */
public class Player {
    private static ArrayList<Property> owned;
    private int cash, positionID;
    private boolean bankrupt, currentTurn;

    
    public boolean isCurrentTurn() { return currentTurn; }
    public void setCurretTurn(boolean b) { this.currentTurn = b; }
    public boolean isBankrupt() { return this.bankrupt; }
    public int getCurrentTileID() {return this.positionID; }
    
    public int getTotalAssests() {
         throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * Moves a certain number of tiles forward 
     * @param i number of tiles to move forward
     */
    public void move(int i, GameInstance instance) {
        addPassGoCash(positionID+i);
        this.positionID = (positionID+i)%40;
        instance.getBoard().getTileFromID(positionID).action(instance, this);
    }
    
    /**
     * Moves player to tile with ID
     * @param id ID of tile to move player to
     */
    public void moveTo(int id) {
        addPassGoCash(positionID+id);
        positionID = id;
    }
    
    public void addCash(int value) {
        this.cash +=value;
    }
    
    private void addPassGoCash(int value) {
        if(value > 39) this.addCash(200); //Pass go
    }
}

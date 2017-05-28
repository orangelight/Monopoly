/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orangelight.monopolyserver.Main.Game.Player;

import com.orangelight.monopolyserver.Main.Game.GameInstance;

/**
 *
 * @author Alex
 */
public class Player {
    private String playerID;
    private int cash, positionID;
    private boolean bankrupt, currentTurn;
    private Debt currentDebt;
    private PropertyAuction currentPropertyAuction;

    public Player(String id) {
        this.playerID = id;
    }
    
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
        if(positionID+i > 39) this.addCash(200); //Pass go
        this.positionID = (positionID+i)%40;
        instance.getBoard().getTileFromID(positionID).action(instance, this);
    }
    
    /**
     * Moves player to tile with ID
     * @param id ID of tile to move player to
     */
    public void moveTo(int id) {
        if(id < positionID) this.addCash(200);
        positionID = id;
    }
    
    public void addCash(int value) {
        this.cash +=value;
    }
    
    public boolean canSubCash(int value) {
        return (getCash()-value) >= 0;
    }
    
    public void endTurn() {
        setAcution(null);
    }
    
    public void setDebt(Debt d) {
        this.currentDebt = d;
    }
    
    public void setAcution(PropertyAuction p) {
        this.currentPropertyAuction = p;
    }
    
    public boolean isInDebt() { return this.currentDebt != null; }
    
    public String getPlayerID() { return this.playerID; }
    
    public int getCash() { return this.cash; }
    
    public PropertyAuction getAcution() {
        return this.currentPropertyAuction;
    }
    
    public Debt getDebt() {
        return this.currentDebt;
    }
    
}

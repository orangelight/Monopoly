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
    private int cash, positionID, jailTurn;
    private boolean bankrupt, currentTurn, jail;
    private Debt currentDebt;

    public Player(String id) {
        this.playerID = id;
        this.jailTurn = -1;
    }
    
    public boolean isCurrentTurn() { return currentTurn; }
    public void setCurretTurn(boolean b) { this.currentTurn = b; }
    public boolean isBankrupt() { return this.bankrupt; }
    public int getCurrentTileID() {return this.positionID; }
    public boolean isJailed() { return this.jail; }
    public int turnsInJail() { return jailTurn;}
    public void addJailTurn() { jailTurn++;}
    public void setJail(boolean b) { this.jail = b;}
    
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
    public void moveTo(int id, boolean goCash) {
        if(goCash && id < positionID) this.addCash(200);
        positionID = id;
    }
    
    public void jailPlayer(GameInstance g) {
        this.jail = true;
        this.jailTurn = 1;
        moveTo(10, false);
        g.setEligibleForRollAgain(false);
        endTurn();
        g.nextTurn();
    }
    
    public void unJailPlayer() {
        this.jail = false;
        this.jailTurn = -1;
    }
    
    public void addCash(int value) {
        this.cash +=value;
    }
    
    public boolean canSubCash(int value) {
        return (getCash()-value) >= 0;
    }
    
    public void endTurn() {
       
    }
    
    public void setDebt(Debt d) {
        this.currentDebt = d;
    }
    
   
    
    public boolean isInDebt() { return this.currentDebt != null; }
    
    public String getPlayerID() { return this.playerID; }
    
    public int getCash() { return this.cash; }
    
    
    
    public Debt getDebt() {
        return this.currentDebt;
    }
    
}

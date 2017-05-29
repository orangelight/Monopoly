/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orangelight.monopolyserver.Main.Game.Board;

import com.orangelight.monopolyserver.Main.Game.GameInstance;
import com.orangelight.monopolyserver.Main.Game.Player.Player;

/**
 *
 * @author Alex
 */
public abstract class CCard {
    private int id, typeID;
    private String description, ownerID;
    private int[] data;
    
    public CCard(int id, int type, String desc, int[] data) {
        this.id = id;
        this.description = desc;
        this.data = data;
    }
    
    public int getID() { return this.id;}
    public boolean isTaken() { return ownerID!=null; }
    public void setOwner(String id) { this.ownerID = id; }
    public int[] getData() { return this.data; }
    public String getOwnerID() { return this.ownerID; }
    public int getType() { return this.getType(); }
    
    public abstract void action(GameInstance game, Player currentPlayer);
}

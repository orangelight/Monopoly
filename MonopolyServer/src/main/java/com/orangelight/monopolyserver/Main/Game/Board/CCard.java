/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orangelight.monopolyserver.Main.Game.Board;

import com.orangelight.monopolyserver.Main.Game.GameInstance;
import com.orangelight.monopolyserver.Main.Game.Player.Player;
import com.orangelight.monopolyserver.Main.Game.Tradable;

/**
 *
 * @author Alex
 */
public abstract class CCard implements Tradable{
    private int id, typeID;
    private String description;
    private int[] data;
    private Player owner;
    
    public CCard(int id, int type, String desc, int[] data) {
        this.id = id;
        this.description = desc;
        this.data = data;
    }
    
    public int getID() { return this.id;}
    public boolean isTaken() { return owner!=null; }
    public void setOwner(Player p) { this.owner = p; }
    public int[] getData() { return this.data; }
    public Player getOwner() { return this.owner; }
    public int getType() { return this.typeID; }
    
    public abstract void action(GameInstance game, Player currentPlayer);
}

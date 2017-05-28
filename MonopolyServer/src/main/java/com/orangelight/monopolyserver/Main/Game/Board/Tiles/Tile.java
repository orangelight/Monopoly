/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orangelight.monopolyserver.Main.Game.Board.Tiles;

import com.orangelight.monopolyserver.Main.Game.GameInstance;
import com.orangelight.monopolyserver.Main.Game.Player.Player;

/**
 *
 * @author Alex
 */
public abstract class Tile {
    private int id, propertyID;
    private String name;
    private boolean tax, chance, communityChest;
    
    public Tile(int id, String name, int porpID, boolean tax, boolean chance, boolean cChest) {
        this.id = id;
        this.name = name;
        this.propertyID = porpID;
        this.tax = tax;
        this.chance = chance;
        this.communityChest = cChest;
    }
    
    public int getID() {return this.id;}
    public String getName() {return this.name;}
    public int getPropertyID() {return this.propertyID;}
    public abstract void action(GameInstance game, Player currentPlayer);
}

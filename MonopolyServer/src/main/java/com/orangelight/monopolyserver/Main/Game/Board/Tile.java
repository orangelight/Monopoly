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
public abstract class Tile {
    private int id;
    private String name;
    
    public Tile(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public int getID() {return this.id;}
    public String getName() {return this.name;}
    public abstract void action(GameInstance game, Player currentPlayer);
}

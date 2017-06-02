/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orangelight.monopolyserver.Main.Game.Auction;

import com.orangelight.monopolyserver.Main.Game.Player.Player;

/**
 *
 * @author Alex
 */
public class Bid implements Comparable<Bid>{
    private int amount;
    private Player playerID;
    public Bid(int value, Player p) {
        this.amount = value;
        this.playerID = p;
    }
    
    public int getAmount() {
        return this.amount;
    }
    
    public Player getOwner() {
        return this.playerID;
    }

    @Override
    public int compareTo(Bid o) {
        if(this.getAmount() > o.getAmount()) return 1;
        if(this.getAmount() < o.getAmount()) return -1;
        else return 0;
    }
}

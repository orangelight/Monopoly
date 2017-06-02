/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orangelight.monopolyserver.Main.Game.Auction;

import com.orangelight.monopolyserver.Main.Game.GameInstance;
import com.orangelight.monopolyserver.Main.Game.Player.Player;
import com.orangelight.monopolyserver.Main.Game.Tradable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author Alex
 */
public class Auction {
    private ArrayList<Bid> bids;
    private Tradable item;
    
    public Auction(Tradable item) {
        this.item = item;
        bids = new ArrayList<>();
    }
    
    public void addBid(Bid b) {
        this.bids.add(b);
    }
    
    public boolean hasPlayerPlacedBid(String s) {
        for(Bid b : bids) {
            if(b.getOwner().getPlayerID().equals(s)) return true;
        }
        return false;
    }
    
    public boolean canStartAuction(GameInstance game) {
        for(Player p : game.getPlayers()) {
            if(!p.isBankrupt() && !hasPlayerPlacedBid(p.getPlayerID())) return false;
        }
        return true;
    }
    
    public void auction() {
        item.setOwner(Collections.max(bids).getOwner().getPlayerID());
    }
}

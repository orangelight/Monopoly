package com.orangelight.monopolyserver.Main.Game;

import java.util.ArrayList;


/**
 *
 * @author Alex
 */
public class Trade {
    private String ownerID, clientID;
    private ArrayList<Tradable> ownerItems, clientItems;
    private int ownerCash, clientCash;
    
    public Trade(String owner, String client) {
        this.ownerID = owner;
        this.clientID = client;
        ownerItems = new ArrayList<>();
        clientItems = new ArrayList<>();
    }
    
    public void setOwnerCash(int i) { this.ownerCash = i; }
    public void setClientCash(int i) { this.clientCash = i; }
    public void addOwnerTradable(Tradable t) {
        this.ownerItems.add(t);
    }
    public void addClientTradable(Tradable t) {
        this.clientItems.add(t);
    }
    public String getClientID() { return this.clientID; }
    public int getOwnerItemsSize() { return ownerItems.size(); }
    public int getClientItemsSize() { return clientItems.size();}
    
    public void accept() {
        
    }
}

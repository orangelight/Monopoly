/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orangelight.monopolyserver.Main.Game.Player;

/**
 *
 * @author Alex
 */
public class Debt {
    private Player paying, receiving;
    private int amount;
    
    public Debt(Player pay, Player rec, int value) {
        this.paying = pay;
        this.receiving = rec;
        this.amount = value;
    }
    
    public int getAmount() { return this.amount; }
    public Player getPaying() { return this.paying; }
    public Player getReceiving() { return this.receiving; }
    
}

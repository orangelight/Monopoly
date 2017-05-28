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
    private String payingID, receivingID;
    private int amount;
    
    public Debt(String pay, String rec, int value) {
        this.payingID = pay;
        this.receivingID = rec;
        this.amount = value;
    }
    
    public int getAmount() { return this.amount; }
    public String getPaying() { return this.payingID; }
    public String getReceiving() { return this.receivingID; }
    
}

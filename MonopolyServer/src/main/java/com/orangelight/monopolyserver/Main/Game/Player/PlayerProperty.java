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
public class PlayerProperty {
     private int id, houses, price, colorID, baseRent, oneHouseRent, twoHouseRent, threeHouseRent, fourHouseRent, hotelRent, houseCost;
     private boolean mortgaged, utilitie, railroad, hotel;
     private String ownerID;
     
     public PlayerProperty(int id, int color, int price, int rent, int rent1H, int rent2H, int rent3H, int rent4H, int rentHotel, int houseCost, boolean railRoad, boolean utilitie) {
         this.id = id;
         this.colorID = color;
         this.price = price;
         this.baseRent = rent;
         this.oneHouseRent = rent1H;
         this.twoHouseRent = rent2H;
         this.threeHouseRent = rent3H;
         this.fourHouseRent = rent4H;
         this.hotelRent = rentHotel;
         this.houseCost = houseCost;
         this.railroad = railRoad;
         this.utilitie = utilitie; 
     }
     
     /**
      * If the property is owned then the owner will not be null
      * @return 
      */
     public boolean isOwned() {
         return ownerID != null;
     }
     
     public boolean isMortgaged() {
         return mortgaged;
     }
     
     public String getOwnerID() { return this.ownerID; }
     public int getPrice() { return this.price; }
     
     public int getRent() {
         throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
     }
     
     public void setOwner(Player p) { this.ownerID = p.getPlayerID();}
}

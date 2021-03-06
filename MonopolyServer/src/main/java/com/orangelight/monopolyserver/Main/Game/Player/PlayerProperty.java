/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orangelight.monopolyserver.Main.Game.Player;

import com.orangelight.monopolyserver.Main.Game.GameInstance;
import com.orangelight.monopolyserver.Main.Game.Tradable;

/**
 *
 * @author Alex
 */
public class PlayerProperty implements Tradable{
     private int id, houses, price, colorID, baseRent, oneHouseRent, twoHouseRent, threeHouseRent, fourHouseRent, hotelRent, houseCost;
     private boolean mortgaged, utilitie, railroad, hotel, traded;
     private Player owner;
     
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
         this.mortgaged = false;
         this.traded = false;
     }
     
     /**
      * If the property is owned then the owner will not be null
      * @return 
      */
     public boolean isOwned() {
         return owner != null;
     }
     
     public boolean isMortgaged() {
         return mortgaged;
     }
     public boolean isRailroad() {
         return railroad;
     }
     
     public boolean isJustTraded() { return this.traded; }
     public void setJustTraded(boolean b) { this.traded = b;}
     
     public boolean isUtilitie() {
         return utilitie;
     }
     
     public int getColorID() {
         return colorID;
     }
     
     public int getID() {
         return id;
     }
     
     public void setMortgage(boolean b) {
         this.mortgaged = b;
     }
     
     public int getHouses() { return this.houses; }
     
     public boolean hasHotel() { return this.hotel; }
     
     public Player getOwner() { return this.owner; }
     public int getPrice() { return this.price; }
     
     public int getHouseCost() { return this.houseCost; }
     public int getHotelCost() { return this.houseCost; }
     public void setHouses(int i) { this.houses = i; }
     public void addHouse() { this.houses++; }
     public void setHotel(boolean b) { this.hotel = b; }
     
     public int getRent(GameInstance g) {
         if (!this.railroad && !this.utilitie) {
             if (houses == 0 && !hotel) {
                 if(g.doesPlayerOwnAllColor(this.getOwner(), this)) return this.baseRent*2;
                 else return this.baseRent;
             } else if (houses == 1) {
                 return this.oneHouseRent;
             } else if (houses == 2) {
                 return this.twoHouseRent;
             } else if (houses == 3) {
                 return this.threeHouseRent;
             } else if (houses == 4) {
                 return this.fourHouseRent;
             } else {
                 return this.hotelRent;
             }
         } else if (this.railroad) {
             int num = g.getPlayerRRNumberForRent(this.getOwner());
             if (num == 1) {
                 return this.oneHouseRent;
             } else if (num == 2) {
                 return this.twoHouseRent;
             } else if (num == 3) {
                 return this.threeHouseRent;
             } else if (num == 4) {
                 return this.fourHouseRent;
             } else {
                 return 0;
             }
         } else if (this.utilitie) {
             int num = g.getPlayerUtilNumberForRent(this.getOwner());
             if (num == 1) {
                 return this.oneHouseRent*g.getCurrentDiceRollSum();
             } else if (num == 2) {
                 return this.twoHouseRent*g.getCurrentDiceRollSum();
             } else {
                 return 0;
             }
         } else {
             return 0;
         }
         
     }
     
     public void setOwner(Player p) { this.owner = p;}
}

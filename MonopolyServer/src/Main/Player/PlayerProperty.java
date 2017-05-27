/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main.Player;

/**
 *
 * @author Alex
 */
public class PlayerProperty {
     private int houses, price, colorID, baseRent, oneHouseRent, twoHouseRent, threeHouseRent, fourHouseRent, hotelRent, houseCost;
     private boolean mortgaged;
     private Player owner;
     
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
     
     public Player getOwner() { return this.owner; }
     
     public int getRent() {
         throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
     }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main.Board;

import Main.GameInstance;
import Main.Player.Debt;
import Main.Player.Player;
import Main.Player.PlayerProperty;

/**
 *
 * @author Alex
 */
public class Property extends Tile {
    private int propertyID;

    public Property(int id, String name, int propertyid) {
        super(id, name);
        this.propertyID = propertyid;
    }

    @Override
    public void action(GameInstance game, Player currentPlayer) {
        PlayerProperty propData = game.getPlayerProperty(propertyID);
        if(propData.isOwned() && !propData.isMortgaged() && !propData.getOwner().equals(currentPlayer)) {//Have to make player pay money to property owner
            currentPlayer.setDebt(new Debt(currentPlayer, propData.getOwner(), propData.getRent()));
        } else if(!propData.isOwned()) { //Property is not owned
            
        } 
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orangelight.monopolyserver.Main.Game.Board.Tiles;

import com.orangelight.monopolyserver.Main.Game.GameInstance;
import com.orangelight.monopolyserver.Main.Game.Player.Debt;
import com.orangelight.monopolyserver.Main.Game.Player.Player;
import com.orangelight.monopolyserver.Main.Game.Player.PlayerProperty;
import com.orangelight.monopolyserver.Main.Game.Player.PropertyAuction;

/**
 *
 * @author Alex
 */
public class PropertyTile extends Tile {

    public PropertyTile(int id, String name, int porpID, boolean tax, boolean chance, boolean cChest) {
        super(id, name, porpID, tax, chance, cChest);
    }

    @Override
    public void action(GameInstance game, Player currentPlayer) {
        PlayerProperty propData = game.getPlayerProperty(getPropertyID());
        if(propData.isOwned() && !propData.isMortgaged() && !propData.getOwnerID().equals(currentPlayer.getPlayerID())) {//Have to make player pay money to property owner
            int rent = propData.getRent(game);
            if(currentPlayer.canSubCash(rent)) {
                currentPlayer.addCash(-rent);
                game.getPlayerFromID(propData.getOwnerID()).addCash(rent);
            } else {
                currentPlayer.setDebt(new Debt(currentPlayer.getPlayerID(), propData.getOwnerID(), rent));
            }
            
        } else if(!propData.isOwned()) { //Property is not owned
            currentPlayer.setAcution(new PropertyAuction(getPropertyID()));
        } 
    }
}

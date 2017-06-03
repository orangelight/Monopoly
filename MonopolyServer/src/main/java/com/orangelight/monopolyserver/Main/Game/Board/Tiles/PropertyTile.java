/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orangelight.monopolyserver.Main.Game.Board.Tiles;

import com.orangelight.monopolyserver.Main.Game.GameInstance;
import com.orangelight.monopolyserver.Main.Game.Player.Player;
import com.orangelight.monopolyserver.Main.Game.Player.PlayerProperty;

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
        if(propData.isOwned() && !propData.isMortgaged() && !propData.getOwner().equals(currentPlayer)) {//Have to make player pay money to property owner
            int rent = propData.getRent(game);
            currentPlayer.addCash(-rent);
            propData.getOwner().addCash(rent);
        } else if(!propData.isOwned()) { //Property is not owned
            
        } 
    }
}

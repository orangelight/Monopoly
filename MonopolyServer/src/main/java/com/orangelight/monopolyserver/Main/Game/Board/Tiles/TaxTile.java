/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orangelight.monopolyserver.Main.Game.Board.Tiles;

import com.orangelight.monopolyserver.Main.Game.GameInstance;
import com.orangelight.monopolyserver.Main.Game.Player.Player;

/**
 *
 * @author Alex
 */
public class TaxTile extends Tile{

    public TaxTile(int id, String name, int porpID, boolean tax, boolean chance, boolean cChest) {
        super(id, name, porpID, tax, chance, cChest);
    }
    
    @Override
    public void action(GameInstance game, Player currentPlayer) {
        
    }
    
}

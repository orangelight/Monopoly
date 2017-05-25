/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main.Tiles;

import Main.GameInstance;
import Main.Player;

/**
 *
 * @author Alex
 */
public class Property extends Tile {
    private int houses, price, colorID, baseRent, oneHouseRent, twoHouseRent, threeHouseRent, fourHouseRent, hotelRent, houseCost;

    public Property(int id, String name) {
        super(id, name);
    }

    @Override
    public void action(GameInstance game, Player currentPlayer) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

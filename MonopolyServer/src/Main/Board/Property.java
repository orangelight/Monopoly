/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main.Board;

import Main.GameInstance;
import Main.Player.Player;

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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

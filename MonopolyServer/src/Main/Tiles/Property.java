/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main.Tiles;

import Main.GameInstance;

/**
 *
 * @author Alex
 */
public class Property extends Tile {
    private boolean morgaged, hotel;
    private int houses, price;

    public Property(int id, String name) {
        super(id, name);
    }

    @Override
    public void action(GameInstance game) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

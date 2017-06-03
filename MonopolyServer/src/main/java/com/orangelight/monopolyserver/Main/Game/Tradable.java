/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orangelight.monopolyserver.Main.Game;

import com.orangelight.monopolyserver.Main.Game.Player.Player;

/**
 *
 * @author Alex
 */
public interface Tradable {
    public void setOwner(Player p);
    public Player getOwner();
}

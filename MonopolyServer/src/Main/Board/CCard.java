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
public abstract class CCard {
    public abstract void action(GameInstance game, Player currentPlayer);
}
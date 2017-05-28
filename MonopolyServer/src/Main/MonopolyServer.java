/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Main.Board.Board;
import Main.Player.Player;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Alex
 */
public class MonopolyServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
       ArrayList<Player> p = new ArrayList<>();
       p.add(new Player());
        new GameInstance(p, new Board());
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orangelight.monopolyserver.Main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orangelight.monopolyserver.Main.Game.GameInstance;
import com.orangelight.monopolyserver.Main.Game.Board.Board;
import com.orangelight.monopolyserver.Main.Game.Board.Tiles.Tile;
import com.orangelight.monopolyserver.Main.Game.Player.Debt;
import com.orangelight.monopolyserver.Main.Game.Player.Player;
import java.io.IOException;
import java.util.ArrayList;
import spark.Request;
import spark.Response;
import spark.Route;
import static spark.Spark.*;

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
       p.add(new Player("alex"));
       GameInstance game = new GameInstance(new Board());
       game.newGame(p ,1500);
        get("/board", (request, response) -> {
            response.type("application/json");
            return new Gson().toJson(game.getBoard());
        });
        
        get("/game", (request, response) -> {
            response.type("application/json");
            return new GsonBuilder().serializeNulls().create().toJson(game);
        });
       
        put("/endTurn", (request, response) -> {
            game.nextTurn();
            return "turn ended ";
        });
    }
    
}

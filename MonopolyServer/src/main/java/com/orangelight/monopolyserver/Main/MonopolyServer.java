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
import com.orangelight.monopolyserver.Main.Game.Board.Tiles.*;
import com.orangelight.monopolyserver.Main.Game.Player.*;
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
       p.add(new Player("joe"));
       p.add(new Player("bob"));
       p.add(new Player("luke"));
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
            Player currentPlayer = game.getCurrentPlayer();
            if(true) { //request.headers("id").equals(currentPlayer.getPlayerID())
                 if(!currentPlayer.isInDebt()) {
                     game.nextTurn();
                      return "turn ended ";
                 } else {
                      return "Still owe money";
                 }
               
            } else {
                return "Not your turn";
            }
           
            
        });
        
        put("/buyproperty", (request, response) -> {
            Player currentPlayer = game.getCurrentPlayer();
             if(true) { //request.headers("id").equals(currentPlayer.getPlayerID())
                 if(currentPlayer.getAcution() != null) {
                     PlayerProperty prop =  game.getPlayerProperty(currentPlayer.getAcution().getPropertyID());
                     if(currentPlayer.canSubCash(prop.getPrice())) {
                         currentPlayer.addCash(-prop.getPrice());
                         prop.setOwner(currentPlayer);
                         currentPlayer.setAcution(null);
                         return "Bought";
                     } else return "You don't have enough money";
                 } else return "You can't buy this";
            } else {
                return "Not your turn";
            }
        });
        
         put("/paydebt", (request, response) -> {
            Player currentPlayer = game.getCurrentPlayer();
             if(true) { //request.headers("id").equals(currentPlayer.getPlayerID())
                 if(currentPlayer.getDebt() != null) {
                     Debt debt = currentPlayer.getDebt();
                     if(currentPlayer.canSubCash(debt.getAmount())) {
                         currentPlayer.addCash(-debt.getAmount());
                         game.getPlayerFromID(debt.getReceiving()).addCash(debt.getAmount());
                         currentPlayer.setDebt(null);
                         return "Whoo you payed your debt";
                     } else return "You don't have enough money";
                 } else return "You Don't owe money";
            } else {
                return "Not your turn";
            }
        });
        
        
    }
    
}

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
import com.orangelight.monopolyserver.Main.Game.Board.CCard;
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
                if(!currentPlayer.isJailed()) {
                 if(!currentPlayer.isInDebt()) {
                     currentPlayer.endTurn();
                     game.nextTurn();
                      return "turn ended ";
                 } else {
                      return "Still owe money";
                 }
                } else {
                    return "You must roll or pay";
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
                         if(currentPlayer.isJailed()) {
                             game.setEligibleForRollAgain(true);
                             game.setNotRollJail(true);
                             currentPlayer.unJailPlayer();
                         }
                         
                         if(debt.getReceiving()!= null) {
                             game.getPlayerFromID(debt.getReceiving()).addCash(debt.getAmount());
                         } 
                         
                         currentPlayer.setDebt(null);
                         return "Whoo you payed your debt";
                     } else return "You don't have enough money";
                 } else return "You Don't owe money";
            } else {
                return "Not your turn";
            }
        });
        
        put("/payjail", (request, response) -> {
            Player currentPlayer = game.getCurrentPlayer();
             if(true) { //request.headers("id").equals(currentPlayer.getPlayerID())
                 if(currentPlayer.isJailed()) {
                     if(currentPlayer.canSubCash(50)) {
                        currentPlayer.addCash(-50);
                        currentPlayer.unJailPlayer();
                        game.outOfJailTurn(true);
                        return "Un-jailed";
                     } else return "You don't have enough money";
                 } else return "You're not in jail";
            } else {
                return "Not your turn";
            }
        });
        
        put("/cardjail", (request, response) -> {
            Player currentPlayer = game.getCurrentPlayer();
             if(true) { //request.headers("id").equals(currentPlayer.getPlayerID())
                 if(currentPlayer.isJailed()) {
                    for(CCard c : game.getChanceCards()) {
                        if(c.getType() ==9 && c.getOwnerID()!= null && c.getOwnerID().equals(currentPlayer.getPlayerID())) {
                            c.setOwner(null);
                            currentPlayer.unJailPlayer();
                            game.outOfJailTurn(true);
                            return "Un-jailed";
                        }
                    }
                    for(CCard c : game.getCommunityCards()) {
                        if(c.getType() ==9 && c.getOwnerID()!= null && c.getOwnerID().equals(currentPlayer.getPlayerID())) {
                            c.setOwner(null);
                            currentPlayer.unJailPlayer();
                            game.outOfJailTurn(true);
                            return "Un-jailed";
                        }
                    }
                    return "You do not have a get out of jail card";
                 } else return "You're not in jail";
            } else {
                return "Not your turn";
            }
        });
        
        put("/rolljail", (request, response) -> {
            Player currentPlayer = game.getCurrentPlayer();
             if(true) { //request.headers("id").equals(currentPlayer.getPlayerID())
                 if(currentPlayer.isJailed()) {
                      game.setCurrentDiceRoll(Board.rollDice());
                      if(game.isDiceDouble()) {
                        currentPlayer.unJailPlayer();
                        game.outOfJailTurn(false);
                        game.setEligibleForRollAgain(false); //Don't get to go again
                        return "Un-jailed";
                      } else {
                          if(currentPlayer.turnsInJail() > 2) {
                                if(currentPlayer.canSubCash(50)) {
                                    currentPlayer.addCash(-50);
                                    game.setEligibleForRollAgain(true);
                                    game.setNotRollJail(true);
                                    currentPlayer.unJailPlayer();
                                } else {
                                    currentPlayer.setDebt(new Debt(currentPlayer.getPlayerID(), null, 50));
                                }
                              return "Pay 50 to get out then roll";
                          }  else {
                              game.nextTurn();
                              return "turn ended";
                          } 
                      }
                 } else return "You're not in jail";
            } else {
                return "Not your turn";
            }
        });
        
        put("/buyhouse/:id", (request, response) -> {
            Player currentPlayer = game.getCurrentPlayer();
            if(isNumeric(request.params("id"))) {
                if(true) { //request.headers("id").equals(currentPlayer.getPlayerID())
                    int id = Integer.parseInt(request.params("id"));
                    if(id < 40 && id > -1) {
                        PlayerProperty prop = game.getPlayerProperty(id);
                        if(prop.getColorID()!=-1 && prop.getOwnerID()!= null && currentPlayer.getPlayerID().equals(prop.getOwnerID())) {
                            if(game.doesPlayerOwnAllColor(currentPlayer.getPlayerID(), prop) && game.canPutHouse(prop)) {
                                if(currentPlayer.canSubCash(prop.getHouseCost())) {
                                    currentPlayer.addCash(-prop.getHouseCost());
                                    prop.addHouse();
                                    return "You bought a house for "+id;
                                } else return "You need money";
                            } else {
                                return "You do not own a monoply on this color/need to buy other houses";
                            }
                        } else {
                             return "You do not own this propperty";
                        }
                    } else {
                        return "Not a vaild id";
                    }
                   
                } else return "Not your turn";
            } else return "That is not a number...";
             
        });
        
        put("/buyhotel/:id", (request, response) -> {
            Player currentPlayer = game.getCurrentPlayer();
            if(isNumeric(request.params("id"))) {
                if(true) { //request.headers("id").equals(currentPlayer.getPlayerID())
                    int id = Integer.parseInt(request.params("id"));
                    if(id < 40 && id > -1) {
                        PlayerProperty prop = game.getPlayerProperty(id);
                        if(prop.getColorID()!=-1 && prop.getOwnerID()!= null && currentPlayer.getPlayerID().equals(prop.getOwnerID())) {
                            if(game.doesPlayerOwnAllColor(currentPlayer.getPlayerID(), prop) && game.canPutHotel(prop)) {
                                if(currentPlayer.canSubCash(prop.getHotelCost())) {
                                    currentPlayer.addCash(-prop.getHotelCost());
                                    prop.setHotel(true);
                                    prop.setHouses(0);
                                    return "You bought a hotel for "+id;
                                } else return "You need money";
                            } else {
                                return "You do not own a monoply on this color/need to buy other houses";
                            }
                        } else {
                             return "You do not own this propperty";
                        }
                    } else {
                        return "Not a vaild id";
                    }
                   
                } else return "Not your turn";
            } else return "That is not a number...";
             
        });
    }
    
    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }
    
}

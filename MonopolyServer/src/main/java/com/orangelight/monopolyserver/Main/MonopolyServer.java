/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orangelight.monopolyserver.Main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orangelight.monopolyserver.Main.Game.Auction.Auction;
import com.orangelight.monopolyserver.Main.Game.Auction.Bid;
import com.orangelight.monopolyserver.Main.Game.GameInstance;
import com.orangelight.monopolyserver.Main.Game.Board.Board;
import com.orangelight.monopolyserver.Main.Game.Board.CCard;
import com.orangelight.monopolyserver.Main.Game.Board.Tiles.*;
import com.orangelight.monopolyserver.Main.Game.Player.*;
import com.orangelight.monopolyserver.Main.Game.Trade;
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
                if(game.getCurrentTrade()!=null) return "On going trade";
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
                 if(game.getCurrentAuction() == null && game.getBoard().getTileFromID(currentPlayer.getCurrentTileID()).getPropertyID() != -1 && game.getPlayerProperty(game.getBoard().getTileFromID(currentPlayer.getCurrentTileID()).getPropertyID()).getOwnerID() == null) {
                     PlayerProperty prop =  game.getPlayerProperty(game.getBoard().getTileFromID(currentPlayer.getCurrentTileID()).getPropertyID());
                     if(currentPlayer.canSubCash(prop.getPrice())) {
                         currentPlayer.addCash(-prop.getPrice());
                         prop.setOwner(currentPlayer.getPlayerID());
                         return "Bought";
                     } else return "You don't have enough money";
                 } else return "You can't buy this";
            } else {
                return "Not your turn";
            }
        });
        
        put("/auctionproperty", (request, response) -> {
            Player currentPlayer = game.getCurrentPlayer();
             if(true) { //request.headers("id").equals(currentPlayer.getPlayerID())
                 if(game.getCurrentAuction() == null && game.getBoard().getTileFromID(currentPlayer.getCurrentTileID()).getPropertyID() != -1 && game.getPlayerProperty(game.getBoard().getTileFromID(currentPlayer.getCurrentTileID()).getPropertyID()).getOwnerID() == null) {
                     PlayerProperty prop =  game.getPlayerProperty(game.getBoard().getTileFromID(currentPlayer.getCurrentTileID()).getPropertyID());
                     game.setCurrentAuction(new Auction(prop));
                    return "Auctioned";
                 } else return "You can't Auction this";
            } else {
                return "Not your turn";
            }
        });
        
        put("/bid/:amount", (request, response) -> {
            Player currentPlayer = game.getCurrentPlayer();
             if(true) { //request.headers("id").equals(currentPlayer.getPlayerID())
                 if(game.getCurrentAuction() != null && game.getCurrentAuction().hasPlayerPlacedBid(request.headers("id"))) {
                     if(isNumeric(request.params("amount"))) {
                         int amount = Integer.parseInt(request.params("amount"));
                         if(game.getPlayerFromID(request.headers("id")).canSubCash(amount)) { //Check if game.getPlayerFromID() is null***********
                             game.getCurrentAuction().addBid(new Bid(amount, game.getPlayerFromID(request.headers("id"))));
                             if(game.getCurrentAuction().canStartAuction(game)) {
                                game.getCurrentAuction().auction();
                             }
                             return "Bid Placed";
                         } else {
                             return "Don't have enough money";
                         }
                     } else {
                         return "amount not a number";
                     }
                 } else {
                     return "Can't Bid";
                 }
                 
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
           
                if(true) { //request.headers("id").equals(currentPlayer.getPlayerID())
                     if(isNumeric(request.params("id"))) {
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
                   } else return "That is not a number..."; 
                } else return "Not your turn";
            
             
        });
        put("/requesttrade/:clientID/:ownerCash/:clientCash/:ownerProp/:clientProp/:ownerchanceID/:clientchanceID/:ownercommunityID/:clientcommunityID", (request, response) -> {
            Player currentPlayer = game.getCurrentPlayer();
            if (true) { //request.headers("id").equals(currentPlayer.getPlayerID())
                if (isNumeric(request.params("ownerCash")) && isNumeric(request.params("clientCash")) && isParamCorrectFormat(request.params("ownerProp")) && isParamCorrectFormat(request.params("clientProp"))) {
                    if (game.doesPlayerExsist(request.params("clientID"))) {
                        Trade trade = new Trade(currentPlayer.getPlayerID(), request.params("clientID"));
                        if (currentPlayer.canSubCash(Integer.parseInt(request.params("ownerCash")))) {
                            trade.setOwnerCash(Integer.parseInt(request.params("ownerCash")));
                        } else return "You don't have enough money";
                        if (currentPlayer.canSubCash(Integer.parseInt(request.params("clientCash")))) {
                            trade.setOwnerCash(Integer.parseInt(request.params("clientCash")));
                        } else return "Your client doesn't have enough money";
                        if(!request.params("ownerProp").equals("-1")) {
                            PlayerProperty[] propList = getPropertiesFromParam(game, request.params("ownerProp"));
                            for(PlayerProperty prop : propList) {
                                if(GameInstance.isPropTradable(prop, currentPlayer.getPlayerID(), game)) {
                                    trade.addOwnerTradable(prop);
                                } else return "Error with owner properties";
                            }
                        }
                       if(!request.params("clientProp").equals("-1")) {
                            PlayerProperty[] propList = getPropertiesFromParam(game, request.params("clientProp"));
                            for(PlayerProperty prop : propList) {
                                if(GameInstance.isPropTradable(prop, trade.getClientID(), game)) {
                                    trade.addClientTradable(prop);
                                } else return "Error with cleint properties";
                            }
                        }
                        if(!request.params("ownerchanceID").equals("-1")) {
                            int id = Integer.parseInt(request.params("ownerchanceID"));
                            for(CCard c : game.getChanceCards()) {
                                if(c.getID()==id) {
                                    if(c.getType()!=9) return "Can't trade this card";
                                    if(!c.isTaken()) return "No one owns this card";
                                    if(!c.getOwnerID().equals(currentPlayer.getPlayerID())) return "You do not own this card";
                                    trade.addClientTradable(c);
                                    break;
                                }
                            }
                        }
                        if(!request.params("clientchanceID").equals("-1")) {
                             int id = Integer.parseInt(request.params("clientchanceID"));
                            for(CCard c : game.getChanceCards()) {
                                if(c.getID()==id) {
                                    if(c.getType()!=9) return "Can't trade this card";
                                    if(!c.isTaken()) return "No one owns this card";
                                    if(!c.getOwnerID().equals(trade.getClientID())) return "You do not own this card";
                                    trade.addClientTradable(c);
                                    break;
                                }
                            }
                        }
                        if(!request.params("ownercommunityID").equals("-1")) {
                            int id = Integer.parseInt(request.params("ownercommunityID"));
                            for(CCard c : game.getCommunityCards()) {
                                if(c.getID()==id) {
                                    if(c.getType()!=9) return "Can't trade this card";
                                    if(!c.isTaken()) return "No one owns this card";
                                    if(!c.getOwnerID().equals(currentPlayer.getPlayerID())) return "You do not own this card";
                                    trade.addClientTradable(c);
                                    break;
                                }
                            }
                        }
                        if(!request.params("clientcommunityID").equals("-1")) {
                            int id = Integer.parseInt(request.params("clientcommunityID"));
                            for(CCard c : game.getCommunityCards()) {
                                if(c.getID()==id) {
                                    if(c.getType()!=9) return "Can't trade this card";
                                    if(!c.isTaken()) return "No one owns this card";
                                    if(!c.getOwnerID().equals(trade.getClientID())) return "You do not own this card";
                                    trade.addClientTradable(c);
                                    break;
                                }
                            }
                        }
                        if(trade.getClientItemsSize() == 0 && trade.getOwnerItemsSize()==0) return "You can't just trade cash";
                        game.setCurrentTrade(trade);
                        return "Trade requested";
                    } else {
                        return "Client does not exsist";
                    }
                } else {
                    return "Not correct format";
                }
            } else {
                return "Not your turn";
            }
        });
   
        put("/tradeaccept", (request, response) -> {
            Player currentPlayer = game.getCurrentPlayer();
            if (true) { //request.headers("id").equals(currentPlayer.getPlayerID())
                    game.getCurrentTrade().accept();
                    game.setCurrentTrade(null);
                    return "You traded";
            } else {
                return "Not your turn";
            }

        });
        
        put("/tradecancel", (request, response) -> {
            Player currentPlayer = game.getCurrentPlayer();
            if (true) { //request.headers("id").equals(currentPlayer.getPlayerID())
                    game.setCurrentTrade(null);
                    return "Trade canceled";
            } else {
                return "Not your turn";
            }

        });
        
        put("/mortgage/:id", (request, response) -> {
            Player currentPlayer = game.getCurrentPlayer();
            if (true) { //request.headers("id").equals(currentPlayer.getPlayerID())
                if(isNumeric(request.params("id"))) {
                    PlayerProperty prop = game.getPlayerProperty(Integer.parseInt(request.params("id")));
                    if(prop != null && prop.getOwnerID() != null && prop.getOwnerID().equals(currentPlayer.getPlayerID()) && !prop.hasHotel() && prop.getHouses()==0 && !prop.isMortgaged()) {
                        for (PlayerProperty colorProp : game.getPlayerProperties()) {
                            if (colorProp.getColorID() == prop.getColorID() && prop.getID() != colorProp.getID()) {
                                if (prop.hasHotel() || prop.getHouses() > 0) {
                                    return "Must sell all buildings on color group";
                                }
                            }
                        }
                        prop.setMortgage(true);
                        currentPlayer.addCash(prop.getPrice()/2);
                        return "Mortgaged";
                    } else {
                        return "Error validating property";
                    }
                } else return "Not valid id";
            } else {
                return "Not your turn";
            }

        });
        
        put("/unmortgage/:id", (request, response) -> {
            Player currentPlayer = game.getCurrentPlayer();
            if (true) { //request.headers("id").equals(currentPlayer.getPlayerID())
                if(isNumeric(request.params("id"))) {
                    PlayerProperty prop = game.getPlayerProperty(Integer.parseInt(request.params("id")));
                    if(prop != null && prop.getOwnerID() != null && prop.getOwnerID().equals(currentPlayer.getPlayerID()) && prop.isMortgaged()) {
                        int amount = (int)((prop.getPrice()/2)*1.1);
                        if(currentPlayer.canSubCash(amount)) {
                            currentPlayer.addCash(-amount);
                            prop.setMortgage(false);
                        } else {
                            return "Don't have enough money";
                        }
                        return "Un-Mortgaged";
                    } else {
                        return "Error validating property";
                    }
                } else return "Not valid id";
            } else {
                return "Not your turn";
            }

        });
        
        put("/bid", (request, response) -> {
            Player currentPlayer = game.getCurrentPlayer();
            if (true) { //request.headers("id").equals(currentPlayer.getPlayerID())
                    game.setCurrentTrade(null);
                    return "Trade canceled";
            } else {
                return "Not your turn";
            }

        });
    }
    

    
    public static boolean isParamCorrectFormat(String s) {
        String[] data = s.split(",");
        for(String number : data) {
            if(!isNumeric(number)) return false;
        }
        return true;
    }
    
    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }
    
    public static PlayerProperty[] getPropertiesFromParam(GameInstance game, String s) {
        String[] stringArrayProps = s.split(",");
        PlayerProperty[] pArray = new PlayerProperty[stringArrayProps.length];
        for(int i = 0; i < stringArrayProps.length; i++) {
            pArray[i] = game.getPlayerProperty(i);
        }
        return pArray;
    }
    
}

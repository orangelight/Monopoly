package com.orangelight.monopolyserver.Main.Game;

import com.orangelight.monopolyserver.Main.Game.Player.*;
import com.orangelight.monopolyserver.Main.Game.Board.*;
import com.orangelight.monopolyserver.Main.Game.Board.Tiles.Tile;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Alex
 */
public class GameInstance {
    private ArrayList<Player> players;
    private int[] currentDiceRoll;
    private ArrayList<PlayerProperty> properties;
    private final Board board;
    private int doubleDiceNum = 0, chanceIndex =0, communityIndex = 0;
    private boolean eligibleForRollAgain, noRollJail = false;
    private ArrayList<CCard> chance, community;
    private Trade currentTrade;
    
    public GameInstance(Board b) throws IOException {
        properties = new ArrayList<>();
        chance = new ArrayList<>();
        community = new ArrayList<>();
        populatePlayerProperties();
        populateChanceCards();
        populateCommunityCards();
        this.board = b;
    }
    
    public void newGame(ArrayList<Player> players, int startingCash) {
        this.players = choosePlayerRotation(players);
        this.players.get(this.players.size() -1).setCurretTurn(true); //Set it to last player so when we call nextTurn it circles around to the first player
        this.currentDiceRoll = new int[] {-1, -1}; 
        for(Player p : players) p.addCash(1500);
        eligibleForRollAgain = false;
        Collections.shuffle(chance);
        Collections.shuffle(community);
        this.nextTurn();
    }

    public void loadGame(int gameID) {
        
    }
    
    private static ArrayList<Player> choosePlayerRotation(ArrayList<Player>  p) {
        Collections.shuffle(p);
        return p;
    }
    
    public void setEligibleForRollAgain(boolean b) {
        this.eligibleForRollAgain = b;
    }
    
    public Trade getCurrentTrade() {
        return currentTrade;
    }
    
    public void setCurrentTrade(Trade t) {
        this.currentTrade = t;
    }
    
    public Player getNextPlayer() {
        Player lastPlayer = players.get(getCurrentPlayerIndex());
        lastPlayer.endTurn();  
        if(eligibleForRollAgain && !lastPlayer.isJailed()) {
            return lastPlayer;
        } else {
            Player p = players.get(getNextPlayerIndex());;
            lastPlayer.setCurretTurn(false);
            return p;
        }
    }
    
    public void regularTurn(Player p) {
        p.move(getCurrentDiceRollSum(), this);
        board.getTileFromID(p.getCurrentTileID()).action(this, p);
    }
    /**
     * ONLY FOR IF YOU PAY OR USE A CARD DO NOT USE FOR DOUBLES
     */
    public void outOfJailTurn(boolean roll) {
        Player currentPlayer = getCurrentPlayer();
        if(roll) setCurrentDiceRoll(Board.rollDice());
        if (isDiceDouble()) { //If last roll was double
            doubleDiceNum++;
            if (doubleDiceNum == 3) { //Speeding
                currentPlayer.jailPlayer(this);
                nextTurn();//End turn right after you get jailed
                return;
            } else {
                eligibleForRollAgain = true;
            }
        } else {
            doubleDiceNum = 0;
            eligibleForRollAgain = false;
        }
        regularTurn(currentPlayer);
    }
    
    public void nextTurn() {
        Player currentPlayer = getNextPlayer();
        currentPlayer.setCurretTurn(true);
        
        if (!currentPlayer.isJailed()) {
            if(!noRollJail) {
                setCurrentDiceRoll(Board.rollDice());
            } else {
                setNotRollJail(false);
            }
            if (isDiceDouble()) { //If last roll was double
                doubleDiceNum++;
                if (doubleDiceNum == 3) { //Speeding
                    currentPlayer.jailPlayer(this);
                    nextTurn();//End turn right after you get jailed
                    return;
                } else {
                    eligibleForRollAgain = true;
                }
            } else {
                doubleDiceNum = 0;
                eligibleForRollAgain = false;
            }
            regularTurn(currentPlayer);
        } else {
            currentPlayer.addJailTurn();
        }
    }
    
    private int getNextPlayerIndex() {
        for(int index = getCurrentPlayerIndex()+1; index < players.size(); index++) {
            if(!players.get(index).isBankrupt()) return index;
        }
        return getFirstNotBankruptPlayerIndex();
    }
    
    private int getFirstNotBankruptPlayerIndex() {
        for(int index = 0; index < players.size(); index++) {
            if(!players.get(index).isBankrupt()) return index;
        }
        return -1; //This means everyone is bankrupt
    }
    
    public int getCurrentPlayerIndex() {
        for(int index = 0; index < players.size(); index++) {
            if(players.get(index).isCurrentTurn()) return index;
        }
        return -1;//No player has current turn? Something is messed up
    }
    
    private boolean isWinner() {
        int isAlive = 0;
        for(Player p : players) {
            if(!p.isBankrupt()) isAlive++;
            if(isAlive <= 2) return false;
        }
        return true;
    }
    
    
    public void setCurrentDiceRoll(int[] a) {
        this.currentDiceRoll = a;
    }
    
    public Board getBoard() {
        return board;
    }
    
    public int[] getCurrentDiceRoll() {return currentDiceRoll;}
    public int getCurrentDiceRollSum() {return currentDiceRoll[0]+currentDiceRoll[1];}
    
    public PlayerProperty getPlayerProperty(int id) {
        if(id < 0 || id > properties.size()-1) return null;
        return properties.get(id);
    }
    
    private void populatePlayerProperties() throws FileNotFoundException , IOException{
         try (BufferedReader br = new BufferedReader(new FileReader( getClass().getClassLoader().getResource("PropertyData.csv").getFile()))) {
            for (String line; (line = br.readLine()) != null;) {
                String[] lineData=  line.split(",");
                properties.add(new PlayerProperty(Integer.parseInt(lineData[0]), Integer.parseInt(lineData[2]), Integer.parseInt(lineData[3]), Integer.parseInt(lineData[4]), Integer.parseInt(lineData[5]), Integer.parseInt(lineData[6]), Integer.parseInt(lineData[7]), Integer.parseInt(lineData[8]), Integer.parseInt(lineData[0]), Integer.parseInt(lineData[10]), Boolean.parseBoolean(lineData[12]), Boolean.parseBoolean(lineData[11])));
            }
         }
    }
    
    private void populateChanceCards() throws FileNotFoundException, IOException {
        try (BufferedReader br = new BufferedReader(new FileReader( getClass().getClassLoader().getResource("ChanceData.csv").getFile()))) {
            for (String line; (line = br.readLine()) != null;) {
                String[] lineData=  line.split(",");
                chance.add(getCardFromLine(lineData));
            }
         }
    }
    
    private void populateCommunityCards()throws FileNotFoundException, IOException {
         try (BufferedReader br = new BufferedReader(new FileReader( getClass().getClassLoader().getResource("CommunityData.csv").getFile()))) {
            for (String line; (line = br.readLine()) != null;) {
                String[] lineData=  line.split(",");
                community.add(getCardFromLine(lineData));
            }
         }
    }
    
    private CCard getCardFromLine(String[] s) {
        int type = Integer.parseInt(s[1]);
        if (type == 0) { //Bank Pays you money
            return new CCard(Integer.parseInt(s[0]), type, s[2], new int[]{Integer.parseInt(s[3])}){

                @Override
                public void action(GameInstance game, Player currentPlayer) {
                    currentPlayer.addCash(getData()[0]);
                }
            };
        } else if (type == 1) {//You pay money to bank
            return new CCard(Integer.parseInt(s[0]), type, s[2], new int[]{Integer.parseInt(s[3])}) {
                
                @Override
                public void action(GameInstance game, Player currentPlayer) {
                    currentPlayer.setDebt(new Debt(currentPlayer.getPlayerID(), null, getData()[0]));
                }
            };
        } else if(type == 2) {//Go to place
            return new CCard(Integer.parseInt(s[0]), type, s[2], new int[]{Integer.parseInt(s[3]), Integer.parseInt(s[4])}) {
                
                @Override
                public void action(GameInstance game, Player currentPlayer) {
                     currentPlayer.moveTo(getData()[0], (getData()[1]==1));
                     game.getBoard().getTileFromID(currentPlayer.getCurrentTileID()).action(game, currentPlayer);
                }
            };
        } else if(type == 3) {//Go back spaces
            return new CCard(Integer.parseInt(s[0]), type, s[2], new int[]{Integer.parseInt(s[3])}) {
                
                @Override
                public void action(GameInstance game, Player currentPlayer) {
                     currentPlayer.moveTo(currentPlayer.getCurrentTileID() - getData()[0], false);
                     game.getBoard().getTileFromID(currentPlayer.getCurrentTileID()).action(game, currentPlayer);
                }
            };
        } else if(type == 4) {//Find nearist utility
            return new CCard(Integer.parseInt(s[0]), type, s[2], new int[]{Integer.parseInt(s[3])}) {
                
                @Override
                public void action(GameInstance game, Player currentPlayer) {
                     currentPlayer.moveTo(findNearestUtility(currentPlayer.getCurrentTileID()), false);
                     game.getBoard().getTileFromID(currentPlayer.getCurrentTileID()).action(game, currentPlayer);//Some how make us pay ten times amount
                }
            };
        } else if(type == 5) {//Find nearist railroad
            return new CCard(Integer.parseInt(s[0]), type, s[2], new int[]{Integer.parseInt(s[3])}) {
                
                @Override
                public void action(GameInstance game, Player currentPlayer) {
                     currentPlayer.moveTo(findNearestRailRoad(currentPlayer.getCurrentTileID()), false);
                     game.getBoard().getTileFromID(currentPlayer.getCurrentTileID()).action(game, currentPlayer);//Some how make us pay two times amount
                }
            };
        } else if (type == 6) {//Pay for each house and hotel
            return new CCard(Integer.parseInt(s[0]), type, s[2], new int[]{Integer.parseInt(s[3]), Integer.parseInt(s[4])}) {
                
                @Override
                public void action(GameInstance game, Player currentPlayer) {
                    int numHouses = 0, numHotels = 0;
                    for(PlayerProperty p : game.properties) {
                        if(p.getOwnerID() != null && currentPlayer.getPlayerID().equals(p.getOwnerID())) {
                            if(p.hasHotel()) numHotels++;
                            else numHouses+=p.getHouses();
                        }
                    }
                    if(numHotels!=0&numHouses!=0) {
                        currentPlayer.setDebt(new Debt(currentPlayer.getPlayerID(), null, (getData()[0]*numHouses)+(getData()[1]*numHotels)));
                    }
                    
                }
            };
        } else if (type == 7) {//Pay each player
            return new CCard(Integer.parseInt(s[0]), type, s[2], new int[]{Integer.parseInt(s[3])}) {
                
                @Override
                public void action(GameInstance game, Player currentPlayer) {
                    throw new UnsupportedOperationException();//Need to rework the debt system
                }
            };
        } else if (type == 8) {//Go to jail
            return new CCard(Integer.parseInt(s[0]), type, s[2], null) {
                
                @Override
                public void action(GameInstance game, Player currentPlayer) {
                    currentPlayer.jailPlayer(game);
                }
            };
        } else if(type ==9) {//get out of jail card
            return new CCard(Integer.parseInt(s[0]), type, s[2], null) {
                
                @Override
                public void action(GameInstance game, Player currentPlayer) {
                    this.setOwner(currentPlayer.getPlayerID());
                }
            };
        } else if (type == 10) {//Each player gives you
            return new CCard(Integer.parseInt(s[0]), type, s[2], new int[]{Integer.parseInt(s[3])}) {
                
                @Override
                public void action(GameInstance game, Player currentPlayer) {
                    throw new UnsupportedOperationException();//Need to rework the debt system...
                }
            };
        }
        return null;
    } 
    
    public Player getCurrentPlayer() {
        return players.get(getCurrentPlayerIndex());
    }
    
    public void setNotRollJail(boolean b) {
        this.noRollJail = b;
    }
    
    public ArrayList<CCard> getChanceCards() {
        return this.chance;
    }
    
    public ArrayList<CCard> getCommunityCards() {
        return this.community;
    }
    
    public Player getPlayerFromID(String id) {
        for(Player p : this.players) {
            if(p.getPlayerID().equals(id)) {
                return p;
            }
        }
        System.err.println("Could not find player in ID look up");
        return null;
    }
    
    public int getPlayerRRNumberForRent(String id) {
        int numOfRR = 0;
        for(PlayerProperty prop : properties) {
            if(prop.isRailroad()) {
                if(prop.getOwnerID()!= null && prop.getOwnerID().equals(id)) numOfRR++;
            }
        }
        return numOfRR;
    }
    
     public int getPlayerUtilNumberForRent(String id) {
        int numOfUtil = 0;
        for(PlayerProperty prop : properties) {
            if(prop.isUtilitie()) {
                if(prop.getOwnerID()!= null && prop.getOwnerID().equals(id)) numOfUtil++;
            }
        }
        return numOfUtil;
    }
     
     public boolean doesPlayerOwnAllColor(String id, PlayerProperty propColor) {
          for(PlayerProperty prop : properties) {
              if(!prop.isRailroad() && !prop.isUtilitie() && prop.getColorID() == propColor.getColorID()) {
                  if(prop.getOwnerID()== null) return false;
                  else if(!prop.getOwnerID().equals(id)) return false;
              }
          }
          return true;
     }
     
     public boolean isDiceDouble() {
        return (currentDiceRoll[0]==currentDiceRoll[1]);
     }
     
     public CCard pullChanceCard() {
         if(chanceIndex > chance.size()-1) {
             chanceIndex = 0;
         }
         if(!chance.get(chanceIndex).isTaken()){
             return chance.get(chanceIndex++);
         } else {
             chanceIndex++;
             return pullChanceCard();
         }
         
     }
     
     public CCard pullCommunityCard() {
         if(communityIndex > community.size()-1) {
             communityIndex = 0;
         }
         if(!community.get(communityIndex).isTaken()){
             return community.get(communityIndex++);
         } else {
             communityIndex++;
             return pullChanceCard();
         }
     }
     
     public int findNearestUtility(int currentPos) {
         int min = 100, minID = -1;
         for(PlayerProperty prop : properties) {
             if(prop.isUtilitie()) {
                 for(Tile t : this.getBoard().getTiles()) {
                     if(t.getPropertyID() == prop.getID()) {
                         if(t.getID() < currentPos) {//We passed the util
                             if(currentPos-t.getID() < min && currentPos-t.getID() < 20) {
                                 min = currentPos-t.getID();
                                 minID = t.getID();
                             } else if(currentPos-t.getID() > 20 && 40 - (currentPos-t.getID()) < min) {
                                 min = 40 - (currentPos-t.getID());
                                 minID = t.getID();
                             }
                         } else { //Ahead of us
                             if(t.getID()-currentPos < 20 && t.getID()-currentPos < min) {
                                 min = t.getID()-currentPos;
                                 minID = t.getID();
                             } else if(t.getID()-currentPos > 20 && 40-(t.getID()-currentPos) < min) {
                                 min = 40-(t.getID()-currentPos);
                                 minID = t.getID();
                             }
                         }
                         break;
                     }
                 }
             }
         }
         return minID;
     }
     
     public int findNearestRailRoad(int currentPos) {
         int min = 100, minID = -1;
         for(PlayerProperty prop : properties) {
             if(prop.isRailroad()) {
                 for(Tile t : this.getBoard().getTiles()) {
                     if(t.getPropertyID() == prop.getID()) {
                         if(t.getID() < currentPos) {//We passed the util
                             if(currentPos-t.getID() < min && currentPos-t.getID() < 20) {
                                 min = currentPos-t.getID();
                                 minID = t.getID();
                             } else if(currentPos-t.getID() > 20 && 40 - (currentPos-t.getID()) < min) {
                                 min = 40 - (currentPos-t.getID());
                                 minID = t.getID();
                             }
                         } else { //Ahead of us
                             if(t.getID()-currentPos < 20 && t.getID()-currentPos < min) {
                                 min = t.getID()-currentPos;
                                 minID = t.getID();
                             } else if(t.getID()-currentPos > 20 && 40-(t.getID()-currentPos) < min) {
                                 min = 40-(t.getID()-currentPos);
                                 minID = t.getID();
                             }
                         }
                         break;
                     }
                 }
             }
         }
         return minID;
     }
     
     public boolean canPutHouse(PlayerProperty propColor) {
         if(propColor.getHouses() == 4 || numberOfHouses() > 31) return false;//Can't have more than 4 houses
         if(propColor.isMortgaged()) return false;
         for(PlayerProperty p: properties) {
             if(p.getColorID() == propColor.getColorID() && propColor.getID() != p.getID()) {
                 if(Math.abs((propColor.getHouses()+1)-p.getHouses()) > 1 || p.isMortgaged()) {
                     return false;
                 }
             }
         }
         return true;
     }
     
      public boolean canPutHotel(PlayerProperty propColor) {
         if(propColor.getHouses() < 4 || propColor.hasHotel() || numberOfHotels() > 11) return false;
         for(PlayerProperty p: properties) {
             if(p.getColorID() == propColor.getColorID() && propColor.getID() != p.getID()) {
                 if(!p.hasHotel() && p.getHouses() < 4) return false;
             }
         }
         return true;
     }
      
      public int numberOfHouses() {
          int houseSum = 0;
          for(PlayerProperty p : properties) {
              if(p.getOwnerID()!=null) houseSum+=p.getHouses();
          }
          return houseSum;
      }
      
       public int numberOfHotels() {
          int hotelSum = 0;
          for(PlayerProperty p : properties) {
              if(p.getOwnerID()!=null && p.hasHotel()) hotelSum++;
          }
          return hotelSum;
      }
      
      public boolean doesPlayerExsist(String s) {
        if (players.stream().anyMatch((p) -> (p.getPlayerID().equals(s)))) {
            return true;
        }
          return false;
      }
      
      public static boolean isPropTradable(PlayerProperty prop, String owner, GameInstance game) {
          if(prop != null && prop.getOwnerID()!= null) {
              if(!prop.getOwnerID().equals(owner)) return false;
              for(PlayerProperty p : game.properties) {
                  if(p.getColorID()==prop.getColorID() &&(p.getHouses() > 0 || p.hasHotel())) return false;
              }
              return true;
          } else return false;
      }
      
      public ArrayList<PlayerProperty> getPlayerProperties() {
          return this.properties;
      }
}

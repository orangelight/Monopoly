package com.orangelight.monopolyserver.Main.Game.Board;


import com.orangelight.monopolyserver.Main.Game.Board.Tiles.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Alex
 */
public class Board {
    private Tile[] tiles;
    
    public Board() throws IOException {
        tiles = new Tile[40];
        populateTiles();
        
    }
    /**
     * Constructs the tiles array from a config file
     */
    private void populateTiles() throws FileNotFoundException, IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(getClass().getClassLoader().getResource("BoardData.csv").getFile()))) {
            int index = 0;
            for (String line; (line = br.readLine()) != null;) {
                String[] lineData=  line.split(",");
                //The corners that do nothing
                if(Integer.parseInt(lineData[0])== 0 || Integer.parseInt(lineData[0])== 10 || Integer.parseInt(lineData[0])== 20) {
                    tiles[index++] = (new CornerTile(Integer.parseInt(lineData[0]), lineData[1], -1, false, false, false));
                } else if(Integer.parseInt(lineData[0])== 30) { //Go to jail
                    tiles[index++] = (new GoToJail(Integer.parseInt(lineData[0]), lineData[1], -1, false, false, false));
                } else if(Boolean.parseBoolean(lineData[3])) { //Community
                    tiles[index++] = (new CardTile(Integer.parseInt(lineData[0]), lineData[1], -1, false, false, true));
                } else if(Boolean.parseBoolean(lineData[4])) { //Chance
                    tiles[index++] = (new CardTile(Integer.parseInt(lineData[0]), lineData[1], -1, false, true, false));
                } else if(Boolean.parseBoolean(lineData[5])) { //Tax
                    tiles[index++] = (new TaxTile(Integer.parseInt(lineData[0]), lineData[1], -1, true, false, false));   
                } else if(Integer.parseInt(lineData[2]) != -1) {
                    tiles[index++] = (new PropertyTile(Integer.parseInt(lineData[0]), lineData[1],Integer.parseInt(lineData[2]), false, false, false));
                } else {
                    System.err.println("Error reading board csv");
                }
            }  
        }
    }
    
    /**
     * Roll the Dice
     * @return byte array of two random numbers (1-6) to repersent dice
     */
    public static int[] rollDice() {
        return new int[] { ((int)(Math.random()*6))+1, ((int)(Math.random()*6))+1};
    }
    
    public static boolean areDoubles(int[] dice) {
        return (dice[0]==dice[1]);
    }
    
    public Tile getTileFromID(int id) {
        return tiles[id];
    }
    
    public Tile[] getTiles() {return tiles;}
}

package Main;

import Main.Tiles.Tile;
import java.util.ArrayList;

/**
 *
 * @author Alex
 */
public class Board {
    private ArrayList<Tile> tiles;
    
    public Board() {
        tiles = new ArrayList<>();
        populateTiles();
        
    }
    /**
     * Constructs the tiles array from a config file
     */
    private void populateTiles() {
        
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
        return tiles.get(id);
    }
}

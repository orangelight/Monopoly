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
}

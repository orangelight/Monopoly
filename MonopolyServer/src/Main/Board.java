package Main;

import Main.Tiles.Tile;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Alex
 */
public class Board {
    private ArrayList<Tile> tiles;
    
    public Board() throws IOException {
        tiles = new ArrayList<>();
        populateTiles();
        
    }
    /**
     * Constructs the tiles array from a config file
     */
    private void populateTiles() throws FileNotFoundException, IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Alex\\Downloads\\MonopData.csv"))) {
            for (String line; (line = br.readLine()) != null;) {
                String[] lineData=  line.split(",");
                //The corners that do nothing
                if(Integer.parseInt(lineData[0])== 0 || Integer.parseInt(lineData[0])== 10 || Integer.parseInt(lineData[0])== 20) {
                    tiles.add(new Tile(Integer.parseInt(lineData[0]), lineData[1]) {

                        @Override
                        public void action(GameInstance game, Player currentPlayer) {
                            
                        }
                    });
                } else if(Integer.parseInt(lineData[0])== 30) { //Jail
                    tiles.add(new Tile(Integer.parseInt(lineData[0]), lineData[1]) {

                        @Override
                        public void action(GameInstance game, Player currentPlayer) {
                            
                        }
                    });
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
        return tiles.get(id);
    }
}

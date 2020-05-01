//This is a singleton class and sets up the
//background canvas for the game
package code;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class CaveMap {
	//setting it up as a singleton
	private CaveMap() { }
	
	private static CaveMap CaveMapInstance;
	public static CaveMap getCaveMap() {
		try{
            CaveMapInstance = new CaveMap();
        }catch (Exception e) {
            throw new RuntimeException("Exception occured in creating singleton instance");
        }
		return CaveMapInstance;
	}
	
	private final int tileSize = 20;
    private final int numTilesHoriz = 300;
    private final int numTilesVert = 300;
    private final int numFilledTiles = numTilesHoriz * numTilesVert / 2;
    private final int totalTiles = numTilesHoriz * numTilesVert; 
    
    public int getTileSize() { return tileSize; }
    
	public int getNumTilesHoriz() { return numTilesHoriz; }
    public int getNumTilesVert() { return numTilesVert; }
    public int getNumFilledTiles() { return numFilledTiles; }
    public int getTotalTiles() { return totalTiles; }

    //This method returns a Pane after creating the
    //background, coloring it and filling the tiles
    public Pane createBackground() {

        List<Integer> filledTiles = sampleWithoutReplacement(numFilledTiles, numTilesHoriz * numTilesVert);

        Canvas canvas = new Canvas(numTilesHoriz * tileSize, numTilesVert * tileSize);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.DIMGRAY);

        Pane pane = new Pane(canvas);

        pane.setMinSize(numTilesHoriz * tileSize, numTilesVert * tileSize);
        pane.setPrefSize(numTilesHoriz * tileSize, numTilesVert * tileSize);
        pane.setMaxSize(numTilesHoriz * tileSize, numTilesVert * tileSize);
        
        List<Integer> totalTilesList = sampleWithoutReplacement(totalTiles, numTilesHoriz * numTilesVert);

        //coloring the tiles
        gc.setFill(Color.DIMGRAY);
        for (Integer tile: totalTilesList) {
            int x = (tile % numTilesHoriz) * tileSize ;
            int y = (tile / numTilesHoriz) * tileSize ;
            gc.fillRect(x, y, tileSize, tileSize);
        }
        //filling them with a set color: grey
        gc.setFill(Color.rgb(130, 130, 130));
        for (Integer tile : filledTiles) {
            int x = (tile % numTilesHoriz) * tileSize ;
            int y = (tile / numTilesHoriz) * tileSize ;
            gc.fillRect(x, y, tileSize/7, tileSize/7);
        }

        return pane ;
    }
    
    private List<Integer> sampleWithoutReplacement(int sampleSize, int populationSize) {
        Random rng = new Random();
        List<Integer> population = new ArrayList<>();
        for (int i = 0 ; i < populationSize; i++) 
            population.add(i);
        List<Integer> sample = new ArrayList<>();
        for (int i = 0 ; i < sampleSize ; i++) 
            sample.add(population.remove(rng.nextInt(population.size())));
        return sample;
    }
}

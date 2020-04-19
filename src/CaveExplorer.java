import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class CaveExplorer extends Application {
	// Set tile size and the horizontal and vertical size
    private final int tileSize = 20;
    private final int numTilesHoriz = 500;
    private final int numTilesVert = 500;
    private final int numFilledTiles = numTilesHoriz * numTilesVert / 2;
    private final int totalTiles = numTilesHoriz * numTilesVert; 
    
    Pane pane = createBackground();
    Rectangle baseRect = new Rectangle(numTilesHoriz*tileSize/2, numTilesVert*tileSize/2, 20, 20);
    Player player = new Player(20, 20, baseRect);
    
    Enemy enemy = new Enemy(tileSize);
    Thread enemyThread;
    

    @Override
    public void start(Stage primaryStage) {
    	// Create pane 
        // Create player 
        pane.getChildren().add(player.playerRect);
		player.addObserver(enemy);
		player.Timer(pane);
        // Create scene
        Scene scene = new Scene(new BorderPane(pane), 800, 800);
        // Set clip for scene
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(scene.widthProperty());
        clip.heightProperty().bind(scene.heightProperty());

        clip.xProperty().bind(Bindings.createDoubleBinding(
                () -> clampRange(player.playerRect.getX() - scene.getWidth() / 2, 0, pane.getWidth() - scene.getWidth()), 
                player.playerRect.xProperty(), scene.widthProperty()));
        clip.yProperty().bind(Bindings.createDoubleBinding(
                () -> clampRange(player.playerRect.getY() - scene.getHeight() / 2, 0, pane.getHeight() - scene.getHeight()), 
                player.playerRect.yProperty(), scene.heightProperty()));

        pane.setClip(clip);
        pane.translateXProperty().bind(clip.xProperty().multiply(-1));
        pane.translateYProperty().bind(clip.yProperty().multiply(-1));
		enemy.addToPane(pane);
        
        // Player movement
        scene.setOnKeyPressed(e -> player.processKey(e.getCode(), true));
        scene.setOnKeyReleased(e -> player.processKey(e.getCode(), false));

        
        


        primaryStage.setScene(scene);
        primaryStage.show();
        
		enemyThread = new Thread(enemy);
		enemyThread.start();

        player.playerTimer.start();
    }
    
	@SuppressWarnings("deprecation")
	@Override
	public void stop(){
		enemyThread.stop();
	}


    private Pane createBackground() {

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
        
        gc.setFill(Color.DIMGRAY);
        for (Integer tile: totalTilesList) {
            int x = (tile % numTilesHoriz) * tileSize ;
            int y = (tile / numTilesHoriz) * tileSize ;
            gc.fillRect(x, y, tileSize, tileSize);
        }
        gc.setFill(Color.rgb(130, 130, 130));
        for (Integer tile : filledTiles) {
            int x = (tile % numTilesHoriz) * tileSize ;
            int y = (tile / numTilesHoriz) * tileSize ;
            gc.fillRect(x, y, tileSize/7, tileSize/7);
        }

        return pane ;
    }

    private double clampRange(double value, double min, double max) {
        if (value < min) return min ;
        if (value > max) return max ;
        return value ;
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

    private void shoot(Player who)
    {
    	Rectangle bullet = new Rectangle(who.playerRect.getTranslateX() + 20, who.playerRect.getTranslateY(), 5, 20);
    	bullet.setFill(Color.BLACK);
    	pane.getChildren().add(bullet);
    }
    public static void main(String[] args) {
        launch(args);
    }
}
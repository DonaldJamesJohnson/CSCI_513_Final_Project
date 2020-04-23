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
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class CaveExplorer extends Application {
	//getting instance of the CaveMap
    CaveMap caveMap = CaveMap.getCaveMap();

    //setting up the factories
    PowerUpFactory powerUpFactory = new PowerUpFactory();//caveMap.getNumTilesHoriz(), caveMap.getTileSize());
    EnemyFactory enemyFactory = new EnemyFactory();

	// Set tile size and the horizontal and vertical size
    Pane pane = caveMap.createBackground();
    Rectangle baseRect = new Rectangle(
    		caveMap.getNumTilesHoriz() * caveMap.getTileSize() / 2, 
    		caveMap.getNumTilesVert() * caveMap.getTileSize() / 2,
    		20, 
    		20 );

    Rectangle speedRect = new Rectangle(
            caveMap.getNumTilesHoriz() * caveMap.getTileSize() / 2,
            caveMap.getNumTilesVert() * caveMap.getTileSize() / 2,
            20,
            20 );

    Player player = new Player(20, 20, baseRect);
    
    Enemy enemy = enemyFactory.getEnemy(caveMap.getTileSize());
    Enemy enemy2 = enemyFactory.getEnemy(caveMap.getTileSize());

    PowerUp power1 = powerUpFactory.getPowerUp("SpeedBoost", 100, 100, speedRect);
    

    @Override
    public void start(Stage primaryStage) {
    	// Create pane 
        // Create player 
        pane.getChildren().add(player.playerRect);
        pane.getChildren().add(power1.getPowerUpShape());
		player.addObserver(enemy);
		player.addObserver(enemy2);
		player.Timer(pane);
		enemy.Timer(pane);
		enemy2.Timer(pane);
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
		enemy2.addToPane(pane);
        
        // Player movement
        scene.setOnKeyPressed(e -> player.processKey(e.getCode(), true));
        scene.setOnKeyReleased(e -> player.processKey(e.getCode(), false));

        primaryStage.setScene(scene);
        primaryStage.show();

        player.playerTimer.start();
        enemy.enemyTimer.start();
        enemy2.enemyTimer.start();
    }

    private double clampRange(double value, double min, double max) {
        if (value < min) return min ;
        if (value > max) return max ;
        return value ;
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
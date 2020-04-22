import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class CaveExplorer extends Application {
	// Set tile size and the horizontal and vertical size
    CaveMap caveMap = new CaveMap();
	
    Pane pane = caveMap.createBackground();
    Rectangle baseRect = new Rectangle(
    		caveMap.getNumTilesHoriz() * caveMap.getTileSize() / 2, 
    		caveMap.getNumTilesVert() * caveMap.getTileSize() / 2,
    		20, 
    		20 );
    
    Player player = new Player(20, 20, baseRect);
    
    Enemy enemy = new Enemy(caveMap.getTileSize());
    List<Bullet> bullets = new ArrayList<Bullet>();
    
    public void start(Stage primaryStage) {
    	// Create pane 
        // Create player 
        pane.getChildren().add(player.playerRect);
		player.addObserver(enemy);
		//player.addObserver(enemy2);
		//enemy.Timer(pane);
		//enemy2.Timer(pane);
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
		pane.getChildren().add(enemy.enemySprite.circle);
		//enemy2.addToPane(pane);
		
		AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0 ;
            @Override
            public void handle(long now) {
                long elapsedNanos = now - lastUpdate ;
                if (lastUpdate < 0) {
                    lastUpdate = now ;
                    return ;
                }
                double elapsedSeconds = elapsedNanos / 1_000_000_000.0 ;
                double deltaX = 0 ;
                double deltaY = 0 ;
                double shootDX = 0;
                double shootDY = 0;
                if (player.right) deltaX += player.speed ;
                if (player.left) deltaX -= player.speed ;
                if (player.down) deltaY += player.speed ;
                if (player.up) deltaY -= player.speed ;
                player.playerRect.setX(clampRange(player.playerRect.getX() + deltaX * elapsedSeconds, 0, pane.getWidth() - player.playerRect.getWidth()));
                player.playerRect.setY(clampRange(player.playerRect.getY() + deltaY * elapsedSeconds, 0, pane.getHeight() - player.playerRect.getHeight()));
    	    	
                enemy.enemySprite.setX(clampRange(enemy.enemySprite.circle.getCenterX() + (enemy.xMove * elapsedSeconds), 0, pane.getWidth() - enemy.enemySprite.circle.getRadius()));
                enemy.enemySprite.setY(clampRange(enemy.enemySprite.circle.getCenterY() + (enemy.yMove * elapsedSeconds), 0, pane.getHeight() - enemy.enemySprite.circle.getRadius()));
                enemy.move();
                
                if (!bullets.isEmpty())
                {
                    for(Bullet b : bullets) {
                    	if (b.direction == "up") b.moveUp();
                    	if (b.direction == "down") b.moveDown();
                    	if (b.direction == "left") b.moveLeft();
                    	if (b.direction == "right") b.moveRight();
                    	if (b.getBoundsInParent().intersects(enemy.enemySprite.circle.getBoundsInParent())) pane.getChildren().remove(enemy.enemySprite.circle);
                    }
                }
                lastUpdate = now ;
            }
        };
        
        // Player movement
        scene.setOnKeyPressed(e -> processKey(e.getCode(), true));
        scene.setOnKeyReleased(e -> processKey(e.getCode(), false));

        primaryStage.setScene(scene);
        primaryStage.show();
        
        timer.start();
       // player.playerTimer.start();
       // enemy.enemyTimer.start();
       // enemy2.enemyTimer.start();
    }


    
    public void processKey(KeyCode code, boolean on) {
        switch (code) {
        case A:
        	player.move();
            player.left = on ;
            break ;
        case D: 
    		player.move();
    		player.right = on ;
            break ;
        case W:
    		player.move();
    		player.up = on ;
            break ;
        case S:
    		player.move();
    		player.down = on ;
            break ;

        case UP: 
        	shoot("up");
        	break;
        case LEFT:
        	shoot("left");
        	break;
        case RIGHT: 
        	shoot("right");
        	break;
        case DOWN:
        	shoot("down");
        	break;
        	
        default:
            break ;
        }
    }


    private double clampRange(double value, double min, double max) {
        if (value < min) return min ;
        if (value > max) return max ;
        return value ;
    }
    public static void main(String[] args) {
        launch(args);
    }
    
    
    private void shoot(String direction)
    {
    	Bullet b = new Bullet(player.getPlayerLocationX() + 10, player.getPlayerLocationY() + 10, 5, 5, direction, Color.BLACK);
    	bullets.add(b);
    	pane.getChildren().add(b);
    }
    
    
    
    private static class Bullet extends Rectangle {
    	final String direction;
    	boolean dead = false;
    	
    	Bullet(double x, double y, double w, double h, String dir, Color color)
    	{
    		super(w, h, color);
    		this.direction = dir;
    		setTranslateX(x);
    		setTranslateY(y);    	
    	}
    	
		void moveLeft()
		{
			setTranslateX(getTranslateX() - 1);
		}
		
		void moveRight()
		{
			setTranslateX(getTranslateX() + 1);
		}
		
		void moveUp()
		{
			setTranslateY(getTranslateY() - 1);
		}
		
		void moveDown()
		{
			setTranslateY(getTranslateY() + 1);
		}
		
    }
}
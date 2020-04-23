import java.util.ArrayList;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
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

    Player player = new Player(2000, 2000, baseRect, 10000);
    

    Enemy enemy = enemyFactory.getEnemy(caveMap.getTileSize(), 300);
    Enemy enemy2 = enemyFactory.getEnemy(caveMap.getTileSize(), 300);

    PowerUp power1 = powerUpFactory.getPowerUp("SpeedBoost", 100, 100, speedRect);


    List<Bullet> bullets = new ArrayList<Bullet>();    
    public void start(Stage primaryStage) {
    	// Create pane 
        // Create player 
        pane.getChildren().add(player.playerRect);
        pane.getChildren().add(power1.getPowerUpShape());
		player.addObserver(enemy);
		player.setWeaponBehavior(new BaseWeapon());
        // Create scene
        Scene scene = new Scene(new BorderPane(pane), 800, 800);
        // Set clip for scene
        setClip(scene);
		
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
                if (player.right) deltaX += player.speed ;
                if (player.left) deltaX -= player.speed ;
                if (player.down) deltaY += player.speed ;
                if (player.up) deltaY -= player.speed ;
                player.playerRect.setX(clampRange(player.playerRect.getX() + deltaX * elapsedSeconds, 0, pane.getWidth() - player.playerRect.getWidth()));
                player.playerRect.setY(clampRange(player.playerRect.getY() + deltaY * elapsedSeconds, 0, pane.getHeight() - player.playerRect.getHeight()));
    	    	
                enemy.enemySprite.setX(clampRange(enemy.enemySprite.circle.getCenterX() + (enemy.xMove * elapsedSeconds), 0, pane.getWidth() - enemy.enemySprite.circle.getRadius()));
                enemy.enemySprite.setY(clampRange(enemy.enemySprite.circle.getCenterY() + (enemy.yMove * elapsedSeconds), 0, pane.getHeight() - enemy.enemySprite.circle.getRadius()));
                enemy.move();
                
                if (player.playerRect.getBoundsInParent().intersects(enemy.enemySprite.circle.getBoundsInParent())) player.setHealth(-1);
                
                if (!bullets.isEmpty())
                {
                    for(Bullet b : bullets) {
                    	if (b.yDirection == -1) b.moveUp();
                    	if (b.yDirection == 1) b.moveDown();
                    	if (b.xDirection == -2) b.moveLeft();
                    	if (b.xDirection == 2) b.moveRight();
                    	if (b.getBoundsInParent().intersects(enemy.enemySprite.circle.getBoundsInParent())) 
                    	{
                    		pane.getChildren().remove(b);
                    		enemy.setHealth(-1);
                    	}
                    	if (enemy.currentHealth == 0) pane.getChildren().remove(enemy.enemySprite.circle);
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
        
        player.setHealth(-2);
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
        	shooting(0, -1);
        	break;
        case DOWN:
        	shooting(0, 1);
        	break;
        case LEFT:
        	shooting(-2, 0);
        	break;
        case RIGHT: 
        	shooting(2, 0);
        	break;
        	
        default:
            break ;
        }
    }
    
    private void shooting(int xdirection, int ydirection)
    {
    	List<Bullet> b = new ArrayList<Bullet>();
    	b = player.performShoot(xdirection, ydirection);
    	for (Bullet bullet : b) 
    	{
    		bullets.add(bullet);
        	pane.getChildren().add(bullet);
    	}
    }


    private double clampRange(double value, double min, double max) {
        if (value < min) return min ;
        if (value > max) return max ;
        return value ;
    }
    
    private void setClip(Scene scene)
    {
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
    }
    public static void main(String[] args) {
        launch(args);
    }
}
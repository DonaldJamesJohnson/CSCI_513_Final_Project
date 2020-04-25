import java.util.ArrayList;
import java.util.List;

import com.sun.webkit.Timer;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.Random;


public class CaveExplorer extends Application {
	//getting instance of the CaveMap
    CaveMap caveMap = CaveMap.getCaveMap();

    //setting up the factories
    PowerUpFactory powerUpFactory = new PowerUpFactory();
    EnemyFactory enemyFactory = new EnemyFactory();
	Random rand = new Random();

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

    Rectangle healthRect = new Rectangle(
            caveMap.getNumTilesHoriz() * caveMap.getTileSize() / 2,
            caveMap.getNumTilesVert() * caveMap.getTileSize() / 2,
            20,
            20 );

    Rectangle weaponRect = new Rectangle(
            caveMap.getNumTilesHoriz() * caveMap.getTileSize() / 2,
            caveMap.getNumTilesVert() * caveMap.getTileSize() / 2,
            20,
            20 );

    Player player = new Player(2000, 2000, baseRect, 100);

    PowerUp power1 = powerUpFactory.getPowerUp("SpeedBoost", 1800, 1900, speedRect);

    PowerUp powerhp = powerUpFactory.getPowerUp("HealthBoost", 1800, 1900, healthRect);

    List<Bullet> bullets = new ArrayList<Bullet>();
    List<Enemy> enemies = new ArrayList<Enemy>();
    int totalEnemies;
    Label scoreLabel = new Label();
    Label healthLabel = new Label();
    Font font = new Font ("Monospace", 15);

    private double speedTimer;
    private double weaponTimer;
    private boolean poweredUp = false;
    private boolean gunsUp = false;
    private boolean isPowerUpAvailable = false;
    private double powerUpTimer;

    public void start(Stage primaryStage) {
    	// Create pane 
        // Create player 
        pane.getChildren().add(player.playerRect);
		player.setWeaponBehavior(new BaseWeapon());
		createEnemies(2);
		totalEnemies = enemies.size();
        // Create scene
        Scene scene = new Scene(new BorderPane(pane), 800, 800);
        // Set clip for scene
        setClip(scene);
        powerUpTimer = System.currentTimeMillis() / 1000;
		
		AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0 ;
            @Override
            public void handle(long now) {
                long elapsedNanos = now - lastUpdate ;
                if (lastUpdate < 0) {
                    lastUpdate = now ;
                    return ;
                }
                double elapsedSeconds = elapsedNanos / 1_000_000_000.0;
                double deltaX = 0 ;
                double deltaY = 0 ;
                if (player.right) deltaX += player.speed ;
                if (player.left) deltaX -= player.speed ;
                if (player.down) deltaY += player.speed ;
                if (player.up) deltaY -= player.speed ;
                player.playerRect.setX(clampRange(player.playerRect.getX() + deltaX * elapsedSeconds, 0, pane.getWidth() - player.playerRect.getWidth()));
                player.playerRect.setY(clampRange(player.playerRect.getY() + deltaY * elapsedSeconds, 0, pane.getHeight() - player.playerRect.getHeight()));

                //adding powerups after some time
                if (powerUpTimer + 7 < (System.currentTimeMillis() / 1000)) {
                    if (!isPowerUpAvailable) {
                        int type = rand.nextInt(3);
                        if (type == 0) {
                            createPowerUps(player.getPlayerLocationX(), player.getPlayerLocationY(), power1);
                        } else if (type == 1) {
                            createPowerUps(player.getPlayerLocationX(), player.getPlayerLocationY(), powerhp);
                        } else {
                            createPowerUps(player.getPlayerLocationX(), player.getPlayerLocationY(), weaponRect);
                        }
                        isPowerUpAvailable = true;
                    }
                }

                if (!enemies.isEmpty())
                {
                	for (Enemy e: enemies)
                	{
                        e.enemySprite.setX(clampRange(e.enemySprite.circle.getCenterX() + (e.xMove * elapsedSeconds), 0, pane.getWidth() - e.enemySprite.circle.getRadius()));
                        e.enemySprite.setY(clampRange(e.enemySprite.circle.getCenterY() + (e.yMove * elapsedSeconds), 0, pane.getHeight() - e.enemySprite.circle.getRadius()));
                        e.move();	
                        if (player.playerRect.getBoundsInParent().intersects(e.enemySprite.circle.getBoundsInParent()) && !e.dead)
                        {
                        	player.setHealth(-1);
                        	healthLabel.setText("Health: " + player.currentHealth);
                        }
                	}	
                }

                if(weaponRect != null) {
                    if(player.playerRect.getBoundsInParent().intersects(weaponRect.getBoundsInParent())) {
                        int wRand = rand.nextInt(2);
                        if (wRand == 0)
                            player.setWeaponBehavior(new MultiShotWeapon());
                        else
                            player.setWeaponBehavior(new AllAroundShotWeapon());
                        weaponTimer = System.currentTimeMillis() / 1000;
                        isPowerUpAvailable = false;
                        gunsUp = true;
                        powerUpTimer = System.currentTimeMillis() / 1000;
                        pane.getChildren().remove(weaponRect);
                    }
                }

                if(powerhp != null) {
                    if(player.playerRect.getBoundsInParent().intersects(powerhp.getPowerUpShape().getBoundsInParent())) {
                        player.currentHealth = player.maxHealth;
                        isPowerUpAvailable = false;
                        healthLabel.setText("Health: " + player.currentHealth);s
                        powerUpTimer = System.currentTimeMillis() / 1000;
                        pane.getChildren().remove(powerhp.getPowerUpShape());
                    }
                }

                if (power1 != null) {
                    if(player.playerRect.getBoundsInParent().intersects(power1.getPowerUpShape().getBoundsInParent())) {
                        player.speed = 750;
                        speedTimer = System.currentTimeMillis() / 1000;
                        poweredUp = true;
                        isPowerUpAvailable = false;
                        powerUpTimer = System.currentTimeMillis() / 1000;
                        pane.getChildren().remove(power1.getPowerUpShape());
                    }
                }

                if (poweredUp) {
                    if (speedTimer + 3 < (System.currentTimeMillis() / 1000)) {
                        player.speed = 500;
                        poweredUp = false;
                    }
                }

                if (gunsUp) {
                    if(weaponTimer + 3 < (System.currentTimeMillis() / 1000)) {
                        player.setWeaponBehavior(new BaseWeapon());
                        gunsUp = false;
                    }
                }

                if (!bullets.isEmpty())
                {
                    for(Bullet b : bullets) {
                    	if (b.yDirection == -1) b.moveUp();
                    	if (b.yDirection == 1) b.moveDown();
                    	if (b.xDirection == -2) b.moveLeft();
                    	if (b.xDirection == 2) b.moveRight();
                        if (!enemies.isEmpty())
                        {
                        	for (Enemy e: enemies)
                        	{
                            	if (b.getBoundsInParent().intersects(e.enemySprite.circle.getBoundsInParent()) && !b.dead) 
                            	{
                            		pane.getChildren().remove(b);
                            		e.setHealth(-1);
                            		System.out.println("Enemy health: " + e.currentHealth);
                            		b.dead = true;
                            	}
                            	if (e.currentHealth <= 0 && !e.dead) 
                            		{
                            		pane.getChildren().remove(e.enemySprite.circle);
                            		e.dead = true;
                            		totalEnemies--;
                            		scoreLabel.setText("Enemies: " + totalEnemies);
                            		}
                        	}	
                        }

                    }
                }

                scoreLabel.setTranslateX(pane.getTranslateX() * -1);
                scoreLabel.setTranslateY((pane.getTranslateY() * -1) + 15);
                healthLabel.setTranslateX(pane.getTranslateX() * -1);
                healthLabel.setTranslateY(pane.getTranslateY() * -1);
                lastUpdate = now;
            }
        };
        
        // Player movement
        scene.setOnKeyPressed(e -> processKey(e.getCode(), true));
        scene.setOnKeyReleased(e -> processKey(e.getCode(), false));

        primaryStage.setScene(scene);
        primaryStage.show();
        
        timer.start();
        scoreLabel.setText("Enemies: " + totalEnemies);
        scoreLabel.setFont(font);
        healthLabel.setText("Health: " + player.currentHealth);
        healthLabel.setFont(font);
       
      
        pane.getChildren().add(scoreLabel);
        pane.getChildren().add(healthLabel);
    }

    private void createPowerUps(double playerx, double playery, PowerUp pow)
    {
        int xLimitUp = (int)playerx + 250;
        int xLimitDown = (int)playerx - 250;
        int yLimitUp = (int)playery + 250;
        int yLimitDown = (int)playery - 250;
        int x = rand.nextInt((xLimitUp - xLimitDown) + 1) + xLimitDown;
        int y = rand.nextInt((yLimitUp - yLimitDown) + 1) + yLimitDown;
        pow.setPositionX(x);
        pow.setPositionY(y);
        pane.getChildren().add(pow.getPowerUpShape());
    }

    private void createPowerUps(double playerx, double playery, Rectangle rect)
    {
        rect.setStroke(Color.RED);
        rect.setFill(Color.rgb(200, 0, 0, 1));
        int xLimitUp = (int)playerx + 250;
        int xLimitDown = (int)playerx - 250;
        int yLimitUp = (int)playery + 250;
        int yLimitDown = (int)playery - 250;
        int x = rand.nextInt((xLimitUp - xLimitDown) + 1) + xLimitDown;
        int y = rand.nextInt((yLimitUp - yLimitDown) + 1) + yLimitDown;
        rect.setX(x);
        rect.setY(y);
        pane.getChildren().add(rect);
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
    
    private void createEnemies(int n)
    {
    	for (int i = 0; i < n; i++)
    	{
    		enemies.add(enemyFactory.getEnemy(caveMap.getTileSize(), 3));
    	}
    	for (Enemy e : enemies)
    	{
			int x = rand.nextInt(caveMap.getNumTilesHoriz());
			int y = rand.nextInt(caveMap.getNumTilesVert());
    		e.enemySprite.setPositionX(x);
    		e.enemySprite.setPositionY(y);
    		pane.getChildren().add(e.enemySprite.circle);
    		player.addObserver(e);
    		player.move();
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
    }
    public static void main(String[] args) {
        launch(args);
    }
}
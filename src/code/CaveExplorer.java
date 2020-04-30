/* CaveExplorer.java
 * Donald Johnson
 * 
 * CaveExplorer is the core of this project. It contains the main method, which in turn launches the start method of the application.
 * The start method makes everything visible in the primary stage, and contains the AnimationTimer which contains all important interactions 
 * in the game. 
 */

package code;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
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
    // Set the pane
    Pane pane = caveMap.createBackground();
    // Set the scene
    Scene scene = new Scene(new BorderPane(pane), 800, 800);
    
    AnimationTimer timer;

    Rectangle weaponRect = new Rectangle(
            caveMap.getNumTilesHoriz() * caveMap.getTileSize() / 2,
            caveMap.getNumTilesVert() * caveMap.getTileSize() / 2,
            40,
            40 );
    // Create a new player
    Player player = new Player(caveMap.getNumTilesHoriz() * caveMap.getTileSize() / 2, caveMap.getNumTilesVert() * caveMap.getTileSize() / 2);

    PowerUp powerhp = PowerUpFactory.getPowerUp("HealthBoost", caveMap.getNumTilesHoriz() * caveMap.getTileSize() / 2, caveMap.getNumTilesVert() * caveMap.getTileSize() / 2);
    PowerUp power1 = PowerUpFactory.getPowerUp("SpeedBoost", caveMap.getNumTilesHoriz() * caveMap.getTileSize() / 2, caveMap.getNumTilesVert() * caveMap.getTileSize() / 2);

    // Creating list to store the bullets and enemies currently on screen
    List<Bullet> bullets = new ArrayList<Bullet>();
    List<Enemy> enemies = new ArrayList<Enemy>();
    int totalEnemies;

    private double speedTimer;
    private double weaponTimer;
    private boolean poweredUp = false;
    private boolean gunsUp = false;
    
    // Creating labels for score, health, and game over, as well as the font used by the labels
    Label enemyLabel;
    Label healthLabel;
    Label gameOverLabel;
    Font font;
    
	Random rand = new Random();
	
	// Defines the various states of the game
	public enum State
	{
		MENU,
		GAME,
		END
	};
	
	public State state = State.MENU;
	Menu menu = new Menu(caveMap.getNumTilesHoriz() * caveMap.getTileSize() / 2, caveMap.getNumTilesVert() * caveMap.getTileSize() / 2);
	boolean createMenu = true;
    
    public void start(Stage primaryStage) {
        setClip(scene);            
        primaryStage.setScene(scene);
        primaryStage.show();
    	
        setTotalEnemies(200);
		
		timer = new AnimationTimer() {
            private long lastUpdate = 0 ;
            @Override
            public void handle(long now) {
                long elapsedNanos = now - lastUpdate ;
                if (lastUpdate < 0) {
                    lastUpdate = now ;
                    return;
                }
                double elapsedSeconds = elapsedNanos / 1_000_000_000.0 ;
                if (getState() == State.GAME) update(elapsedSeconds);
                else if (getState() == State.MENU)
                {
                	if (createMenu) createMenu();
                	else {
                    	if (menu.start) 
                    	{
                    		setState(State.GAME);
                    		pane.getChildren().remove(menu.startButton);
                    		pane.getChildren().remove(menu.exitButton);
                    		pane.getChildren().remove(menu.gameLabel);
                            createGame();
                    	}
                    	else if (menu.exit)
                    	{
                    		Platform.exit();
                    		System.exit(0);
                    	}	
                	}
                }

                lastUpdate = now ;
            }
        };
        
        createMenu();


        timer.start();
        

	}
    	
    private void update(double seconds)
    {
    	// Set the deltas for player movement
    	double deltaX = 0 ;
        double deltaY = 0 ;
        if (player.right) deltaX += player.getSpeed() ;
        if (player.left) deltaX -= player.getSpeed() ;
        if (player.down) deltaY += player.getSpeed() ;
        if (player.up) deltaY -= player.getSpeed() ;
        // Ensure that the player is the center of the screen until reaching the clip border
        player.playerRect.setX(clampRange(player.playerRect.getX() + deltaX * seconds, 0, pane.getWidth() - player.playerRect.getWidth()));
        player.playerRect.setY(clampRange(player.playerRect.getY() + deltaY * seconds, 0, pane.getHeight() - player.playerRect.getHeight()));
    	
        // Iterate through the enemies and move them. Also check to see if the enemies are touching the player and apply damage if so
        if (!enemies.isEmpty() && !player.dead)
        {
        	for (Enemy e: enemies)
        	{
                e.enemySprite.setX(clampRange(e.enemySprite.circle.getCenterX() + (e.xMove * seconds), 0, pane.getWidth() - e.enemySprite.circle.getRadius()));
                e.enemySprite.setY(clampRange(e.enemySprite.circle.getCenterY() + (e.yMove * seconds), 0, pane.getHeight() - e.enemySprite.circle.getRadius()));
                e.move();	
                if (player.playerRect.getBoundsInParent().intersects(e.enemySprite.circle.getBoundsInParent()) && !e.dead)
                {
                	player.setHealth(-e.damage);
                	healthLabel.setText("Health: " + getPlayerHealth());
                	if (getPlayerHealth() <= 0) 
                	{
                		player.setSpeed(0);
                		setState(State.MENU);
                		createMenu = true;
                		menu.start = false;
                		endGame();
                	}
                }
        	}	
        }
        // Iterate through enemies and if they touch each other, then one of them adds the other as a child
        if (!enemies.isEmpty())
        {
        	for (Enemy e: enemies)
        	{
                for (Enemy e2: enemies)
                {
                	if (e.innerEnemies.size() < 200)
                	{                        	
                       	if (e.containsEnemy(e2) && e2 != e && !e.innerEnemies.contains(e2)) 
                		{
                		e.addChild(e2);
                		e2.enemySprite.circle.resize(0, 0);
                		}
                	}
                }
        	}	
        }   
        // Iterate through the list of bullets and move them. Also check to see if they are hitting an enemy and deal damage and remove the bullet
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
                    	if (b.getBoundsInParent().intersects(e.enemySprite.circle.getBoundsInParent()) && !b.dead && !e.dead) 
                    	{
                    		pane.getChildren().remove(b);
                    		e.setHealth(-player.damage);
                    		b.dead = true;
                    	}
                    	if (e.currentHealth <= 0 && !e.dead) 
                    		{
                    		pane.getChildren().remove(e.enemySprite.circle);
                    		e.dead = true;
                    		setTotalEnemies(totalEnemies - 1);
                    		enemyLabel.setText("Enemies: " + getTotalEnemies());
                    		}
                    	if (getTotalEnemies() == 0) winGame();
                	}	
                }
            }
        }
        // Handle weapon power up iteracting with player
        if(weaponRect != null) {
        	if(player.playerRect.getBoundsInParent().intersects(weaponRect.getBoundsInParent())) {
                int wRand = rand.nextInt(2);
                if (wRand == 0)
                    player.setWeaponBehavior(new MultiShotWeapon());
                else
                    player.setWeaponBehavior(new AllAroundShotWeapon());
                weaponTimer = System.currentTimeMillis() / 1000;
                gunsUp = true;
                pane.getChildren().remove(weaponRect);
            }
        }
        // Handle health power up interacting with player
        if(powerhp != null) {
            if(player.playerRect.getBoundsInParent().intersects(powerhp.getPowerUpShape().getBoundsInParent())) {
            	player.setHealth(player.maxHealth - getPlayerHealth());
                healthLabel.setText("Health: " + getPlayerHealth());
                pane.getChildren().remove(powerhp.getPowerUpShape());
            }
        }
        // Handle speed power up interacting with player
        if (power1 != null) {
            if(player.playerRect.getBoundsInParent().intersects(power1.getPowerUpShape().getBoundsInParent())) {
                player.setSpeed(750);
                speedTimer = System.currentTimeMillis() / 1000;
                poweredUp = true;
                pane.getChildren().remove(power1.getPowerUpShape());
            }
        }
        // Check speed timer if powered up
        if (poweredUp) {
            if (speedTimer + 10 < (System.currentTimeMillis() / 1000)) {
                player.resetSpeed();
                poweredUp = false;
            }
        }
        // Check gun timer if powered up
        if (gunsUp) {
            if(weaponTimer + 10 < (System.currentTimeMillis() / 1000)) {
                player.setWeaponBehavior(new BaseWeapon());
                gunsUp = false;
            }
        }
        // Move the labels with the pane as it follows the player
        enemyLabel.setTranslateX(pane.getTranslateX() * -1);
        enemyLabel.setTranslateY((pane.getTranslateY() * -1) + 15);
        healthLabel.setTranslateX(pane.getTranslateX() * -1);
        healthLabel.setTranslateY(pane.getTranslateY() * -1);
    }
    // Set up the arrays for bullets and enemies and call the various create functions  
    private void createGame()
    {
        bullets = new ArrayList<Bullet>();
        enemies = new ArrayList<Enemy>();
		createPlayer();
		createEnemies(getTotalEnemies());
        createPowerUps(player.getPlayerLocationX(), player.getPlayerLocationY(), powerhp);
        createPowerUps(player.getPlayerLocationX(), player.getPlayerLocationY(), weaponRect);
        createPowerUps(player.getPlayerLocationX(), player.getPlayerLocationY(), power1);
        createLabels();
    	setClip(scene);
    }
    // Remove the various elements from the screen and prep for a new game
    private void endGame()
    {
    	pane.getChildren().removeAll(bullets);
    	for (Enemy e : enemies)
    	{
    		pane.getChildren().remove(e.enemySprite.circle);
    	}
    	pane.getChildren().remove(player.playerRect);
    	pane.getChildren().remove(power1.getPowerUpShape());
    	pane.getChildren().remove(powerhp.getPowerUpShape());
    	pane.getChildren().remove(weaponRect);
    	pane.getChildren().remove(healthLabel);
    	pane.getChildren().remove(enemyLabel);
    	player = new Player(caveMap.getNumTilesHoriz() * caveMap.getTileSize() / 2, caveMap.getNumTilesVert() * caveMap.getTileSize() / 2);
    	totalEnemies = 200;
    	setClip(scene);
    }
    // Win the game, set the state to END, create the game over label, and stop the animation timer
    private void winGame() 
    {
    	setState(State.END);
    	gameOverLabel = new Label("You Won!");
    	Font gameOverFont = new Font("Monospace", 100);
    	gameOverLabel.setFont(gameOverFont);
    	gameOverLabel.setMinHeight(100);
    	gameOverLabel.setMinWidth(300);
    	gameOverLabel.setTranslateX((player.getPlayerLocationX() - gameOverLabel.getMinWidth() / 2) - 50);
    	gameOverLabel.setTranslateY((player.getPlayerLocationY() - gameOverLabel.getMinHeight() / 2) - 100);
    	timer.stop();
    	for (Bullet b : bullets)
    	{
    		pane.getChildren().remove(b);
    	}
    	pane.getChildren().remove(healthLabel);
    	pane.getChildren().remove(enemyLabel);
    	pane.getChildren().add(gameOverLabel);
    }
    // Create the power ups
    public void createPowerUps(double playerx, double playery, PowerUp pow)
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

    public void createPowerUps(double playerx, double playery, Rectangle rect)
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
    // Process key inputs from the user
    private void processKey(KeyCode code, boolean on) {
    	if (getState() == State.GAME)
    	{    	
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
    }
    // Perform the shooting action
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
    // Set up a new player and allow for user input
    private void createPlayer()
    {
    	player = new Player(caveMap.getNumTilesHoriz() * caveMap.getTileSize() / 2, caveMap.getNumTilesVert() * caveMap.getTileSize() / 2);
        pane.getChildren().add(player.playerRect);
		player.setWeaponBehavior(new BaseWeapon());
        // Player movement
        scene.setOnKeyPressed(e -> processKey(e.getCode(), true));
        scene.setOnKeyReleased(e -> processKey(e.getCode(), false));	
    }
    // Create n enemies and place them randomly on the screen and notify them of the player position
    private void createEnemies(int n)
    {
    	for (int i = 0; i < n; i++)
    	{
    		enemies.add(EnemyFactory.getEnemy(caveMap.getTileSize(), 10));
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
    // Create new labels and add them to the pane
    private void createLabels()
    {
    	enemyLabel = new Label();
        healthLabel = new Label();
        font = new Font ("Monospace", 15);
        enemyLabel.setText("Enemies: " + getTotalEnemies());
        enemyLabel.setFont(font);
        healthLabel.setText("Health: " + getPlayerHealth());
        healthLabel.setFont(font);
        pane.getChildren().add(enemyLabel);
        pane.getChildren().add(healthLabel);
    }
    // Creat the menu and add the menu buttons
    private void createMenu()
    {
    	createMenu = false;
		pane.getChildren().add(menu.startButton);
		pane.getChildren().add(menu.exitButton);
		pane.getChildren().add(menu.gameLabel);	
    }
    // Returns the state of the game
    private State getState()
    {
    	return state;
    }
    // Sets the state of the game
    private void setState(State s)
    {
    	state = s;
    }
    // Get the total enemies that are alive
    private int getTotalEnemies()
    {
    	return totalEnemies;
    }
    // Set the total enemies that are alive
    private void setTotalEnemies(int n)
    {
    	totalEnemies = n;
    }
    // Returns the player's current health
    private int getPlayerHealth()
    {
    	return player.currentHealth;
    }
    // Sets the clip of the scene to be centered on the player until the player reaches the boundary of the map
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
    // Clamps a range of values to a min and max
    private double clampRange(double value, double min, double max) {
        if (value < min) return min ;
        if (value > max) return max ;
        return value ;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
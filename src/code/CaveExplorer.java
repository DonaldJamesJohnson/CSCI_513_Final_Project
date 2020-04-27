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
  
    Pane pane = caveMap.createBackground();
    
    Scene scene = new Scene(new BorderPane(pane), 800, 800);
    
    AnimationTimer timer;

    Rectangle weaponRect = new Rectangle(
            caveMap.getNumTilesHoriz() * caveMap.getTileSize() / 2,
            caveMap.getNumTilesVert() * caveMap.getTileSize() / 2,
            20,
            20 );
    
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
    private boolean isPowerUpAvailable = false;
    private double powerUpTimer;
    
    // Creating labels for score, health, and game over
    Label enemyLabel;
    Label healthLabel;
    Label gameOverLabel;
    Font font;
    
	Random rand = new Random();
	
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
    	
        //pane.getChildren().add(power1.getPowerUpShape());

        powerUpTimer = System.currentTimeMillis() / 1000;

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
    	double deltaX = 0 ;
        double deltaY = 0 ;
        if (player.right) deltaX += player.getSpeed() ;
        if (player.left) deltaX -= player.getSpeed() ;
        if (player.down) deltaY += player.getSpeed() ;
        if (player.up) deltaY -= player.getSpeed() ;
        player.playerRect.setX(clampRange(player.playerRect.getX() + deltaX * seconds, 0, pane.getWidth() - player.playerRect.getWidth()));
        player.playerRect.setY(clampRange(player.playerRect.getY() + deltaY * seconds, 0, pane.getHeight() - player.playerRect.getHeight()));
    	
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
                		System.out.println(getState());
                		createMenu = true;
                		menu.start = false;
                		endGame();
                	}
                }
        	}	
        }
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
                		}
                	}
                }
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
            	player.setHealth(player.maxHealth - getPlayerHealth());
                isPowerUpAvailable = false;
                healthLabel.setText("Health: " + getPlayerHealth());
                powerUpTimer = System.currentTimeMillis() / 1000;
                pane.getChildren().remove(powerhp.getPowerUpShape());
            }
        }

        if (power1 != null) {
            if(player.playerRect.getBoundsInParent().intersects(power1.getPowerUpShape().getBoundsInParent())) {
                player.setSpeed(750);
                speedTimer = System.currentTimeMillis() / 1000;
                poweredUp = true;
                isPowerUpAvailable = false;
                powerUpTimer = System.currentTimeMillis() / 1000;
                pane.getChildren().remove(power1.getPowerUpShape());
            }
        }

        if (poweredUp) {
            if (speedTimer + 10 < (System.currentTimeMillis() / 1000)) {
                player.resetSpeed();
                poweredUp = false;
            }
        }

        if (gunsUp) {
            if(weaponTimer + 10 < (System.currentTimeMillis() / 1000)) {
                player.setWeaponBehavior(new BaseWeapon());
                gunsUp = false;
            }
        }

        enemyLabel.setTranslateX(pane.getTranslateX() * -1);
        enemyLabel.setTranslateY((pane.getTranslateY() * -1) + 15);
        healthLabel.setTranslateX(pane.getTranslateX() * -1);
        healthLabel.setTranslateY(pane.getTranslateY() * -1);
    }
       
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
    	setClip(scene);
    }
    
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
    
    private void createPlayer()
    {
    	player = new Player(caveMap.getNumTilesHoriz() * caveMap.getTileSize() / 2, caveMap.getNumTilesVert() * caveMap.getTileSize() / 2);
        pane.getChildren().add(player.playerRect);
		player.setWeaponBehavior(new BaseWeapon());
        // Player movement
        scene.setOnKeyPressed(e -> processKey(e.getCode(), true));
        scene.setOnKeyReleased(e -> processKey(e.getCode(), false));	
    }
    
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
    
    private void createMenu()
    {
    	createMenu = false;
		pane.getChildren().add(menu.startButton);
		pane.getChildren().add(menu.exitButton);
		pane.getChildren().add(menu.gameLabel);	
    }
    
    private State getState()
    {
    	return state;
    }
    
    private void setState(State s)
    {
    	state = s;
    }
    
    private int getTotalEnemies()
    {
    	return totalEnemies;
    }
    
    private void setTotalEnemies(int n)
    {
    	totalEnemies = n;
    }
    
    private int getPlayerHealth()
    {
    	return player.currentHealth;
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
    
    private double clampRange(double value, double min, double max) {
        if (value < min) return min ;
        if (value > max) return max ;
        return value ;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
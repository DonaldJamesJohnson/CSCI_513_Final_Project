/* File: Player.java
 * 
 * Created by: Donald Johnson
 * 
 * Purpose: The Player class stores and changes the player's location when a prompted by a key event triggered in CaveExplorer.
 * 			Instances of this class are observed by instances of Enemy. 
 */
import java.util.Observable;

import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Player extends Observable
{
    Rectangle playerRect = new Rectangle();
    AnimationTimer playerTimer;
    int speed = 500;
    private boolean up ;
    private boolean down ;
    private boolean left ;
    private boolean right ;
	
	public Player(int X, int Y, Rectangle r) 
	{
		playerRect = r;
        playerRect.setStroke(Color.BLACK);
        playerRect.setFill(Color.rgb(80, 80, 160, 1));
        playerRect.setX(X);
        playerRect.setY(Y);
	}
	
	public double getPlayerLocationX() 
	{
		//System.out.println("x: " + currentLocation.x + " | y: " + currentLocation.y);
		return playerRect.getX();
	}
	
	public double getPlayerLocationY() 
	{
		//System.out.println("x: " + currentLocation.x + " | y: " + currentLocation.y);
		return playerRect.getY();
	}
	
	public void Timer(Pane pane)
	{
		playerTimer = new AnimationTimer() {
            private long lastUpdate = -1 ;
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
                if (right) deltaX += speed ;
                if (left) deltaX -= speed ;
                if (down) deltaY += speed ;
                if (up) deltaY -= speed ;
                playerRect.setX(clampRange(playerRect.getX() + deltaX * elapsedSeconds, 0, pane.getWidth() - playerRect.getWidth()));
                playerRect.setY(clampRange(playerRect.getY() + deltaY * elapsedSeconds, 0, pane.getHeight() - playerRect.getHeight()));
                lastUpdate = now ;
            }
        };
	}
	
	 public void processKey(KeyCode code, boolean on) {
	        switch (code) {
	        case A:
	    		setChanged();
	    		notifyObservers(this);
	            left = on ;
	            break ;
	        case D: 
	    		setChanged();
	    		notifyObservers(this);
	            right = on ;
	            break ;
	        case W:
	    		setChanged();
	    		notifyObservers(this);
	            up = on ;
	            break ;
	        case S:
	    		setChanged();
	    		notifyObservers(this);
	            down = on ;
	            break ;
	            /*
	        case UP: 
	        	shoot(player);
	        case LEFT:
	        	shoot(player);
	        case RIGHT: 
	        	shoot(player);
	        case DOWN:
	        	shoot(player);
	        	*/
	        default:
	            break ;
	        }
	    }
	 
	    private double clampRange(double value, double min, double max) {
	        if (value < min) return min ;
	        if (value > max) return max ;
	        return value ;
	    }
}

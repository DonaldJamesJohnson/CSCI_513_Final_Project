/* File: Player.java
 * 
 * Created by: Donald Johnson
 * 
 * Purpose: The Player class stores and changes the player's location when a prompted by a key event triggered in CaveExplorer.
 * 			Instances of this class are observed by instances of Enemy. 
 */

import java.util.Observable;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Player extends Observable
{
    Rectangle playerRect = new Rectangle();
    int speed = 500;
    int shootSpeed = 100;
    public boolean up ;
    public boolean down ;
    public boolean left ;
    public boolean right ;
    public boolean shoot;
    public boolean shootUp;
    public boolean shootDown;
    public boolean shootLeft;
    public boolean shootRight;
	
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
		return playerRect.getX();
	}
	
	public double getPlayerLocationY() 
	{
		return playerRect.getY();
	}
		
	public void move() {
		setChanged();
		notifyObservers(this);
	}
	

}

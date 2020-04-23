/* File: Player.java
 * 
 * Created by: Donald Johnson
 * 
 * Purpose: The Player class stores and changes the player's location when a prompted by a key event triggered in CaveExplorer.
 * 			Instances of this class are observed by instances of Enemy. 
 */

import java.util.List;
import java.util.Observable;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Player extends Observable
{
    Rectangle playerRect = new Rectangle();
    WeaponBehavior wep;
    int maxHealth;
    int currentHealth;
    boolean dead;
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
	
	public Player(int X, int Y, Rectangle r, int h) 
	{
        maxHealth = h;
        currentHealth = maxHealth;
		playerRect = r;
        playerRect.setStroke(Color.BLACK);
        playerRect.setFill(Color.rgb(80, 80, 160));
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
	
	public void setWeaponBehavior(WeaponBehavior wb)
	{
		wep = wb;
	}
	
	public void setHealth(int diff)
	{
		currentHealth += diff;
		System.out.println(currentHealth);
		setColor();
	}
	
	public void setColor() {
		if (currentHealth <= 0) 
		{
			currentHealth = 0;
			dead = true;
			playerRect.setFill(Color.BLACK);
		}
		else if (currentHealth < maxHealth * 0.3)
		{
			playerRect.setFill(Color.rgb(160, 80, 80));
		}
		else if (currentHealth < maxHealth * 0.6)
		{
			playerRect.setFill(Color.rgb(160, 160, 0));
		}
		else if (currentHealth >= maxHealth * 0.6)
		{
			playerRect.setFill(Color.rgb(80, 80, 160));
		}		
	}
	
	public List<Bullet> performShoot(int xdir, int ydir)
	{
		return wep.shoot(xdir, ydir, getPlayerLocationX() + 10, getPlayerLocationY() + 10);
	}
		
	public void move() {
		setChanged();
		notifyObservers(this);
	}
	

}

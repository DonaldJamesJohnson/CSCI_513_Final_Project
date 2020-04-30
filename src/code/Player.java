/* File: Player.java
 * 
 * Created by: Donald Johnson
 * 
 * Purpose: The Player class stores and changes the player's location when a prompted by a key event triggered in CaveExplorer.
 * 			Instances of this class are observed by instances of Enemy. 
 */
package code;

import java.util.List;
import java.util.Observable;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Player extends Observable
{
    Rectangle playerRect;
    WeaponBehavior wep;
    int maxHealth = 100;
    int currentHealth;
    boolean dead;
    int defaultSpeed = 500;
    int speed;
    int shootSpeed = 1000;
    int damage = 2;
    int[] color = new int[3];
    public boolean up ;
    public boolean down ;
    public boolean left ;
    public boolean right ;
    public boolean shoot;
    public boolean shootUp;
    public boolean shootDown;
    public boolean shootLeft;
    public boolean shootRight;
	
	public Player(int X, int Y) 
	{
		playerRect = new Rectangle(0, 0, 20, 20);
        currentHealth = maxHealth;
        playerRect.setStroke(Color.BLACK);
        playerRect.setFill(Color.rgb(80, 80, 160));
        playerRect.setX(X);
        playerRect.setY(Y);
        speed = defaultSpeed;
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

	public String getWeaponBehavior() {
		return wep.getName();
	}
	
	public void setHealth(int diff)
	{
		currentHealth += diff;
		setColor();
	}
	
	public int getHealth()
	{
		return currentHealth;
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
			color[0] = 160;
			color[1] = 80;
			color[2] = 80;
			playerRect.setFill(Color.rgb(color[0], color[1], color[2]));
		}
		else if (currentHealth < maxHealth * 0.6)
		{
			color[0] = 160;
			color[1] = 160;
			color[2] = 0;
			playerRect.setFill(Color.rgb(color[0], color[1], color[2]));
		}
		else if (currentHealth >= maxHealth * 0.6)
		{
			color[0] = 80;
			color[1] = 80;
			color[2] = 160;
			playerRect.setFill(Color.rgb(color[0], color[1], color[2]));
		}		
	}
	
	public int[] getColor()
	{
		return color;
	}
	
	public void setSpeed(int s)
	{
		speed = s;
	}
	
	public void resetSpeed()
	{
		speed = defaultSpeed;
	}
	
	public int getSpeed() 
	{
		return speed;
	}
	
	public int getDefaultSpeed()
	{
		return defaultSpeed;
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

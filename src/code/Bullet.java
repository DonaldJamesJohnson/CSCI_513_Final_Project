/* Bullet.java
 * Donald Johnson
 * 
 * Bullet is a class that defines the characteristics of the bullets shot by the player, and their movement patterns.
 */

package code;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Bullet extends Rectangle {
    	final int xDirection;
    	final int yDirection;
    	boolean dead = false;
    	
    	Bullet(double x, double y, double w, double h, int xdir, int ydir, Color color)
    	{
    		super(w, h, color);
    		this.xDirection = xdir;
    		this.yDirection = ydir;
    		setTranslateX(x);
    		setTranslateY(y);    	
    	}
    	
		void moveLeft()
		{
			setTranslateX(getTranslateX() - 5);
		}
		
		void moveRight()
		{
			setTranslateX(getTranslateX() + 5);
		}
		
		void moveUp()
		{
			setTranslateY(getTranslateY() - 5);
		}
		
		void moveDown()
		{
			setTranslateY(getTranslateY() + 5);
		}
		
    }
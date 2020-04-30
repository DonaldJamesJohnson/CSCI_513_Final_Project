/* WeaponBehavior.java
 * Donald Johnson
 * 
 * WeaponBehavior is an interface which sets the framework for concrete shooting strategies
 */
package code;
import java.util.List;

public interface WeaponBehavior{
	public List<Bullet> shoot(int xdirection, int ydirection, double x, double y);
	
	public String getName();
}

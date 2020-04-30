/* BaseWeapon.java
 * Donald Johnson
 * 
 * BaseWeapon is a concrete WeaponBehavior for shooting Bullets
 */

package code;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;

public class BaseWeapon implements WeaponBehavior {

    public List<Bullet> shoot(int xdirection, int ydirection, double x, double y)
    {
    	List<Bullet> b = new ArrayList<Bullet>();
    	Bullet b1 = new Bullet(x, y, 5, 5, xdirection, ydirection, Color.BLACK);
    	b.add(b1);
    	return b;
    }

	@Override
	public String getName() {
		return "Base Weapon";
	}
}

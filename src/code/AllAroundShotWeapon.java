/* AllAroundShotWeapon.java
 * Donald Johnson
 * 
 * AllAroundShotWeapon is a concrete WeaponBehavior for shooting Bullets
 */

package code;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;

public class AllAroundShotWeapon implements WeaponBehavior{
	public List<Bullet> shoot(int xdirection, int ydirection, double x, double y) {
    	List<Bullet> b = new ArrayList<Bullet>();

        Bullet b1 = new Bullet(x, y, 5, 5, 0, -1, Color.BLACK);	// Shoot up
    	Bullet b2 = new Bullet(x, y, 5, 5, 0, 1, Color.BLACK); // Shoot down
        Bullet b3 = new Bullet(x, y, 5, 5, 2, 0, Color.BLACK); // Shoot right
        Bullet b4 = new Bullet(x, y, 5, 5, -2, 0, Color.BLACK); // Shoot left

    	b.add(b1);
    	b.add(b2);
    	b.add(b3);
    	b.add(b4);
    	return b;
	}

	@Override
	public String getName() {
		return "All Around Shot Weapon";
	}
}

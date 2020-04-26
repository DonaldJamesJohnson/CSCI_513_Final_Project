package code;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;

public class MultiShotWeapon implements WeaponBehavior{

	public List<Bullet> shoot(int xdirection, int ydirection, double x, double y) {
    	List<Bullet> b = new ArrayList<Bullet>();
    	Bullet b1 = new Bullet(x, y, 5, 5, xdirection, ydirection, Color.BLACK);
    	Bullet b2 = new Bullet(x, y, 5, 5, xdirection * -1, ydirection * -1, Color.BLACK);
    	b.add(b1);
    	b.add(b2);
    	return b;
	}

	@Override
	public String getName() {
		return "Multi Shot Weapon";
	}

}

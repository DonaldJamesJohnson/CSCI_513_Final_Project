package tc;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import code.Player;

import code.BaseWeapon;

import code.AllAroundShotWeapon;

import code.MultiShotWeapon;

class PlayerTest {
	
	@Test
	public void testGetPlayerLocationX() 
	{
		code.Player player = new Player(1000, 1000);
		assertTrue(player.getPlayerLocationX() == 1000);
	}
	
	@Test
	public void testGetPlayerLocationY()
	{
		code.Player player = new Player(1000, 1000);
		assertTrue(player.getPlayerLocationY() == 1000);
	}

	@Test
	public void testSetWeaponBehavior()
	{
		code.Player player = new Player(0, 0);
		player.setWeaponBehavior(new BaseWeapon());
		assertTrue(player.getWeaponBehavior() == "Base Weapon");
	}
	
	@Test
	public void testSetWeaponBehavior2()
	{
		code.Player player = new Player(0, 0);
		player.setWeaponBehavior(new MultiShotWeapon());
		assertTrue(player.getWeaponBehavior() == "Multi Shot Weapon");
	}
	
	@Test
	public void testSetWeaponBehavior3()
	{
		code.Player player = new Player(0, 0);
		player.setWeaponBehavior(new AllAroundShotWeapon());
		assertTrue(player.getWeaponBehavior() == "All Around Shot Weapon");
	}
	
	@Test
	public void testSetHealth()
	{
		code.Player player = new Player(0, 0);
		player.setHealth(-50);
		assertTrue(player.getHealth() == 50);
	}
	
	
	@Test
	public void testSetColor()
	{
		code.Player player = new Player(0, 0);
		player.setHealth(-50);
		System.out.println(player.getColor());
		int[] colorTest = new int[3];
		colorTest[0] = 160;
		colorTest[1] = 160;
		colorTest[2] = 0;
		int[] colorReturn;
		colorReturn = player.getColor();
		assertTrue(colorReturn[0] == colorTest[0]);
		assertTrue(colorReturn[1] == colorTest[1]);
		assertTrue(colorReturn[2] == colorTest[2]);
	}
	
	@Test
	public void testSetSpeed()
	{
		code.Player player = new Player(0, 0);
		player.setSpeed(1000);
		assertTrue(player.getSpeed() == 1000);
	}
	
	@Test
	public void testResetSpeed()
	{
		code.Player player = new Player(0, 0);
		player.setSpeed(1000);
		player.resetSpeed();
		assertTrue(player.getSpeed() == player.getDefaultSpeed());
	}
	
}

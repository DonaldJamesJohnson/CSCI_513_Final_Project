/* File: PirateShip.java
 * 
 * Created by: Donald Johnson
 * 
 * Purpose: The PirateShip class stores the location of a pirate ship and handles the movement conditions when prompted by the update method.
 * 			Instances of this class are observers of instances of Ships.
 */

import java.awt.Point;
import java.util.Observable;
import java.util.Observer;

public class PirateShip implements Observer
{
	public Point piratePosition = new Point();
	public Point shipPosition = new Point();
	public int[][] oceanGrid;
	public enum OceanItems
	{
		OCEAN(0),
		ISLAND(1),
		SHIP(2),
		PIRATE(3);
		
		public final int intValue;
		OceanItems(int intValue)
		{
			this.intValue = intValue;
		}
		
		public int getIntValue() { return intValue; }
	}
	
	public PirateShip(int x, int y, int[][] oceanGrid) {
		piratePosition.x = x;
		piratePosition.y = y;
		this.oceanGrid = oceanGrid;
	}
	
	public Point getShipLocation()
	{
		System.out.println("px: " + piratePosition.x + " | py: " + piratePosition.y);
		return piratePosition;
	}


	public void update(Observable o, Object arg1) 
	{
		if (o instanceof Ship)
		{
			shipPosition = ((Ship)o).getShipLocation();
			move();
		}
		
	}
	
	public void move()
	{
		System.out.println("MOVING");
		System.out.println("shipx: " + shipPosition.getLocation().x + " | shipy: " + shipPosition.getLocation().y);
		System.out.println("pShipx: " + piratePosition.getLocation().x + " | pShipy: " + piratePosition.getLocation().y);
		
		if (Math.abs(shipPosition.getLocation().x - piratePosition.getLocation().x) >= Math.abs(shipPosition.getLocation().y - piratePosition.getLocation().y))
		{
			if (shipPosition.getLocation().x > piratePosition.getLocation().x) 
			{
					if ((oceanGrid[piratePosition.x+1][piratePosition.y] == OceanItems.OCEAN.getIntValue() || oceanGrid[piratePosition.x+1][piratePosition.y] == OceanItems.SHIP.getIntValue()) && piratePosition.getLocation().x < oceanGrid.length-1) 
					{
						piratePosition.x = piratePosition.x + 1;
					}					
					else
					{
						if (shipPosition.getLocation().y > piratePosition.getLocation().y && (oceanGrid[piratePosition.x][piratePosition.y+1] == OceanItems.OCEAN.getIntValue() || oceanGrid[piratePosition.x][piratePosition.y+1] == OceanItems.SHIP.getIntValue()) && piratePosition.getLocation().y < oceanGrid.length-1)
						{
							piratePosition.y = piratePosition.y + 1;
						}
						else if (shipPosition.getLocation().y < piratePosition.getLocation().y && (oceanGrid[piratePosition.x][piratePosition.y-1] == OceanItems.OCEAN.getIntValue() || oceanGrid[piratePosition.x][piratePosition.y-1] == OceanItems.SHIP.getIntValue()) && piratePosition.getLocation().y > 0)
						{
							piratePosition.y = piratePosition.y - 1;
						}
					}
					
			}
			else 
			{
				if ((oceanGrid[piratePosition.x-1][piratePosition.y] == OceanItems.OCEAN.getIntValue() || oceanGrid[piratePosition.x-1][piratePosition.y] == OceanItems.SHIP.getIntValue())) 
				{
					piratePosition.x = piratePosition.x - 1;
				}
				else
				{
					if (shipPosition.getLocation().y > piratePosition.getLocation().y && (oceanGrid[piratePosition.x][piratePosition.y+1] == OceanItems.OCEAN.getIntValue() || oceanGrid[piratePosition.x][piratePosition.y+1] == OceanItems.SHIP.getIntValue()) && piratePosition.getLocation().y < oceanGrid.length-1)
					{
						piratePosition.y = piratePosition.y + 1;
					}
					else if (shipPosition.getLocation().y < piratePosition.getLocation().y && (oceanGrid[piratePosition.x][piratePosition.y-1] == OceanItems.OCEAN.getIntValue() || oceanGrid[piratePosition.x][piratePosition.y-1] == OceanItems.SHIP.getIntValue()) && piratePosition.getLocation().y > 0)
					{
						piratePosition.y = piratePosition.y - 1;
					}
				}
				
			}
		}
		else
		{
			if (shipPosition.getLocation().y > piratePosition.getLocation().y && piratePosition.getLocation().y < oceanGrid.length-1) 
			{
				if (oceanGrid[piratePosition.x][piratePosition.y+1] == OceanItems.OCEAN.getIntValue() || oceanGrid[piratePosition.x][piratePosition.y+1] == OceanItems.SHIP.getIntValue()) 
				{
					piratePosition.y = piratePosition.y + 1;
				}
				else
				{
					if (shipPosition.getLocation().x > piratePosition.getLocation().x && (oceanGrid[piratePosition.x+1][piratePosition.y] == OceanItems.OCEAN.getIntValue() || oceanGrid[piratePosition.x+1][piratePosition.y] == OceanItems.SHIP.getIntValue()) && piratePosition.getLocation().x < oceanGrid.length-1)
					{
						piratePosition.x = piratePosition.x + 1;
					}
					else if (shipPosition.getLocation().x < piratePosition.getLocation().x && (oceanGrid[piratePosition.x-1][piratePosition.y] == OceanItems.OCEAN.getIntValue() || oceanGrid[piratePosition.x-1][piratePosition.y] == OceanItems.SHIP.getIntValue()) && piratePosition.getLocation().x > 0)
					{
						piratePosition.x = piratePosition.x - 1;
					}
				}
			}
			else 
			{
				if ((oceanGrid[piratePosition.x][piratePosition.y-1] == OceanItems.OCEAN.getIntValue() || oceanGrid[piratePosition.x][piratePosition.y-1] == OceanItems.SHIP.getIntValue()) && piratePosition.getLocation().y > 0) 
				{
					piratePosition.y = piratePosition.y - 1;
				}
				else
				{
					if (shipPosition.getLocation().x > piratePosition.getLocation().x && (oceanGrid[piratePosition.x+1][piratePosition.y] == OceanItems.OCEAN.getIntValue() || oceanGrid[piratePosition.x+1][piratePosition.y] == OceanItems.SHIP.getIntValue()) && piratePosition.getLocation().x < oceanGrid.length-1)
					{
						piratePosition.x = piratePosition.x + 1;
					}
					else if (shipPosition.getLocation().x < piratePosition.getLocation().x && (oceanGrid[piratePosition.x-1][piratePosition.y] == OceanItems.OCEAN.getIntValue() || oceanGrid[piratePosition.x-1][piratePosition.y] == OceanItems.SHIP.getIntValue()) && piratePosition.getLocation().x > 0)
					{
						piratePosition.x = piratePosition.x - 1;
					}
				}
			}
		}
	}

}

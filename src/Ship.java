/* File: Ship.java
 * 
 * Created by: Donald Johnson
 * 
 * Purpose: The Ship class stores and changes the ship's location when a prompted by a key event triggered in OceanExplorer.
 * 			Instances of this class are observed by instances of PirateShips. 
 */

import java.awt.Point;
import java.util.Observable;

public class Ship extends Observable
{
	Point currentLocation = new Point();
	
	public Ship(int X, int Y) 
	{
		currentLocation.x = X;
		currentLocation.y = Y;
	}
	
	public Point getShipLocation() 
	{
		System.out.println("x: " + currentLocation.x + " | y: " + currentLocation.y);
		return currentLocation;
	}
	
	public void goEast() {
		currentLocation.x = currentLocation.x + 1;
		setChanged();
		notifyObservers();
	}
	
	public void goWest() {
		currentLocation.x = currentLocation.x - 1;
		setChanged();
		notifyObservers();
	}
	
	public void goNorth() {
		currentLocation.y = currentLocation.y - 1;
		setChanged();
		notifyObservers();
	}
	
	public void goSouth() {
		currentLocation.y = currentLocation.y + 1;
		setChanged();
		notifyObservers();
	}
}

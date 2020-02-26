/* File: OceanMap.java
 * 
 * Created by: Donald Johnson
 * 
 *  Purpose: The OceanMap class stores data about the map. Namely, the map itself, the dimensionality of the map, and the scale. 
 */
public class OceanMap {
	int[][] oceanMap = new int[10][10];
	final int dimension = 10;
	final int scale = 50;
	
	public int[][] getMap(){
		return oceanMap;
	}
	
}

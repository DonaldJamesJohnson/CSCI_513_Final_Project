public enum CaveItems {
	FLOOR(0),
	WALL(1),
	PLAYER(2),
	ENEMY(3);
	
	public final int intValue;
	CaveItems(int intValue)
	{
		this.intValue = intValue;
	}
	
	public int getIntValue() { return intValue; }
}

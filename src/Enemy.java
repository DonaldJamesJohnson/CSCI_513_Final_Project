/* File: Enemy.java
 * 
 * Created by: Donald Johnson
 * 
 * Purpose: The PirateShip class stores the location of a pirate ship and handles the movement conditions when prompted by the update method.
 * 			Instances of this class are observers of instances of Ships.
 */

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


class EnemySprite{
	Circle circle;
	int scalingFactor;
	int radius = 10;
	EnemySprite(int x, int y, int scalingFactor){

		circle= new Circle();
		circle.setCenterX(x);
		circle.setCenterY(y);
		circle.setRadius(radius);
		this.scalingFactor = scalingFactor;
	}
	
	Circle getCircle(){
		return circle;
	}
	
	void setX(double x){
		circle.setCenterX(x);
	}
	
	void setY(double y){
		circle.setCenterY(y);
	}
	
	double getX(){
		return circle.getCenterX();
	}
	
	double getY(){
		return circle.getCenterY();
	}
	
	public void setLineColor(Circle circle, Color color){
		circle.setStroke(color);
		circle.setFill(color);
	}
	
	public void setPositionX(double x){
		circle.setCenterX(x*scalingFactor + (scalingFactor/2));
	}
	
	public void setPositionY(double y ){
		circle.setCenterY(y*scalingFactor + (scalingFactor/2));
	}
}

public class Enemy implements Observer {
	int maxHealth;
	int combinedHealth;
	int currentHealth;
	boolean dead;
	Boolean running = true;
	Random random = new Random();
	int scalingFactor;
	EnemySprite enemySprite;
	Point playerPosition = new Point();
	double xMove;
	double yMove;
	double speed = 200;
	int damage = 1;
	List<Enemy> innerEnemies = new ArrayList<Enemy>();
	
	public Enemy(int scalingFactor, int h){
		int x = random.nextInt(50);
		int y = random.nextInt(50);	
		enemySprite = new EnemySprite(x,y,scalingFactor);
		enemySprite.setLineColor(enemySprite.circle, Color.rgb(100, 200, 0));
		this.scalingFactor = scalingFactor;
		this.maxHealth = h;
		this.currentHealth = maxHealth;
		this.combinedHealth = currentHealth;
		this.dead = false;
	}
	
	public void addToPane(Pane pane){
		Circle circle = enemySprite.getCircle();
		enemySprite.setPositionX(random.nextInt(50));
		enemySprite.setPositionY(random.nextInt(50));
		pane.getChildren().add(circle);
	}
	
	public void update(Observable o, Object arg) {
		if (o instanceof Player)
		{
			playerPosition.x = (int)((Player)o).getPlayerLocationX();
			playerPosition.y = (int)((Player)o).getPlayerLocationY();	
		}
		
	}	

	public void move() {
    		// Move X
    		double xDiff = playerPosition.getX() - enemySprite.circle.getCenterX();
    		if (xDiff > enemySprite.circle.getRadius()) xMove = speed;
    		else if (enemySprite.circle.getRadius() > xDiff) xMove = speed * -1.0;
    		else xMove = 0;
    		// Move Y
    		double yDiff = playerPosition.getY() - (enemySprite.circle.getCenterY());
    		if (yDiff > enemySprite.circle.getRadius()) yMove = speed;
    		else if (enemySprite.circle.getRadius() > yDiff) yMove = speed * -1.0;
    		else yMove = 0;
	}
	
	public boolean containsEnemy(Enemy enemy)
	{
		boolean inSelf = (this.enemySprite.circle.getBoundsInParent().intersects(enemy.enemySprite.circle.getBoundsInParent()) && !enemy.dead);
		return inSelf;
	}
	
	public void addChild(Enemy enemy) 
	{
			innerEnemies.add(enemy);
			int radius = (int) this.enemySprite.circle.getRadius();
			this.enemySprite.circle.setRadius(radius += 2);
			this.combinedHealth = currentHealth;
			this.speed += 1;
			this.damage++;
	}
	
	public void moveRelative(double X, double Y) {
		enemySprite.circle.setCenterX(enemySprite.circle.getCenterX()+X);
		enemySprite.circle.setCenterY(enemySprite.circle.getCenterY()+Y);	
		for(Enemy child: innerEnemies){
			child.moveRelative(X,Y);
		}
	}
	
	public void setHealth(int diff)
	{
		currentHealth += diff;
		setColor();
	}
	
	public void setColor() {
		if (currentHealth <= combinedHealth * 0.3)
		{
			enemySprite.setLineColor(enemySprite.circle, Color.rgb(200, 40, 0));
		}
		else if (currentHealth <= combinedHealth * 0.6)
		{
			enemySprite.setLineColor(enemySprite.circle, Color.rgb(255, 180, 0));
		}
		else if (currentHealth > combinedHealth * 0.6)
		{
			enemySprite.setLineColor(enemySprite.circle, Color.rgb(100, 200, 0));
		}		
	}
}

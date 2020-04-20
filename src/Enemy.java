/* File: Enemy.java
 * 
 * Created by: Donald Johnson
 * 
 * Purpose: The PirateShip class stores the location of a pirate ship and handles the movement conditions when prompted by the update method.
 * 			Instances of this class are observers of instances of Ships.
 */

import java.awt.Point;
import java.util.Observable;
import java.util.Observer;


import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.collections.ObservableList;
import javafx.scene.Node;
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
	
	Boolean running = true;
	int radius;
	Random random = new Random();
	int scalingFactor;
	EnemySprite enemySprite;
	Point playerPosition = new Point();
	AnimationTimer enemyTimer;
	double xMove;
	double yMove;
	double speed = 2;
	
	public Enemy(int scalingFactor){
		int x = random.nextInt(50);
		int y = random.nextInt(50);	
		System.out.println("x: " + x);
		System.out.println("y: " + y);
		enemySprite = new EnemySprite(x,y,scalingFactor);
		enemySprite.setLineColor(enemySprite.circle, Color.INDIANRED);
		this.radius = 10;
		this.scalingFactor = scalingFactor;
	}
	
	public void addToPane(Pane pane){
		Circle circle = enemySprite.getCircle();
		enemySprite.setPositionX(random.nextInt(50));
		enemySprite.setPositionY(random.nextInt(50));
		System.out.println("Adding circle to pane: " + circle.getCenterX() + " " + circle.getCenterY() + " " + radius);
		pane.getChildren().add(circle);
	}
	
	public void Timer(Pane pane)
	{
		enemyTimer = new AnimationTimer() {
            private long lastUpdate = 0 ;
            @Override
            public void handle(long now) {
                long elapsedNanos = now - lastUpdate ;
                if (lastUpdate < 0) {
                    lastUpdate = now ;
                    return ;
                }
                double elapsedSeconds = elapsedNanos / 10000000.0;
                enemySprite.setX(clampRange(enemySprite.circle.getCenterX() + (xMove * elapsedSeconds), 0, pane.getWidth() - enemySprite.circle.getRadius()));
                enemySprite.setY(clampRange(enemySprite.circle.getCenterY() + (yMove * elapsedSeconds), 0, pane.getHeight() - enemySprite.circle.getRadius()));
                lastUpdate = now ;
                move();
            }
        };
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Player)
		{
			playerPosition.x = (int)((Player)o).getPlayerLocationX();
			playerPosition.y = (int)((Player)o).getPlayerLocationY();	
		}
		
	}	

	
	public void move() {
    		// Move X
			//System.out.println("player position: " + playerPosition.getX() + ", " + playerPosition.getY());
			//System.out.println("enemy  position: " + enemySprite.circle.getCenterX() + ", " + enemySprite.circle.getCenterY());
    		double xDiff = playerPosition.getX() - enemySprite.circle.getCenterX();
    		//System.out.println("xDiff: " + xDiff);
    		if (xDiff > radius) xMove = speed;
    		else if (radius > xDiff) xMove = speed * -1.0;
    		else xMove = 0;
    		//System.out.println("xMove: " + xMove);
    		// Move Y
    		double yDiff = playerPosition.getY() - (enemySprite.circle.getCenterY());
    		//System.out.println("yDiff: " + yDiff);
    		if (yDiff > radius) yMove = speed;
    		else if (radius > yDiff) yMove = speed * -1.0;
    		else yMove = 0;
    		//System.out.println("yMove: " + yMove);
	}
	
    private double clampRange(double value, double min, double max) {
        if (value < min) return min ;
        if (value > max) return max ;
        return value ;
    }

}

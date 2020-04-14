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
	
	void setX(int x){
		circle.setCenterX(x);
	}
	
	void setY(int y){
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
	
	public void setPositionX(int x){
		circle.setCenterX(x*scalingFactor + (scalingFactor/2));
	}
	
	public void setPositionY(int y){
		circle.setCenterY(y*scalingFactor + (scalingFactor/2));
	}
}

public class Enemy implements Runnable, Observer {
	
	Boolean running = true;
	int radius;
	Random random = new Random();
	int scalingFactor;
	EnemySprite[] enemySprites = new EnemySprite[10];
	Point playerPosition = new Point();
	AnimationTimer enemyTimer;
	int xMove;
	int yMove;
	int speed = 100;
	
	public Enemy(int scalingFactor){
		for(int j = 0; j < 10; j++){
			int x = random.nextInt(50);
			int y = random.nextInt(50);	
			System.out.println("x: " + x);
			System.out.println("y: " + y);
			enemySprites[j] = new EnemySprite(x,y,scalingFactor);
		}
		this.radius = 10;
		this.scalingFactor = scalingFactor;
	}
	
	public void addToPane(Pane pane){
		for(EnemySprite enemySprite: enemySprites){
			
			Circle circle = enemySprite.getCircle();
			enemySprite.setPositionX(random.nextInt(50));
			enemySprite.setPositionY(random.nextInt(50));
			System.out.println("Adding circle to pane: " + circle.getCenterX() + " " + circle.getCenterY() + " " + radius);
			pane.getChildren().add(circle);
		}
	}
	
	public void Timer(Pane pane)
	{
		enemyTimer = new AnimationTimer() {
            private long lastUpdate = -1 ;
            @Override
            public void handle(long now) {
                long elapsedNanos = now - lastUpdate ;
                if (lastUpdate < 0) {
                    lastUpdate = now ;
                    return ;
                }
                double elapsedSeconds = elapsedNanos / 1_000_000_000.0 ;
                double deltaX = 0 ;
                double deltaY = 0 ;
                for(EnemySprite enemySprite: enemySprites){
                if (xMove > 0) deltaX += speed ;
                if (xMove < 0) deltaX -= speed ;
                if (yMove > 0) deltaY += speed ;
                if (yMove < 0) deltaY -= speed ;
                enemySprite.circle.setCenterX(clampRange(enemySprite.circle.getCenterX() + deltaX * elapsedSeconds, 0, pane.getWidth() - enemySprite.circle.getRadius()*2));
                enemySprite.circle.setCenterY(clampRange(enemySprite.circle.getCenterY() + deltaY * elapsedSeconds, 0, pane.getHeight() - enemySprite.circle.getRadius()*2));
                lastUpdate = now ;
                }
            }
        };
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Player)
		{
			System.out.println("Moving");
			playerPosition.x = (int)((Player)o).getPlayerLocationX();
			playerPosition.y = (int)((Player)o).getPlayerLocationY();
			move();
		}
		
	}	

	
	public void move() {
		for(EnemySprite enemySprite: enemySprites){
    		// Move X
			System.out.println("player position: " + playerPosition.getX() + ", " + playerPosition.getY());
			System.out.println("enemy  position: " + enemySprite.circle.getCenterX() + ", " + enemySprite.circle.getCenterY());
    		double xDiff = playerPosition.getX() - enemySprite.circle.getCenterX();
    		System.out.println("xDiff: " + xDiff);

    		if (xDiff > radius) xMove = (int) (enemySprite.circle.getCenterX() + 5);
    		else if (radius > xDiff) xMove = (int) (enemySprite.circle.getCenterX()  - 5);
    		else xMove = 0;
    		enemySprite.setX(xMove);
    		System.out.println("xMove: " + enemySprite.getX());
    		// Move Y
    		double yDiff = playerPosition.getY() - (enemySprite.circle.getCenterY());
    		System.out.println("yDiff: " + yDiff);
    		if (yDiff > radius) yMove = (int) (enemySprite.circle.getCenterY()  + 5);
    		else if (radius > yDiff) yMove = (int) (enemySprite.circle.getCenterY()  - 5);
    		else yMove = 0;
    		
    		enemySprite.setY(yMove);
    		System.out.println("yMove: " + enemySprite.getY());
    	}
	}
	
    private double clampRange(double value, double min, double max) {
        if (value < min) return min ;
        if (value > max) return max ;
        return value ;
    }

			
	@Override
    public void run() {
		
	  
      while (true) {
    	try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
    	move();
    	
      }
      
    }

}

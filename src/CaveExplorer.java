/* File: OceanExplorer.java
 * 
 * Created by: Donald Johnson
 * 
 * Purpose: The OceanExplorer class uses the javafx libraries to display an instance of an OceanMap, a Ship, and multiple instances of PirateShips.
 */

import java.awt.Point;
import java.util.Random;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class CaveExplorer extends Application 
{
	
	CaveMap caveMap = new CaveMap();
	AnchorPane myPane = new AnchorPane();
	Scene scene = new Scene(myPane, caveMap.scale*caveMap.dimension, caveMap.scale*caveMap.dimension);
	Image playerImage;
	ImageView playerImageView;
	Image enemyImage;
	ImageView pirateImageView_1;
	ImageView pirateImageView_2;
	int[][] caveGrid = caveMap.getMap();
	Random rand = new Random();
	int rand_x = rand.nextInt(caveMap.dimension);
	int rand_y = rand.nextInt(caveMap.dimension);
	//Player ship = new Player(rand_x, rand_y);
	//Enemy pirateShip_1 = new Enemy(0, 0, caveGrid);
	//Enemy pirateShip_2 = new Enemy(1, 1, caveGrid);
	
	
	public void start(Stage caveStage) throws Exception 
	{
		caveStage.setScene(scene);
		caveStage.setTitle("Christopher Columbus Game");
		caveStage.show();
		//ship.addObserver(pirateShip_1);
		//ship.addObserver(pirateShip_2);
		//drawMap();
		//placeIsland(30);
		//placeShip();
		//loadShipImage();
		//pirateShip_1.piratePosition = placePirate(pirateShip_1);
		//pirateShip_2.piratePosition = placePirate(pirateShip_2);
		//loadPirateImage_1();
		//loadPirateImage_2();
		//startSailing();
	}
	
	public static void main(String[] args) 
	{
		launch(args);
	}
	
//	public void drawMap() 						// Draws an empty ocean map
//	{						
//		for (int y = 0; y < caveMap.dimension; y++) 
//		{
//			for (int x = 0; x < caveMap.dimension; x++) 
//			{
//				caveGrid[x][y] = CaveItems.FLOOR.getIntValue();
//				Rectangle rect = new Rectangle(x*caveMap.scale, y*caveMap.scale, caveMap.scale, caveMap.scale);
//				rect.setStroke(Color.BLACK);
//				rect.setFill(Color.AQUAMARINE);
//				myPane.getChildren().add(rect);
//			}
//		}
//	}
//	
//	public void placeIsland(int num)			// Places n islands randomly on the map
//	{
//		Random rand = new Random();
//		for (int i = 0; i < num; i++)
//		{
//			rand_x = rand.nextInt(caveMap.dimension);
//			rand_y = rand.nextInt(caveMap.dimension);
//			caveGrid[rand_x][rand_y] = CaveItems.WALL.getIntValue();
//			Rectangle rect = new Rectangle(rand_x*caveMap.scale, rand_y*caveMap.scale, caveMap.scale, caveMap.scale);
//			rect.setStroke(Color.BLACK);
//			rect.setFill(Color.FORESTGREEN);
//			myPane.getChildren().add(rect);
//		}
//	}
//	
//	public void placeShip()						// Places the player ship randomly on the map
//	{
//		if (caveGrid[ship.getShipLocation().x][ship.getShipLocation().y] != CaveItems.FLOOR.getIntValue())
//		{
//			while(caveGrid[ship.getShipLocation().x][ship.getShipLocation().y] != CaveItems.FLOOR.getIntValue())
//			{
//				rand_x = rand.nextInt(caveMap.dimension);
//				rand_y = rand.nextInt(caveMap.dimension);
//				ship.currentLocation.x = rand_x;
//				ship.currentLocation.y = rand_y;
//			}
//		}
//		caveGrid[ship.getShipLocation().x][ship.getShipLocation().y] = CaveItems.PLAYER.getIntValue();
//	}
//	
//	public Point placePirate(Enemy p)		// Places a pirate ship randomly on the map
//	{
//		if (caveGrid[p.getShipLocation().x][p.getShipLocation().y] != CaveItems.FLOOR.getIntValue())
//		{
//			while(caveGrid[p.getShipLocation().x][p.getShipLocation().y] != CaveItems.FLOOR.getIntValue())
//			{
//				rand_x = rand.nextInt(caveMap.dimension);
//				rand_y = rand.nextInt(caveMap.dimension);
//				p.piratePosition.x = rand_x;
//				p.piratePosition.y = rand_y;
//			}	
//		}
//		caveGrid[p.getShipLocation().x][p.getShipLocation().y] = CaveItems.ENEMY.getIntValue();
//		return p.piratePosition;
//	}
//	
//	public void loadShipImage() 				// Loads the player ship image
//	{
//		playerImage = new Image("ship.png", 50, 50, true, true);
//		playerImageView = new ImageView(playerImage);
//		playerImageView.setX(ship.getShipLocation().x * caveMap.scale);
//		playerImageView.setY(ship.getShipLocation().y * caveMap.scale);
//		myPane.getChildren().add(playerImageView);
//	}
//	
//	public void loadPirateImage_1()				// Loads the pirate ship image
//	{
//		enemyImage = new Image("pirateShip.png", 50, 50, true, true);
//		pirateImageView_1 = new ImageView(enemyImage);
//		pirateImageView_1.setX(pirateShip_1.getShipLocation().x * caveMap.scale);
//		pirateImageView_1.setY(pirateShip_1.getShipLocation().y * caveMap.scale);
//		myPane.getChildren().add(pirateImageView_1);
//	}
//	
//	public void loadPirateImage_2()				// Loads the second pirate ship image
//	{
//		enemyImage = new Image("pirateShip.png", 50, 50, true, true);
//		pirateImageView_2 = new ImageView(enemyImage);
//		pirateImageView_2.setX(pirateShip_2.getShipLocation().x * caveMap.scale);
//		pirateImageView_2.setY(pirateShip_2.getShipLocation().y * caveMap.scale);
//		myPane.getChildren().add(pirateImageView_2);	
//	}
//	
//	private void startSailing() 				// Handles key events and moves the player ship accordingly
//	{
//		scene.setOnKeyPressed(new EventHandler<KeyEvent>() 
//		{
//			public void handle(KeyEvent ke) 
//			{
//				switch(ke.getCode())
//				{
//					case RIGHT:
//						if (ship.getShipLocation().x < caveMap.dimension-1) 
//						{
//							if (caveGrid[ship.getShipLocation().x+1][ship.getShipLocation().y] == CaveItems.FLOOR.getIntValue()) 
//								{
//								caveGrid[ship.getShipLocation().x][ship.getShipLocation().y] = CaveItems.FLOOR.getIntValue();
//								caveGrid[ship.getShipLocation().x+1][ship.getShipLocation().y] = CaveItems.PLAYER.getIntValue();
//								caveGrid[pirateShip_1.getShipLocation().x][pirateShip_1.getShipLocation().y] = CaveItems.FLOOR.getIntValue();
//								caveGrid[pirateShip_2.getShipLocation().x][pirateShip_2.getShipLocation().y] = CaveItems.FLOOR.getIntValue();
//									ship.goEast();
//									caveGrid[pirateShip_1.getShipLocation().x][pirateShip_1.getShipLocation().y] = CaveItems.ENEMY.getIntValue();
//									caveGrid[pirateShip_2.getShipLocation().x][pirateShip_2.getShipLocation().y] = CaveItems.ENEMY.getIntValue();
//								}
//						}
//						break;
//					case LEFT:
//						if (ship.getShipLocation().x > 0) 
//						{
//							if (caveGrid[ship.getShipLocation().x-1][ship.getShipLocation().y] == CaveItems.FLOOR.getIntValue()) 
//								{
//								caveGrid[ship.getShipLocation().x][ship.getShipLocation().y] = CaveItems.FLOOR.getIntValue();
//								caveGrid[ship.getShipLocation().x-1][ship.getShipLocation().y] = CaveItems.PLAYER.getIntValue();
//								caveGrid[pirateShip_1.getShipLocation().x][pirateShip_1.getShipLocation().y] = CaveItems.FLOOR.getIntValue();
//								caveGrid[pirateShip_2.getShipLocation().x][pirateShip_2.getShipLocation().y] = CaveItems.FLOOR.getIntValue();
//									ship.goWest();
//									caveGrid[pirateShip_1.getShipLocation().x][pirateShip_1.getShipLocation().y] = CaveItems.ENEMY.getIntValue();
//									caveGrid[pirateShip_2.getShipLocation().x][pirateShip_2.getShipLocation().y] = CaveItems.ENEMY.getIntValue();
//								}
//						}
//						break;
//					case UP:
//						if (ship.getShipLocation().y > 0) {
//							if (caveGrid[ship.getShipLocation().x][ship.getShipLocation().y-1] == CaveItems.FLOOR.getIntValue())
//								{
//								caveGrid[ship.getShipLocation().x][ship.getShipLocation().y] = CaveItems.FLOOR.getIntValue();
//								caveGrid[ship.getShipLocation().x][ship.getShipLocation().y-1] = CaveItems.PLAYER.getIntValue();
//								caveGrid[pirateShip_1.getShipLocation().x][pirateShip_1.getShipLocation().y] = CaveItems.FLOOR.getIntValue();
//								caveGrid[pirateShip_2.getShipLocation().x][pirateShip_2.getShipLocation().y] = CaveItems.FLOOR.getIntValue();
//									ship.goNorth();
//									caveGrid[pirateShip_1.getShipLocation().x][pirateShip_1.getShipLocation().y] = CaveItems.ENEMY.getIntValue();
//									caveGrid[pirateShip_2.getShipLocation().x][pirateShip_2.getShipLocation().y] = CaveItems.ENEMY.getIntValue();
//								}
//						}
//						break;
//					case DOWN:
//						if (ship.getShipLocation().y < caveMap.dimension-1)
//						{
//							if (caveGrid[ship.getShipLocation().x][ship.getShipLocation().y+1] == CaveItems.FLOOR.getIntValue())
//								{
//								caveGrid[ship.getShipLocation().x][ship.getShipLocation().y] = CaveItems.FLOOR.getIntValue();
//								caveGrid[ship.getShipLocation().x][ship.getShipLocation().y+1] = CaveItems.PLAYER.getIntValue();
//									caveGrid[pirateShip_1.getShipLocation().x][pirateShip_1.getShipLocation().y] = CaveItems.FLOOR.getIntValue();
//									caveGrid[pirateShip_2.getShipLocation().x][pirateShip_2.getShipLocation().y] = CaveItems.FLOOR.getIntValue();
//									ship.goSouth();	
//									caveGrid[pirateShip_1.getShipLocation().x][pirateShip_1.getShipLocation().y] = CaveItems.ENEMY.getIntValue();
//									caveGrid[pirateShip_2.getShipLocation().x][pirateShip_2.getShipLocation().y] = CaveItems.ENEMY.getIntValue();
//								}
//						}
//						break;
//					default:
//						break;
//				}
//				playerImageView.setX(ship.getShipLocation().x * caveMap.scale);
//				playerImageView.setY(ship.getShipLocation().y * caveMap.scale);
//				pirateImageView_1.setX(pirateShip_1.getShipLocation().x * caveMap.scale);
//				pirateImageView_1.setY(pirateShip_1.getShipLocation().y * caveMap.scale);
//				pirateImageView_2.setX(pirateShip_2.getShipLocation().x * caveMap.scale);
//				pirateImageView_2.setY(pirateShip_2.getShipLocation().y * caveMap.scale);
//			}
//		});	
//	}

}
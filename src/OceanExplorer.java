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

public class OceanExplorer extends Application 
{
	
	OceanMap oceanMap = new OceanMap();
	AnchorPane myPane = new AnchorPane();
	Scene scene = new Scene(myPane, oceanMap.scale*oceanMap.dimension, oceanMap.scale*oceanMap.dimension);
	Image shipImage;
	ImageView shipImageView;
	Image pirateImage;
	ImageView pirateImageView_1;
	ImageView pirateImageView_2;
	int[][] oceanGrid = oceanMap.getMap();
	Random rand = new Random();
	int rand_x = rand.nextInt(oceanMap.dimension);
	int rand_y = rand.nextInt(oceanMap.dimension);
	Ship ship = new Ship(rand_x, rand_y);
	PirateShip pirateShip_1 = new PirateShip(0, 0, oceanGrid);
	PirateShip pirateShip_2 = new PirateShip(1, 1, oceanGrid);
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
	
	
	public void start(Stage oceanStage) throws Exception 
	{
		oceanStage.setScene(scene);
		oceanStage.setTitle("Christopher Columbus Game");
		oceanStage.show();
		ship.addObserver(pirateShip_1);
		ship.addObserver(pirateShip_2);
		drawMap();
		placeIsland(10);
		placeShip();
		loadShipImage();
		pirateShip_1.piratePosition = placePirate(pirateShip_1);
		pirateShip_2.piratePosition = placePirate(pirateShip_2);
		loadPirateImage_1();
		loadPirateImage_2();
		startSailing();
	}
	
	public static void main(String[] args) 
	{
		launch(args);
	}
	
	public void drawMap() 						// Draws an empty ocean map
	{						
		for (int y = 0; y < oceanMap.dimension; y++) 
		{
			for (int x = 0; x < oceanMap.dimension; x++) 
			{
				oceanGrid[x][y] = OceanItems.OCEAN.getIntValue();
				Rectangle rect = new Rectangle(x*oceanMap.scale, y*oceanMap.scale, oceanMap.scale, oceanMap.scale);
				rect.setStroke(Color.BLACK);
				rect.setFill(Color.PALETURQUOISE);
				myPane.getChildren().add(rect);
			}
		}
	}
	
	public void placeIsland(int num)			// Places n islands randomly on the map
	{
		Random rand = new Random();
		for (int i = 0; i < num; i++)
		{
			rand_x = rand.nextInt(oceanMap.dimension);
			rand_y = rand.nextInt(oceanMap.dimension);
			oceanGrid[rand_x][rand_y] = OceanItems.ISLAND.getIntValue();
			Rectangle rect = new Rectangle(rand_x*oceanMap.scale, rand_y*oceanMap.scale, oceanMap.scale, oceanMap.scale);
			rect.setStroke(Color.BLACK);
			rect.setFill(Color.DARKGREEN);
			myPane.getChildren().add(rect);
		}
	}
	
	public void placeShip()						// Places the player ship randomly on the map
	{
		if (oceanGrid[ship.getShipLocation().x][ship.getShipLocation().y] != OceanItems.OCEAN.getIntValue())
		{
			while(oceanGrid[ship.getShipLocation().x][ship.getShipLocation().y] != OceanItems.OCEAN.getIntValue())
			{
				rand_x = rand.nextInt(oceanMap.dimension);
				rand_y = rand.nextInt(oceanMap.dimension);
				ship.currentLocation.x = rand_x;
				ship.currentLocation.y = rand_y;
			}
		}
		oceanGrid[ship.getShipLocation().x][ship.getShipLocation().y] = OceanItems.SHIP.getIntValue();
	}
	
	public Point placePirate(PirateShip p)		// Places a pirate ship randomly on the map
	{
		if (oceanGrid[p.getShipLocation().x][p.getShipLocation().y] != OceanItems.OCEAN.getIntValue())
		{
			while(oceanGrid[p.getShipLocation().x][p.getShipLocation().y] != OceanItems.OCEAN.getIntValue())
			{
				rand_x = rand.nextInt(oceanMap.dimension);
				rand_y = rand.nextInt(oceanMap.dimension);
				p.piratePosition.x = rand_x;
				p.piratePosition.y = rand_y;
			}	
		}
		oceanGrid[p.getShipLocation().x][p.getShipLocation().y] = OceanItems.PIRATE.getIntValue();
		return p.piratePosition;
	}
	
	public void loadShipImage() 				// Loads the player ship image
	{
		shipImage = new Image("ship.png", 50, 50, true, true);
		shipImageView = new ImageView(shipImage);
		shipImageView.setX(ship.getShipLocation().x * oceanMap.scale);
		shipImageView.setY(ship.getShipLocation().y * oceanMap.scale);
		myPane.getChildren().add(shipImageView);
	}
	
	public void loadPirateImage_1()				// Loads the pirate ship image
	{
		pirateImage = new Image("pirateShip.png", 50, 50, true, true);
		pirateImageView_1 = new ImageView(pirateImage);
		pirateImageView_1.setX(pirateShip_1.getShipLocation().x * oceanMap.scale);
		pirateImageView_1.setY(pirateShip_1.getShipLocation().y * oceanMap.scale);
		myPane.getChildren().add(pirateImageView_1);
	}
	
	public void loadPirateImage_2()				// Loads the second pirate ship image
	{
		pirateImage = new Image("pirateShip.png", 50, 50, true, true);
		pirateImageView_2 = new ImageView(pirateImage);
		pirateImageView_2.setX(pirateShip_2.getShipLocation().x * oceanMap.scale);
		pirateImageView_2.setY(pirateShip_2.getShipLocation().y * oceanMap.scale);
		myPane.getChildren().add(pirateImageView_2);	
	}
	
	private void startSailing() 				// Handles key events and moves the player ship accordingly
	{
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() 
		{
			public void handle(KeyEvent ke) 
			{
				switch(ke.getCode())
				{
					case RIGHT:
						if (ship.getShipLocation().x < oceanMap.dimension-1) 
						{
							if (oceanGrid[ship.getShipLocation().x+1][ship.getShipLocation().y] == OceanItems.OCEAN.getIntValue()) 
								{
									oceanGrid[ship.getShipLocation().x][ship.getShipLocation().y] = OceanItems.OCEAN.getIntValue();
									oceanGrid[ship.getShipLocation().x+1][ship.getShipLocation().y] = OceanItems.SHIP.getIntValue();
									oceanGrid[pirateShip_1.getShipLocation().x][pirateShip_1.getShipLocation().y] = OceanItems.OCEAN.getIntValue();
									oceanGrid[pirateShip_2.getShipLocation().x][pirateShip_2.getShipLocation().y] = OceanItems.OCEAN.getIntValue();
									ship.goEast();
									oceanGrid[pirateShip_1.getShipLocation().x][pirateShip_1.getShipLocation().y] = OceanItems.PIRATE.getIntValue();
									oceanGrid[pirateShip_2.getShipLocation().x][pirateShip_2.getShipLocation().y] = OceanItems.PIRATE.getIntValue();
								}
						}
						break;
					case LEFT:
						if (ship.getShipLocation().x > 0) 
						{
							if (oceanGrid[ship.getShipLocation().x-1][ship.getShipLocation().y] == OceanItems.OCEAN.getIntValue()) 
								{
									oceanGrid[ship.getShipLocation().x][ship.getShipLocation().y] = OceanItems.OCEAN.getIntValue();
									oceanGrid[ship.getShipLocation().x-1][ship.getShipLocation().y] = OceanItems.SHIP.getIntValue();
									oceanGrid[pirateShip_1.getShipLocation().x][pirateShip_1.getShipLocation().y] = OceanItems.OCEAN.getIntValue();
									oceanGrid[pirateShip_2.getShipLocation().x][pirateShip_2.getShipLocation().y] = OceanItems.OCEAN.getIntValue();
									ship.goWest();
									oceanGrid[pirateShip_1.getShipLocation().x][pirateShip_1.getShipLocation().y] = OceanItems.PIRATE.getIntValue();
									oceanGrid[pirateShip_2.getShipLocation().x][pirateShip_2.getShipLocation().y] = OceanItems.PIRATE.getIntValue();
								}
						}
						break;
					case UP:
						if (ship.getShipLocation().y > 0) {
							if (oceanGrid[ship.getShipLocation().x][ship.getShipLocation().y-1] == OceanItems.OCEAN.getIntValue())
								{
									oceanGrid[ship.getShipLocation().x][ship.getShipLocation().y] = OceanItems.OCEAN.getIntValue();
									oceanGrid[ship.getShipLocation().x][ship.getShipLocation().y-1] = OceanItems.SHIP.getIntValue();
									oceanGrid[pirateShip_1.getShipLocation().x][pirateShip_1.getShipLocation().y] = OceanItems.OCEAN.getIntValue();
									oceanGrid[pirateShip_2.getShipLocation().x][pirateShip_2.getShipLocation().y] = OceanItems.OCEAN.getIntValue();
									ship.goNorth();
									oceanGrid[pirateShip_1.getShipLocation().x][pirateShip_1.getShipLocation().y] = OceanItems.PIRATE.getIntValue();
									oceanGrid[pirateShip_2.getShipLocation().x][pirateShip_2.getShipLocation().y] = OceanItems.PIRATE.getIntValue();
								}
						}
						break;
					case DOWN:
						if (ship.getShipLocation().y < oceanMap.dimension-1)
						{
							if (oceanGrid[ship.getShipLocation().x][ship.getShipLocation().y+1] == OceanItems.OCEAN.getIntValue())
								{
									oceanGrid[ship.getShipLocation().x][ship.getShipLocation().y] = OceanItems.OCEAN.getIntValue();
									oceanGrid[ship.getShipLocation().x][ship.getShipLocation().y+1] = OceanItems.SHIP.getIntValue();
									oceanGrid[pirateShip_1.getShipLocation().x][pirateShip_1.getShipLocation().y] = OceanItems.OCEAN.getIntValue();
									oceanGrid[pirateShip_2.getShipLocation().x][pirateShip_2.getShipLocation().y] = OceanItems.OCEAN.getIntValue();
									ship.goSouth();	
									oceanGrid[pirateShip_1.getShipLocation().x][pirateShip_1.getShipLocation().y] = OceanItems.PIRATE.getIntValue();
									oceanGrid[pirateShip_2.getShipLocation().x][pirateShip_2.getShipLocation().y] = OceanItems.PIRATE.getIntValue();
								}
						}
						break;
					default:
						break;
				}
				shipImageView.setX(ship.getShipLocation().x * oceanMap.scale);
				shipImageView.setY(ship.getShipLocation().y * oceanMap.scale);
				pirateImageView_1.setX(pirateShip_1.getShipLocation().x * oceanMap.scale);
				pirateImageView_1.setY(pirateShip_1.getShipLocation().y * oceanMap.scale);
				pirateImageView_2.setX(pirateShip_2.getShipLocation().x * oceanMap.scale);
				pirateImageView_2.setY(pirateShip_2.getShipLocation().y * oceanMap.scale);
			}
		});	
	}

}
/* Menu.java
 * Donald Johnson
 * 
 * Menu defines the buttons to start or exit the game, the game label, and sets the attributes of these. 
 * It also handles the user input of clicking on the buttons
 */
package code;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class Menu implements EventHandler<ActionEvent> {
	
	Button startButton;
	Button exitButton;
	Label gameLabel;
	boolean start;
	boolean exit;
	
	Menu(int x, int y)
	{
		gameLabel = new Label();
		gameLabel.setText("Cave Explorer");
		Font font = new Font("Monospace", 40);
		gameLabel.setFont(font);
		gameLabel.setMinHeight(100);
		gameLabel.setMinWidth(300);
		gameLabel.setTranslateX(x - gameLabel.getMinWidth()/2 + 10);
		gameLabel.setTranslateY(y - gameLabel.getMinHeight()/2 - 250);
		startButton = new Button("Start Game");
		startButton.setMinHeight(50);
		startButton.setMinWidth(200);
		startButton.setTranslateX(x - startButton.getMinWidth()/2);
		startButton.setTranslateY(y - startButton.getMinHeight()/2 - 100);
		startButton.setOnAction(this);
		exitButton = new Button("Exit Game");
		exitButton.setMinHeight(50);
		exitButton.setMinWidth(200);
		exitButton.setTranslateX(x - startButton.getMinWidth()/2);
		exitButton.setTranslateY(y - startButton.getMinHeight()/2);
		exitButton.setOnAction(this);
	}

	@Override
	public void handle(ActionEvent event) {
		if (event.getSource() == startButton) start = true;
		else exit = true;
		
	}

}

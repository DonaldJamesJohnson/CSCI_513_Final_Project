//This interface is used to refer all
//the powerups and allows for greater
//abstraction in the main class.
package code;
import javafx.scene.shape.Shape;

public interface PowerUp {
    Shape getPowerUpShape();
    void setPositionX(double x);
    void setPositionY(double y);
    double getPositionX();
    double getPositionY();
}

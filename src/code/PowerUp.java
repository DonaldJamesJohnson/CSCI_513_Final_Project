package code;
import javafx.scene.shape.Shape;

public interface PowerUp {
    Shape getPowerUpShape();
    void setPositionX(double x);
    void setPositionY(double y);
    double getPositionX();
    double getPositionY();
}

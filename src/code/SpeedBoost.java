package code;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class SpeedBoost implements PowerUp {
    Rectangle speedRect;

    public SpeedBoost(int X, int Y)
    {
        speedRect = new Rectangle(0, 0, 20, 20);;
        speedRect.setStroke(Color.GREEN);
        speedRect.setFill(Color.rgb(10, 200, 200, 1));
        speedRect.setX(X);
        speedRect.setY(Y);
    }

    @Override
    public Shape getPowerUpShape() {
        return speedRect;
    }

    @Override
    public void setPositionX(double x) {
        speedRect.setX(x);
    }

    @Override
    public void setPositionY(double y) {
        speedRect.setY(y);
    }

}

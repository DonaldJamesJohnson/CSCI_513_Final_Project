//This class implements the PowerUp interface
//and contains code to set up a rectangle
//which can then be picked up by the user and
//gives a healthboost.

package code;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class HealthBoost implements PowerUp {
    Rectangle healthRect;

    public HealthBoost(int X, int Y)
    {
        healthRect = new Rectangle(0, 0, 40, 40);;
        healthRect.setStroke(Color.GREEN);
        healthRect.setFill(Color.rgb(0, 200, 0, 1));
        healthRect.setX(X);
        healthRect.setY(Y);
    }

    @Override
    public Shape getPowerUpShape() {
        return healthRect;
    }

    @Override
    public void setPositionX(double x) {
        healthRect.setX(x);
    }

    @Override
    public void setPositionY(double y) {
        healthRect.setY(y);
    }

    @Override
    public double getPositionX() { return healthRect.getX(); }

    @Override
    public double getPositionY() { return healthRect.getY(); }

}

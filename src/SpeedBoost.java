import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class SpeedBoost implements PowerUp {
    Rectangle speedRect = new Rectangle();

    public SpeedBoost(int X, int Y, Rectangle r)
    {
        speedRect = r;
        speedRect.setStroke(Color.GREEN);
        speedRect.setFill(Color.rgb(10, 200, 0, 1));
        speedRect.setX(X);
        speedRect.setY(Y);
    }

    private double clampRange(double value, double min, double max) {
        if (value < min) return min ;
        if (value > max) return max ;
        return value ;
    }

    @Override
    public Shape getPowerUpShape() {
        return speedRect;
    }
}

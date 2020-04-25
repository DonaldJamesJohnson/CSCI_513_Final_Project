import javafx.scene.shape.Rectangle;

public class PowerUpFactory {
    public PowerUp getPowerUp(String powerUpType, int x, int y, Rectangle rect) {
        if(powerUpType == "SpeedBoost")
            return new SpeedBoost(x, y, rect);
        else if(powerUpType == "HealthBoost")
            return new HealthBoost(x, y, rect);
        return null;
    }
}

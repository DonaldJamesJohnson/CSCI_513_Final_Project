package code;

public class PowerUpFactory {
    public static PowerUp getPowerUp(String powerUpType, int x, int y) {
        if(powerUpType == "SpeedBoost")
            return new SpeedBoost(x, y);
        else if(powerUpType == "HealthBoost")
            return new HealthBoost(x, y);
        return null;
    }
}

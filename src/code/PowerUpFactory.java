//This is the factory class for the powerups and
//has one static method so it doesn't need to
//be instantiated.
package code;

public class PowerUpFactory {

    //This static method returns the right powerup object after checking
    //against the input string and assigns it the coordinates given.
    public static PowerUp getPowerUp(String powerUpType, int x, int y) {
        if(powerUpType == "SpeedBoost")
            return new SpeedBoost(x, y);
        else if(powerUpType == "HealthBoost")
            return new HealthBoost(x, y);
        return null;
    }
}

package tc;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import code.PowerUpFactory;
import code.HealthBoost;
import code.SpeedBoost;

class PowerupTest {

    @Test
    public void testGetHealthBoostLocationX()
    {
        code.PowerUp pow = PowerUpFactory.getPowerUp("HealthBoost", 1000, 1000);
        assertTrue(pow.getPositionX() == 1000);
    }

    @Test
    public void testGetHealthBoostLocationY()
    {
        code.PowerUp pow = PowerUpFactory.getPowerUp("HealthBoost", 1000, 1000);
        assertTrue(pow.getPositionY() == 1000);
    }

    @Test
    public void testGetSpeedBoostLocationX()
    {
        code.PowerUp pow = PowerUpFactory.getPowerUp("SpeedBoost", 1000, 1000);
        assertTrue(pow.getPositionX() == 1000);
    }

    @Test
    public void testGetSpeedBoostLocationY()
    {
        code.PowerUp pow = PowerUpFactory.getPowerUp("SpeedBoost", 1000, 1000);
        assertTrue(pow.getPositionY() == 1000);
    }

}

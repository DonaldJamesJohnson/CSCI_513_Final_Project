package code;
public class EnemyFactory {
    public static Enemy getEnemy(int scale, int health) {
        return new Enemy(scale, health);
    }
}

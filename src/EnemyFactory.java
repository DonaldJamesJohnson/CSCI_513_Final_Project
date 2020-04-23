public class EnemyFactory {
    public Enemy getEnemy(int scale, int health) {
        return new Enemy(scale, health);
    }
}

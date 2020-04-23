public class EnemyFactory {
    public Enemy getEnemy(int scale) {
        return new Enemy(scale);
    }
}

package uwu.openjfx.components;

public class BossComponent extends EnemyComponent {

    public BossComponent(int healthPoints, String type, int width, int height) {
        super(healthPoints, type, width, height);
    }

    @Override
    public void die() {
        super.die();
        System.out.println("Player wins!!! Show prof.'s pic : )");
    }
}

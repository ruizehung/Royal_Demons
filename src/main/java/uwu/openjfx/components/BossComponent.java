package uwu.openjfx.components;

public class BossComponent extends EnemyComponent {

    public BossComponent(int healthPoints, String assetName, int width, int height, int frames) {
        super(healthPoints, assetName, width, height, frames);
        getLife().setDieBehavior(new PlayerWinWhenDie());
    }
}

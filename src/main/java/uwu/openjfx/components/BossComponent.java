package uwu.openjfx.components;

import uwu.openjfx.behaviors.WinWhenDie;

// Todo: add more boss behavior here
public class BossComponent extends EnemyComponent {

    public BossComponent(int healthPoints, String assetName, int width, int height, int frames) {
        super(healthPoints, assetName, width, height, frames);
        setDieBehavior(new WinWhenDie());
    }
}

package uwu.openjfx.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import uwu.openjfx.RoyalType;
import uwu.openjfx.behaviors.DoNothing;

// Todo: add more boss behavior here
public class BossComponent extends EnemyComponent {

    public BossComponent(int healthPoints, String assetName, int width, int height, int frames) {
        super(healthPoints, assetName, width, height, frames, "finalboss", "melee");
//        setDieBehavior(new WinWhenDie());
        setDieBehavior(new DoNothing());
    }

    @Override
    public void die() {
        super.die();
        Entity finalDoor = FXGL.getGameWorld().getEntitiesByType(RoyalType.FINALDOOR).get(0);
        finalDoor.getViewComponent().clearChildren();
        finalDoor.getViewComponent().addChild(FXGL.texture("woodenDoorOpened.png"));
    }
}

package uwu.openjfx.weapons;

import com.almasb.fxgl.entity.Entity;

public interface AngleBehavior {
    void calculateAnglePlayerRelative(Entity player, double mouseCurrX, double mouseCurrY);
}

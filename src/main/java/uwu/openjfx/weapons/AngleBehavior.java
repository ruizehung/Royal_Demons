package uwu.openjfx.weapons;

import com.almasb.fxgl.entity.Entity;

/*
    This interface creates a contract with any class that is a projectile and must calculate the
    angle relative to its spawn location and the mouse location.
 */
public interface AngleBehavior {
    void calculateAnglePlayerRelative(Entity player, double mouseCurrX, double mouseCurrY);
}

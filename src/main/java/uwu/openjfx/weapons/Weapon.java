package uwu.openjfx.weapons;

import com.almasb.fxgl.entity.Entity;

public interface Weapon {
    void attack(Entity player, boolean ultimateActivated, double mouseCurrX, double mouseCurrY);
    int getDuration(boolean ultimateActivated);
}

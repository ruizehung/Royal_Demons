package uwu.openjfx.weapons;

import com.almasb.fxgl.entity.Entity;

public interface Weapon {
    void prepAttack(Entity player);
    void attack(Entity player, double mouseCurrX, double mouseCurrY);
    int getDuration(boolean ultimateActivated);
}

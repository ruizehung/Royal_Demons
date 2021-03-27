package uwu.openjfx.weapons;

import com.almasb.fxgl.entity.Entity;

/*
    This interface creates a contract with another class whose responsibility is to do
    the following:
    - Charge-up animation up until it spawns the actual hitbox
    - Spawn the actual hitbox for the attack (or projectile)
    - Find how long the attack will actually last (depending whether or not ultimate is activated)
 */
public interface Weapon {
    void prepAttack(Entity player);
    void attack(Entity player, double mouseCurrX, double mouseCurrY);
    int getDuration(boolean ultimateActivated);
}

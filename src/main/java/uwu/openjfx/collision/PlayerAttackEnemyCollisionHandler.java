package uwu.openjfx.collision;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.IDComponent;
import com.almasb.fxgl.physics.CollisionHandler;
import uwu.openjfx.MapGeneration.Room;
import uwu.openjfx.RoyalType;
import uwu.openjfx.components.AttackThroughComponent;
import uwu.openjfx.components.HealthComponent;

public class PlayerAttackEnemyCollisionHandler extends CollisionHandler  {
    public PlayerAttackEnemyCollisionHandler() {
        super(RoyalType.PLAYERATTACK, RoyalType.ENEMY);
    }

    @Override
    protected void onCollisionBegin(Entity weapon, Entity enemy) {
        if (weapon.hasComponent(ProjectileComponent.class)) {
            if (!weapon.getComponent(ProjectileComponent.class).isPaused()) {
                HealthComponent enemyHealth = enemy.getObject("enemyComponent");
                enemyHealth.deductHealth();
                AttackThroughComponent playerWeaponAttackThrough =
                        weapon.getComponent(AttackThroughComponent.class);
                if (!playerWeaponAttackThrough.isActive()) {
                    weapon.removeFromWorld();
                }
            }
        } else {
            HealthComponent enemyHealth = enemy.getObject("enemyComponent");
            enemyHealth.deductHealth();
            AttackThroughComponent playerWeaponAttackThrough =
                    weapon.getComponent(AttackThroughComponent.class);
            if (!playerWeaponAttackThrough.isActive()) {
                weapon.removeFromWorld();
            }
        }
    }
}

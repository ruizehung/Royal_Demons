package uwu.openjfx.collision;

import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import uwu.openjfx.RoyalType;
import uwu.openjfx.components.AttackMultipleComponent;
import uwu.openjfx.components.HealthComponent;

public class PlayerAttackEnemyCollisionHandler extends CollisionHandler  {
    public PlayerAttackEnemyCollisionHandler() {
        super(RoyalType.PLAYERATTACK, RoyalType.ENEMY);
    }

    @Override
    public void onCollisionBegin(Entity weapon, Entity enemy) {
        HealthComponent enemyHealth = enemy.getObject("enemyComponent");
        enemyHealth.deductHealth();

        /*
            weapon disappears on two conditions:
            1.) weapon is a projectile, is not paused, and does not attack multiple enemies
            2.) weapon is not a projectile and does not attack multiple enemies
         */

        if (((weapon.hasComponent(ProjectileComponent.class))
            && (weapon.hasComponent(AttackMultipleComponent.class))
            && (!weapon.getComponent(ProjectileComponent.class).isPaused())
            && (!weapon.getComponent(AttackMultipleComponent.class).isActive()))
            || ((!weapon.hasComponent(ProjectileComponent.class))
            && (weapon.hasComponent(AttackMultipleComponent.class))
            && (weapon.getComponent(AttackMultipleComponent.class).isPaused()))) {
            weapon.removeFromWorld();
        }
    }
}

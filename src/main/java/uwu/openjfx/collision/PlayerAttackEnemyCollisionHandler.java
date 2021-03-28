package uwu.openjfx.collision;

import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import uwu.openjfx.RoyalType;
import uwu.openjfx.components.AttackMultipleComponent;
import uwu.openjfx.components.EnemyComponent;
import uwu.openjfx.components.HealthComponent;

/*
    This class is responsible for when the hitbox of a player attack touches an enemy
    - Weapon disappears on two conditions:
    1.) if weapon is a projectile, is NOT paused, and does NOT attack multiple enemies
    2.) if a weapon is NOT a projectile and does NOT attack multiple enemies
    - Enemy takes damage on two conditions:
    1.) if weapon is a projectile and is NOT paused
    2.) if weapon is NOT a projectile
 */
public class PlayerAttackEnemyCollisionHandler extends CollisionHandler  {
    public PlayerAttackEnemyCollisionHandler() {
        super(RoyalType.PLAYERATTACK, RoyalType.ENEMY);
    }

    @Override
    public void onCollisionBegin(Entity weapon, Entity enemy) {
        if (((weapon.hasComponent(ProjectileComponent.class))
            && (weapon.hasComponent(AttackMultipleComponent.class))
            && (!weapon.getComponent(ProjectileComponent.class).isPaused())
            && (!weapon.getComponent(AttackMultipleComponent.class).isActive()))
            || ((!weapon.hasComponent(ProjectileComponent.class))
            && (weapon.hasComponent(AttackMultipleComponent.class))
            && (weapon.getComponent(AttackMultipleComponent.class).isPaused()))) {
            weapon.removeFromWorld();
        }

        if (((weapon.hasComponent(ProjectileComponent.class))
            && (!weapon.getComponent(ProjectileComponent.class).isPaused()))
            || (!weapon.hasComponent(ProjectileComponent.class))) {
            HealthComponent enemyHealth = enemy.getObject("enemyComponent");
            enemyHealth.deductHealth();
            if (enemy.hasComponent(EnemyComponent.class)) {
                enemy.getComponent(EnemyComponent.class).knockBackFromPlayer();
            }
        }
    }
}

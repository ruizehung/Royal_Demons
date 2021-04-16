package uwu.openjfx.collision;

import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import uwu.openjfx.RoyalType;
import uwu.openjfx.components.AttackDamageComponent;
import uwu.openjfx.components.DamageOverTimeComponent;
import uwu.openjfx.components.EnemyComponent;
import uwu.openjfx.components.ExplosionAtDistComponent;
import uwu.openjfx.components.PlayerComponent;

/*
    This class is responsible for when the hitbox of a player attack touches an enemy
    - Weapon disappears on three conditions:
    1.) if weapon is a projectile, is NOT paused, and does NOT attack multiple enemies
    2.) if a weapon is NOT a projectile and does NOT attack multiple enemies
    3.) if all of the above and attack is a bombed arrow
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
            && (weapon.hasComponent(AttackDamageComponent.class))
            && (!weapon.getComponent(ProjectileComponent.class).isPaused())
            && (!weapon.getComponent(AttackDamageComponent.class).isActive()))
            || ((!weapon.hasComponent(ProjectileComponent.class))
            && (weapon.hasComponent(AttackDamageComponent.class))
            && (weapon.getComponent(AttackDamageComponent.class).isPaused()))) {
            if (weapon.hasComponent(ExplosionAtDistComponent.class)
                && weapon.getComponent(ExplosionAtDistComponent.class).getExplodeColl()) {
                weapon.getComponent(ExplosionAtDistComponent.class).explode();
            }
            weapon.removeFromWorld();
        }

        if (((weapon.hasComponent(ProjectileComponent.class))
            && (!weapon.getComponent(ProjectileComponent.class).isPaused()))
            || (!weapon.hasComponent(ProjectileComponent.class))) {
            EnemyComponent enemyComponent = enemy.getObject("CreatureComponent");
            if (!weapon.hasComponent(DamageOverTimeComponent.class)) {
                enemyComponent.knockBackFromPlayer();
            }
            enemyComponent.deductHealth(
                weapon.getComponent(AttackDamageComponent.class).getAttackDamage(),
                PlayerComponent.getAttackPower(),
                enemyComponent.getBlockProbability(),
                enemyComponent.getArmorStat(),
                PlayerComponent.getPiercePow());
        }
    }

    @Override
    public void onCollision(Entity weapon, Entity enemy) {
        if (weapon.hasComponent(DamageOverTimeComponent.class)) {
            EnemyComponent enemyComponent = enemy.getObject("CreatureComponent");
            enemyComponent.deductHealth(
                weapon.getComponent(AttackDamageComponent.class).getAttackDamage(),
                PlayerComponent.getAttackPower(),
                enemyComponent.getBlockProbability(),
                enemyComponent.getArmorStat(),
                PlayerComponent.getPiercePow());
        }
    }
}

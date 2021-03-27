package uwu.openjfx.collision;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import uwu.openjfx.RoyalType;
import uwu.openjfx.components.EnemyComponent;
import uwu.openjfx.components.HealthComponent;
import uwu.openjfx.components.PlayerComponent;

/*
    This class is responsible for handling player TOUCHING enemy collision.
 */
public class PlayerEnemyCollisionHandler extends CollisionHandler {
    private double velocityDecrementer = 1.1;
    private boolean unstoppableX = false;
    private boolean unstoppableY = false;

    public PlayerEnemyCollisionHandler() {
        super(RoyalType.PLAYER, RoyalType.ENEMY);
    }

    protected void onCollisionBegin(Entity player, Entity enemy) {
        if ((enemy.getComponent(EnemyComponent.class).getPlayerLeavesRadius())) {
            velocityDecrementer = 2;
            unstoppableX = false;
            unstoppableY = false;
        }
    }

    public void onCollision(Entity player, Entity enemy) {
        PlayerComponent playerComponent = player.getComponent(PlayerComponent.class);
        EnemyComponent enemyComponent = enemy.getComponent(EnemyComponent.class);
        HealthComponent playerHealth = playerComponent;
        if (!playerHealth.getIsInvulnerable()) {
            playerHealth.deductHealth();
        }

        if (enemy.hasComponent(EnemyComponent.class)
            && enemy.getComponent(EnemyComponent.class).getMassEffect()) {
            PhysicsComponent enemyPhysics = enemy.getComponent(PhysicsComponent.class);
            PhysicsComponent playerPhysics = player.getComponent(PhysicsComponent.class);

            if (!unstoppableX) {
                enemyPhysics.setVelocityX(enemyPhysics.getVelocityX()
                    + (playerPhysics.getVelocityX() > 0
                    ? -velocityDecrementer : velocityDecrementer));
                velocityDecrementer *= 1.1;
            }

            if (!unstoppableY) {
                enemyPhysics.setVelocityY(enemyPhysics.getVelocityY()
                        + (playerPhysics.getVelocityY() > 0
                        ? -velocityDecrementer : velocityDecrementer));
                velocityDecrementer *= 1.1;
            }

            if (((Math.abs(enemyPhysics.getVelocityX()) > Math.abs(playerComponent.getSpeed()))
                || unstoppableX) && (playerComponent.isPressingMovementKeys())) {
                enemyPhysics.setVelocityX(-playerComponent.getSpeed() * Math.signum(enemyPhysics.getVelocityX()));
                enemy.getComponent(EnemyComponent.class).setCollidingWithPlayer(true);
                unstoppableX = true;
            }
            if (((Math.abs(enemyPhysics.getVelocityY()) > Math.abs(playerComponent.getSpeed()))
                    || unstoppableY) && (playerComponent.isPressingMovementKeys())) {
                enemyPhysics.setVelocityY(-playerComponent.getSpeed() * Math.signum(enemyPhysics.getVelocityY()));
                enemy.getComponent(EnemyComponent.class).setCollidingWithPlayer(true);
                unstoppableY = true;
            }

            if (playerPhysics.getVelocityX() == 0) {
                enemyPhysics.setVelocityX(0);
            }
            if (playerPhysics.getVelocityY() == 0) {
                enemyPhysics.setVelocityY(0);
            }
        }
    }
}

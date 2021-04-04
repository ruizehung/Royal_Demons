package uwu.openjfx.collision;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsComponent;
import uwu.openjfx.MainApp;
import uwu.openjfx.RoyalType;
import uwu.openjfx.components.BossComponent;
import uwu.openjfx.components.EnemyComponent;
import uwu.openjfx.components.PlayerComponent;

/*
    This class is responsible for handling player TOUCHING enemy collision.
    This class does the following:
    - if an enemy is denoted to be taken under Mass Effect (mass matters), then
    it will hold RESISTANCE to player pushing
    - player pushing a resistant enemy will eventually be pushing essentially a brick wall
    - player loses health when touching an enemy (if damage has not been taken recently)
 */
public class PlayerEnemyCollisionHandler extends CollisionHandler {
    private double velocityNormalizeVal = 1.1;
    private boolean equilibriumX = false;
    private boolean equilibriumY = false;
    private PlayerComponent playerComponent;
    private PhysicsComponent playerPhysics;
    private EnemyComponent enemyComponent;
    private PhysicsComponent enemyPhysics;

    public PlayerEnemyCollisionHandler() {
        super(RoyalType.PLAYER, RoyalType.ENEMY);
    }

    protected void onCollisionBegin(Entity player, Entity enemy) {
        enemyComponent = enemy.getObject("CreatureComponent");

        /*
            When colliding with an enemy for the first time, evaluate whether or not
            player has left and returned, or has already pushed this Mass affected enemy prior.
         */
        if ((enemyComponent.getPlayerLeavesRadius())) {
            velocityNormalizeVal = 2; // reset velocityNormalizeVal
            equilibriumX = false;
            equilibriumY = false;
        }
    }

    public void onCollision(Entity player, Entity enemy) {
        playerComponent = player.getComponent(PlayerComponent.class);
        if (!playerComponent.isInvulnerable()) {
            playerComponent.deductHealth(1);
        }

        if (!MainApp.isIsTesting()) {
            if (enemyComponent.getMassEffect()) {
                playerPhysics = player.getComponent(PhysicsComponent.class);
                enemyPhysics = enemy.getComponent(PhysicsComponent.class);
                reachEquilibrium();
                stabilizeEquilibrium();
            }
        }
    }

    private void reachEquilibrium() {
        /*
            If enemy is not unstoppable, then start slowly resisting the player's pushing.
            - If player is pushing right, then push left
            - If player is pushing left, then push right
            - If player is pushing down, then push up
            - If player is pushing up, then push down
         */
        if (!equilibriumX) {
            enemyPhysics.setVelocityX(enemyPhysics.getVelocityX()
                    + (playerPhysics.getVelocityX() > 0
                    ? -velocityNormalizeVal : velocityNormalizeVal));
            velocityNormalizeVal *= 1.1;
        }

        if (!equilibriumY) {
            enemyPhysics.setVelocityY(enemyPhysics.getVelocityY()
                    + (playerPhysics.getVelocityY() > 0
                    ? -velocityNormalizeVal : velocityNormalizeVal));
            velocityNormalizeVal *= 1.1;
        }

        /*
            If player is not moving up/down (only moving left/right), then enemy should
            also not be moving up/down.
            If player is not moving left/right (only moving up/down), then enemy should
            also not be moving left/right.
         */
        if (playerPhysics.getVelocityX() == 0) {
            enemyPhysics.setVelocityX(0);
        }
        if (playerPhysics.getVelocityY() == 0) {
            enemyPhysics.setVelocityY(0);
        }
    }

    private void stabilizeEquilibrium() {
        /*
            If enemy is pushing at the same magnitude as player, then equilibrium has been
            achieved. Set enemy's velocity to be permanently equal with player's velocity
            in the opposite direction. If player is not moving, then player is no longer
            applying force to the enemy, therefore stop applying equal and opposite force.
         */
        if (((Math.abs(enemyPhysics.getVelocityX()) > Math.abs(playerComponent.getSpeed()))
                || equilibriumX) && (playerComponent.isPressingMovementKeys())) {
            enemyPhysics.setVelocityX(
                    -playerComponent.getSpeed() * Math.signum(enemyPhysics.getVelocityX()));
            enemyComponent.setCollidingWithPlayer(true);
            equilibriumX = true;
        }
        if (((Math.abs(enemyPhysics.getVelocityY()) > Math.abs(playerComponent.getSpeed()))
                || equilibriumY) && (playerComponent.isPressingMovementKeys())) {
            enemyPhysics.setVelocityY(
                    -playerComponent.getSpeed() * Math.signum(enemyPhysics.getVelocityY()));
            enemyComponent.setCollidingWithPlayer(true);
            equilibriumY = true;
        }
    }
}
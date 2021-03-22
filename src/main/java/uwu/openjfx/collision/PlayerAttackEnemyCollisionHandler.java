package uwu.openjfx.collision;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.IDComponent;
import com.almasb.fxgl.physics.CollisionHandler;
import uwu.openjfx.MapGeneration.Room;
import uwu.openjfx.RoyalType;
import uwu.openjfx.components.EnemyComponent;
import uwu.openjfx.components.HealthComponent;

public class PlayerAttackEnemyCollisionHandler extends CollisionHandler  {
    public PlayerAttackEnemyCollisionHandler() {
        super(RoyalType.PLAYERATTACK, RoyalType.ENEMY);
    }

    @Override
    protected void onCollisionBegin(Entity sword, Entity enemy) {
        HealthComponent enemyHealth = enemy.getObject("enemyComponent");
        enemyHealth.deductHealth();
        IDComponent idComponent = enemy.getComponent(IDComponent.class);
        Room curRoom = FXGL.geto("curRoom");
        curRoom.setEntityData(idComponent.getId(), "isAlive", 0);
    }
}

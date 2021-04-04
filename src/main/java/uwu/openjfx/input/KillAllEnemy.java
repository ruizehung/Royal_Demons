package uwu.openjfx.input;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import org.jetbrains.annotations.NotNull;
import uwu.openjfx.RoyalType;
import uwu.openjfx.components.EnemyComponent;
import uwu.openjfx.components.Life;

import java.util.List;

public class KillAllEnemy extends UserAction {

    public KillAllEnemy(@NotNull String name) {
        super(name);
    }

    @Override
    protected void onActionBegin() {
        List<Entity> enemies = FXGL.getGameWorld().getEntitiesByType(RoyalType.ENEMY);
        for (Entity enemy: enemies) {
            EnemyComponent enemyComponent = enemy.getObject("enemyComponent");
            enemyComponent.getLife().setXY(enemy.getX(), enemy.getY());
            enemyComponent.getLife().deductHealth(Integer.MAX_VALUE);
            enemyComponent.removeFromWorld();
        }
    }
}

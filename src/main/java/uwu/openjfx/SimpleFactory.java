package uwu.openjfx;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import javafx.geometry.Point2D;

public class SimpleFactory implements EntityFactory {
    @Spawns("enemy")
    public Entity newEnemy(SpawnData data) {
        return FXGL.entityBuilder(data)
//                .view(new Rectangle(40, 40, Color.RED))
                .view("skelet_idle_anim_f0_32x32.png")
                .with(new ProjectileComponent(new Point2D(1, 0), 150))
                .build();

    }

    @Spawns("ally")
    public Entity newAlly(SpawnData data) {
        return FXGL.entityBuilder(data)
                .view("lizard_m_idle_anim_f0_40x70.png")
//                .with(new ProjectileComponent(new Point2D(-1, 0), 150))
                .build();
    }
}

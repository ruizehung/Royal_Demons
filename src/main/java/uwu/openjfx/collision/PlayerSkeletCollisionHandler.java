package uwu.openjfx.collision;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.components.IDComponent;
import com.almasb.fxgl.physics.CollisionHandler;
import uwu.openjfx.MapGeneration.Room;
import uwu.openjfx.RoyalType;

public class PlayerSkeletCollisionHandler extends CollisionHandler {

    public PlayerSkeletCollisionHandler() {
        super(RoyalType.PLAYER, RoyalType.ENEMY);
    }

    @Override
    protected void onCollisionBegin(Entity player, Entity skelet) {
//        skelet.removeFromWorld();
//        IDComponent idComponent = skelet.getComponent(IDComponent.class);
//        Room curRoom = FXGL.geto("curRoom");
//        curRoom.setEntityData(idComponent.getId(), "isAlive", 0);
    }
}

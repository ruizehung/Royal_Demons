package uwu.openjfx.collision;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.IDComponent;
import com.almasb.fxgl.physics.CollisionHandler;
import uwu.openjfx.RoomData;
import uwu.openjfx.RoyalType;

public class PlayerSkeletCollisionHandler extends CollisionHandler {

    public PlayerSkeletCollisionHandler() {
        super(RoyalType.PLAYER, RoyalType.SKELET);
    }

    @Override
    protected void onCollisionBegin(Entity player, Entity skelet) {
        skelet.removeFromWorld();
        IDComponent idComponent = skelet.getComponent(IDComponent.class);
        RoomData curRoomData = FXGL.geto("curRoom");
        curRoomData.setEntityData(idComponent.getId(), "isAlive", 0);
    }
}

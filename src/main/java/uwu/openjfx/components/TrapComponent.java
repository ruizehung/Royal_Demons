package uwu.openjfx.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.IDComponent;
import uwu.openjfx.MapGeneration.Room;


public class TrapComponent extends Component {
    public void trigger() {
        final String action = getEntity().getProperties().getString("action");
        switch (action) {
            case "remove":
                getEntity().removeFromWorld();
                break;
            default:
        }

        Room curRoom = FXGL.geto("curRoom");
        IDComponent idComponent = entity.getComponent(IDComponent.class);
        curRoom.setEntityData(idComponent.getId(), "triggered", 1);
    }
}

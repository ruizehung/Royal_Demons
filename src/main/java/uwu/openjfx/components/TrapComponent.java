package uwu.openjfx.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.IDComponent;
import javafx.scene.image.ImageView;
import uwu.openjfx.MapGeneration.Room;


public class TrapComponent extends Component {
    public void trigger() {
        final String action = getEntity().getProperties().getString("action");
        switch (action) {
        case "remove":
            getEntity().removeFromWorld();
            break;
        case "changeView":
            getEntity().getViewComponent().clearChildren();
            getEntity().getViewComponent().addChild(new ImageView(FXGL.image(
                    getEntity().getProperties().getString("newView")
            )));
            break;
        default:
        }

        // save the fact that this trap has been triggered to entity data in the room
        // so when player goes back to this room it is still triggered
        Room curRoom = FXGL.geto("curRoom");
        IDComponent idComponent = entity.getComponent(IDComponent.class);
        curRoom.setEntityData(idComponent.getId(), "triggered", 1);
    }
}

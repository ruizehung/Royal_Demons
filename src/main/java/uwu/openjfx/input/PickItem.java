package uwu.openjfx.input;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.UserAction;

public class PickItem extends UserAction {
    public PickItem(String name) {
        super(name);
    }
    @Override
    protected void onActionBegin() {
        FXGL.set("Epressed", true);
    }

    @Override
    protected void onActionEnd() {
        FXGL.set("Epressed", false);
    }
}

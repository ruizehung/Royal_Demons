package uwu.openjfx.input;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.UserAction;

public class UseItem extends UserAction {
    public UseItem(String name) {
        super(name);
    }

    @Override
    protected void onActionBegin() {
        FXGL.set("Fpressed", true);
    }

    @Override
    protected void onActionEnd() {
        FXGL.set("Fpressed", false);
    }
}

package uwu.openjfx.input;

import com.almasb.fxgl.input.UserAction;
import uwu.openjfx.UI;

public class UseHealthPot extends UserAction {
    public UseHealthPot(String useHealthPot) {
        super(useHealthPot);
    }

    @Override
    protected void onActionBegin() {
        UI.useHealthPot();
    }
}

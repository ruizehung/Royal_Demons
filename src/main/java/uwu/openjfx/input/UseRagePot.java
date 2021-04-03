package uwu.openjfx.input;

import com.almasb.fxgl.input.UserAction;
import uwu.openjfx.UI;

public class UseRagePot extends UserAction {
    public UseRagePot(String useRagePot) {
        super(useRagePot);
    }

    @Override
    protected void onActionBegin() {
        UI.useRagePot();
    }
}

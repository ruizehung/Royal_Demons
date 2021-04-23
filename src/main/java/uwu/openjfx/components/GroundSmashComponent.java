package uwu.openjfx.components;

import com.almasb.fxgl.entity.component.Component;
import uwu.openjfx.MainApp;

public class GroundSmashComponent extends Component {
    private int counter = 0;

    @Override
    public void onUpdate(double tpf) {
        counter++;
        if (counter >= 4 * 60) {
            counter = 0;
            MainApp.addToHitBoxDestruction(getEntity());
        }
    }
}

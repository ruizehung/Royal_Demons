package uwu.openjfx.items;

import com.almasb.fxgl.entity.Entity;

public class CannotUse implements Usable {

    @Override
    public void use(Entity entity) {
        System.err.println("Error! Using CannotUse Item...");
    }

    @Override
    public boolean isUsable() {
        return false;
    }
}

package uwu.openjfx.items;

import com.almasb.fxgl.entity.Entity;

public interface Usable {
    void use(Entity entity);

    boolean isUsable();
}

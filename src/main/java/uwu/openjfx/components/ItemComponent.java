package uwu.openjfx.components;


import com.almasb.fxgl.entity.component.Component;

public class ItemComponent extends Component {
    String name;

    public ItemComponent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

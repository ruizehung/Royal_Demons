package uwu.openjfx;

import com.almasb.fxgl.core.collection.PropertyMap;
import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.IDComponent;
import com.almasb.fxgl.net.BundleUDPMessageReader;
import jdk.jshell.spi.SPIResolutionException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class RoomData {
    private String name;
    private Map<Integer, Map<String, Integer>> entitiesData;

    public RoomData(String name, List<Entity> entityList) {
        this.name = name;
        entitiesData = new HashMap<>();
        for (Entity entity : entityList) {
            if (entity.isType(RoyalType.SKELET)) {
                IDComponent idComponent = entity.getComponent(IDComponent.class);
                Map<String, Integer> properties = new HashMap<>();
                properties.put("isAlive", 1);
                entitiesData.put(idComponent.getId(), properties);
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEntityData(int id, String propertyName) {
        return entitiesData.get(id).get(propertyName);
    }

    public void setEntityData(int id, String propertyName, int val) {
        entitiesData.get(id).put(propertyName, val);
    }

}

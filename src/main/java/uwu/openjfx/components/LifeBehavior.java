package uwu.openjfx.components;

import javafx.beans.property.IntegerProperty;

public interface LifeBehavior {
    void increaseHealth(int point);

    void deductHealth(int point);

    boolean isInvulnerable();

    int getHealthPoints();

    IntegerProperty getHealthIntegerProperty();

    void setHealthPoints(int healthPoints);

    int getMaxHealthPoints();

    void setMaxHealthPoints(int maxHealthPoints);

    void setDieBehavior(DieBehavior dieBehavior);

    DieBehavior getDieBehavior();

    boolean dead();

    void die();

}

package uwu.openjfx.behaviors;

import javafx.beans.property.IntegerProperty;

public interface HasLife {
    void increaseHealth(int point);

    void deductHealth(double point, double attackPower,
                      double blockProb, double armor, int pierce);

    boolean isInvulnerable();

    int getHealthPoints();

    IntegerProperty getHealthIntegerProperty();

    void setHealthPoints(int healthPoints);

    int getMaxHealthPoints();

    void setMaxHealthPoints(int maxHealthPoints);

    void setDieBehavior(Behavior dieBehavior);

    Behavior getDieBehavior();

    boolean dead();

    void die();

}

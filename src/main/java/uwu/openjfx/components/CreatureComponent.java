package uwu.openjfx.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import uwu.openjfx.MainApp;
import uwu.openjfx.behaviors.Behavior;
import uwu.openjfx.behaviors.DoNothing;
import uwu.openjfx.behaviors.HasLife;

import java.util.Timer;

public class CreatureComponent extends Component implements HasLife {

    private int healthPoints;
    private int maxHealthPoints;
    private boolean isInvulnerable;
    private IntegerProperty playerHealthIntegerProperty;
    private Behavior dieBehavior;

    public CreatureComponent(int healthPoints, int maxHealthPoints, Behavior dieBehavior) {
        this.healthPoints = healthPoints;
        this.maxHealthPoints = maxHealthPoints;
        this.playerHealthIntegerProperty = new SimpleIntegerProperty(healthPoints);
        setDieBehavior(dieBehavior);
    }

    public CreatureComponent(int healthPoints, int maxHealthPoints) {
        this(healthPoints, maxHealthPoints, new DoNothing());
    }

    @Override
    public void increaseHealth(int point) {
        this.healthPoints = Math.min(this.healthPoints + point, maxHealthPoints);
        playerHealthIntegerProperty.set(healthPoints);
        if (this instanceof BossComponent) {
            BossComponent.setBossHealthProperty(healthPoints);
        }
    }

    @Override
    public void deductHealth(double point, double attackPower,
                             double blockProb, double armor, int pierce) {
        /*
            point: raw damage to be dealt
            attackPower: 1.0 means deal raw damage, 3.0 means deal triple times raw damage
            blockProb: % chance of being damaged (0% means never block, 100% means always block)
            armor: 1.0 means get hit full force, 2.0 means get hit by half the force
            pierce: 1.0 means normal chance to block. 0 means pierce through their block attempt
         */
        if (armor > 0) { // cannot divide by 0
            int blockRand = (int) (Math.random() * 100) + 1;
            if (blockRand <= blockProb * pierce) {
                if (!MainApp.isIsTesting()) {
                    FXGL.play("block.wav");
                }
                return;
            }
            double damageDealt = (point * attackPower) / armor;
            if (this instanceof EnemyComponent) {
                if (damageDealt > getHealthPoints()) {
                    PlayerComponent.addToDamageDealt(getHealthPoints());
                } else {
                    PlayerComponent.addToDamageDealt(damageDealt);
                }
            }
            if (this instanceof BossComponent) {
                BossComponent.setBossHealthProperty(
                    (int) (healthPoints - damageDealt));
            }
            healthPoints -= damageDealt;
            if (healthPoints <= 0) { // die
                healthPoints = 0;
                die();
                if (this instanceof EnemyComponent) {
                    PlayerComponent.addToMonstersKilled();
                }
            } else {
                isInvulnerable = true;
                invulnerability();
            }
            playerHealthIntegerProperty.set(healthPoints);
        }
    }

    private void invulnerability() {
        isInvulnerable = true;
        Timer t = new java.util.Timer();
        t.schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        isInvulnerable = false;
                        t.cancel();
                    }
                }, !(this instanceof EnemyComponent) ? 2000 : 100
        );
    }

    @Override
    public int getHealthPoints() {
        return healthPoints;
    }

    @Override
    public void setHealthPoints(int healthPoints) {
        playerHealthIntegerProperty.set(healthPoints);
        this.healthPoints = healthPoints;
    }

    @Override
    public int getMaxHealthPoints() {
        return maxHealthPoints;
    }

    @Override
    public void setMaxHealthPoints(int maxHealthPoints) {
        this.maxHealthPoints = maxHealthPoints;
    }

    @Override
    public void setDieBehavior(Behavior dieBehavior) {
        this.dieBehavior = dieBehavior;
    }

    @Override
    public Behavior getDieBehavior() {
        return dieBehavior;
    }

    @Override
    public boolean dead() {
        return healthPoints <= 0;
    }

    @Override
    public boolean isInvulnerable() {
        return isInvulnerable;
    }

    @Override
    public IntegerProperty getHealthIntegerProperty() {
        return playerHealthIntegerProperty;
    }

    @Override
    public void die() {
        dieBehavior.act(getEntity());
    }
    
}

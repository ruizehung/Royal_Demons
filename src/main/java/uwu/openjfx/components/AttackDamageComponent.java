package uwu.openjfx.components;

import com.almasb.fxgl.entity.component.Component;

/*
    This class is allows a weapon to attack multiple enemies at once, rather than
    disappearing at the first enemy hit.
 */
public class AttackDamageComponent extends Component {
    private boolean active;
    private double attackDamage;
    public AttackDamageComponent(boolean active, double attackDamage) {
        this.active = active;
        this.attackDamage = attackDamage;
    }
    public AttackDamageComponent() { }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public double getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(double attackDamage) {
        this.attackDamage = attackDamage;
    }
}

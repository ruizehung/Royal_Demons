import com.almasb.fxgl.entity.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uwu.openjfx.MainApp;
import uwu.openjfx.components.BossComponent;

public class TestFinalBoss {
    @BeforeEach
    public void init() {
        MainApp.setIsTesting(true);
    }

    //jason 5 version 2
    @Test
    void testFinalBossTransformation() {
        int origHealth = 50;
        Entity boss = new Entity();
        BossComponent bossComponent = new BossComponent(origHealth, "", 0, 0,
            0, "melee");
        boss.addComponent(bossComponent);
        boss.setProperty("CreatureComponent", bossComponent);
        String transform
            = bossComponent.transformBoss("", 0, 0, 0, "ranged");
        assert transform.equals("transformed");
    }
}

package uwu.openjfx.components;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;
import uwu.openjfx.MainApp;
import uwu.openjfx.weapons.Bow2;
import uwu.openjfx.weapons.MagicStaff0;
import uwu.openjfx.weapons.MagicStaff1;

import static com.almasb.fxgl.dsl.FXGL.spawn;

/*
    This class is responsible for creating an explosion AOE effect.
 */
public class ExplosionAtDistComponent extends Component {
    private Entity explosionHitBox;
    private Point2D origDist;
    private final boolean explodeColl;

    public ExplosionAtDistComponent(boolean explodeOnCollision) {
        this.explodeColl = explodeOnCollision;
    }

    public void onAdded() {
        origDist = new Point2D(getEntity().getX(), getEntity().getY());
    }
    @Override
    public void onUpdate(double tpf) {
        if (explodeColl) {
            double x = getEntity().getX() - origDist.getX();
            double y = getEntity().getY() - origDist.getY();
            if (Math.sqrt(x * x + y * y) > 250) {
                explode();
                getEntity().removeFromWorld();
            }
        }
    }

    public void explode() {
        int hitBoxWidth = 175;
        int hitBoxHeight = 175;
        int novaDuration = 200;
        int novaOffsetX = 30;
        if (PlayerComponent.getCurrentWeapon() instanceof MagicStaff0) {
            FXGL.play("skills/explosion_small.wav");
            int frameWidth = 96;
            int frameHeight = 96;
            Entity blueNova = spawn("weapon",
                new SpawnData(
                    getEntity().getX() - ((double) frameWidth) + novaOffsetX,
                    getEntity().getY() - ((double) frameHeight)).
                    put("weaponFile", "blueNova_96x96").
                    put("duration", novaDuration).
                    put("frameWidth", frameWidth).
                    put("frameHeight", frameHeight).
                    put("fpr", 28).
                    put("weaponSprite", "assets/textures/ui/weapons/staff0_ui.png"));
            blueNova.setScaleX(2);
            blueNova.setScaleY(2);
        } else if (PlayerComponent.getCurrentWeapon() instanceof MagicStaff1) {
            FXGL.play("skills/explosion_med.wav");
            int frameWidth = 96;
            int frameHeight = 96;
            Entity orangeNova = spawn("weapon",
                new SpawnData(
                    getEntity().getX() - ((double) frameWidth) + novaOffsetX,
                    getEntity().getY() - ((double) frameHeight)).
                    put("weaponFile", "orangeNova_96x96").
                    put("duration", novaDuration).
                    put("frameWidth", frameWidth).
                    put("frameHeight", frameHeight).
                    put("fpr", 28).
                    put("weaponSprite", "assets/textures/ui/weapons/staff0_ui.png"));
            orangeNova.setScaleX(2);
            orangeNova.setScaleY(2);
        } else if (PlayerComponent.getCurrentWeapon() instanceof Bow2) {
            FXGL.play("skills/explosion_med.wav");
            int frameWidth = 64;
            int frameHeight = 64;
            Entity orangeNova = spawn("weapon",
                new SpawnData(
                    getEntity().getX() - ((double) (frameWidth * 3) / 2) + novaOffsetX,
                    getEntity().getY() - ((double) (frameHeight * 3) / 2)).
                    put("weaponFile", "bombArrowExplosion_64x64").
                    put("duration", 500).
                    put("frameWidth", frameWidth).
                    put("frameHeight", frameHeight).
                    put("fpr", 44).
                    put("weaponSprite", "assets/textures/ui/weapons/staff0_ui.png"));
            orangeNova.setScaleX(3);
            orangeNova.setScaleY(3);
        }

        explosionHitBox = spawn("meleeSwordHitBox",
            new SpawnData(getEntity().getX() - 55,
                getEntity().getY() - ((double) hitBoxHeight / 2)).
                put("width", hitBoxWidth).put("height", hitBoxHeight).
                put("damage", 50.0));
        Runnable runnable = () -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            MainApp.addToHitBoxDestruction(explosionHitBox);
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public boolean getExplodeColl() {
        return explodeColl;
    }
}

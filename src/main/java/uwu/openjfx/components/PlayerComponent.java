package uwu.openjfx.components;

import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.util.Duration;
import uwu.openjfx.DieScreenMenu;
import uwu.openjfx.weapons.Bow_0;
import uwu.openjfx.weapons.GoldenSword_0;
import uwu.openjfx.weapons.MagicStaff_0;
import uwu.openjfx.weapons.Weapon;

import java.util.Timer;

public class PlayerComponent extends HealthComponent {

    private PhysicsComponent physics;

    private AnimatedTexture texture; // current player animation

    private AnimationChannel animIdle, animWalk, animAutoAttack, animSwordUltimate1, animSwordUltimate2;

    private Weapon currentWeapon; // Player's current weapon

    private double currMouseX; // mouse input for x
    private double currMouseY; // mouse input for y

    private boolean isPressingMovementKeys = false; // Player is moving with WASD / Arrow keys
    private boolean prepAttack = false; // Player has initiated attack charge/channel
    private boolean startAttack = false; // Player does the actual attack
    private boolean ultimateActivated = false; // Player is using ultimate
    private boolean ultimateCD = false; // how long until Player can activate Ultimate again

    //TODO: remove temp vars and put in char state class
    public static String playerName;
    public static String playerWeapon;
    public static String gameDifficulty;
    public static int gold;

    public PlayerComponent(int healthPoints) {
        super(healthPoints);
        if (currentWeapon == null) {
            switch (playerWeapon) {
                case "Sword":
                    currentWeapon = new GoldenSword_0();
                    break;
                case "Bow":
                    currentWeapon = new Bow_0();
                    break;
                case "Wand":
                    currentWeapon = new MagicStaff_0();
                    break;
                default:
            }
        }
        animIdle = new AnimationChannel(FXGL.image("creatures/lizard_m_40x55.png"), 9,
                40, 55, Duration.seconds(0.5), 0, 3);
        animWalk = new AnimationChannel(FXGL.image("creatures/lizard_m_40x55.png"), 9,
                40, 55, Duration.seconds(0.5), 4, 7);
        animAutoAttack = new AnimationChannel(FXGL.image("creatures/lizard_m_40x55.png"), 9,
                40, 55, Duration.millis(currentWeapon.getDuration(ultimateActivated) / 1000), 8, 8);

        texture = new AnimatedTexture(animIdle);
        texture.loop();
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(20, 25));
        entity.getViewComponent().addChild(texture);
    }

    @Override
    public void onUpdate(double tpf) {
        if (!isPressingMovementKeys) {
            normalizeVelocityX();
            normalizeVelocityY();
        }
        // region Movement
        if (!prepAttack) { // if Player has initiated an attack, then do not perform walk/idle animations
            if (physics.isMoving()) {
                if (texture.getAnimationChannel() != animWalk) {
                    texture.loopAnimationChannel(animWalk);
                }
            } else {
                if (texture.getAnimationChannel() != animIdle) {
                    texture.loopAnimationChannel(animIdle);
                }
            }
        }
        //endregion

        //region Player performs attack
        if (startAttack) { // Player performs the actual attack
            currentWeapon.attack(getEntity(), currMouseX, currMouseY);
            startAttack = false;
            prepAttack = false;
            ultimateActivated = false;
        }
        // Player ultimate is on cooldown from recent activation
        if (ultimateCD) {
            FXGL.getGameTimer().runAtInterval(() -> {
                ultimateCD = false;
                FXGL.getGameTimer().clear();
            }, Duration.seconds(2));
        }
        // endregion
    }

    // region Player Movement
    // As long as player has not initiated an attack, can move
    private void normalizeVelocityX() {
        if (physics.getVelocityX() != 0) {
            if (physics.getVelocityX() > 0) {
                physics.setVelocityX(physics.getVelocityX() - 1);
                if (physics.getVelocityX() < 0) {
                    physics.setVelocityX(0);
                }
            } else if (physics.getVelocityX() < 0) {
                physics.setVelocityX(physics.getVelocityX() + 1);
                if (physics.getVelocityX() > 0) {
                    physics.setVelocityX(0);
                }
            }
        }
    }

    private void normalizeVelocityY() {
        if (physics.getVelocityY() != 0) {
            if (physics.getVelocityY() > 0) {
                physics.setVelocityY(physics.getVelocityY() - 1);
                if (physics.getVelocityY() < 0) {
                    physics.setVelocityY(0);
                }
            } else if (physics.getVelocityY() < 0) {
                physics.setVelocityY(physics.getVelocityY() + 1);
                if (physics.getVelocityY() > 0) {
                    physics.setVelocityY(0);
                }
            }
        }
    }

    public void left() {
        if (!prepAttack) {
            getEntity().setScaleX(-1);
            physics.setVelocityX(-170);
        }
    }

    public void right() {
        if (!prepAttack) {
            getEntity().setScaleX(1);
            physics.setVelocityX(170);
        }
    }

    public void up() {
        if (!prepAttack) {
            physics.setVelocityY(-170);
        }
    }

    public void down() {
        if (!prepAttack) {
            physics.setVelocityY(170);
        }
    }

    public void stop() {
        physics.setVelocityX(0);
        physics.setVelocityY(0);
    }
    // endregion

    // region Player Attack functions

    public void autoAttack(boolean ultimateActivated) {
        this.ultimateActivated = ultimateActivated;
        if (currMouseX > entity.getX() + 20) { // turn player in direction of mouse
            entity.setScaleX(1);
        } else {
            entity.setScaleX(-1);
        }
        texture.playAnimationChannel(animAutoAttack); // play attack animation
        prepAttack = true; // Player has initiated attack
        stop(); // stop moving
        Timer t = new java.util.Timer(); // Player performs the actual attack after duration amount of milliseconds
        t.schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        startAttack = true; // Player performs the actual attack
                        t.cancel();
                    }
                }, currentWeapon.getDuration(ultimateActivated)
        );
        currentWeapon.prepAttack(getEntity());
    }

    public boolean isAttacking() {
        return prepAttack; // used in MainApp for LMB/RMB input, confirmation of whether or not Player is attacking
    }

    private Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    private void setCurrentWeapon(Weapon weapon) {
        currentWeapon = weapon;
    }

    public void setPressingMovementKeys(boolean moving) {
        isPressingMovementKeys = moving;
    }

    // endregion

    public void setMousePosition(double mouseXPos, double mouseYPos) {
        currMouseX = mouseXPos;
        currMouseY = mouseYPos;
    }

    @Override
    public void die() {
        FXGL.getSceneService().pushSubScene(new DieScreenMenu(MenuType.GAME_MENU));
    }
}

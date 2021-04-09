package uwu.openjfx.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.util.Duration;
import uwu.openjfx.MainApp;
import uwu.openjfx.UI;
import uwu.openjfx.behaviors.GameOverWhenDie;
import uwu.openjfx.weapons.Bow0;
import uwu.openjfx.weapons.GoldenSword0;
import uwu.openjfx.weapons.MagicStaff0;
import uwu.openjfx.weapons.Weapon;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/*
    This class is responsible for the following:
    - Animation of the player: movement, idleness, attacking
    - Being the caller for attacking, which in turn spawns the necessary weapon of type Weapon
 */
public class PlayerComponent extends CreatureComponent {

    private PhysicsComponent physics;

    private AnimatedTexture texture; // current player animation

    private AnimationChannel animIdle;
    private AnimationChannel animWalk;
    private AnimationChannel animAutoAttack;

    private static Weapon currentWeapon; // Player's current weapon
    private static List<Weapon> weaponInventoryList = new ArrayList<>();
    private static double attackPower = 1;

    private double currMouseX; // mouse input for x
    private double currMouseY; // mouse input for y

    private double speed = 170;
    private boolean overDrive = false;
    private double velocityDecrementer = 5;
    private boolean isPressingMovementKeys = false; // Player is moving with WASD / Arrow keys
    private boolean prepAttack = false; // Player has initiated attack charge/channel
    private boolean startAttack = false; // Player does the actual attack
    private boolean ultimateActivated = false; // Player is using ultimate
    private boolean ultimateCD = false; // how long until Player can activate Ultimate again

    // Todo: char state class?
    private static String playerName;
    private static String playerWeapon;
    private static String gameDifficulty;

    // Todo: inventory-esque things, move?
    private static int gold;
    private static int healthPotAmount;
    private static int ragePotAmount;

    public PlayerComponent(int maxHealthPoints) {
        super(maxHealthPoints, maxHealthPoints, new GameOverWhenDie());

        if (!MainApp.isIsTesting()) {
            animIdle = new AnimationChannel(FXGL.image("creatures/lizard_m_40x55.png"), 9,
                    40, 55, Duration.seconds(0.5), 0, 3);
            animWalk = new AnimationChannel(FXGL.image("creatures/lizard_m_40x55.png"), 9,
                    40, 55, Duration.seconds(0.5), 4, 7);
            animAutoAttack = new AnimationChannel(FXGL.image("creatures/lizard_m_40x55.png"), 9,
                    40, 55, Duration.millis(currentWeapon.getDuration(ultimateActivated) / 1000f),
                    8, 8);

            texture = new AnimatedTexture(animIdle);
            texture.loop();
        }

    }


    @Override
    public void onAdded() {
        if (!MainApp.isIsTesting()) {
            entity.getTransformComponent().setScaleOrigin(new Point2D(20, 25));
            entity.getViewComponent().addChild(texture);
        }
    }

    @Override
    public void onUpdate(double tpf) {
        if (!isPressingMovementKeys || overDrive) {
            normalizeVelocityX();
            normalizeVelocityY();
        }
        // region Movement
        if (!prepAttack) {
            // if Player has initiated an attack, then do not perform walk/idle animations
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
                physics.setVelocityX(physics.getVelocityX() - velocityDecrementer);
                if (physics.getVelocityX() < 0) {
                    physics.setVelocityX(0);
                }
            } else if (physics.getVelocityX() < 0) {
                physics.setVelocityX(physics.getVelocityX() + velocityDecrementer);
                if (physics.getVelocityX() > 0) {
                    physics.setVelocityX(0);
                }
            }
        }
    }

    private void normalizeVelocityY() {
        if (physics.getVelocityY() != 0) {
            if (physics.getVelocityY() > 0) {
                physics.setVelocityY(physics.getVelocityY() - velocityDecrementer);
                if (physics.getVelocityY() < 0) {
                    physics.setVelocityY(0);
                }
            } else if (physics.getVelocityY() < 0) {
                physics.setVelocityY(physics.getVelocityY() + velocityDecrementer);
                if (physics.getVelocityY() > 0) {
                    physics.setVelocityY(0);
                }
            }
        }
    }

    public void left() {
        if (!prepAttack) {
            getEntity().setScaleX(-1);
            physics.setVelocityX(-speed);
        }
    }

    public void right() {
        if (!prepAttack) {
            getEntity().setScaleX(1);
            physics.setVelocityX(speed);
        }
    }

    public void up() {
        if (!prepAttack) {
            physics.setVelocityY(-speed);
        }
    }

    public void down() {
        if (!prepAttack) {
            physics.setVelocityY(speed);
        }
    }

    public void stop() {
        physics.setVelocityX(0);
        physics.setVelocityY(0);
    }

    public void setOverDrive(boolean overDrive) {
        this.overDrive = overDrive;
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
        // Player performs the actual attack after duration amount of milliseconds
        Timer t = new java.util.Timer();
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

    public double getSpeed() {
        return speed;
    }

    public boolean isPressingMovementKeys() {
        return isPressingMovementKeys;
    }

    public boolean isAttacking() {
        // used in MainApp for LMB/RMB input, confirmation of whether or not Player is attacking
        return prepAttack;
    }

    public static Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    public static void setCurrentWeapon(Weapon weapon) {
        currentWeapon = weapon;
        UI.setWeaponProperty(currentWeapon.getWeaponSprite());
    }

    public static double getAttackPower() {
        return attackPower;
    }

    public static void setAttackPower(double attackPower) {
        PlayerComponent.attackPower = attackPower;
    }

    public void setPressingMovementKeys(boolean moving) {
        isPressingMovementKeys = moving;
    }

    // endregion

    public void setMousePosition(double mouseXPos, double mouseYPos) {
        currMouseX = mouseXPos;
        currMouseY = mouseYPos;
    }

    public void addGold(int gold) {
        PlayerComponent.gold += gold;
        UI.getGoldProperty().set(PlayerComponent.gold);
    }

    public static void setGold(int gold) {
        PlayerComponent.gold = gold;
        UI.getGoldProperty().set(PlayerComponent.gold);
    }

    public static int getGold() {
        return gold;
    }

    public static String getPlayerName() {
        return playerName;
    }

    public static void setPlayerName(String playerName) {
        PlayerComponent.playerName = playerName;
    }

    public static String getPlayerWeapon() {
        return playerWeapon;
    }

    public static void setPlayerWeapon(String playerWeapon) {
        PlayerComponent.playerWeapon = playerWeapon;
    }

    public static String getGameDifficulty() {
        return gameDifficulty;
    }

    public static void setGameDifficulty(String gameDifficulty) {
        PlayerComponent.gameDifficulty = gameDifficulty;
    }

    public static List<Weapon> getWeaponInventoryList() {
        return weaponInventoryList;
    }

    public static void addWeaponToInventory(Weapon weapon) {
        for (Weapon playerWeapon: weaponInventoryList) {
            if (playerWeapon.getName().equals(weapon.getName())) {
                return;
            }
        }
        weaponInventoryList.add(weapon);
    }

}

package uwu.openjfx.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import uwu.openjfx.MainApp;
import uwu.openjfx.UI;
import uwu.openjfx.behaviors.GameOverWhenDie;
import uwu.openjfx.weapons.Weapon;
import java.util.ArrayList;
import java.util.List;

/*
    This class is responsible for the following:
    - Animation of the player: movement, idleness, attacking
    - Being the caller for attacking, which in turn spawns the necessary weapon of type Weapon
 */
public class PlayerComponent extends CreatureComponent {

    private PhysicsComponent physics;

    private AnimatedTexture texture; // current player animation
    private AnimatedTexture textureRaged; // current player animation if raged

    private AnimationChannel animIdle; // idle player anim
    private AnimationChannel animWalk; // walk player anim
    private AnimationChannel animAutoAttack; // attack player anim
    private AnimationChannel animRagedIdle; // purple glow effect idle player anim
    private AnimationChannel animRagedWalk; // purple glow effect walk player anim

    private static Weapon currentWeapon; // Player's current weapon
    private static List<Weapon> weaponInventoryList = new ArrayList<>();
    private static double attackPower = 1; // Player attack power based on power buff or normal
    private static int piercePow = 1; // Player pierces through blocks if 0
    private static boolean isAttackPowerBuffed = false; // player has drank a rage potion recently
    private static int attackPowerBuffDuration = 300; // how long attack power buff lasts / 60 sec
    private static boolean isChanneling = false; // if player is channeling (fire breath)

    private double currMouseX; // mouse input for x
    private double currMouseY; // mouse input for y

    private double speed = 170;
    private final double velocityDecrementer = 5; // rate at which player decelerates
    private boolean isPressingMovementKeys = false; // Player is moving with WASD / Arrow keys
    private boolean prepAttack = false; // Player has initiated attack charge/channel
    private boolean startAttack = false; // Player does the actual attack
    private boolean ultimateActivated = false; // Player is using ultimate
    private boolean ultimateCD = false; // how long until Player can activate Ultimate again

    // Todo: char state class?
    private static String playerName;
    private static String gameDifficulty;
    private static int monstersKilled;
    private static double damageDealt;

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
            animRagedIdle = new AnimationChannel(FXGL.image("creatures/lizard_m_glow_50x65.png"), 9,
                50, 65, Duration.seconds(2), 0, 3);
            animRagedWalk = new AnimationChannel(FXGL.image("creatures/lizard_m_glow_50x65.png"), 9,
                50, 65, Duration.seconds(2), 4, 7);

            texture = new AnimatedTexture(animIdle);
            texture.loop();
            textureRaged = new AnimatedTexture(animRagedIdle);
            textureRaged.loop();
            textureRaged.setTranslateX(-5);
            textureRaged.setTranslateY(-5);
            textureRaged.setVisible(false);
        }

    }


    @Override
    public void onAdded() {
        if (!MainApp.isIsTesting()) {
            entity.getTransformComponent().setScaleOrigin(new Point2D(20, 25));
            entity.getViewComponent().addChild(textureRaged);
            entity.getViewComponent().addChild(texture);
        }
    }

    @Override
    public void onUpdate(double tpf) {
        // region Movement
        if (!isPressingMovementKeys) {
            normalizeVelocityX();
            normalizeVelocityY();
        }
        if (!prepAttack) {
            // if Player has initiated an attack, then do not perform walk/idle animations
            if (physics.isMoving()) {
                if (texture.getAnimationChannel() != animWalk) {
                    texture.loopAnimationChannel(animWalk);
                    if (attackPower > 1) {
                        textureRaged.loopAnimationChannel(animRagedWalk);
                    }
                }
            } else {
                if (texture.getAnimationChannel() != animIdle) {
                    texture.loopAnimationChannel(animIdle);
                    if (attackPower > 1) {
                        textureRaged.loopAnimationChannel(animRagedIdle);
                    }
                }
            }
        }
        //endregion

        //region Player performs attack
        if (startAttack) { // Player performs the actual attack
            currentWeapon.attack(getEntity(), currMouseX, currMouseY);
            prepAttack = false;
            startAttack = false;
            if (!isChanneling) {
                ultimateActivated = false;
            }
        }
        if (currentWeapon.getClass().getName().contains("MagicStaff")) {
            if (isChanneling()) {
                speed = 85;
            } else {
                speed = 170;
                ultimateActivated = false;
            }
        }
        if (isAttackPowerBuffed) {
            attackPowerBuffDuration--;
            textureRaged.setVisible(true);
            if (attackPowerBuffDuration <= 0) {
                attackPowerBuffDuration = 300;
                isAttackPowerBuffed = false;
            }
        } else {
            textureRaged.setVisible(false);
        }
        if (isInvulnerable()) {
            texture.setOpacity(0.5);
            textureRaged.setOpacity(0.5);
        } else {
            texture.setOpacity(1);
            textureRaged.setOpacity(1);
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
            if (!isChanneling()) {
                getEntity().setScaleX(-1);
            }
            physics.setVelocityX(-speed);
        }
    }

    public void right() {
        if (!prepAttack) {
            if (!isChanneling()) {
                getEntity().setScaleX(1);
            }
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

    public void faceRight() {
        getEntity().setScaleX(1);
    }

    public boolean isPressingMovementKeys() {
        return isPressingMovementKeys;
    }

    public void setPressingMovementKeys(boolean moving) {
        isPressingMovementKeys = moving;
    }
    // endregion

    // region Player Attack functions
    public void autoAttack(boolean ultimateActivated) {
        this.ultimateActivated = ultimateActivated;
        if (ultimateActivated) {
            ultimateCD = true;
            Runnable runnable = () -> {
                try {
                    Thread.sleep((long) currentWeapon.getUltimateCD() * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ultimateCD = false;
            };
            Thread thread = new Thread(runnable);
            thread.start();
        }
        if (currMouseX > entity.getX() + 20) { // turn player in direction of mouse
            entity.setScaleX(1);
        } else {
            entity.setScaleX(-1);
        }
        texture.playAnimationChannel(animAutoAttack); // play attack animation
        prepAttack = true; // Player has initiated attack
        stop(); // stop moving
        // Player performs the actual attack after duration amount of milliseconds
        Runnable runnable = () -> {
            try {
                Thread.sleep(currentWeapon.getDuration(ultimateActivated));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            startAttack = true; // Player performs the actual attack
        };
        Thread thread = new Thread(runnable);
        thread.start();
        currentWeapon.getDuration(ultimateActivated);
        currentWeapon.prepAttack(getEntity());
    }

    public static void channelAttack() {
        isChanneling = true;
        Runnable runnable = () -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            isChanneling = false;
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public static boolean isChanneling() {
        return isChanneling;
    }

    public boolean getUltimateCD() {
        return ultimateCD;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public boolean isAttacking() {
        // used in MainApp for LMB/RMB input, confirmation of whether or not Player is attacking
        return prepAttack;
    }

    public static void setIsAttackPowerBuffed(boolean buffed) {
        isAttackPowerBuffed = buffed;
        piercePow = 0;
        attackPower = 2.5;
    }

    public static double getAttackPower() {
        return attackPower;
    }

    public static void setPiercePow(int pierce) {
        piercePow = pierce;
    }

    public static int getPiercePow() {
        return piercePow;
    }
    // endregion

    // region Player Weapon
    public static void setCurrentWeapon(Weapon weapon) {
        currentWeapon = weapon;
        UI.setWeaponProperty(currentWeapon.getWeaponSprite());
    }

    public static Weapon getCurrentWeapon() {
        return currentWeapon;
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
    // endregion

    // region Gold
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
    // endregion

    // region Player Name & Game Misc.
    public static String getPlayerName() {
        return playerName;
    }

    public static void setPlayerName(String playerName) {
        PlayerComponent.playerName = playerName;
    }

    public static String getGameDifficulty() {
        return gameDifficulty;
    }

    public static void setGameDifficulty(String gameDifficulty) {
        PlayerComponent.gameDifficulty = gameDifficulty;
    }

    public void setMousePosition(double mouseXPos, double mouseYPos) {
        currMouseX = mouseXPos;
        currMouseY = mouseYPos;
    }

    public static int getMonstersKilled() {
        return monstersKilled;
    }

    public static void addToMonstersKilled() {
        monstersKilled++;
    }

    public static double getDamageDealt() {
        return damageDealt;
    }

    public static void addToDamageDealt(double damageDone) {
        damageDealt += damageDone;
    }
    // endregion
}

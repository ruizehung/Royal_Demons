package uwu.openjfx.collision;

import com.almasb.fxgl.cutscene.Cutscene;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import uwu.openjfx.RoyalType;
import uwu.openjfx.components.PlayerComponent;

import java.util.ArrayList;
import java.util.Arrays;

public class PlayerFinalDoorCollisionHandler extends CollisionHandler {

    public PlayerFinalDoorCollisionHandler() {
        super(RoyalType.PLAYER, RoyalType.FINALDOOR);
    }

    @Override
    protected void onCollisionBegin(Entity player, Entity finalDoor) {
        if (FXGL.getGameWorld().getEntitiesByType(RoyalType.ENEMY).size() <= 0) {
            System.out.println("Teleport Player to final room");
            ArrayList<String> savePrincess = new ArrayList<>(Arrays.asList(
                    "1.name = " + PlayerComponent.getPlayerName(),
                    "2.name = " + PlayerComponent.getPlayerName() + "'s dino lover",
                    "1.image = ui_yoshi.png",
                    "2.image = ui_peach.png",
                    "1: omg is dat u??",
                    "2: ya boo, thnx for saving me",
                    "1: np, lets go home and watch some netflix ^^"
            ));
            Cutscene cs = new Cutscene(savePrincess);
            FXGL.getCutsceneService().startCutscene(new Cutscene(savePrincess));
        } else {
            System.out.println("Player should kill the boss first");
        }

    }
}

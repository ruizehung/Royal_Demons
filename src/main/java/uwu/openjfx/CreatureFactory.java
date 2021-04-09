package uwu.openjfx;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.components.IrremovableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import javafx.geometry.Point2D;
import uwu.openjfx.behaviors.DropCoinBehavior;
import uwu.openjfx.behaviors.DropItemComponent;
import uwu.openjfx.components.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreatureFactory implements EntityFactory {
    @Spawns("player")
    public Entity newPlayer(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        // physics.addGroundSensor(new HitBox("GROUND_SENSOR",
        //         new Point2D(16, 38), BoundingShape.box(6, 8)));

        // this avoids player sticking to walls
        physics.setFixtureDef(new FixtureDef().friction(0.0f));

        PlayerComponent playerComponent = new PlayerComponent(10);

        return FXGL.entityBuilder(data)
                .type(RoyalType.PLAYER)
                .bbox(new HitBox(BoundingShape.polygon(new Point2D(3, 15), new Point2D(38, 15),
                        new Point2D(38, 55), new Point2D(3, 55))))
                .with(physics)
                .with(new CollidableComponent(true))
                .with(new IrremovableComponent())
                .with(playerComponent)
                .with("CreatureComponent", playerComponent)
                .build();
    }

    @Spawns("minion")
    public Entity newMinion(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.setFixtureDef(new FixtureDef().friction(1.0f));

        List<String> minionList = FXGL.geto("normalMinionList");
        String minionFileName = minionList.get(FXGL.random(0, minionList.size() - 1));
        List<Integer> widthHeight = parseSizes(minionFileName);
        EnemyComponent enemyComponent = new EnemyComponent(
                100,
                "creatures/minions/normal/" + minionFileName,
                widthHeight.get(0), widthHeight.get(1));

        enemyComponent.setDieBehavior(new DropCoinBehavior(1, 5));

        // TODO__: better to manually define bbox tailor to each minion
        List<Point2D> point2DList = Arrays.asList(
                new Point2D(3, 5),
                new Point2D(widthHeight.get(0) - 3, 5),
                new Point2D(widthHeight.get(0) - 3, widthHeight.get(1) - 2),
                new Point2D(3, widthHeight.get(1) - 2)
        );

        return FXGL.entityBuilder(data)
                .type(RoyalType.ENEMY)
                .bbox(new HitBox(BoundingShape.polygon(point2DList)))
                .with(physics)
                .with(new CollidableComponent(true))
                .with(enemyComponent)
                .with("CreatureComponent", enemyComponent)
                .build();
    }

    @Spawns("forestMinion")
    public Entity newForestMinion(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.setFixtureDef(new FixtureDef().friction(1.0f));

        List<String> minionList = FXGL.geto("forestMinionList");
        String minionFileName = minionList.get(FXGL.random(0, minionList.size() - 1));
        List<Integer> widthHeight = parseSizes(minionFileName);
        EnemyComponent enemyComponent = new EnemyComponent(
                100,
                "creatures/minions/forest/" + minionFileName,
                widthHeight.get(0), widthHeight.get(1));
        enemyComponent.setDieBehavior(new DropCoinBehavior(1, 5));

        // Ent has too much empty space at top
        int startingY = minionFileName.startsWith("Ent") ? 3 : 10;

        // Todo: better to manually define bbox tailor to each minion
        List<Point2D> point2DList = Arrays.asList(
                new Point2D(3, startingY),
                new Point2D(widthHeight.get(0) - 3, startingY),
                new Point2D(widthHeight.get(0) - 3, widthHeight.get(1) - 2),
                new Point2D(3, widthHeight.get(1) - 2)
        );

        return FXGL.entityBuilder(data)
                .type(RoyalType.ENEMY)
                .bbox(new HitBox(BoundingShape.polygon(point2DList)))
                .with(physics)
                .with(new CollidableComponent(true))
                .with(enemyComponent)
                .with("CreatureComponent", enemyComponent)
                .build();
    }

    @Spawns("miniBoss")
    public Entity newMiniBoss(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.setFixtureDef(new FixtureDef().friction(1.0f));

        List<String> minionList = FXGL.geto("miniBossList");
        String miniBossFileName = minionList.get(FXGL.random(0, minionList.size() - 1));
        List<Integer> widthHeight = parseSizes(miniBossFileName);

        DropItemComponent dropItemWhenDie = new DropItemComponent(Arrays.asList("Heart"));
        EnemyComponent enemyComponent = new EnemyComponent(100,
                "creatures/miniBoss/" + miniBossFileName,
                widthHeight.get(0), widthHeight.get(1));
        enemyComponent.setDieBehavior(dropItemWhenDie);

        // TODO_: better to manually define bbox tailor to each minion
        List<Point2D> point2DList = Arrays.asList(
                new Point2D(3, 5),
                new Point2D(widthHeight.get(0) - 3, 5),
                new Point2D(widthHeight.get(0) - 3, widthHeight.get(1) - 2),
                new Point2D(3, widthHeight.get(1) - 2)
        );

        return FXGL.entityBuilder(data)
                .type(RoyalType.ENEMY)
                .bbox(new HitBox(BoundingShape.polygon(point2DList)))
                .with(physics)
                .with(new CollidableComponent(true))
                .with(enemyComponent)
                .with(dropItemWhenDie)
                .with("CreatureComponent", enemyComponent)
                .build();
    }

    @Spawns("finalBoss")
    public Entity newfinalBoss(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.setFixtureDef(new FixtureDef().friction(1.0f));

        String bossFileName = "Golem_168x105.png";
        List<Integer> widthHeight = parseSizes(bossFileName);

        BossComponent bossComponent = new BossComponent(
                100, "creatures/boss/" + bossFileName,
                widthHeight.get(0), widthHeight.get(1), 12);

        // TODO_: better to manually define bbox tailor to each minion
        List<Point2D> point2DList = Arrays.asList(
                new Point2D(3, 5),
                new Point2D(widthHeight.get(0) - 3, 5),
                new Point2D(widthHeight.get(0) - 3, widthHeight.get(1) - 2),
                new Point2D(3, widthHeight.get(1) - 2)
        );

        return FXGL.entityBuilder(data)
                .type(RoyalType.ENEMY)
                .bbox(new HitBox(BoundingShape.polygon(point2DList)))
                .with(physics)
                .with(new CollidableComponent(true))
                .with(bossComponent)
                .with("CreatureComponent", bossComponent)
                .build();
    }

    @Spawns("coin")
    public Entity newCoin(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(RoyalType.COIN)
                .bbox(new HitBox(BoundingShape.circle(8)))
                .with(new CollidableComponent(true))
                .with(new CoinComponent(FXGL.random(1, 6)))
                .build();
    }

    private List<Integer> parseSizes(String fileName) {
        List<Integer> widthHeight = new ArrayList<>();
        String pattern = "(\\d+)x(\\d+)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(fileName);
        m.find();
        String[] sizes = m.group(0).split("x");
        for (String size : sizes) {
            widthHeight.add(Integer.parseInt(size));
        }
        return widthHeight;
    }

    @Spawns("initialRoomNPC")
    public Entity newInitialRoomNPC(SpawnData data) {
        InitialNPCComponent initialNPCComponent = new InitialNPCComponent();
        return FXGL.entityBuilder(data)
                .type(RoyalType.NPC)
                .bbox(new HitBox(BoundingShape.polygon(new Point2D(3, 15), new Point2D(38, 15),
                        new Point2D(38, 55), new Point2D(3, 55))))
                .with(new CollidableComponent(true))
                .with(initialNPCComponent)
                .with("Interactable", initialNPCComponent)
                .build();
    }

    @Spawns("GreenFElf")
    public Entity newGreenFElf(SpawnData data) {
        GreenFElfComponent greenFElfComponent = new GreenFElfComponent();
        return FXGL.entityBuilder(data)
                .type(RoyalType.NPC)
                .bbox(new HitBox(BoundingShape.polygon(new Point2D(3, 15), new Point2D(38, 15),
                        new Point2D(38, 55), new Point2D(3, 55))))
                .with(new CollidableComponent(true))
                .with(greenFElfComponent)
                .with("Interactable", greenFElfComponent)
                .build();
    }
}

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import uwu.openjfx.MainApp;
import uwu.openjfx.MapGeneration.GameMap;
import uwu.openjfx.MapGeneration.Room;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class TestMapGeneration {

    private final int rooms = 40;

    @BeforeEach
    public void init() {
        MainApp.setIsTesting(true);
    }

    @RepeatedTest(500)
    void testBossRoomAtLeast6RoomsAway() {
        GameMap gameMap = new GameMap(rooms);
        gameMap.generateRooms();

        assert (gameMap.getBossRoom().getDistFromInitRoom() > 6);
        gameMap = null;
    }

    @RepeatedTest(500)
    void testBossRoomReachable() {
        GameMap gameMap = new GameMap(rooms);
        gameMap.generateRooms();

        Room initialRoom = gameMap.getInitialRoom();
        Room bossRoom = gameMap.getBossRoom();

        boolean foundBossRoom = false;

        Queue<Room> queue = new LinkedList<>();
        Set<Room> visited = new HashSet();
        queue.add(initialRoom);
        visited.add(initialRoom);

        while (!queue.isEmpty()) {
            Room room = queue.poll();
            if (room == bossRoom) {
                foundBossRoom = true;
                break;
            }

            for (Room adjRoom : room.getAdjacentRooms()) {
                if (adjRoom != null && !visited.contains(adjRoom)) {
                    queue.add(adjRoom);
                    visited.add(adjRoom);
                }
            }
        }

        assert (foundBossRoom);
        gameMap = null;
    }

    @RepeatedTest(500)
    void testInitialRoomHas4AdjRooms() {
        GameMap gameMap = new GameMap(rooms);
        gameMap.generateRooms();
        assert (gameMap.getInitialRoom().getNumAdjRooms() == 4);
        assert (gameMap.getInitialRoom().getNorthRoom() != null);
        assert (gameMap.getInitialRoom().getEastRoom() != null);
        assert (gameMap.getInitialRoom().getSouthRoom() != null);
        assert (gameMap.getInitialRoom().getWestRoom() != null);
        gameMap = null;
    }
}

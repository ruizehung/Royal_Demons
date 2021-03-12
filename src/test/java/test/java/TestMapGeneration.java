package test.java;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import uwu.openjfx.model.GameState;
import uwu.openjfx.model.Room;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;


public class TestMapGeneration {

    GameState gameState = GameState.getInstance();

    // ray 3
    @RepeatedTest(100)
    void testBoosRoomAtLeast6RoomsAway() {
        gameState.generateNewMap();
        assert(gameState.getGameMap().getBossRoom().getDistFromInitRoom() > 6);
    }

    // ray 4
    @RepeatedTest(100)
    void testBoosRoomReachable() {
        gameState.generateNewMap();

        Room initialRoom = gameState.getGameMap().getInitialRoom();
        Room bossRoom = gameState.getGameMap().getBossRoom();

        Boolean foundBossRoom = false;

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
    }

}

package uwu.openjfx.Model;


import java.util.*;

public class GameMap {
    private Random random = new Random();

    private int numOfRooms;
    private int finalBossDist;

    private Map<Coordinate, Room> rooms = new HashMap<Coordinate, Room>();
    private Room initialRoom;
    private Room bossRoom;

    public GameMap() {
        this(20);
    }

    public GameMap(int numOfRooms) {
        this.numOfRooms = numOfRooms;
        finalBossDist = random.nextInt(3) + 6;
        System.out.println("finalBossDist: " + finalBossDist);
        generateRooms();
    }

    private void generateRooms() {
        // generate first room
        initialRoom = new Room(new Coordinate(0,0), 4);
        rooms.put(initialRoom.getCoordinate(), initialRoom);

        Queue<Coordinate> roomsToCreate = new LinkedList<>();

        roomsToCreate.add(new Coordinate(0, 1));
        roomsToCreate.add(new Coordinate(1, 0));
        roomsToCreate.add(new Coordinate(0, -1));
        roomsToCreate.add(new Coordinate(-1, 0));

        int numRoomsGenerated = 1;
        int maxDistFromInitRoom = 0;

        while (!roomsToCreate.isEmpty() && (numRoomsGenerated < numOfRooms || maxDistFromInitRoom < finalBossDist)) {
            Coordinate coordinate = roomsToCreate.poll();
            if (getRoom(coordinate) == null) { // if the coordinate does not have a room yet
                Room newRoom = new Room(coordinate);
                rooms.put(coordinate, newRoom);

                ++numRoomsGenerated;
                if (newRoom.getDistFromInitRoom() > maxDistFromInitRoom) {
                    maxDistFromInitRoom = newRoom.getDistFromInitRoom();
                    bossRoom = newRoom;
                }
                generateAdjacentRooms(newRoom, roomsToCreate);
            }
        }

        // connect the rooms

        // determine shop rooms



    }

    private void generateAdjacentRooms(Room room, Queue<Coordinate> roomsToCreate) {
        // check how many adjacent rooms already exist
        List<Coordinate> adjacentCoordinates = room.getAdjacentCoordinates();
        List<Coordinate> availableAdjacentCoordinates = new ArrayList<>();
        for (Coordinate coordinate : adjacentCoordinates) {
            if (getRoom(coordinate) == null) {
                availableAdjacentCoordinates.add(coordinate);
            }
        }

        if (availableAdjacentCoordinates.size() == 0) {
            room.setNumAdjRooms(4);
        } else {
            int numNewRoomsToCreate = random.nextInt(availableAdjacentCoordinates.size() + 1);
            room.setNumAdjRooms(4 - availableAdjacentCoordinates.size() + numNewRoomsToCreate);

            // add new coordinates to roomsToCreate
            for (int i = 0; i < numNewRoomsToCreate; ++i) {
                int randomIndex = random.nextInt(availableAdjacentCoordinates.size());
                Coordinate newCoordinate = availableAdjacentCoordinates.get(randomIndex);
                roomsToCreate.add(newCoordinate);
                availableAdjacentCoordinates.remove(randomIndex);
            }
        }
    }

    public Room getRoom(Coordinate coordinate) {
        return rooms.get(coordinate);
    }


    public int getNumOfRooms() {
        return numOfRooms;
    }

    public void setNumOfRooms(int numOfRooms) {
        this.numOfRooms = numOfRooms;
    }

    public Room getInitialRoom() {
        return initialRoom;
    }

    public Map<Coordinate, Room> getRooms() {
        return rooms;
    }

    public Room getBossRoom() {
        return bossRoom;
    }
}

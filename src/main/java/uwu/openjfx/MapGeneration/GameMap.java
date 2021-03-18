package uwu.openjfx.MapGeneration;


import javafx.util.Pair;

import java.util.*;

public class GameMap {
    private Random random = new Random();

    private int numOfRooms;
    private int finalBossDist;

    private Map<Coordinate, Room> rooms = new HashMap<>();
    private Room initialRoom;
    private Room bossRoom;

    private List<Pair<Pair<Integer, Integer>, String>> directions = new ArrayList<>();

    public GameMap() {
        this(40);
    }

    public GameMap(int numOfRooms) {
        directions.add(new Pair(new Pair(0, 1), "North"));
        directions.add(new Pair(new Pair(1, 0), "East"));
        directions.add(new Pair(new Pair(0, -1), "South"));
        directions.add(new Pair(new Pair(-1, 0), "West"));

        this.numOfRooms = numOfRooms;
        finalBossDist = random.nextInt(3) + 7;
        generateRooms();
        //debug print statement for boss room distance
        //System.out.println("boss room distance: " + bossRoom.getDistFromInitRoom());
    }

    private void generateRooms() {
        // generate first room
        initialRoom = new Room(new Coordinate(0, 0), 4);
        rooms.put(initialRoom.getCoordinate(), initialRoom);

        List<Coordinate> roomsToCreate = new ArrayList<>();

        generate4RoomsAroundInitialRoom(roomsToCreate);

        int numRoomsGenerated = 5;
        int maxDistFromInitRoom = 0;

        while (numRoomsGenerated < numOfRooms || maxDistFromInitRoom < finalBossDist) {
            if (roomsToCreate.isEmpty()) {
                for (Room room: rooms.values()) {
                    for (Coordinate coordinate: room.getAdjacentCoordinates()) {
                        if (getRoom(coordinate) == null) {
                            roomsToCreate.add(coordinate);
                        }
                    }
                    if (roomsToCreate.size() > 2) {
                        break;
                    }
                }
            }

            Coordinate coordinate = roomsToCreate.remove(random.nextInt(roomsToCreate.size()));
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
        for (Room room : rooms.values()) {
            connectRoomWithAdjacentRooms(room);
        }

        // determine shop rooms

    }

    private void generate4RoomsAroundInitialRoom(List<Coordinate> roomsToCreate) {
        for (Pair<Pair<Integer, Integer>, String> dir : directions) {
            Room room = new Room(new Coordinate(dir.getKey().getKey(), dir.getKey().getValue()));
            rooms.put(room.getCoordinate(), room);
            generateAdjacentRooms(room, roomsToCreate);
        }
    }

    private void generateAdjacentRooms(Room room, List<Coordinate> roomsToCreate) {
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

    public void connectRoomWithAdjacentRooms(Room room) {
        Coordinate coordinate;
        Coordinate adjacentCoordinate = new Coordinate(0, 0);
        Room adjacentRoom;
        for (Pair<Pair<Integer, Integer>, String> dir : directions) {
            coordinate = room.getCoordinate();
            adjacentCoordinate.setX(coordinate.getX() + dir.getKey().getKey());
            adjacentCoordinate.setY(coordinate.getY() + dir.getKey().getValue());
            adjacentRoom = rooms.get(adjacentCoordinate);
            if (adjacentRoom != null) {
                switch (dir.getValue()) {
                case "North":
                    room.setNorthRoom(adjacentRoom);
                    adjacentRoom.setSouthRoom(room);
                    break;
                case "East":
                    room.setEastRoom(adjacentRoom);
                    adjacentRoom.setWestRoom(room);
                    break;
                case "South":
                    room.setSouthRoom(adjacentRoom);
                    adjacentRoom.setNorthRoom(room);
                    break;
                case "West":
                    room.setWestRoom(adjacentRoom);
                    adjacentRoom.setEastRoom(room);
                    break;
                default:
                }
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

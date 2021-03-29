package uwu.openjfx.MapGeneration;

import com.almasb.fxgl.dsl.FXGL;
import uwu.openjfx.MainApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room {
    private Coordinate coordinate;

    // num of Neighbors == num of doors
    private int numAdjRooms;

    // references to neighbor rooms
    private Room northRoom;
    private Room southRoom;
    private Room westRoom;
    private Room eastRoom;

    private Boolean visited = false;
    // set different room type based on numb of adjacent rooms
    private String roomType;
    private Map<Integer, Map<String, Integer>> entitiesData;


    public Room(Coordinate coordinate) {
        entitiesData = new HashMap<>();
        this.coordinate = coordinate;
        if (!MainApp.isIsTesting()) {
            List<String> roomTypeList = FXGL.geto("roomTypeList");
            roomType = roomTypeList.get(FXGL.random(0, roomTypeList.size() - 1));
        }
        // roomType = "60_60_4_door";
    }

    public Room(Coordinate coordinate, int numAdjRooms) {
        this(coordinate);
        this.numAdjRooms = numAdjRooms;
    }

    public Boolean visited() {
        return visited;
    }

    public void setVisited(Boolean visited) {
        this.visited = visited;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public int getEntityData(int id, String propertyName) {
        return entitiesData.get(id).get(propertyName);
    }

    public void setEntityData(int id, String propertyName, int val) {
        if (entitiesData.get(id) == null) {
            entitiesData.put(id, new HashMap<>());
        }
        entitiesData.get(id).put(propertyName, val);
    }

    public Boolean enemiesCleared() {
        // May not work if we have ally creatures
        for (Map<String, Integer> data : entitiesData.values()) {
            if (data.get("isAlive") != null && data.get("isAlive") == 1) {
                return false;
            }
        }
        return true;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public List<Coordinate> getAdjacentCoordinates() {
        ArrayList<Coordinate> adjacentCoordinates = new ArrayList<>();
        adjacentCoordinates.add(new Coordinate(coordinate.getX(), coordinate.getY() + 1));
        adjacentCoordinates.add(new Coordinate(coordinate.getX() + 1, coordinate.getY()));
        adjacentCoordinates.add(new Coordinate(coordinate.getX(), coordinate.getY() - 1));
        adjacentCoordinates.add(new Coordinate(coordinate.getX() - 1, coordinate.getY()));
        return adjacentCoordinates;
    }

    public int getDistFromInitRoom() {
        return Math.abs(coordinate.getX()) + Math.abs(coordinate.getY());
    }

    public Room getNorthRoom() {
        return northRoom;
    }

    public void setNorthRoom(Room northRoom) {
        this.northRoom = northRoom;
    }

    public Room getSouthRoom() {
        return southRoom;
    }

    public void setSouthRoom(Room southRoom) {
        this.southRoom = southRoom;
    }

    public Room getWestRoom() {
        return westRoom;
    }

    public void setWestRoom(Room westRoom) {
        this.westRoom = westRoom;
    }

    public Room getEastRoom() {
        return eastRoom;
    }

    public void setEastRoom(Room eastRoom) {
        this.eastRoom = eastRoom;
    }

    public int getNumAdjRooms() {
        return numAdjRooms;
    }

    public void setNumAdjRooms(int numAdjRooms) {
        this.numAdjRooms = numAdjRooms;
    }

    public List<Room> getAdjacentRooms() {
        ArrayList<Room> adjacentRooms = new ArrayList<>();
        adjacentRooms.add(getNorthRoom());
        adjacentRooms.add(getEastRoom());
        adjacentRooms.add(getSouthRoom());
        adjacentRooms.add(getWestRoom());
        return adjacentRooms;
    }

}

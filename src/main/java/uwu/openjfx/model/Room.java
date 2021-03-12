package uwu.openjfx.model;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private Coordinate coordinate;

    // num of Neighbors == num of doors
    private int numAdjRooms;

    // references to neighbor rooms
    private Room northRoom;
    private Room southRoom;
    private Room westRoom;
    private Room eastRoom;

    public Room(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public Room(Coordinate coordinate, int numAdjRooms) {
        this(coordinate);
        this.numAdjRooms = numAdjRooms;
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

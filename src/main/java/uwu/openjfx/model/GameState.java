package uwu.openjfx.model;


public class GameState {
    private static GameState instance = new GameState();
    private GameMap gameMap;
    private Room currentRoom;

    private GameState() {

    }

    public static GameState getInstance() {
        return instance;
    }

    public void generateNewMap() {
        gameMap = new GameMap(90);
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }
}

package uwu.openjfx.Model;

public class GameState {
    private static GameMap gameMap;

    public GameState() {
        gameMap = new GameMap(20);
    }

    public static GameMap getGameMap() {
        return gameMap;
    }
}

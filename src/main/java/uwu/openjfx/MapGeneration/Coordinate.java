package uwu.openjfx.MapGeneration;

import java.util.Objects;

/*
This class is inspired by jabu.10245's answer at
stackoverflow.com/questions/34092551/java-hashmap-in-hashmap-store-all-coordinates-of-pixels
 */

public class Coordinate {
    private int x;
    private int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int hashCode() {
        return Objects.hash(x, y);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof Coordinate)) {
            return false;
        }

        Coordinate anotherCoordinate = (Coordinate) obj;
        return this.x == anotherCoordinate.x && this.y == anotherCoordinate.y;
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}

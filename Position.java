
public class Position {
    private int x = 0;
    private int y = 0;

    public Position(int x, int y) {
        setX(x);
        setY(y);
    }

    public Position(Position other) {
        setX(other.x);
        setY(other.y);
    }

    public void move(Direction dir) {
        x += dir.getXMod();
        y += dir.getYMod();
    }

    public boolean equals(Position other) {
        return other.x == x && other.y == y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
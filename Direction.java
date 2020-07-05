public enum Direction {

    NORTH(-1, 0), EAST(0, 1), SOUTH(1, 0), WEST(0, -1);

    private final int xMod;
    private final int yMod;

    private Direction(int xMod, int yMod) {
        this.xMod = xMod;
        this.yMod = yMod;
    }

    public Direction nextDirection(){
        int nextDir = (this.ordinal() + 1) % Direction.values().length;
        return Direction.values()[nextDir];
    }

    public int getXMod() {
        return xMod;
    }

    public int getYMod() {
        return yMod;
    }
}
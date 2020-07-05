import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

public class Snake {

    private Element[][] map;
    private Vector<Position> elements;

    public Snake(int sizeX, int sizeY, int snakeSize) throws NoValidDirectionException {
        elements = new Vector<>();
        map = new Element[sizeX][sizeY];
        // Fill map with empty fields
        for (Element[] e : map) {
            Arrays.fill(e, Element.EMPTY);
        }
        createStartingSnake(snakeSize);
        generateApple();
    }

    public void move(Direction dir) throws InvalidPositionException {
        Position temp = new Position(elements.get(0));
        temp.move(dir);
        for (int i = 0; i < elements.size(); i++) {
            Position old = new Position(elements.get(i));
            map[old.getX()][old.getY()] = Element.EMPTY;
            elements.set(i, temp);
            temp = old;
        }
        for (int i = 0; i < elements.size(); i++) {
            if (i == 0) {
                map[elements.get(i).getX()][elements.get(i).getY()] = Element.HEAD;
            } else {
                map[elements.get(i).getX()][elements.get(i).getY()] = Element.BODY;
            }
        }
    }

    private void generateApple() {
        Vector<Position> vector = new Vector<>();
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[0].length; y++) {
                if (map[x][y] == Element.EMPTY) {
                    vector.add(new Position(x, y));
                }
            }
        }
        Random random = new Random();
        Position apple = vector.get(random.nextInt(vector.size()));
        map[apple.getX()][apple.getY()] = Element.APPLE;
    }

    private void createStartingSnake(int size) throws NoValidDirectionException {
        // get center
        int centerX = (int) (map.length / 2);
        int centerY = (int) (map[0].length / 2);
        Position pos = new Position(centerX, centerY);
        elements.add(new Position(pos));

        // place snakehead
        map[centerX][centerY] = Element.HEAD;
        // decrease size by one
        size--;
        while (size > 0) {
            Direction dir = getEmptyDirection(pos);
            if (dir == null) {
                throw new NoValidDirectionException();
            }
            // move position in direction dir
            pos.move(dir);
            try {
                setPosition(pos, Element.BODY);
                elements.add(new Position(pos));
            } catch (InvalidPositionException e) {
                // Can not occure, because it is checked beforhand
                e.printStackTrace();
            }
            // decrease size by one
            size--;
        }
    }

    private Direction getEmptyDirection(Position pos) {
        Direction dir = Direction.NORTH;
        do {
            Position attempt = new Position(pos.getX(), pos.getY());
            attempt.move(dir);
            if (isPositionValid(attempt)) {
                return dir;
            }
            dir = dir.nextDirection();
        } while (dir != Direction.NORTH);
        return null;
    }

    private void setPosition(Position pos, Element e) throws InvalidPositionException {
        if (isPositionValid(pos)) {
            map[pos.getX()][pos.getY()] = e;
        } else {
            throw new InvalidPositionException(pos);
        }
    }

    private boolean isPositionValid(Position pos) {
        boolean posInBounds = pos.getX() >= 0 && pos.getX() < map.length && pos.getY() >= 0
                && pos.getY() < map[0].length;
        if (posInBounds) {
            return map[pos.getX()][pos.getY()] == Element.EMPTY || map[pos.getX()][pos.getY()] == Element.APPLE;
        }
        return posInBounds;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(" # ".repeat(map.length + 2));
        buffer.append("\n");

        for (Element[] array : map) {
            buffer.append(" # ");
            for (Element e : array) {
                switch (e) {
                    case APPLE:
                        buffer.append(" A ");
                        break;
                    case BODY:
                        buffer.append(" B ");
                        break;
                    case EMPTY:
                        buffer.append("   ");
                        break;
                    case HEAD:
                        buffer.append(" H ");
                        break;
                    default:
                        buffer.append("   ");
                        break;
                }
            }
            buffer.append(" #\n");
        }

        buffer.append(" # ".repeat(map.length + 2));
        return buffer.toString();
    }
}
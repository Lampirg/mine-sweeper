package map.cell;

/**
 *
 * @author Lampirg
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

    public int getY() {
        return y;
    }
    
    
    public Coordinate getLeftUpCoordinate() {
        int previousY = getY() - 1;
        if (previousY < 0)
            previousY = 0;
        int previousX = getX() - 1;
        if (previousX < 0)
            previousX = 0;
        return new Coordinate(previousX, previousY);
    }
    
    public Coordinate getRightDownCoordinate(int height, int width) {
        int nextY = getY() + 1;
        if (nextY >= height)
            nextY = height - 1;
        int nextX = getX() + 1;
        if (nextX >= width)
            nextX = width - 1;
        return new Coordinate(nextX, nextY);
    }
    
    public static int getDistance(Coordinate next, Coordinate previous) {
        final int yAmount = next.getY() - previous.getY() + 1;
        final int xAmount = next.getX() - previous.getX() + 1;
        return yAmount * xAmount;
    }
}

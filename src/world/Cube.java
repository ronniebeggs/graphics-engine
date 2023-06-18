package world;

import edu.princeton.cs.algs4.StdDraw;

public class Cube extends Entity {
    public int sideLength;
    public int xPosition;
    public int yPosition;

    public Cube(int x, int y, int z, int length) {
        super(x, y, z);
        sideLength = length;
    }

    public void render() {
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.filledSquare(xPosition, yPosition, (float) sideLength / 2);
    }

}

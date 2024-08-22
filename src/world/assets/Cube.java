package world.assets;

import util.Coordinate;
import util.Mesh;
import world.RenderableEntity;

import java.awt.*;

public class Cube extends RenderableEntity {
    int sideLength;
    Color color;
    public Cube(int x, int y, int z, int sideLength, Color color) {
        super(x, y, z, 0, 0, 0);
        this.sideLength = sideLength;
        this.color = color;
        createCube();
    }
    /**
     * Initialize a basic multicolored cube for testing 3d projections.
     * */
    public void createCube() {
        int halfLength = sideLength / 2;
        // insert vertices clockwise from bottom left corner
        Coordinate vertex1 = new Coordinate(xPosition - halfLength, yPosition - halfLength, zPosition - halfLength);
        Coordinate vertex2 = new Coordinate(xPosition - halfLength, yPosition + halfLength, zPosition - halfLength);
        Coordinate vertex3 = new Coordinate(xPosition + halfLength, yPosition + halfLength, zPosition - halfLength);
        Coordinate vertex4 = new Coordinate(xPosition + halfLength, yPosition - halfLength, zPosition - halfLength);
        Coordinate vertex5 = new Coordinate(xPosition - halfLength, yPosition - halfLength, zPosition + halfLength);
        Coordinate vertex6 = new Coordinate(xPosition - halfLength, yPosition + halfLength, zPosition + halfLength);
        Coordinate vertex7 = new Coordinate(xPosition + halfLength, yPosition + halfLength, zPosition + halfLength);
        Coordinate vertex8 = new Coordinate(xPosition + halfLength, yPosition - halfLength, zPosition + halfLength);
        // create faces of the cube
        Mesh frontFace = new Mesh(this, new Coordinate[]{vertex1, vertex2, vertex3, vertex4}, color);
        Mesh topFace = new Mesh(this, new Coordinate[]{vertex2, vertex6, vertex7, vertex3}, color);
        Mesh bottomFace = new Mesh(this, new Coordinate[]{vertex1, vertex5, vertex8, vertex4}, color);
        Mesh backFace = new Mesh(this, new Coordinate[]{vertex5, vertex6, vertex7, vertex8}, color);
        Mesh leftFace = new Mesh(this, new Coordinate[]{vertex1, vertex2, vertex6, vertex5}, color);
        Mesh rightFace = new Mesh(this, new Coordinate[]{vertex4, vertex3, vertex7, vertex8}, color);
        meshes.add(frontFace);
        meshes.add(topFace);
        meshes.add(bottomFace);
        meshes.add(backFace);
        meshes.add(leftFace);
        meshes.add(rightFace);
    }
}

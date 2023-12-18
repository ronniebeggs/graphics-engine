package world;

import edu.princeton.cs.algs4.StdDraw;
import util.Coordinate;
import util.Mesh;

import java.util.ArrayList;

public class Cube extends Entity {

    int sideLength;
    ArrayList<Coordinate> vertices;
    ArrayList<Mesh> meshes;

    public Cube(int x, int y, int z, int sideLength) {
        super(x, y, z);
        this.sideLength = sideLength;
        createVertices();
    }

    public void createVertices() {
        vertices = new ArrayList<>();
        meshes = new ArrayList<>();
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
        vertices.add(vertex1);
        vertices.add(vertex2);
        vertices.add(vertex3);
        vertices.add(vertex4);
        vertices.add(vertex5);
        vertices.add(vertex6);
        vertices.add(vertex7);
        vertices.add(vertex8);
        Mesh frontFace = new Mesh(new Coordinate[]{vertex1, vertex2, vertex3, vertex4}, StdDraw.RED);
        Mesh topFace = new Mesh(new Coordinate[]{vertex2, vertex6, vertex7, vertex3}, StdDraw.BLUE);
//        Mesh bottomFace = new Mesh(new Coordinate[]{vertex1, vertex5, vertex8, vertex4}, StdDraw.GREEN);
//        Mesh backFace = new Mesh(new Coordinate[]{vertex5, vertex6, vertex7, vertex8}, StdDraw.YELLOW);
//        Mesh leftFace = new Mesh(new Coordinate[]{vertex1, vertex2, vertex5, vertex4}, StdDraw.ORANGE);
        Mesh rightFace = new Mesh(new Coordinate[]{vertex4, vertex3, vertex7, vertex8}, StdDraw.WHITE);
        meshes.add(frontFace);
        meshes.add(topFace);
//        meshes.add(bottomFace);
//        meshes.add(backFace);
//        meshes.add(leftFace);
        meshes.add(rightFace);
    }

    public void render() {
        for (Mesh mesh : meshes) {
            renderMesh(mesh);
        }
    }

    public void renderMesh(Mesh mesh) {
        Coordinate origin = new Coordinate(0, 0, 0);
        StdDraw.setPenColor(mesh.getColor());
        int numVertices = mesh.getNumVertices();
        double[] xVertices = new double[numVertices];
        double[] yVertices = new double[numVertices];
        for (int i = 0; i < numVertices; i++) {
            Coordinate transformed = transformCoordinate(mesh.getVertices()[i], origin);
            xVertices[i] = transformed.getX() + 300;
            yVertices[i] = transformed.getY() + 300;
        }
        StdDraw.filledPolygon(xVertices, yVertices);
    }

    public Coordinate transformCoordinate(Coordinate entityCoord, Coordinate cameraCoord) {
        double dX = entityCoord.getX() - cameraCoord.getX();
        double dY = entityCoord.getY() - cameraCoord.getY();
        double dZ = entityCoord.getZ() - cameraCoord.getZ();

        double focalLength = 50;
        // E = (eX, eY, eZ) coordinate represents the display surface's position relative to the camera pinhole
        double eX = 0;
        double eY = 0;
        double eZ = focalLength;
        // (bX, bY) position on the 2d screen surface
        double bX = (double) ((eZ / dZ) * dX + eX);
        double bY = (double) ((eZ / dZ) * dY + eY);
        return new Coordinate(bX, bY, 0);
    }
}

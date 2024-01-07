package world;

import edu.princeton.cs.algs4.StdDraw;
import util.Coordinate;
import util.Mesh;

import java.awt.*;
import java.util.ArrayList;

public class Sphere extends Entity {
    public double radius;
    public int numSlices; // number of vertical slices around the sphere (n)
    public int numStacks; // number of horizontal slices around the sphere (m)
    public Sphere(double x, double y, double z, double r, int n, int m) {
        super(x, y, z);
        this.radius = r;
        this.numSlices = n;
        this.numStacks = m;
        this.meshes = new ArrayList<>();
        createMesh();
    }
    public void createMesh() {
        Coordinate top = new Coordinate(xPosition, yPosition + radius, zPosition);
        // Coordinate bottom = new Coordinate(xPosition, yPosition - radius, zPosition);

        double sliceAngle = (double) Math.TAU / numSlices;
        double stackAngle = (double) Math.PI / numStacks;

        // create top triangles
        double phi = 1 * stackAngle;
        double distanceFromYAxis = radius * Math.sin(phi);
        double distanceFromXZPlane = radius * Math.cos(phi);
        double y = yPosition + distanceFromXZPlane;

        for (int n = 0; n < numSlices; n++) {
            double theta = n * sliceAngle;

            double x0 = xPosition + distanceFromYAxis * Math.cos(theta);
            double z0 = zPosition + distanceFromYAxis * Math.sin(theta);
            Coordinate v0 = new Coordinate(x0, y, z0);

            double x1 = xPosition + distanceFromYAxis * Math.cos(theta + sliceAngle);
            double z1 = zPosition + distanceFromYAxis * Math.sin(theta + sliceAngle);
            Coordinate v1 = new Coordinate(x1, y, z1);

            Color meshColor = StdDraw.BOOK_BLUE;
            if (n % 2 == 0) {
                meshColor = StdDraw.BOOK_RED;
            }

            meshes.add(new Mesh(new Coordinate[]{top, v0, v1}, meshColor));
        }

//        double alpha = (double) 360 / numSlices;
//        for (int n = 0; n < numSlices; n++) {
//
//            double beta = (double) 180 / numStacks;
//            for (int m = 0; m < numStacks; m++) {
//
//            }
//        }

    }

}

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

        double sliceAngle = (double) Math.TAU / numSlices;
        double stackAngle = (double) Math.PI / numStacks;

        double phi;
        double distanceFromYAxis;
        double distanceFromXZPlane;
        double y;

        // create top triangles
        phi = 1 * stackAngle;
        distanceFromYAxis = radius * Math.sin(phi);
        distanceFromXZPlane = radius * Math.cos(phi);
        y = yPosition + distanceFromXZPlane;

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


        for (int n = 0; n < numSlices; n++) {
            double theta = n * sliceAngle;
            for (int m = 1; m < numStacks - 1; m++) {
                phi = m * stackAngle;

                double distanceFromYAxis0 = radius * Math.sin(phi);
                double distanceFromXZPlane0 = radius * Math.cos(phi);
                double y0 = yPosition + distanceFromXZPlane0;

                double x00 = xPosition + distanceFromYAxis0 * Math.cos(theta);
                double z00 = zPosition + distanceFromYAxis0 * Math.sin(theta);
                Coordinate v00 = new Coordinate(x00, y0, z00);

                double x01 = xPosition + distanceFromYAxis0 * Math.cos(theta + sliceAngle);
                double z01 = zPosition + distanceFromYAxis0 * Math.sin(theta + sliceAngle);
                Coordinate v01 = new Coordinate(x01, y0, z01);

                double distanceFromYAxis1 = radius * Math.sin(phi + stackAngle);
                double distanceFromXZPlane1 = radius * Math.cos(phi + stackAngle);
                double y1 = yPosition + distanceFromXZPlane1;

                double x10 = xPosition + distanceFromYAxis1 * Math.cos(theta);
                double z10 = zPosition + distanceFromYAxis1 * Math.sin(theta);
                Coordinate v10 = new Coordinate(x10, y1, z10);

                double x11 = xPosition + distanceFromYAxis1 * Math.cos(theta + sliceAngle);
                double z11 = zPosition + distanceFromYAxis1 * Math.sin(theta + sliceAngle);
                Coordinate v11 = new Coordinate(x11, y1, z11);

                Color meshColor = StdDraw.BOOK_BLUE;
                if (n % 2 == 0) {
                    meshColor = StdDraw.BOOK_RED;
                }

                meshes.add(new Mesh(new Coordinate[]{v00, v10, v11, v01}, meshColor));
            }
        }







        // create bottom triangles
        Coordinate bottom = new Coordinate(xPosition, yPosition - radius, zPosition);

        phi = (numStacks - 1) * stackAngle;
        distanceFromYAxis = radius * Math.sin(phi);
        distanceFromXZPlane = radius * Math.cos(phi);
        y = yPosition + distanceFromXZPlane;

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

            meshes.add(new Mesh(new Coordinate[]{bottom, v0, v1}, meshColor));
        }
    }

}

package world.assets;

import edu.princeton.cs.algs4.StdDraw;
import util.Coordinate;
import util.Mesh;
import world.RenderableEntity;

import java.awt.*;
import java.util.ArrayList;

public class Sphere extends RenderableEntity {
    public double radius;
    public int numSlices; // number of vertical slices around the sphere (n)
    public int numStacks; // number of horizontal slices around the sphere (m)
    public Sphere(double x, double y, double z, double pitch, double yaw, double roll, double r, int n, int m) {
        super(x, y, z, pitch, yaw, roll);
        this.radius = r;
        this.numSlices = n;
        this.numStacks = m;
        createMesh();
    }
    /** Create a UV Sphere surface mesh according to the inputted number of slices/stacks. */
    public void createMesh() {
        double sliceAngle = (double) Math.TAU / numSlices;
        double stackAngle = (double) Math.PI / numStacks;

        Coordinate top = new Coordinate(xPosition, yPosition, zPosition + radius);
        Coordinate bottom = new Coordinate(xPosition, yPosition, zPosition - radius);

        Color meshColor = StdDraw.BOOK_BLUE;
        // iterate through each slice (vertical section) and create meshes from the top down
        for (int n = 0; n < numSlices; n++) {
            double theta = n * sliceAngle; // angle parallel to the equator (longitude)
            double phi = stackAngle; // angle parallel to z-axis (latitude)

            // create top triangle
            double distanceFromZAxis = radius * Math.sin(phi);
            double distanceFromXYPlane = radius * Math.cos(phi);
            double z = distanceFromXYPlane;

            double x0 = distanceFromZAxis * Math.cos(theta);
            double y0 = distanceFromZAxis * Math.sin(theta);
            Coordinate v0 = Coordinate.fullPositionRotation(this, new Coordinate(x0, y0, z));

            double x1 = distanceFromZAxis * Math.cos(theta + sliceAngle);
            double y1 = distanceFromZAxis * Math.sin(theta + sliceAngle);
            Coordinate v1 = Coordinate.fullPositionRotation(this,new Coordinate(x1, y1, z));

            meshes.add(new Mesh(this, new Coordinate[]{top, v0, v1}, meshColor));

            // save previously computed coordinates to limit duplicates
            Coordinate previousV0 = v0;
            Coordinate previousV1 = v1;

            // render middle quad meshes
            for (int m = 1; m < numStacks - 1; m++) {
                phi += stackAngle;

                distanceFromZAxis = radius * Math.sin(phi);
                distanceFromXYPlane = radius * Math.cos(phi);
                z = distanceFromXYPlane;

                x0 = distanceFromZAxis * Math.cos(theta);
                y0 = distanceFromZAxis * Math.sin(theta);
                v0 = Coordinate.fullPositionRotation(this, new Coordinate(x0, y0, z));

                x1 = xPosition + distanceFromZAxis * Math.cos(theta + sliceAngle);
                y1 = zPosition + distanceFromZAxis * Math.sin(theta + sliceAngle);
                v1 = Coordinate.fullPositionRotation(this, new Coordinate(x1, y1, z));

                meshes.add(new Mesh(this, new Coordinate[]{previousV0, v0, v1, previousV1}, meshColor));

                previousV0 = v0;
                previousV1 = v1;
            }

            // add bottom triangle
            meshes.add(new Mesh(this, new Coordinate[]{previousV0, bottom, previousV1}, meshColor));
        }
    }

}

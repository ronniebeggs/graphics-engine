package world;

import edu.princeton.cs.algs4.StdDraw;
import util.Coordinate;
import util.Mesh;

import java.awt.*;
import java.util.ArrayList;

public class Spacecraft extends Entity {
    public double tankLength; // length of the spacecraft tank (also determines nose length)
    public double tankRadius; // radius of the spacecraft tank
    public int numSlices; // number of mesh slices
    public Spacecraft(double x, double y, double z, double pitch, double yaw, double roll, double tankLength, double tankRadius, int numSlices) {
        super(x, y, z, pitch, yaw, roll);
        this.tankLength = tankLength;
        this.tankRadius = tankRadius;
        this.numSlices = numSlices;
        this.meshes = new ArrayList<>();
        createMesh();
    }
    /** Create the surface mesh for the spacecraft entity. Initialize it pointing down the positive x-axis. */
    public void createMesh() {
        // save bottom coordinates to render separately at the end
        Coordinate[] bottomCoordinates = new Coordinate[numSlices];

        Coordinate nose = Coordinate.fullPositionRotation(this, new Coordinate(tankLength, 0, 0));
        Coordinate startTop = Coordinate.fullPositionRotation(this, new Coordinate(tankLength / 2, 0, tankRadius));
        Coordinate startBottom = Coordinate.fullPositionRotation(this, new Coordinate(-tankLength / 2, 0, tankRadius));
        bottomCoordinates[0] = startBottom;

        Coordinate previousTop = startTop;
        Coordinate previousBottom = startBottom;

        // rotate around ship and create each tank and nose slices
        double sliceAngle = (double) Math.TAU / numSlices;
        for (int n = 1; n < numSlices; n++) {
            double theta = n * sliceAngle;

            Coordinate bottom = Coordinate.fullPositionRotation(
                    this,
                    new Coordinate(
                            -tankLength / 2,
                    tankRadius * Math.sin(theta),
                    tankRadius * Math.cos(theta)
            ));
            bottomCoordinates[n] = bottom;

            Coordinate top = Coordinate.fullPositionRotation(
                    this,
                    new Coordinate(
                            tankLength / 2,
                    tankRadius * Math.sin(theta),
                    tankRadius * Math.cos(theta)
            ));

            Color meshColor = StdDraw.GRAY;
            if (n == 1) {
                meshColor = StdDraw.BOOK_BLUE;
            }

            // create the tank mesh slice
            meshes.add(new Mesh(new Coordinate[]{previousBottom, previousTop, top, bottom}, meshColor));
            // create the nose mesh slice
            meshes.add(new Mesh(new Coordinate[]{previousTop, nose, top}, StdDraw.BOOK_RED));

            previousTop = top;
            previousBottom = bottom;
        }
        // create the last tank and nose meshes connected to the original points
        meshes.add(new Mesh(new Coordinate[]{previousBottom, previousTop, startTop, startBottom}, StdDraw.GRAY));
        meshes.add(new Mesh(new Coordinate[]{previousTop, nose, startTop}, StdDraw.BOOK_RED));
        // create mesh at the bottom of the tank
        meshes.add(new Mesh(bottomCoordinates, StdDraw.PRINCETON_ORANGE));
    }
}

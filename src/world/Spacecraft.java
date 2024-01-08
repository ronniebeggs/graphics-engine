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
    public Spacecraft(double x, double y, double z, double tankLength, double tankRadius, int numSlices) {
        super(x, y, z);
        this.tankLength = tankLength;
        this.tankRadius = tankRadius;
        this.numSlices = numSlices;
        this.meshes = new ArrayList<>();
        createMesh();
    }
    /** Create the surface mesh for the spacecraft entity. */
    public void createMesh() {
        // save bottom coordinates to render separately at the end
        Coordinate[] bottomCoordinates = new Coordinate[numSlices];

        Coordinate nose = new Coordinate(xPosition, yPosition + tankLength, zPosition);
        Coordinate startTop = new Coordinate(xPosition + tankRadius, yPosition + (tankLength / 2), zPosition);
        Coordinate startBottom = new Coordinate(xPosition + tankRadius, yPosition - (tankLength / 2), zPosition);
        bottomCoordinates[0] = startBottom;

        Coordinate previousTop = startTop;
        Coordinate previousBottom = startBottom;

        // rotate around ship and create each tank and nose slices
        double sliceAngle = (double) Math.TAU / numSlices;
        for (int n = 1; n < numSlices; n++) {
            double theta = n * sliceAngle;

            Coordinate bottom = new Coordinate(
                    xPosition + tankRadius * Math.cos(theta),
                    yPosition - (tankLength / 2),
                    zPosition + tankRadius * Math.sin(theta)
            );
            bottomCoordinates[n] = bottom;

            Coordinate top = new Coordinate(
                    xPosition + tankRadius * Math.cos(theta),
                    yPosition + (tankLength / 2),
                    zPosition + tankRadius * Math.sin(theta)
            );

            // create the tank mesh slice
            meshes.add(new Mesh(new Coordinate[]{previousBottom, previousTop, top, bottom}, StdDraw.GRAY));
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

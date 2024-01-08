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
        super(x, y, z);
        this.tankLength = tankLength;
        this.tankRadius = tankRadius;
        this.numSlices = numSlices;
        this.meshes = new ArrayList<>();
        setDirection(pitch, yaw, roll);
        createMesh();
    }
    /** Create the surface mesh for the spacecraft entity. */
    public void createMesh() {
        // save bottom coordinates to render separately at the end
        Coordinate[] bottomCoordinates = new Coordinate[numSlices];

        Coordinate nose = directionTransform(new Coordinate(0, tankLength, 0));
        Coordinate startTop = directionTransform(new Coordinate(tankRadius, tankLength / 2, 0));
        Coordinate startBottom = directionTransform(new Coordinate(tankRadius, -tankLength / 2, 0));
        bottomCoordinates[0] = startBottom;

        Coordinate previousTop = startTop;
        Coordinate previousBottom = startBottom;

        // rotate around ship and create each tank and nose slices
        double sliceAngle = (double) Math.TAU / numSlices;
        for (int n = 1; n < numSlices; n++) {
            double theta = n * sliceAngle;

            Coordinate bottom = directionTransform(new Coordinate(
                    tankRadius * Math.cos(theta),
                    -tankLength / 2,
                    tankRadius * Math.sin(theta)
            ));
            bottomCoordinates[n] = bottom;

            Coordinate top = directionTransform(new Coordinate(
                    tankRadius * Math.cos(theta),
                    tankLength / 2,
                    tankRadius * Math.sin(theta)
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

//    public Coordinate directionTransform(Coordinate original) {
//        double X = original.getX();
//        double Y = original.getY();
//        double Z = original.getZ();
//
//        double thetaX = -Math.toRadians(pitch); // pitch
//        double thetaY = -Math.toRadians(yaw - 90); // yaw
//        double thetaZ = Math.toRadians(roll); // roll
//
//        double dX = Math.cos(thetaY) * (Math.sin(thetaZ) * Y + Math.cos(thetaZ) * X) - Math.sin(thetaY) * Z;
//        double dY = Math.sin(thetaX) * (Math.cos(thetaY) * Z + Math.sin(thetaY) * (Math.sin(thetaZ) * Y + Math.cos(thetaZ) * X)) + Math.cos(thetaX) * (Math.cos(thetaZ) * Y - Math.sin(thetaZ) * X);
//        double dZ = Math.cos(thetaX) * (Math.cos(thetaY) * Z + Math.sin(thetaY) * (Math.sin(thetaZ) * Y + Math.cos(thetaZ) * X)) - Math.sin(thetaX) * (Math.cos(thetaZ) * Y - Math.sin(thetaZ) * X);
//
//        return new Coordinate(dX, dY, dZ);
//    }

    /**
     * Scale and rotate an object's relative positions axis to real simulation positions.
     * @param relativeToObject position relative to the object's axes.
     * @return position transformed to real simulation axes.
     * */
    public Coordinate directionTransform(Coordinate relativeToObject) {
        // calculate positions relative to the object's center
        double xE = relativeToObject.getX();
        double yE = relativeToObject.getY();
        double zE = relativeToObject.getZ();

        // yaw transformation
        double yawRadians = Math.toRadians(yaw);
        double x1 = xE * Math.cos(yawRadians) + zE * Math.sin(yawRadians);
        double y1 = yE;
        double z1 = -xE * Math.sin(yawRadians) + zE * Math.cos(yawRadians);

        // pitch transformation
        double pitchRadians = Math.toRadians(pitch);
        double x2 = x1 * Math.cos(pitchRadians) - y1 * Math.sin(pitchRadians);
        double y2 = x1 * Math.sin(pitchRadians) + y1 * Math.cos(pitchRadians);
        double z2 = z1;

        // roll transformation
        double rollRadians = Math.toRadians(roll);
        double x = x2;
        double y = -z2 * Math.sin(rollRadians) + y2 * Math.cos(rollRadians);
        double z = z2 * Math.cos(rollRadians) + y2 * Math.sin(rollRadians);

        // transform relative positions to real simulation positions
        return new Coordinate(
                x + getPosition().getX(),
                y + getPosition().getY(),
                z + getPosition().getZ()
        );
    }
}

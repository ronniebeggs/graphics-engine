package engine;

import edu.princeton.cs.algs4.StdDraw;
import util.Coordinate;
import util.Mesh;
import world.Entity;
import world.World;

import java.awt.Color;
import java.util.PriorityQueue;

public class Renderer {
    /**
     * `MeshRankNode` allows us the rank meshes based on proximity to the camera.
     * Mitigates awkward overlap of different meshes.
     * */
    public static class MeshRankNode implements Comparable {
        double distance;
        Mesh mesh;
        public MeshRankNode(Mesh mesh, double distance) {
            this.mesh = mesh;
            this.distance = distance;
        }
        @Override
        public int compareTo(Object o) {
            if (o instanceof MeshRankNode other) {
                return Double.compare(other.distance, this.distance);
            }
            throw new IllegalArgumentException();
        }

    }
    private int displayWidth;
    private int displayHeight;
    private double verticalViewAngle;
    private double focalLength;
    private final Coordinate camera = new Coordinate(0, 0, 0);
    /**
     * Initializes StdDraw parameters and launches the StdDraw window. w and h are the
     * width and height of the world in pixels.
     *
     * @param width width of the window in pixels.
     * @param height height of the window in pixels.
     * @param fovY vertical view angle of the camera.
     */
    public void initialize(int width, int height, double fovY) {
        this.displayWidth = width;
        this.displayHeight = height;
        this.verticalViewAngle = fovY;
        this.focalLength = displayHeight / (2 * Math.tan(Math.toRadians(verticalViewAngle)));

        StdDraw.setCanvasSize(width, height);
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);

        StdDraw.clear(new Color(0, 0, 0));
        StdDraw.enableDoubleBuffering();
        StdDraw.show();
    }

    /**
     * Clears the display then renders each entity within the world.
     * @param world current world state to be rendered.
     * */
    public void renderFrame(World world) {
        StdDraw.clear(new Color(0, 0, 0));
        StdDraw.enableDoubleBuffering();
        for (Entity entity : world.fetchEntities()) {
            renderEntity(entity);
        }
        StdDraw.show();
    }

    /**
     * Render each of an entity's meshes in decreasing order of distance to the camera.
     * @param entity entity to be rendered.
     * */
    public void renderEntity(Entity entity) {
        // map each mesh to its distance relative to the camera, and place within priority queue.
        PriorityQueue<MeshRankNode> meshRank = new PriorityQueue<>();
        for (Mesh mesh : entity.getMeshes()) {
            Coordinate averagePosition = mesh.averagePosition();
            double deltaXSquared = Math.pow(camera.getX() - averagePosition.getX(), 2);
            double deltaYSquared = Math.pow(camera.getY() - averagePosition.getY(), 2);
            double deltaZSquared = Math.pow(camera.getZ() - averagePosition.getZ(), 2);
            double distanceToCamera = Math.sqrt(deltaXSquared + deltaYSquared + deltaZSquared);
            meshRank.add(new MeshRankNode(mesh, distanceToCamera));
        }
        // render each mesh in decreasing order relative to the camera to mitigate rendering overlap.
        while (!meshRank.isEmpty()) {
            renderMesh(meshRank.remove().mesh);
        }
    }

    /**
     * Draw and fill the mesh using StdDraw library.
     * @param mesh mesh to be rendered.
     * */
    public void renderMesh(Mesh mesh) {
        StdDraw.setPenColor(mesh.getColor());
        int numVertices = mesh.getNumVertices();
        double[] xVertices = new double[numVertices];
        double[] yVertices = new double[numVertices];
        for (int i = 0; i < numVertices; i++) {
            Coordinate transformed = transformCoordinate(mesh.getVertices()[i]);
            xVertices[i] = transformed.getX() + (double) displayWidth / 2;
            yVertices[i] = transformed.getY() + (double) displayHeight / 2;
        }
        StdDraw.filledPolygon(xVertices, yVertices);
    }

    /**
     * Transform a 3D coordinate to a 2D projection on screen.
     * @param position 3D position within world.
     * @return renderable 2D coordinate.
     * */
    public Coordinate transformCoordinate(Coordinate position) {
        // D = (dX, dY, dz) -> position of point A with respect to a coordinate system defined by the camera
        double dX = position.getX() - camera.getX();
        double dY = position.getY() - camera.getY();
        double dZ = position.getZ() - camera.getZ();
        // E = (eX, eY, eZ) -> position of the display surface plane position relative to the camera pinhole
        double eX = 0;
        double eY = 0;
        double eZ = focalLength;
        // (bX, bY) -> transformed position on the 2d screen surface
        double bX = (double) ((eZ / dZ) * dX + eX);
        double bY = (double) ((eZ / dZ) * dY + eY);
        return new Coordinate(bX, bY, 0);
    }
}


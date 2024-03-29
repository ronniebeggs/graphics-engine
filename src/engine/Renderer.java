package engine;

import edu.princeton.cs.algs4.StdDraw;
import util.Coordinate;
import util.Mesh;
import world.RenderableEntity;
import world.World;
import world.Camera;
import world.Entity;

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
    private double focalLength;
    private Camera camera;
    private Entity[] lightSources;
    /**
     * Initializes StdDraw parameters and launches the StdDraw window. w and h are the
     * width and height of the world in pixels.
     *
     * @param width width of the window in pixels.
     * @param height height of the window in pixels.
     * @param fovY vertical view angle of the camera.
     */
    public void initialize(Camera camera, Entity[] lightSources, int width, int height, double verticalViewAngle) {
        this.camera = camera;
        this.lightSources = lightSources;
        this.displayWidth = width;
        this.displayHeight = height;
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
        for (RenderableEntity entity : world.fetchRenderableEntities()) {
            if (shouldRenderEntity(entity, 50)) {
                renderEntity(entity);
            }
        }
        StdDraw.show();
    }

    /**
     * Render each of an entity's meshes in decreasing order of distance to the camera.
     * @param entity entity to be rendered.
     * */
    public void renderEntity(RenderableEntity entity) {
        // map each mesh to its distance relative to the camera, and place within priority queue.
        PriorityQueue<MeshRankNode> meshRank = new PriorityQueue<>();
        for (Mesh mesh : entity.getMeshes()) {
            Coordinate meshPosition = mesh.averagePosition();
            Coordinate cameraPosition = camera.getPosition();
            double distanceToCamera = cameraPosition.distance3D(meshPosition);
            meshRank.add(new MeshRankNode(mesh, distanceToCamera));
        }
        // render each mesh in decreasing order relative to the camera to mitigate rendering overlap.
        while (!meshRank.isEmpty()) {
            renderMesh(meshRank.remove().mesh);
        }
    }

    /**
     * Shade the mesh using the `lightSource`s in the simulation.
     * @param mesh target mesh to apply the shader too.
     * @return adjusted shader color.
     * */
    public Color shadeMesh(Mesh mesh) {
        double strongestFacingRatio = 0.1;
        // iterate through lightSources and find the brightest light
        for (int lightIndex = 0; lightIndex < lightSources.length; lightIndex++) {
            Entity light = lightSources[lightIndex];
            // light sources shouldn't light themselves up
            if (light.equals(mesh.getParent())) {
                continue;
            }

            Coordinate lightPosition = light.getPosition();
            Coordinate meshPosition = mesh.averagePosition();
            Coordinate lightVector = Coordinate.normalize(new Coordinate(
                    -(lightPosition.getX() - meshPosition.getX()),
                    -(lightPosition.getY() - meshPosition.getY()),
                    -(lightPosition.getZ() - meshPosition.getZ())
            ));

            double facingRatio = Coordinate.dotProduct(lightVector, mesh.getNormalVector());
            if (facingRatio > strongestFacingRatio) {
                strongestFacingRatio = facingRatio;
            }
        }
        // shade the mesh according to the brightest light source
        double brightnessProportion = strongestFacingRatio * 1;
        float[] colorComponents = new float[3];
        mesh.getColor().getColorComponents(colorComponents);
        return new Color(
                (int) (colorComponents[0] * brightnessProportion * 255),
                (int) (colorComponents[1] * brightnessProportion * 255),
                (int) (colorComponents[2] * brightnessProportion * 255)
        );
    }

    /**
     * Draw and fill the mesh using StdDraw library.
     * @param mesh mesh to be rendered.
     * */
    public void renderMesh(Mesh mesh) {
        Color adjustedColor = shadeMesh(mesh);
        StdDraw.setPenColor(adjustedColor);
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
        Coordinate cameraPosition = camera.getPosition();
        double X = position.getX() - cameraPosition.getX();
        double Y = position.getY() - cameraPosition.getY();
        double Z = position.getZ() - cameraPosition.getZ();
        // Theta = (thetaX, thetaY, thetaZ) -> tait-bryan angles
        Coordinate cameraTilt = camera.getCameraTilt();
        double thetaX = -Math.toRadians(cameraTilt.getX()); // pitch
        double thetaY = -Math.toRadians(cameraTilt.getY() - 90); // yaw
        double thetaZ = Math.toRadians(cameraTilt.getZ()); // roll
        // I have no idea if this is going to work
        double dX = Math.cos(thetaY) * (Math.sin(thetaZ) * Y + Math.cos(thetaZ) * X) - Math.sin(thetaY) * Z;
        double dY = Math.sin(thetaX) * (Math.cos(thetaY) * Z + Math.sin(thetaY) * (Math.sin(thetaZ) * Y + Math.cos(thetaZ) * X)) + Math.cos(thetaX) * (Math.cos(thetaZ) * Y - Math.sin(thetaZ) * X);
        double dZ = Math.cos(thetaX) * (Math.cos(thetaY) * Z + Math.sin(thetaY) * (Math.sin(thetaZ) * Y + Math.cos(thetaZ) * X)) - Math.sin(thetaX) * (Math.cos(thetaZ) * Y - Math.sin(thetaZ) * X);
        // E = (eX, eY, eZ) -> position of the display surface plane position relative to the camera pinhole
        double eX = 0;
        double eY = 0;
        double eZ = focalLength;
        // (bX, bY) -> transformed position on the 2d screen surface
        double bX = (double) ((eZ / dZ) * dX + eX);
        double bY = (double) ((eZ / dZ) * dY + eY);
        return new Coordinate(bX, bY, 0);
    }

    public boolean shouldRenderEntity(RenderableEntity entity, double frontClip) {
        return camera.distanceToViewPlane(entity) >= frontClip;
    }
}


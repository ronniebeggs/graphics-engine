package engine;

import edu.princeton.cs.algs4.StdDraw;
import world.*;
import util.Coordinate;

/**
 * Class that handles the overarching operations of the project.
 * Solicits user input, captures the world state, and renders each frame.
 * */
public class Engine {

    Renderer ter = new Renderer();
    Camera camera;
    public final int DISPLAY_WIDTH = 600;
    public final int DISPLAY_HEIGHT = 600;
    public final int VERTICAL_VIEW_ANGLE = 60;

    /**
     * Test function for rendering individual scenes.
     * */
    public void singleFrameTest() {
        World world = new World();
        camera = new Camera(0, 0, 0);
//        Sphere sphere = new Sphere(0, 0, 100, 50, 12, 6);
        Spacecraft spacecraft = new Spacecraft(0, 0, 100, 0, 0, 0, 100, 30, 12);
        world.insertEntity(spacecraft);
        ter.initialize(camera, DISPLAY_WIDTH, DISPLAY_HEIGHT, VERTICAL_VIEW_ANGLE);
        ter.renderFrame(world);
    }

    public void mainLoop() {
        World world = new World();
        camera = new Camera(0, 0, 0);
//        Sphere sphere = new Sphere(0, 0, 100, 50, 48, 24);
        Spacecraft spacecraft = new Spacecraft(0, 0, 100, 90, 0, 270, 100, 20, 12);
        world.insertEntity(spacecraft);
        ter.initialize(camera, DISPLAY_WIDTH, DISPLAY_HEIGHT, VERTICAL_VIEW_ANGLE);
        while (true) {
            ter.renderFrame(world);
            if (StdDraw.hasNextKeyTyped()) {
                char keyPress = StdDraw.nextKeyTyped();
                boolean isTargetLocked = true;
                if (isTargetLocked) {
                    fixedOrbitalMovement(spacecraft, keyPress);
                } else {
                    freeMovement(spacecraft, keyPress);
                }
            }
        }
    }
    public void freeMovement(Entity target, char keyPress) {
        switch (keyPress) {
            case 'u' -> {
                camera.pointToward(target);
            }
            case 'w' -> {
                camera.moveFrontal(10);
            }
            case 's' -> {
                camera.moveFrontal(-10);
            }
            case 'd' -> {
                camera.moveLateral(10);
            }
            case 'a' -> {
                camera.moveLateral(-10);
            }
            case 'q' -> {
                camera.moveTransverse(10);
            }
            case 'e' -> {
                camera.moveTransverse(-10);
            }
            case 'i' -> {
                camera.rotatePitch(10);
            }
            case 'k' -> {
                camera.rotatePitch(-10);
            }
            case 'l' -> {
                camera.rotateYaw(-10);
            }
            case 'j' -> {
                camera.rotateYaw(10);
            }
        };
    }
    public void fixedOrbitalMovement(Entity target, char keyPress) {
        switch (keyPress) {
            case 'w' -> {
                camera.moveFrontal(10);
            }
            case 's' -> {
                camera.moveFrontal(-10);
            }
            case 'd' -> {
                camera.rotateAround(target, 10);
            }
            case 'a' -> {
                camera.rotateAround(target, -10);
            }
            case 'q' -> {
                camera.moveTransverse(10);
            }
            case 'e' -> {
                camera.moveTransverse(-10);
            }
            case 'i' -> {
                camera.rotatePitch(10);
            }
            case 'k' -> {
                camera.rotatePitch(-10);
            }
            case 'l' -> {
                camera.rotateYaw(-10);
            }
            case 'j' -> {
                camera.rotateYaw(10);
            }
        }
    }
}

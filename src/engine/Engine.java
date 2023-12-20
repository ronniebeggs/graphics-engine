package engine;

import edu.princeton.cs.algs4.StdDraw;
import world.World;
import world.Cube;
import world.Camera;
import world.Entity;

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
        Cube cube = new Cube(-50, -50, 100, 60);
        world.insertEntity(cube);
        ter.initialize(camera, DISPLAY_WIDTH, DISPLAY_HEIGHT, VERTICAL_VIEW_ANGLE);
        ter.renderFrame(world);
    }

    public void mainLoop() {
        World world = new World();
        camera = new Camera(0, 0, 0);
        Cube cube = new Cube(0, 0, 100, 60);
        world.insertEntity(cube);
        ter.initialize(camera, DISPLAY_WIDTH, DISPLAY_HEIGHT, VERTICAL_VIEW_ANGLE);
        while (true) {
            ter.renderFrame(world);
            if (StdDraw.hasNextKeyTyped()) {
                char keyPress = StdDraw.nextKeyTyped();
                boolean isTargetLocked = true;
                if (isTargetLocked) {
                    fixedOrbitalMovement(cube, keyPress);
                } else {
                    freeMovement(keyPress);
                }
            }
        }
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
        }
    }

    public void freeMovement(char keyPress) {
        switch (keyPress) {
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
            case 'i' -> {
                camera.rotatePitch(-10);
            }
            case 'k' -> {
                camera.rotatePitch(10);
            }
            case 'l' -> {
                camera.rotateYaw(10);
            }
            case 'j' -> {
                camera.rotateYaw(-10);
            }
            case 'q' -> {
                camera.moveTransverse(10);
            }
            case 'e' -> {
                camera.moveTransverse(-10);
            }
        };
    }
}

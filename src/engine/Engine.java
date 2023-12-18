package engine;

import org.checkerframework.checker.units.qual.C;
import world.Square;
import world.World;
import world.Cube;

/**
 * Class that handles the overarching operations of the project.
 * Solicits user input, captures the world state, and renders each frame.
 * */
public class Engine {

    Renderer ter = new Renderer();
    // display dimensions in number of pixels.
    public final int DISPLAY_WIDTH = 600;
    public final int DISPLAY_HEIGHT = 600;
    // depth of the near clipping plane
    public int near = 50;
    // depth of the far clipping plane
    public int far = 300;
    public double aspect = (double) DISPLAY_WIDTH / DISPLAY_HEIGHT;

    /**
     * Test function for rendering individual scenes.
     * */
    public void singleFrameTest() {
        World world = new World();
//        Square frontFace = new Square(0, 0, 100, 100);
//        Square topFace = new Square(200, 0, 200, 100);
//        world.insertEntity(frontFace);
//        world.insertEntity(topFace);
        Cube cube = new Cube(-60, -60, 100, 60);
        world.insertEntity(cube);
        ter.initialize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        ter.renderFrame(world);
    }

    public int fetchCenterX() {
        return DISPLAY_WIDTH / 2;
    }
    public int fetchCenterY() {
        return DISPLAY_HEIGHT / 2;
    }
}

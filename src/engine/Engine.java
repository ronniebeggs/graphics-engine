package engine;

import world.World;
import world.Cube;

/**
 * Class that handles the overarching operations of the project.
 * Solicits user input, captures the world state, and renders each frame.
 * */
public class Engine {

    Renderer ter = new Renderer();
    public final int DISPLAY_WIDTH = 600;
    public final int DISPLAY_HEIGHT = 600;
    public final int VERTICAL_VIEW_ANGLE = 60;

    /**
     * Test function for rendering individual scenes.
     * */
    public void singleFrameTest() {
        World world = new World();
        Cube cube = new Cube(-50, -50, 100, 60);
        world.insertEntity(cube);
        ter.initialize(DISPLAY_WIDTH, DISPLAY_HEIGHT, VERTICAL_VIEW_ANGLE);
        ter.renderFrame(world);
    }
}

package engine;

import world.World;

/**
 * Class that handles the overarching operations of the project.
 * Solicits user input, captures the world state, and renders each frame.
 * */
public class Engine {

    Renderer ter = new Renderer();
    // display dimensions in number of pixels.
    public final int DISPLAY_WIDTH = 600;
    public final int DISPLAY_HEIGHT = 600;

    /**
     * Test function for rendering individual scenes.
     * */
    public void singleFrameTest() {
        World world = new World();
        ter.initialize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        ter.renderFrame(world);
    }
}

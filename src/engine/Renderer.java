package engine;

import edu.princeton.cs.algs4.StdDraw;
import world.Entity;
import world.World;

import java.awt.Color;

public class Renderer {
    private int width;
    private int height;

    /**
     * Initializes StdDraw parameters and launches the StdDraw window. w and h are the
     * width and height of the world in pixels.
     *
     * @param w width of the window in pixels.
     * @param h height of the window in pixels.
     */
    public void initialize(int w, int h) {
        this.width = w;
        this.height = h;
        StdDraw.setCanvasSize(width, height);

        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);

        StdDraw.clear(new Color(0, 0, 0));

        StdDraw.enableDoubleBuffering();
        StdDraw.show();
    }

    /**
     * Bare-bones render method. Calls the render method of each individual entity.
     *
     * @param world current world state to be rendered.
     * */
    public void renderFrame(World world) {
        StdDraw.clear(new Color(0, 0, 0));
        StdDraw.enableDoubleBuffering();
        for (Entity entity : world.fetchEntities()) {
            entity.render();
        }
        StdDraw.show();
    }
}


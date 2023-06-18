package world;

import java.util.HashSet;
import java.util.Set;

public class World {
    // set of all render-able entities contained within the world.
    public Set<Entity> entities;
    public World() {
        entities = new HashSet<>();
    }

    /**
     * Returns all entities to be rendered onto the display.
     * */
    public Set<Entity> fetchEntities() {
        return entities;
    }
}

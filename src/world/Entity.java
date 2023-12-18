package world;

import util.Mesh;
import java.util.List;

public class Entity {
    public int xPosition;
    public int yPosition;
    public int zPosition;
    public List<Mesh> meshes;

    public Entity(int x, int y, int z) {
        xPosition = x;
        yPosition = y;
        zPosition = z;
    }

    public List<Mesh> getMeshes() {
        return this.meshes;
    }
}

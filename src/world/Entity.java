package world;

import util.Coordinate;
import util.Mesh;
import java.util.List;

public class Entity {
    public double xPosition;
    public double yPosition;
    public double zPosition;
    public double pitch; // vertical rotation (looking up and down)
    public double yaw; // rotation within xz-plane (turning right and left)
    public double roll; // rotation relative to viewing plane (barrel roles)
    public List<Mesh> meshes;

    public Entity(double x, double y, double z) {
        xPosition = x;
        yPosition = y;
        zPosition = z;
        pitch = 0;
        yaw = 0;
        roll = 0;
    }
    public Coordinate getPosition() {
        return new Coordinate(xPosition, yPosition, zPosition);
    }
    public void setDirection(double pitch, double yaw, double roll) {
        this.pitch = pitch;
        this.yaw = yaw;
        this.roll = roll;
    }
    public Coordinate getDirection() {
        return new Coordinate(pitch, yaw, roll);
    }
    public List<Mesh> getMeshes() {
        return this.meshes;
    }
}

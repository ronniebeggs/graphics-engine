package world;
import util.Coordinate;

public class Camera extends Entity {
    public double pitch;
    public double yaw;
    public double roll;
    public Camera(double x, double y, double z) {
        super(x, y, z);
        this.pitch = 0;
        this.yaw = 0;
        this.roll = 0;
    }
    public void moveFrontal(double distance) {
        // TODO: change variable names here
        double deltaFrontal = distance * Math.cos(Math.toRadians(yaw));
        double deltaLateral = distance * Math.sin(Math.toRadians(yaw));
        this.zPosition += deltaFrontal;
        this.xPosition += deltaLateral;
    }
    public void moveLateral(double distance) {
        // TODO: change variable names here
        double deltaFrontal = distance * Math.cos(Math.toRadians(yaw + 90));
        double deltaLateral = distance * Math.sin(Math.toRadians(yaw + 90));
        this.zPosition += deltaFrontal;
        this.xPosition += deltaLateral;
    }
    public void rotatePitch(double degree) {
        this.pitch += degree;
    }
    public void rotateYaw(double degree) {
        this.yaw += degree;
    }
    public Coordinate getCameraTilt() {
        return new Coordinate(pitch, yaw, roll);
    }
}

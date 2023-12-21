package world;
import util.Coordinate;

public class Camera extends Entity {
    public double pitch;
    public double yaw;
    public double roll;
    public Camera(double x, double y, double z) {
        super(x, y, z);
        this.pitch = 0;
        this.yaw = 90;
        this.roll = 0;
    }
    public void moveFrontal(double distance) {
        moveInDirection(distance, this.yaw);
    }
    public void moveLateral(double distance) {
        moveInDirection(distance, this.yaw - 90);
    }
    public void moveInDirection(double distance, double angle) {
        double deltaX = distance * Math.cos(Math.toRadians(angle));
        double deltaZ = distance * Math.sin(Math.toRadians(angle));
        this.xPosition += deltaX;
        this.zPosition += deltaZ;
    }
    public void moveTransverse(double distance) {
        this.yPosition += distance;
    }
    public void rotatePitch(double degree) {
        this.pitch += degree;
    }
    public void rotateYaw(double degree) {
        // TODO: floor mod degree to avoid overflow (view angles could be integers with limited precisions)
        this.yaw += degree;
    }
    public void rotateAround(Entity target, double degree) {
        Coordinate cameraPosition = getPosition();
        Coordinate targetPosition = target.getPosition();
        double distanceBetween = cameraPosition.distanceTo(targetPosition);

        double deltaFrontal = distanceBetween * (1 - Math.cos(Math.toRadians(degree)));
        double deltaLateral = distanceBetween * Math.sin(Math.toRadians(degree));

        this.yaw += -degree;
        this.zPosition += deltaFrontal;
        this.xPosition += deltaLateral;
    }
    public Coordinate getCameraTilt() {
        return new Coordinate(pitch, yaw, roll);
    }
}

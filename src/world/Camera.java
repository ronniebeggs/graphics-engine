package world;
import util.Coordinate;

/**
 * Camera object exists within the simulation. Other objects are rendered relative
 * to the camera and its position/direction attributes.
 * */
public class Camera extends Entity {
    public Camera(double x, double y, double z) {
        super(x, y, z, 0, 0, 0);
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
    public void pointToward(Entity target) {
        Coordinate cameraPosition = getPosition();
        Coordinate targetPosition = target.getPosition();
        // calculate the yaw (right-left view)
        double deltaX = targetPosition.getX() - cameraPosition.getX();
        double deltaZ = targetPosition.getZ() - cameraPosition.getZ();
        this.yaw = Math.toDegrees(Math.atan2(deltaZ, deltaX));
        // calculate the pitch (up-down view)
        double deltaY = targetPosition.getY() - cameraPosition.getY();
        double xzDistance = cameraPosition.distanceXZ(targetPosition);
        this.pitch = Math.toDegrees(Math.atan2(deltaY, xzDistance));
    }
    public void rotateAroundHorizontal(Entity target, double degree) {
        Coordinate cameraPosition = getPosition();
        Coordinate targetPosition = target.getPosition();
        // calculate the angle of the camera relative to the target
        double deltaX1 = cameraPosition.getX() - targetPosition.getX();
        double deltaZ1 = cameraPosition.getZ() - targetPosition.getZ();
        double relativeToTarget = Math.atan2(deltaZ1, deltaX1);
        // calculate the new camera position relative to the target position
        double xzDistance = cameraPosition.distanceXZ(targetPosition);
        double deltaX2 = xzDistance * Math.cos(relativeToTarget + Math.toRadians(degree));
        double deltaZ2 = xzDistance * Math.sin(relativeToTarget + Math.toRadians(degree));
        // update camera position and view angle
        this.xPosition = targetPosition.getX() + deltaX2;
        this.zPosition = targetPosition.getZ() + deltaZ2;
        pointToward(target);
    }
    public void rotateAroundVertical(Entity target, double degree) {
        Coordinate cameraPosition = getPosition();
        Coordinate targetPosition = target.getPosition();
        // calculate the angle of the camera relative to the target in the x-z plane
        double deltaX1 = cameraPosition.getX() - targetPosition.getX();
        double deltaZ1 = cameraPosition.getZ() - targetPosition.getZ();
        double relativeAngleXZ = Math.atan2(deltaZ1, deltaX1);
        // calculate the angle of the camera relative to the target in the xz-y plane
        double deltaXZ1 = Math.sqrt(Math.pow(deltaX1, 2) + Math.pow(deltaZ1, 2));
        double deltaY1 = cameraPosition.getY() - targetPosition.getY();
        double relativeAngleY = Math.atan2(deltaY1, deltaXZ1);
        // calculate the new camera position relative to the target position
        double distance3D = cameraPosition.distance3D(targetPosition);
        double deltaXZ2 = distance3D * Math.cos(relativeAngleY + Math.toRadians(degree));
        double deltaY2 = distance3D * Math.sin(relativeAngleY + Math.toRadians(degree));
        // update camera position and view angle
        this.xPosition = targetPosition.getX() + deltaXZ2 * Math.cos(relativeAngleXZ);
        this.zPosition = targetPosition.getZ() + deltaXZ2 * Math.sin(relativeAngleXZ);
        this.yPosition = targetPosition.getY() + deltaY2;
        pointToward(target);
    }
    public Coordinate getCameraTilt() {
        return new Coordinate(pitch, yaw, roll);
    }
}

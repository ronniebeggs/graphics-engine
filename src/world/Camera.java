package world;
import Jama.Matrix;
import util.Coordinate;

/**
 * Camera object exists within the simulation. Other objects are rendered relative
 * to the camera and its position/direction attributes.
 * */
public class Camera extends Entity {
    public Camera(double x, double y, double z, double pitch, double yaw, double roll) {
        super(x, y, z, pitch, yaw, roll);
    }
    /**
     * Move the camera forward/backward relative to the camera's current yaw direction in the xz-plane.
     * @param distance simulation distance to move (positive: forward, negative: backward)
     * */
    public void moveFrontal(double distance) {
        moveInDirection(distance, this.yaw);
    }
    /**
     * Move the camera right/left relative to the camera's current yaw direction in the xy-plane.
     * @param distance simulation distance to move (positive: right, negative: left)
     * */
    public void moveLateral(double distance) {
        moveInDirection(distance, this.yaw + 90);
    }
    private void moveInDirection(double distance, double angle) {
        double deltaX = distance * Math.cos(Math.toRadians(angle));
        double deltaY = distance * Math.sin(Math.toRadians(angle));
        this.xPosition += deltaX;
        this.yPosition += deltaY;
    }
    /**
     * Move the camera up/down relative along the z-axis.
     * @param distanceChange simulation distance to move (positive: up, negative: down)
     * */
    public void moveTransverse(double distanceChange) {
        this.zPosition += distanceChange;
    }
    /**
     * Turn the camera up/down.
     * @param degreeChange change in the pitch angle (positive: up, negative: down)
     * */
    public void rotatePitch(double degreeChange) {
        this.pitch += degreeChange;
    }
    /**
     * Turn the camera right/left.
     * @param degreeChange change in the yaw angle (positive: right, negative: left)
     * */
    public void rotateYaw(double degreeChange) {
        // TODO: floor mod degree to avoid overflow (view angles could be integers with limited precisions)
        this.yaw += degreeChange;
    }
    /**
     * Point the camera toward the target entity by shifting pitch and yaw directions.
     * @param target entity to point at.
     * */
    public void pointToward(Entity target) {
        Coordinate cameraPosition = getPosition();
        Coordinate targetPosition = target.getPosition();
        // calculate the yaw (right-left view)
        double deltaX = targetPosition.getX() - cameraPosition.getX();
        double deltaY = targetPosition.getY() - cameraPosition.getY();
        this.yaw = Math.toDegrees(Math.atan2(deltaY, deltaX));
        // calculate the pitch (up-down view)
        double deltaZ = targetPosition.getZ() - cameraPosition.getZ();
        double xyDistance = cameraPosition.distanceXY(targetPosition);
        this.pitch = -Math.toDegrees(Math.atan2(deltaZ, xyDistance));
    }
    /**
     * Horizontally orbit the camera around the target entity by a certain degree.
     * @param target entity to orbit around and remain pointed at.
     * @param degree degree of horizontal orbit to shift around the entity.
     * */
    public void rotateAroundHorizontal(Entity target, double degree) {
        Coordinate cameraPosition = getPosition();
        Coordinate targetPosition = target.getPosition();
        // calculate the angle of the camera relative to the target
        double deltaX1 = cameraPosition.getX() - targetPosition.getX();
        double deltaY1 = cameraPosition.getY() - targetPosition.getY();
        double relativeToTarget = Math.atan2(deltaY1, deltaX1);
        // calculate the new camera position relative to the target position
        double xyDistance = cameraPosition.distanceXY(targetPosition);
        double deltaX2 = xyDistance * Math.cos(relativeToTarget + Math.toRadians(degree));
        double deltaY2 = xyDistance * Math.sin(relativeToTarget + Math.toRadians(degree));
        // update camera position and view angle
        this.xPosition = targetPosition.getX() + deltaX2;
        this.yPosition = targetPosition.getY() + deltaY2;
        pointToward(target);
    }
    /**
     * Vertically orbit the camera around the target entity by a certain degree.
     * @param target entity to orbit around and remain pointed at.
     * @param degree degree of vertical orbit to shift around the entity.
     * */
    public void rotateAroundVertical(Entity target, double degree) {
        Coordinate cameraPosition = getPosition();
        Coordinate targetPosition = target.getPosition();
        // calculate the angle of the camera relative to the target in the x-y plane
        double deltaX1 = cameraPosition.getX() - targetPosition.getX();
        double deltaY1 = cameraPosition.getY() - targetPosition.getY();
        double relativeAngleXY = Math.atan2(deltaY1, deltaX1);
        // calculate the angle of the camera relative to the target in the xy-z plane
        double deltaXY1 = Math.sqrt(Math.pow(deltaX1, 2) + Math.pow(deltaY1, 2));
        double deltaZ1 = cameraPosition.getZ() - targetPosition.getZ();
        double relativeAngleZ = Math.atan2(deltaZ1, deltaXY1);
        // compute angle of the camera's new position relative to the target
        double newTargetAngle = relativeAngleZ + Math.toRadians(degree);
        // adjust angle if it exceeds a boundary to prevent fully rotating over the target
        if (newTargetAngle >= Math.PI / 2) {
            newTargetAngle = Math.PI / 2;
        } else if (newTargetAngle <= -Math.PI / 2) {
            newTargetAngle = -Math.PI / 2;
        }
        // calculate the new camera position relative to the target position
        double distance3D = cameraPosition.distance3D(targetPosition);
        double deltaXY2 = distance3D * Math.cos(newTargetAngle);
        double deltaZ2 = distance3D * Math.sin(newTargetAngle);
        // update camera position and view angle
        this.xPosition = targetPosition.getX() + deltaXY2 * Math.cos(relativeAngleXY);
        this.yPosition = targetPosition.getY() + deltaXY2 * Math.sin(relativeAngleXY);
        this.zPosition = targetPosition.getZ() + deltaZ2;
        pointToward(target);
    }
    /**
     * Move the camera closer toward the target by a specified distance.
     * @param target entity to move toward and remain pointed at.
     * @param distance absolute change in distance to move the camera by.
     * */
    public void moveTowardTarget(Entity target, double distance) {
        Coordinate cameraPosition = getPosition();
        Coordinate targetPosition = target.getPosition();
        // calculate the angle of the camera relative to the target in the x-y plane
        double deltaX = cameraPosition.getX() - targetPosition.getX();
        double deltaY = cameraPosition.getY() - targetPosition.getY();
        double relativeAngleXY = Math.atan2(deltaY, deltaX);
        double currentDistanceXY = cameraPosition.distanceXY(targetPosition);
        // calculate the angle of the camera relative to the target in the xy-z plane
        double deltaZ = cameraPosition.getZ() - targetPosition.getZ();
        double relativeAngleZ = Math.atan2(deltaZ, currentDistanceXY);
        // calculate the position changes that occur from this zooming movement
        double distance3D = cameraPosition.distance3D(targetPosition);
        double newZHeight = (distance3D - distance) * Math.sin(relativeAngleZ);
        double newXYDistance = (distance3D - distance) * Math.cos(relativeAngleZ);
        // update camera position and view angle
        this.xPosition = targetPosition.getX() + newXYDistance * Math.cos(relativeAngleXY);
        this.yPosition = targetPosition.getY() + newXYDistance * Math.sin(relativeAngleXY);
        this.zPosition = targetPosition.getZ() + newZHeight;
        pointToward(target);
    }
    /** @return `Coordinate` object containing the camera's pitch, yaw, roll angles. */
    public Coordinate getCameraTilt() {
        return new Coordinate(pitch, yaw, roll);
    }
    /**
     * Calculate the distance of a target entity orthogonal to the camera's viewing plane.
     * @param entity target to calculate relative distance to.
     * */
    public double distanceToViewPlane(Entity entity) {
        Coordinate cameraPosition = getPosition();
        Coordinate entityPosition = entity.getPosition();
        double X = entityPosition.getX() - cameraPosition.getX();
        double Y = entityPosition.getY() - cameraPosition.getY();
        double Z = entityPosition.getZ() - cameraPosition.getZ();
        // Theta = (thetaX, thetaY, thetaZ) -> tait-bryan angles
        Coordinate cameraTilt = getCameraTilt();
        double pitch = Math.toRadians(cameraTilt.getX()); // pitch
        double yaw = Math.toRadians(cameraTilt.getY()); // yaw
        // I have no idea if this is going to work
        Matrix inversePitchRotation = new Matrix(new double[][]{
                new double[]{Math.cos(pitch), 0, -Math.sin(pitch)},
                new double[]{0, 1, 0},
                new double[]{Math.sin(pitch), 0, Math.cos(pitch)}
        });
        Matrix inverseYawRotation = new Matrix(new double[][]{
                new double[]{Math.cos(yaw), Math.sin(yaw), 0},
                new double[]{-Math.sin(yaw), Math.cos(yaw), 0},
                new double[]{0, 0, 1}
        });
        Matrix XYZMatrix = new Matrix(new double[][]{new double[]{X}, new double[]{Y}, new double[]{Z}});
        Matrix result = inversePitchRotation.times(inverseYawRotation.times(XYZMatrix));
        // get the distance to the rotated camera's view plane
        return result.get(0, 0);
    }
}

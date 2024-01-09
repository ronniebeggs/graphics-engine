package util;

import world.Entity;

public class Coordinate {
    public double x;
    public double y;
    public double z;

    public Coordinate(double xInitial, double yInitial, double zInitial) {
        this.x = xInitial;
        this.y = yInitial;
        this.z = zInitial;
    }
    public double getX() { return this.x; };
    public double getY() {
        return this.y;
    };
    public double getZ() {
        return this.z;
    };
    public double distance3D(Coordinate other) {
        double deltaXSquared = Math.pow(getX() - other.getX(), 2);
        double deltaYSquared = Math.pow(getY() - other.getY(), 2);
        double deltaZSquared = Math.pow(getZ() - other.getZ(), 2);
        return Math.sqrt(deltaXSquared + deltaYSquared + deltaZSquared);
    }
    public double distanceXZ(Coordinate other) {
        double deltaXSquared = Math.pow(getX() - other.getX(), 2);
        double deltaZSquared = Math.pow(getZ() - other.getZ(), 2);
        return Math.sqrt(deltaXSquared + deltaZSquared);
    }

    /**
     * Transform positions relative to an object to real simulation positions.
     * @param entity target to transform position relative to.
     * @param relativePosition position relative to the target object.
     * @return position transformed to real simulation axes.
     * */
    public static Coordinate fullPositionRotation(Entity entity, Coordinate relativePosition) {
        // calculate positions relative to the object's center
        double x = relativePosition.getX();
        double z = relativePosition.getZ();
        double y = relativePosition.getY();

        Coordinate entityDirection = entity.getDirection();
        double pitchRadians = Math.toRadians(entityDirection.getX()); // pitch
        double yawRadians = Math.toRadians(entityDirection.getY()); // yaw
        double rollRadians = Math.toRadians(entityDirection.getZ()); // roll

        double dX = Math.cos(yawRadians) * (Math.sin(rollRadians) * z + Math.cos(rollRadians) * x) - Math.sin(yawRadians) * y;
        double dY = Math.sin(pitchRadians) * (Math.cos(yawRadians) * y + Math.sin(yawRadians) * (Math.sin(rollRadians) * z + Math.cos(rollRadians) * x)) + Math.cos(pitchRadians) * (Math.cos(rollRadians) * z - Math.sin(rollRadians) * x);
        double dZ = Math.cos(pitchRadians) * (Math.cos(yawRadians) * y + Math.sin(yawRadians) * (Math.sin(rollRadians) * z + Math.cos(rollRadians) * x)) - Math.sin(pitchRadians) * (Math.cos(rollRadians) * z - Math.sin(rollRadians) * x);

        // transform relative positions to real simulation positions
        Coordinate entityPosition = entity.getPosition();
        return new Coordinate(
                dX + entityPosition.getX(),
                dY + entityPosition.getY(),
                dZ + entityPosition.getZ()
        );
    }
    public static Coordinate combinedRotationTransformation(Entity entity, Coordinate relativePosition) {
//        Coordinate rotated = rotateRoll(entity, rotatePitch(entity, rotateYaw(entity, relativePosition)));
        // calculate positions relative to the object's center
        double x = relativePosition.getX();
        double y = relativePosition.getY();
        double z = relativePosition.getZ();

        // yaw transformation
        double yawRadians = -Math.toRadians(entity.getDirection().getY());
        double x1 = (x * Math.cos(yawRadians) + z * Math.sin(yawRadians)) - x;
        double y1 = 0;
        double z1 = (-x * Math.sin(yawRadians) + z * Math.cos(yawRadians)) - z;

        // pitch transformation
        double pitchRadians = Math.toRadians(entity.getDirection().getX());
        double x2 = (x * Math.cos(pitchRadians) - y * Math.sin(pitchRadians)) - x;
        double y2 = (x * Math.sin(pitchRadians) + y * Math.cos(pitchRadians)) - y;
        double z2 = 0;

        double dX = x + x1 + x2;
        double dY = y + y1 + y2;
        double dZ = z + z1 + z2;

//        // roll transformation
//        double rollRadians = Math.toRadians(entity.getDirection().getZ());
//        double dX = x;
//        double dY = -z * Math.sin(rollRadians) + y * Math.cos(rollRadians);
//        double dZ = z * Math.cos(rollRadians) + y * Math.sin(rollRadians);

        Coordinate entityPosition = entity.getPosition();
        return new Coordinate(
                entityPosition.getX() + dX,
                entityPosition.getY() + dY,
                entityPosition.getZ() + dZ
        );
    }

    /**
     * Rotate relative position in the yaw direction.
     * @param entity target to transform position relative to.
     * @param relativePosition position relative to the target object.
     * @return position transformed to real simulation axes.
     * */
    public static Coordinate rotateYaw(Entity entity, Coordinate relativePosition) {
        // calculate positions relative to the object's center
        double x = relativePosition.getX();
        double y = relativePosition.getY();
        double z = relativePosition.getZ();

        // yaw transformation
        double yawRadians = -Math.toRadians(entity.getDirection().getY());
        double dX = x * Math.cos(yawRadians) + z * Math.sin(yawRadians);
        double dY = y;
        double dZ = -x * Math.sin(yawRadians) + z * Math.cos(yawRadians);

        return new Coordinate(dX, dY, dZ);
//        // transform relative positions to real simulation positions
//        Coordinate entityPosition = entity.getPosition();
//        return new Coordinate(
//                dX + entityPosition.getX(),
//                dY + entityPosition.getY(),
//                dZ + entityPosition.getZ()
//        );
    }

    /**
     * Rotate relative position in the pitch direction.
     * @param entity target to transform position relative to.
     * @param relativePosition position relative to the target object.
     * @return position transformed to real simulation axes.
     * */
    public static Coordinate rotatePitch(Entity entity, Coordinate relativePosition) {
        // calculate positions relative to the object's center
        double x = relativePosition.getX();
        double y = relativePosition.getY();
        double z = relativePosition.getZ();

        // pitch transformation
        double pitchRadians = Math.toRadians(entity.getDirection().getX());
        double dX = x * Math.cos(pitchRadians) - y * Math.sin(pitchRadians);
        double dY = x * Math.sin(pitchRadians) + y * Math.cos(pitchRadians);
        double dZ = z;

        return new Coordinate(dX, dY, dZ);
//        // transform relative positions to real simulation positions
//        Coordinate entityPosition = entity.getPosition();
//        return new Coordinate(
//                dX + entityPosition.getX(),
//                dY + entityPosition.getY(),
//                dZ + entityPosition.getZ()
//        );
    }
    /**
     * Rotate relative position in the pitch direction.
     * @param entity target to transform position relative to.
     * @param relativePosition position relative to the target object.
     * @return position transformed to real simulation axes.
     * */
    public static Coordinate rotateRoll(Entity entity, Coordinate relativePosition) {
        // calculate positions relative to the object's center
        double x = relativePosition.getX();
        double y = relativePosition.getY();
        double z = relativePosition.getZ();

        // roll transformation
        double rollRadians = Math.toRadians(entity.getDirection().getZ());
        double dX = x;
        double dY = -z * Math.sin(rollRadians) + y * Math.cos(rollRadians);
        double dZ = z * Math.cos(rollRadians) + y * Math.sin(rollRadians);

        return new Coordinate(dX, dY, dZ);
//        // transform relative positions to real simulation positions
//        Coordinate entityPosition = entity.getPosition();
//        return new Coordinate(
//                dX + entityPosition.getX(),
//                dY + entityPosition.getY(),
//                dZ + entityPosition.getZ()
//        );
    }

    public double[] toArray() {
        return new double[]{x, y, z};
    }
}

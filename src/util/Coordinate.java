package util;

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
    public double distanceTo(Coordinate other) {
        double deltaXSquared = Math.pow(getX() - other.getX(), 2);
        double deltaYSquared = Math.pow(getY() - other.getY(), 2);
        double deltaZSquared = Math.pow(getZ() - other.getZ(), 2);
        return Math.sqrt(deltaXSquared + deltaYSquared + deltaZSquared);
    }
    public double[] coordinateToArray() {
        return new double[]{x, y, z};
    }
}

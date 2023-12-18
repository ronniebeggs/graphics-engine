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
    public double[] coordinateToArray() {
        return new double[]{x, y, z};
    }
}

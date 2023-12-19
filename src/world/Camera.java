package world;

public class Camera extends Entity {
    public double pitch;
    public double yaw;
    public Camera(double x, double y, double z) {
        super(x, y, z);
        this.pitch = 0;
        this.yaw = 0;
    }
    public void moveFrontal(double distance) {
        double deltaFrontal = distance * Math.cos(Math.toRadians(yaw));
        double deltaLateral = distance * Math.sin(Math.toRadians(yaw));
        this.zPosition += deltaFrontal;
        this.xPosition += deltaLateral;
    }
    public void moveLateral(double distance) {
        double deltaFrontal = distance * Math.sin(Math.toRadians(yaw));
        double deltaLateral = distance * Math.cos(Math.toRadians(yaw));
        this.zPosition += deltaFrontal;
        this.xPosition += deltaLateral;
    }
}

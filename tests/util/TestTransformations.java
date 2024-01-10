package util;

import engine.Renderer;
import org.junit.Test;
import world.*;

import static com.google.common.truth.Truth.assertThat;

public class TestTransformations {
    @Test
    public void testYawTransformations() {
        Spacecraft testCraft;
        Coordinate testPoint;
        Coordinate expectedOutput;
        Coordinate yawRotationOutput;

        // basic yaw transformations
        testCraft = new Spacecraft(0, 0, 0, 0, -90, 0, 10, 10, 10);
        testPoint = new Coordinate(10, 0, 10);
        expectedOutput = new Coordinate(10, 0, -10);
        yawRotationOutput = Coordinate.rotateYaw(testCraft, testPoint);
        assertThat(yawRotationOutput.getX()).isEqualTo(expectedOutput.getX());
        assertThat(yawRotationOutput.getY()).isEqualTo(expectedOutput.getY());
        assertThat(yawRotationOutput.getZ()).isEqualTo(expectedOutput.getZ());

        testCraft = new Spacecraft(0, 0, 0, 0, 180, 0, 10, 10, 10);
        testPoint = new Coordinate(-10, 0, 10);
        expectedOutput = new Coordinate(10, 0, -10);
        yawRotationOutput = Coordinate.rotateYaw(testCraft, testPoint);
        assertThat(yawRotationOutput.getX()).isEqualTo(expectedOutput.getX());
        assertThat(yawRotationOutput.getY()).isEqualTo(expectedOutput.getY());
        assertThat(yawRotationOutput.getZ()).isEqualTo(expectedOutput.getZ());
    }

    @Test
    public void testRelativeDistanceCalculations() {
        RenderableEntity entity = new RenderableEntity(100, 0, 0, 0, 0, 0);
        Camera camera = new Camera(0, 0, 0, 0, 0, 0);

        assertThat(Math.round(100 * camera.distanceToViewPlane(entity)) / 100).isEqualTo(100);

        camera.setDirection(0, 60, 0);
        assertThat(Math.round(100 * camera.distanceToViewPlane(entity)) / 100).isEqualTo(50);

        camera.setDirection(0, 90, 0);
        assertThat(Math.round(100 * camera.distanceToViewPlane(entity)) / 100).isEqualTo(0);
    }

    @Test
    public void testNormalVectorCalculations() {
        Coordinate v1 = new Coordinate(1, 0, 0);
        Coordinate v2 = new Coordinate(0, 1, 0);
        Coordinate crossProduct = Coordinate.crossProduct(v1, v2);
    }
}

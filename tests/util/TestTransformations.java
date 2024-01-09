package util;

import org.junit.Test;
import world.Spacecraft;

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
}

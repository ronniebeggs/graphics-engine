package util;

import Jama.Matrix;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class TestProjections {
    @Test
    public void testRotationMatricesEqualsWikipediaComponentMath() {
        double X = 100;
        double Y = 100;
        double Z = 0;

        double thetaX = Math.toRadians(0); // pitch
        double thetaY = Math.toRadians(0); // yaw
        double thetaZ = Math.toRadians(0); // roll

        Matrix thetaXRotation = new Matrix(new double[][]{
                new double[]{1, 0, 0},
                new double[]{0, Math.cos(thetaX), Math.sin(thetaX)},
                new double[]{0, -Math.sin(thetaX), Math.cos(thetaX)}
        });
        Matrix thetaYRotation = new Matrix(new double[][]{
                new double[]{Math.cos(thetaY), 0, -Math.sin(thetaY)},
                new double[]{0, 1, 0},
                new double[]{Math.sin(thetaY), 0, Math.cos(thetaY)}
        });
        Matrix thetaZRotation = new Matrix(new double[][]{
                new double[]{Math.cos(thetaZ), Math.sin(thetaZ), 0},
                new double[]{-Math.sin(thetaZ), Math.cos(thetaZ), 0},
                new double[]{0, 0, 1}
        });
        Matrix XYZMatrix =  new Matrix(new double[][]{
                new double[]{X},
                new double[]{Y},
                new double[]{Z}
        });
        Matrix result = thetaXRotation.times(thetaYRotation.times(thetaZRotation.times(XYZMatrix)));

        double dX = Math.cos(thetaY) * (Math.sin(thetaZ) * Y + Math.cos(thetaZ) * X) - Math.sin(thetaY) * Z;
        double dY = Math.sin(thetaX) * (Math.cos(thetaY) * Z + Math.sin(thetaY) * (Math.sin(thetaZ) * Y + Math.cos(thetaZ) * X)) + Math.cos(thetaX) * (Math.cos(thetaZ) * Y - Math.sin(thetaZ) * X);
        double dZ = Math.cos(thetaX) * (Math.cos(thetaY) * Z + Math.sin(thetaY) * (Math.sin(thetaZ) * Y + Math.cos(thetaZ) * X)) - Math.sin(thetaX) * (Math.cos(thetaZ) * Y - Math.sin(thetaZ) * X);

        assertThat(result.get(0, 0)).isEqualTo(dX);
        assertThat(result.get(1, 0)).isEqualTo(dY);
        assertThat(result.get(2, 0)).isEqualTo(dZ);
    }

    @Test
    public void testProjectionCalculationBehavior() {
        double X = 100;
        double Y = 100;
        double Z = 100;

        double yaw = Math.toRadians(45);
        double pitch = Math.toRadians(0);

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

        assertThat(Math.round(result.get(0, 0))).isEqualTo(Math.round((1 / (Math.pow(2, 0.5) / 2)) * 100));
        assertThat(Math.round(result.get(1, 0))).isEqualTo(Math.round(0));
        assertThat(Math.round(result.get(2, 0))).isEqualTo(Math.round(100));
    }
}

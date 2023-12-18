package util;
import java.awt.Color;
import java.util.List;

public class Mesh {
    public Color color;
    public List<Coordinate> vertices;

    public Mesh(Color color, List<Coordinate> vertices) {
        this.color = color;
        this.vertices = vertices;
    }

}

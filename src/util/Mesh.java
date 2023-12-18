package util;
import java.awt.Color;
import java.util.List;

public class Mesh {
    private Color color;
    private Coordinate[] vertices;
    private int numVertices;

    public Mesh(Coordinate[] vertices, Color color) {
        this.color = color;
        this.vertices = vertices;
        this.numVertices = vertices.length;
    }

    public Coordinate[] getVertices() {
        return vertices;
    }

    public Color getColor() {
        return color;
    }

    public int getNumVertices() {
        return numVertices;
    }

}

package julienl.androidgl;

/**
 * Created by JulienL on 9/30/2015.
 */
public class Vector {

    private double mx;
    private double my;

    public Vector(double x, double y) {
        mx = x;
        my = y;
    }

    public double x() {
        return mx;
    }

    public double y() {
        return my;
    }

    public double length() {
        return Math.sqrt(mx * mx + my * my);
    }

    public Vector add(Vector other) {
        return (new Vector(mx + other.x(), my + other.y()));
    }

    public Vector scale(double ratio) {
        return (new Vector(mx * ratio, my * ratio));
    }

}

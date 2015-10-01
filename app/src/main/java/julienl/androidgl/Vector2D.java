package julienl.androidgl;

/**
 * Created by JulienL on 9/30/2015.
 */
public class Vector2D {

    private double mx;
    private double my;

    public Vector2D(double x, double y) {
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

    public Vector2D add(Vector2D other) {
        return (new Vector2D(mx + other.x(), my + other.y()));
    }

    public Vector2D scale(double ratio) {
        return (new Vector2D(mx * ratio, my * ratio));
    }

}

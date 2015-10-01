package julienl.androidgl;

/**
 * Created by JulienL on 9/30/2015.
 */
public class Point2D {

    private double mx;
    private double my;

    public Point2D(double x, double y) {
        mx = x;
        my = y;
    }

    public double x() {return mx;}
    public double y() {return my;}

    public Point2D add(Vector v) {
        return new Point2D(mx + v.x(), my + v.y());
    }
}

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

    public Point2D add(Vector2D v) {
        return new Point2D(mx + v.x(), my + v.y());
    }

    public static double distance2(Point2D p1, Point2D p2) {
        double x = p1.x() - p2.x();
        double y = p1.y() - p2.y();
        return (x * x + y * y);
    }

    public static double distance(Point2D p1, Point2D p2) {
        return Math.sqrt(Point2D.distance(p1,p2));
    }
}

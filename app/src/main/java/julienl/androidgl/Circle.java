package julienl.androidgl;

import android.util.Log;

/**
 * A two-dimensional circle for use as a drawn object in OpenGL ES 2.0.
 */
public class Circle extends Shape {

    private Point2D mcenter;
    private double mradius;

    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
     */
    public Circle(Point2D center, double radius) {
        mcenter = center;
        mradius = radius;
    }

    public Point2D center() {
        return mcenter;
    }

    public double radius() {
        return mradius;
    }

    public Point2D point(double angleratio) {
        return mcenter.add(new Vector2D(Math.cos(angleratio * (2.0 * Math.PI)),Math.sin(angleratio * (2.0 * Math.PI))).scale(mradius));
    }

    public Polygon polygon(int npoints) {
        Point2D[] point2Ds = new Point2D[npoints];

        short index = 0;
        for (double abscissa : (new Range(0.0,1.0)).samples(npoints)) {
            point2Ds[index] = point(abscissa);
            index += 1;
        }

        return new Polygon(point2Ds);
    }

    public BBox bbox() {
        return BBox.New(center(),radius());
    }

    public static Boolean intersect(Circle c1, Circle c2) {
        double sumrad = (c1.radius() + c2.radius());
        Boolean result = (Point2D.distance2(c1.center(),c2.center()) < (sumrad *sumrad));
       //  Log.v("Test log", "intersect circle result" + result);
        return result;
    }
}
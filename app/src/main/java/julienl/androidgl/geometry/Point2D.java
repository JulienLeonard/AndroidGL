package julienl.androidgl.geometry;

import julienl.androidgl.Vector2D;

/**
 * Created by JulienL on 9/30/2015.
 */
public class Point2D {

    protected double mx;
    protected double my;

    public Point2D() {
        mx = 0.0;
        my = 0.0;
    }

    public Point2D(double x, double y) {
        mx = x;
        my = y;
    }

	static public Point2D New(double x, double y) {
		return new Point2D(x,y);
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

	public static BBox bbox(Point2D p1, Point2D p2) {
		double minx = Math.min(p1.x(),p2.x());
		double maxx = Math.max(p1.x(),p2.x());
		double miny = Math.min(p1.y(),p2.y());
		double maxy = Math.max(p1.y(),p2.y());
		return new BBox(minx,miny,maxx,maxy);
	}
}

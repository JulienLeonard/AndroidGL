package julienl.androidgl.geometry;

import julienl.androidgl.utils.Range;

/**
 * A two-dimensional circle for use as a drawn object in OpenGL ES 2.0.
 */
public class Circle extends Shape {

    protected Point2D mcenter;
    protected double mradius;

    public Circle() {
        mcenter = new Point2D();
        mradius = 1.0;
    }

    public Circle(Point2D center, double radius) {
        mcenter = center;
        mradius = radius;
    }

	public Circle(double x, double y, double radius) {	
		mcenter = new Point2D(x,y);
		mradius = radius;
	}

	static public Circle New(Point2D center, double radius) {
		return new Circle(center,radius);
	}

    public Point2D center() {
        return mcenter;
    }

    public double radius() {
        return mradius;
    }

    public double r() {
		return mradius;
    }

    public double x() {
        return mcenter.x();
    }

    public double y() {
        return mcenter.y();
    }

    public Point2D point(double angleratio) {
        return mcenter.add(new Vector2D(Math.cos(angleratio * (2.0 * Math.PI)),Math.sin(angleratio * (2.0 * Math.PI))).scale(mradius));
    }

	public Circle scale(double ratio) {
		return Circle.New(center(),r() * ratio);
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

	public static Circle circles2tangentout(Circle c1, Circle c2, double radius, double iside) {
		return Circle.circles2tangent(c1,true,c2,true,radius,iside);
	}

	public static Circle circles2tangent(Circle c1, Boolean isout1, Circle c2, Boolean isout2, double radius, double side) {
        if (c1 == null || c2 == null){
            return null;
        }

		double x1 = c1.x();
		double y1 = c1.y();
		double r1 = c1.r();		
		double x2 = c2.x();
		double y2 = c2.y();
		double r2 = c2.r();
		Vector2D s3 = Vector2D.New(c2.center(),c1.center());
		double l3 = s3.length();
		double isens = 1.0;
		if (!isout1 || !isout2) {
			isens = -1.0;
		}
		double l1 = r1 + isens * radius;
		double l2 = r2 + radius;
		double denom = (2.0 * l2 * l3);
		if (denom == 0.0) {
			return null;
		}
		double cosv = (l3 * l3 - l1 * l1 + l2 * l2) / denom;
		if (cosv < -1.0 || cosv > 1.0) {
			return null;
		}
		double angle = Math.acos(cosv) * side;
		Vector2D vnew = s3.rotate(angle).normalize().scale(l2);
		Point2D newcenter = c2.center().add(vnew);
		return Circle.New(newcenter,radius);
	}

	
}
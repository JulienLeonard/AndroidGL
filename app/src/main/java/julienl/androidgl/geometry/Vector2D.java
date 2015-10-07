package julienl.androidgl.geometry;

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

    public Vector2D(Point2D p1, Point2D p2) {
        mx = p2.x() - p1.x();
        my = p2.y() - p1.y();
    }

	public static Vector2D New(double x, double y) {
		return new Vector2D(x,y);
	}

	public static Vector2D New(Point2D p1, Point2D p2) {
		return new Vector2D(p1,p2);
	}

    public double x() {
        return mx;
    }

    public double y() {
        return my;
    }

	public double length2() {
		return mx * mx + my * my;
	}

    public double length() {
        return Math.sqrt(length2());
    }

	public static Vector2D V0  = Vector2D.New(0.0,0.0);
	public static Vector2D VX0 = Vector2D.New(1.0,0.0);
	public static Vector2D VY0 = Vector2D.New(0.0,1.0);

	public Vector2D normalize() {
		double l = length();
		if (l == 0.0) {
			return Vector2D.V0;
		} else {
			return scale(1.0/l);
		}
	}

    public Vector2D add(Vector2D other) {
        return (new Vector2D(mx + other.x(), my + other.y()));
    }

    public Vector2D scale(double ratio) {
        return (new Vector2D(mx * ratio, my * ratio));
    }

    public Vector2D scalex(double ratio) {
        return (new Vector2D(mx * ratio, my ));
    }
	
    public Vector2D scaley(double ratio) {
        return (new Vector2D(mx, my * ratio));
    }

	public Vector2D ortho() {
		return Vector2D.New(-my,mx);
	}

	public Vector2D rotate(double angle) {
		double cosa = Math.cos(angle);
		double sina = Math.sin(angle);
		double x = x();
		double y = y();
		return Vector2D.New(x * cosa - y * sina, x * sina + y * cosa );
	}

	public static double cross(Vector2D v1,Vector2D v2) {
		return (v1.x() * v2.y() - v1.y() * v2.x());
	}

}

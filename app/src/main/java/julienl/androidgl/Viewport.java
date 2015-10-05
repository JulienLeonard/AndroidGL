package julienl.androidgl;

/**
 * Created by JulienL on 10/5/2015.
 */
public class Viewport {

	private Point2D mcenter;
	private double mradius;

	public Viewport(Point2D center, double radius) {
		mcenter = center;
		mradius = radius;
	}

	public Point2D center() {
		return mcenter;
	}

	public double radius() {
		return mradius;
	}

	public Circle circle() {
		return new Circle(center(),radius());
	}

	public double size() {
		return radius() * 2.0;
	}

	public double xmin() {
		return center().x() - radius();
	}

	public double ymin() {
		return center().y() - radius();
	}
}

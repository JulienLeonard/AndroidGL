package julienl.androidgl.geometry;

import julienl.androidgl.utils.Range;

/**
 * Created by JulienL on 10/7/2015.
 */
public class Intersection {

    protected Point2D mpoint;
    protected Segment msegment;

    public Intersection() {
        mpoint   = null;
        msegment = null;
    }

    public Intersection(Point2D point) {
        mpoint   = point;
        msegment = null;
    }

    public Intersection(Segment segment) {
        mpoint   = null;
        msegment = segment;
    }

    public static Intersection intersection(Segment seg1, Segment seg2) {
        return Intersection.intersection(seg1.p1(), seg1.p2(), seg2.p1(), seg2.p2());
    }

	public static Point2D min(Point2D p1, Point2D p2) {
		if (p1.x() == p2.x()) {
			return p1.y() <= p2.y() ? p1 : p2;
		}
		return p1.x() < p2.x() ? p1 : p2;
	}

	public static Point2D max(Point2D p1, Point2D p2) {
		if (p1.x() == p2.x()) {
			return p1.y() >= p2.y() ? p1 : p2;
		}
		return p1.x() > p2.x() ? p1 : p2;
	}

    public static Intersection intersection(Point2D p1, Point2D p2, Point2D p3, Point2D p4) {
        Intersection result = null;
        
		double x1 = p1.x();
		double y1 = p1.y();
		double x2 = p2.x();
		double y2 = p2.y();
		double x3 = p3.x();
		double y3 = p3.y();
		double x4 = p4.x();
		double y4 = p4.y();

		double denom = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
		double uanum = (x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3);
		double ubnum = (x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3);

		if (denom == 0.0 && uanum == 0.0 && ubnum == 0.0) {
			Point2D pp1 = Intersection.min(p1,p2);
			Point2D pp2 = Intersection.max(p1,p2);
			Point2D pp3 = Intersection.min(p3,p4);
			Point2D pp4 = Intersection.max(p3,p4);
			x1 = pp1.x();
			y1 = pp1.y();
			x2 = pp2.x();
			x2 = pp2.y();
			x3 = pp3.x();
			y3 = pp3.y();
			x4 = pp4.x();
			y4 = pp4.y();

			if (x1 == x2 && x2 == x3 && x3 == x4) {
				if (Range.New(y1, y2).contain(y3)) {
					if (Range.New(y1,y2).contain(y4)) {
						result = new Intersection(Segment.New(Point2D.New(x3,y3),Point2D.New(x4,y4)));
					} else {
						result = new Intersection(Segment.New(Point2D.New(x3,y3),Point2D.New(x2,y2)));
					}
				} else {
					if (Range.New(y1,y2).contain(y4)) {
						result = new Intersection(Segment.New(Point2D.New(x1,y1),Point2D.New(x4,y4)));
					} else {
						if (Range.New(y3,y4).contain(y1) && Range.New(y3,y4).contain(y2)) {
							result =  new Intersection(Segment.New(Point2D.New(x1,y1),Point2D.New(x2,y2)));
						} else {
							result = null;
						}
					}
				}
			} else {
				if (Range.New(x1,x2).contain(x3)) {
					if (Range.New(x1,x2).contain(x4)) {
						result = new Intersection(Segment.New(Point2D.New(x3,y3),Point2D.New(x4,y4)));
					} else {
						result = new Intersection(Segment.New(Point2D.New(x3,y3),Point2D.New(x2,y2)));
					}
				} else {
					if (Range.New(x1,x2).contain(x4)) {
						result = new Intersection(Segment.New(Point2D.New(x1,y1),Point2D.New(x4,y4)));
					} else {
						if (Range.New(x3,x4).contain(x1) && Range.New(x3,x4).contain(x2)) {
							result =  new Intersection(Segment.New(Point2D.New(x1,y1),Point2D.New(x2,y2)));
						} else {
							result = null;
						}
					}
				}
			}
			
			return result;
		} else if (denom == 0.0) {
			return null;
		} else {
			double ua = uanum / denom;
			double ub = ubnum / denom;
			if (ua >= 0 && ua <= 1 && ub >= 0 && ub <= 1) {
				double x = x1 + ua * (x2 - x1);
				double y = y1 + ua * (y2 - y1);
				return new Intersection(Point2D.New(x, y));
			}
		}
		return null;
    }
}
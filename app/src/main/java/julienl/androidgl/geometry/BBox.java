package julienl.androidgl.geometry;

import julienl.androidgl.drawing.Viewport;

/**
 * Created by JulienL on 10/5/2015.
 */
public class BBox {

	private double mxmin;
	private double mymin;
	private double mxmax;
	private double mymax;

	public BBox(double xmin, double ymin, double xmax, double ymax) {
		mxmin = xmin;
		mymin = ymin;
		mxmax = xmax;
		mymax = ymax;
	}

	public static BBox New(Point2D ccenter, double csize) {
		return (new BBox(ccenter.x() - csize/2.0, ccenter.y() - csize/2.0, ccenter.x() + csize/2.0, ccenter.y() + csize/2.0));
	}

	public double[] coords() {
		double[] result = new double[4];
		result[0] = mxmin;
		result[1] = mymin;
		result[2] = mxmax;
		result[3] = mymax;
		return result;
	}

	public Point2D center() {
		return new Point2D((mxmin + mxmax)/2.0, (mymin + mymax)/2.0);
	}

	public double xsize() {
		return (mxmax - mxmin);
	}

	public double ysize() {
		return (mymax - mymin);
	}

	public double width() {
		return xsize();
	}

	public double height() {
		return ysize();
	}

	public double size() {
		return Math.max(xsize(),ysize());
	}
    
	public double radius() {
		double rx = xsize() / 2.0;
		double ry = ysize() / 2.0;
		return Math.sqrt(rx * rx + ry * ry);
	}

	public Viewport viewport(double ratio) {
		return new Viewport(center(),radius() * ratio);
	}

	public BBox resize(double factor) {
		double newwidth  = xsize() * factor;
		double newheight = ysize() * factor;
		double xcenter = center().x();
		double ycenter = center().y();
		double newxmin = xcenter - newwidth  / 2.0;
		double newymin = ycenter - newheight / 2.0;
		double newxmax = xcenter + newwidth  / 2.0;
		double newymax = ycenter + newheight / 2.0;
		return new BBox(newxmin,newymin,newxmax,newymax);
	}

	public Boolean contain(Point2D point) {
		double x = point.x();
		double y = point.y();
		if (mxmin <= x && x <= mxmax && mymin <= y && y <= mymax) {
			return true;
		}
		return false;
	}

	public Boolean containBBox(BBox obbox) {
		double[] coords1 = coords();
		double[] coords2 = obbox.coords();
		if (coords1[0] < coords2[0] && coords2[2] < coords1[2] && coords1[1] < coords2[1] && coords2[3] < coords1[3]) {
			return true;
		}
		return false;
	}

	public Point2D[] corners() {
		Point2D[] result = new Point2D[4];
		result[0] = new Point2D(mxmin,mymin);
		result[1] = new Point2D(mxmax,mymin);
		result[2] = new Point2D(mxmax,mymax);
		result[3] = new Point2D(mxmin,mymax);
		return result;
	}

	public Point2D[] squarePoints() {
		Point2D[] result = new Point2D[4];
		double size2 = size()/2.0;
		double centerx = center().x();
		double centery = center().y();
		result[0] = new Point2D(centerx - size2,centery - size2);
		result[1] = new Point2D(centerx - size2,centery + size2);
		result[2] = new Point2D(centerx + size2,centery + size2);
		result[3] = new Point2D(centerx + size2,centery - size2);
		return result;
	}

	public Boolean intersect(BBox obbox) {
		double[] coords1 = coords();
		double[] coords2 = obbox.coords();
		if (coords2[0] < coords1[2] && coords2[2] >= coords1[0] && coords2[1] < coords1[3] && coords2[3] >= coords1[1]) {
			return true;
		}
		return false;		
	}
    
	public BBox[] split4() {
		BBox[] result = new BBox[4];
		double[] ccoords = coords();
		
		double xmin = ccoords[0];
		double ymin = ccoords[1];
		double xmax = ccoords[2];
		double ymax = ccoords[3];

		double middlex = (xmin + xmax) /2.0;
		double middley = (ymin + ymax) /2.0;

		result[0] = new BBox(xmin,ymin,middlex,middley);
		result[1] = new BBox(xmin,middley,middlex,ymax);
		result[2] = new BBox(middlex,ymin,xmax,middley);
		result[3] = new BBox(middlex,middley,xmax,ymax);

		return result;
	}

	public BBox symx(double vsymx) {
		double newxmin = 2.0 * vsymx - mxmin;
		double newxmax = 2.0 * vsymx - mxmax;
		double xmin = Math.min(newxmin,newxmax);
		double xmax = Math.max(newxmin,newxmax);
		return new BBox(xmin,mymin,xmax,mymax);
	}

	public double xmin() {
		return mxmin;
	}

	public double ymin() {
		return mymin;
	}

	public double xmax() {
		return mxmax;
	}

	public double ymax() {
		return mymax;
	}

	public static BBox bbunion(BBox bbox1, BBox bbox2) {
		double xmin = Math.min(bbox1.xmin(),bbox2.xmin());
		double xmax = Math.max(bbox1.xmax(),bbox2.xmax());
		double ymin = Math.min(bbox1.ymin(),bbox2.ymin());
		double ymax = Math.max(bbox1.ymax(),bbox2.ymax());
		return new BBox(xmin,ymin,xmax,ymax);
	}

	public static BBox bbunions(BBox[] bboxlist) {
		if (bboxlist.length < 0) {
			return null;
		}
		BBox result = bboxlist[0];
		for (int i = 1; i < bboxlist.length; i++) {
			result = BBox.bbunion(result,bboxlist[i]);
		}
		return result;
	}

	public String toString() {
		return "" + xmin() + "," + ymin() + "," + xmax() + "," + ymax();
	}
}

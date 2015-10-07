package julienl.androidgl.geometry;

import android.util.Pair;

import java.util.ArrayList;

import julienl.androidgl.utils.Range;
import julienl.androidgl.utils.Utils;

/**
 * Created by JulienL on 10/7/2015.
 */
public class Segment {

    protected Point2D mp1;
	protected Point2D mp2;
	protected Range mrx;
	protected Range   mry;

	public Segment(Point2D p1, Point2D p2) {
		mp1 = p1;
		mp2 = p2;
		mrx = new Range(mp1.x(),mp2.x());
		mry = new Range(mp1.y(),mp2.y());
	}

	public static Segment New(Point2D p1, Point2D p2) {
		return new Segment(p1,p2);
	}

	public Point2D p1() {
		return mp1;
	}

	public Point2D p2() {
		return mp2;
	}

	public Boolean isPoint() {
		return Point2D.distance(mp1,mp2) == 0.0;
	}

	public Point2D middle() {
		return sample(0.5);
	}

	public Point2D sample(double t_) {
		double t = new Range(0.0,1.0).trim(t_);
		return new Point2D(mrx.sample(t),mry.sample(t));
	}

	public ArrayList<Point2D> samples(int npoints) {
		ArrayList<Point2D> result = new ArrayList<Point2D>();
		for (double t: Range.usamples(npoints)) {
			result.add(sample(t));
		}
		return result;
	}

	public ArrayList<Point2D> points() {
		ArrayList<Point2D> result = new ArrayList<Point2D>();
		result.add(mp1);
		result.add(mp2);
		return result;
	}

	public Point2D point(double t) {
		return sample(t);
	}

	public ArrayList<Double> coords() {
		ArrayList<Double> result = new ArrayList<Double>();
		result.add(mp1.x());
		result.add(mp1.y());
		result.add(mp2.x());
		result.add(mp2.y());
		return result;
	}

	public Vector2D vector() {
		return new Vector2D(mp1,mp2);
	}

	public Vector2D normal() {
		return vector().ortho().normalize();
	}

	public double length() {
		return vector().length();
	}

	public double length2() {
		return vector().length2();
	}

	public ArrayList<Segment> split(int ntimes) {
		ArrayList<Segment> result = new ArrayList<Segment>();

		for (Pair<Point2D,Point2D> pair: Utils.pairs(samples(ntimes + 1))) {
			result.add(new Segment(pair.first,pair.second));
		}

		return result;
	} 

	public ArrayList<Segment> splitmaxsize(double maxsize) {
		ArrayList<Segment> result = null;
		if (length() <= maxsize) {
			result = new ArrayList<Segment>();
			result.add(this);
		} else {
			result = split((int)Math.ceil(length()/maxsize));
		}
		return result;
	}

	public Segment subsegment(double t1, double t2) {
		return new Segment(sample(t1),sample(t2));
	}

	public BBox bbox() {
		return Point2D.bbox(mp1, mp2);
	}

	public static Boolean intersect(Segment seg1, Segment seg2) {
		return Segment.intersect(seg1, seg2, false);
	} 

	public static Boolean intersect(Segment seg1, Segment seg2, Boolean strict) {
		return (!(Intersection.intersection(seg1, seg2) == null));
	} 

	public static ArrayList<Segment> sort(ArrayList<Segment> segments) {
		// TODO
		// Collections.sort(segments, new Comparator<Segment>() {
        //     @Override
        //     public int compare(Segment seg1, Segment seg2) {

        //         return seg1.xcoords().compareTo(seg2.xcoords());
        //     }
        // });
		// return segments;
		return null;
	}

}

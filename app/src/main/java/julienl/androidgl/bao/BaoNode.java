package julienl.androidgl.bao;

import java.util.ArrayList;

import julienl.androidgl.geometry.Circle;
import julienl.androidgl.geometry.Point2D;
import julienl.androidgl.geometry.Segment;

/**
 * Created by JulienL on 10/7/2015.
 */
public class BaoNode extends Circle {

	protected BaoCirclePacking mpacking;
	protected Boolean mretouch;
	protected Boolean mnotfound;
	protected int mcolorindex;
	protected int mindex;

	public BaoNode(BaoCirclePacking packing, Circle circle, int colorindex, int index) {
		mpacking    = packing;
		mcenter     = circle.center();
		mradius     = circle.radius();
		mretouch    = false;
		mnotfound   = false;
		mcolorindex = colorindex;
		mindex      = index;
	}

	public int colorindex() {
		return mcolorindex;
	}

	public int index() {
		return mindex;
	}

	public BaoCirclePacking packing() {
		return mpacking;
	}

	public void packing(BaoCirclePacking packing) {
		mpacking = packing;
	}

	public Boolean retouch() {
		return mretouch;
	}

	public void retouch(Boolean retouch) {
		mretouch = retouch;
	}
	
	public Boolean notfound() {
		return mnotfound;
	}

	public void notfound(Boolean notfound) {
		mnotfound = notfound;
	}

	public static ArrayList<BaoNode> nodes(ArrayList<BaoNode> nodes) {
		return BaoNode.nodes(nodes,-1);
	}

	public static ArrayList<BaoNode> nodes(ArrayList<BaoNode> nodes, int startindex) {
		int cindex = startindex;
		if (startindex != -1) {
			cindex += 1;
		}

		ArrayList<BaoNode> result = new ArrayList<BaoNode>();
		for (BaoNode node: nodes) {
			int newindex = startindex == -1 ? -1 : cindex;
			result.add(new BaoNode(null,node,cindex,newindex));
			cindex += 1;
		}
		return result;
	}

	public static ArrayList<BaoNode> fromcircle(Circle circle) {
		return BaoNode.fromcircle(circle,null);
	}

	public static ArrayList<BaoNode> fromcircle(Circle circle, BaoCirclePacking packing) {
		ArrayList<BaoNode> result = new ArrayList<BaoNode>();

		double x = circle.x();
		double y = circle.y();
		double r = circle.r();
		
		result.add(new BaoNode(packing,new Circle(x-r/2.0,y,r/2.0),0,0));
		result.add(new BaoNode(packing,new Circle(x+r/2.0,y,r/2.0),1,1));

		return result;
	}

	public static ArrayList<BaoNode> fromsegment(Segment segment) {
		return BaoNode.fromsegment(segment,null,-1.0);
	}

	public static ArrayList<BaoNode> fromsegment(Segment segment, BaoCirclePacking packing) {
		return BaoNode.fromsegment(segment,packing,-1.0);
	}

	public static ArrayList<BaoNode> fromsegment(Segment segment, BaoCirclePacking packing, double radius_) {
		double radius = radius_ == -1.0 ? segment.length()/2.0 : radius_;
		double offset = radius / segment.length();
		Point2D center1 = segment.sample(0.5 - offset).add(segment.normal().scale(-radius));
		Point2D center2 = segment.sample(0.5 + offset).add(segment.normal().scale(-radius));
		
		ArrayList<BaoNode> result = new ArrayList<BaoNode>();
		result.add(new BaoNode(packing,new Circle(center1,radius),0,0));
		result.add(new BaoNode(packing,new Circle(center2,radius),0,0));
		return result;
	}

	public static ArrayList<BaoNode> baonodes0() {
		return BaoNode.baonodes0(null);
	}

	public static ArrayList<BaoNode> baonodes0(BaoCirclePacking packing) {
		ArrayList<BaoNode> result = new ArrayList<BaoNode>();
		result.add(new BaoNode(packing,new Circle(0.0,0.0,1.0),0,0));
		result.add(new BaoNode(packing,new Circle(2.0,0.0,1.0),0,0));
		return result;
	}

}

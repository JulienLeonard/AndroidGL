package julienl.androidgl;

import java.util.ArrayList;

/**
 * Created by JulienL on 10/5/2015.
 */
public class QuadTree {
	
	private BBox mBBox0;
	private Quad mRootQuad;
	private int  mPush;

	public QuadTree() {
		mBBox0    = new BBox(-1000.0,-1000.0,1000.0,1000.0);
		mRootQuad = new Quad(mBBox0);
		mPush = 0;
	}

	public void push() {
		mPush += 1;
	}

	public void pop() {
		mRootQuad.pop(mPush);
		mPush += -1;
	}

	public void add(Shape shape) {
		mRootQuad.add(shape,mPush);
	}

	public void adds(Shape[] shapes) {
		for (Shape shape: shapes) {
			add(shape);
		}
	}

	public Boolean isColliding(Shape newshape) {
		ArrayList<Shape> shapes = mRootQuad.mayintersect( newshape );
		for (Shape shape: shapes) {
			if (shape.intersect(newshape)) {
				return true;
			}
		}
		return false;
	}

	public ArrayList<Shape> collidings(Shape newshape) {
		ArrayList<Shape> shapes = mRootQuad.mayintersect( newshape );
		ArrayList<Shape> result = new ArrayList<Shape>();
		for (Shape shape: shapes) {
			if (shape.intersect(newshape)) {
				result.add(shape);
			}
		}
		return result;
	}

	public Shape[] shapes() {
		return mRootQuad.shapes();
	}

}

package julienl.androidgl.quadtree;

import java.util.ArrayList;

import julienl.androidgl.geometry.BBox;
import julienl.androidgl.geometry.Shape;
import julienl.androidgl.quadtree.Quad;

/**
 * Created by JulienL on 10/5/2015.
 */
public class QuadTree {
	
	private BBox mBBox0;
	private Quad mRootQuad;
	private int  mPush;

	public QuadTree() {
		mBBox0    = new BBox(-10000.0,-10000.0,10000.0,10000.0);
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

	public void adds(ArrayList<Shape> shapes) {
		for (Shape shape: shapes) {
			add(shape);
		}
	}


	public Boolean isColliding(Shape newshape) {
		ArrayList<Shape> shapes = mRootQuad.mayintersect( newshape );
		for (Shape shape: shapes) {
			if (Shape.intersect(shape,newshape)) {
				return true;
			}
		}
		return false;
	}

	public ArrayList<Shape> collidings(Shape newshape) {
		ArrayList<Shape> shapes = mRootQuad.mayintersect( newshape );
		ArrayList<Shape> result = new ArrayList<Shape>();
		for (Shape shape: shapes) {
			if (Shape.intersect(shape,newshape)) {
				result.add(shape);
			}
		}
		return result;
	}

	public ArrayList<Shape> shapes() {
		return mRootQuad.shapes();
	}

}

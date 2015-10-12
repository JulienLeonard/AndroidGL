package julienl.androidgl.surfaces;

import android.content.Context;

import java.util.Random;

import julienl.androidgl.drawing.Color;
import julienl.androidgl.geometry.Circle;
import julienl.androidgl.geometry.Point2D;
import julienl.androidgl.quadtree.QuadTree;
import julienl.androidgl.utils.Range;

/**
 * Created by JulienL on 10/8/2015.
 */
public class GLSurfaceQuadTree extends GLSurfaceSimpleCircle {

    protected QuadTree mquadtree;

    public GLSurfaceQuadTree(Context context) {
        super(context);
        mquadtree = new QuadTree();
    }

    protected void createcircle(float x, float y) {
        Circle newcircle = new Circle(new Point2D(x + Range.New(-10.0, 10.0).rand(mRandgen), y + Range.New(-10.0, 10.0).rand(mRandgen)), 20.0);
        if (!mquadtree.isColliding(newcircle)) {
            mquadtree.add(newcircle);
            Color color = Color.rand(mRandgen, 1.0);
            this.draw(newcircle, color);
            requestRender();
        }
    }
}

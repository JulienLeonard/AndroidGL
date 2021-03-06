package julienl.androidgl.geometry;

import julienl.androidgl.geometry.Point2D;

/**
 * Created by JulienL on 9/30/2015.
 */
public class Polygon {

    Point2D[] mPoint2Ds;

    public Polygon() {
    }

    public Polygon(Point2D[] point2Ds) {
        mPoint2Ds = point2Ds;
    }

    public Polygon(float[] coords) {
        mPoint2Ds = new Point2D[coords.length/2];
        for (int index = 0; index < coords.length/2; index += 1) {
            mPoint2Ds[index] = new Point2D(coords[2*index],coords[2*index+1]);
        }
    }

    public Point2D[] points() {
        return mPoint2Ds;
    }

    public float[] coords() {
        float[] result = new float[mPoint2Ds.length * 2];
        int index = 0;
        for (Point2D p : mPoint2Ds) {
            result[2 * index] = (float)p.x();
            result[2 * index + 1] = (float)p.y();
            index += 1;
        }
        return result;
    }

    public Point2D middle() {
        float sumx = 0.0f;
        float sumy = 0.0f;
        for (Point2D p : mPoint2Ds) {
            sumx += p.x();
            sumy += p.y();
        }
        float fnpoints = (float) mPoint2Ds.length;

        return new Point2D(sumx/fnpoints,sumy/fnpoints);
    }

    public int GLnpoints() {return (mPoint2Ds.length + 1);}

    public Point2D[] GLpoints() {
        Point2D[] result = new Point2D[(mPoint2Ds.length + 1)];
        result[0] = middle();
        for (int i = 0; i < mPoint2Ds.length; i++) {
            result[i+1] = mPoint2Ds[i];
        }
        return result;
    }

    public int GLOrderSize() {
        return mPoint2Ds.length * 3;
    }

    //
    // basic algo : works only for convex polys
    //
    public short[] GLOrder() {
        short[] result = new short[GLOrderSize()];

        for (short pindex = 1; pindex < mPoint2Ds.length; pindex++) {
            result[pindex * 3]     = 0; // middle point
            result[pindex * 3 + 1] = pindex;
            result[pindex * 3 + 2] = (short) (pindex + 1);
        }

        return result;
    }
}

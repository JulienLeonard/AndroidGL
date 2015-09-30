package julienl.androidgl;

/**
 * Created by JulienL on 9/30/2015.
 */
public class Polygon {

    Point[] mPoints;

    public Polygon() {
    }

    public Polygon(Point[] points) {
        mPoints = points;
    }

    public Polygon(float[] coords) {
        mPoints = new Point[coords.length/2];
        for (int index = 0; index < coords.length/2; index += 1) {
            mPoints[index] = new Point(coords[2*index],coords[2*index+1]);
        }
    }

    public Point[] points() {
        return mPoints;
    }

    public float[] coords() {
        float[] result = new float[mPoints.length * 2];
        int index = 0;
        for (Point p : mPoints) {
            result[2 * index] = p.x();
            result[2 * index + 1] = p.y();
            index += 1;
        }
        return result;
    }

    public Point middle() {
        float sumx = 0.0f;
        float sumy = 0.0f;
        for (Point p : mPoints) {
            sumx += p.x();
            sumy += p.y();
        }
        float fnpoints = (float)mPoints.length;

        return new Point(sumx/fnpoints,sumy/fnpoints);
    }

    //
    // basic algo : works only for convex polys
    //
    public float[] GLCoords() {
        float[] result = new float[(mPoints.length + 1) * 3];
        Point pmiddle = middle();
        short index = 0;
        result[3 * index]     = pmiddle.x();
        result[3 * index + 1] = pmiddle.y();
        result[3 * index + 2] = 0.0f;
        index = +1;

        for (Point p : mPoints) {
            result[3 * index]     = p.x();
            result[3 * index + 1] = p.y();
            result[3 * index + 2] = 0.0f;
            index += 1;
        }
        return result;
    }

    //
    // basic algo : works only for convex polys
    //
    public short[] GLOrder() {
        short[] result = new short[mPoints.length * 3];

        for (short pindex = 0; pindex < mPoints.length-1; pindex++) {
            result[pindex * 3]     = 0; // middle point
            result[pindex * 3 + 1] = pindex;
            result[pindex * 3 + 2] = (short) (pindex + 1);
        }

        return result;
    }
}

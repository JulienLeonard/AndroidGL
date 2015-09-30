package julienl.androidgl;

/**
 * Created by JulienL on 9/30/2015.
 */
public class Point {

    private float mx;
    private float my;

    public Point(float x, float y) {
        mx = x;
        my = y;
    }

    public float x() {return mx;}
    public float y() {return my;}
}

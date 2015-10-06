package julienl.androidgl;

import android.util.Log;

/**
 * Created by JulienL on 10/5/2015.
 */
public abstract class Shape {

    abstract public BBox bbox();
    public static Boolean intersect(Shape shape1, Shape shape2) {
        // Log.v("Test log", "intersect generic");

        if (shape1 instanceof Circle &&  shape2 instanceof Circle) {
            return Circle.intersect((Circle)shape1,(Circle)shape2);
        }

        Log.v("Test log", "intersect generic");
        return false;
    }
}

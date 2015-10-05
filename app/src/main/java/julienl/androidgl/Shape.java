package julienl.androidgl;

/**
 * Created by JulienL on 10/5/2015.
 */
public abstract class Shape {

    public BBox bbox() {
        return null;
    }

    public static Boolean intersect(Shape shape1, Shape shape2) {
        return false;
    }
}

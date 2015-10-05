package julienl.androidgl;

/**
 * Created by JulienL on 10/5/2015.
 */
public abstract class Shape {

    public BBox bbox() {
        return null;
    }

    public Boolean intersect(Shape oshape) {
        return false;
    }
}

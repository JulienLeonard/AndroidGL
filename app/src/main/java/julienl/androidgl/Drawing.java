package julienl.androidgl;

/**
 * Created by JulienL on 10/2/2015.
 */
public class Drawing {

    private Polygon mPolygon;
    private Color   mColor;

    public Drawing(Polygon polygon,Color color) {
        mPolygon = polygon;
        mColor = color;
    }

    public Color color() {
        return mColor;
    }

    public Polygon polygon() {
        return mPolygon;
    }
}

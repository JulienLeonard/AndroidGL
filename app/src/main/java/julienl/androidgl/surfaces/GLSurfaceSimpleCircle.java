package julienl.androidgl.surfaces;

import android.content.Context;

import java.util.Random;

import julienl.androidgl.drawing.Color;
import julienl.androidgl.geometry.Circle;
import julienl.androidgl.geometry.Point2D;
import julienl.androidgl.utils.Range;

/**
 * Created by JulienL on 10/8/2015.
 */
public class GLSurfaceSimpleCircle extends GLSurfaceViewProto {

    protected Random mRandgen;

    public GLSurfaceSimpleCircle(Context context) {
        super(context);
        mRandgen = new Random(0);
    }

    protected void createcircle(float x, float y) {
        Circle newcircle = new Circle(new Point2D(x + Range.New(-10.0, 10.0).rand(mRandgen), y + Range.New(-10.0, 10.0).rand(mRandgen)), 20.0);
        Color color = Color.rand(mRandgen,1.0);
        this.draw(newcircle,color);
        requestRender();
    }

    protected void OnTouchDown(float x, float y, long currentTime) {
        createcircle(x,y);
    }

    protected void OnTouchMove(float x, float y, long currentTime, long elapsedTime) {
        createcircle(x,y);
    }
}

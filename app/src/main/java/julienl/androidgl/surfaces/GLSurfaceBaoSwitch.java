package julienl.androidgl.surfaces;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import julienl.androidgl.bao.BaoCirclePacking;
import julienl.androidgl.bao.BaoCirclePackingSwitch;
import julienl.androidgl.bao.BaoNode;
import julienl.androidgl.bao.BaoPattern;
import julienl.androidgl.bao.BaoPatternSwitch;
import julienl.androidgl.drawing.Color;
import julienl.androidgl.geometry.Circle;
import julienl.androidgl.geometry.Point2D;
import julienl.androidgl.quadtree.QuadTree;

/**
 * Created by JulienL on 10/8/2015.
 */
public class GLSurfaceBaoSwitch extends GLSurfaceViewProto {

    protected QuadTree mQuadTree;
    private BaoCirclePackingSwitch mpacking;

    public GLSurfaceBaoSwitch(Context context) {
        super(context);
        mQuadTree = new QuadTree();

        Circle newcircle = new Circle(new Point2D(500.0, 500.0), 20.0);
        ArrayList<BaoNode> nodes0 = BaoNode.fromcircle(newcircle);


        ArrayList<Color> colorpattern = new ArrayList<Color>();
        colorpattern.add(Color.white());
        colorpattern.add(Color.red());
        ArrayList<Double> radiuspattern = new ArrayList<Double>();
        radiuspattern.add(10.0);
        ArrayList<Double> sidepattern = new ArrayList<Double>();
        sidepattern.add(1.0);
        BaoPatternSwitch baopattern = (BaoPatternSwitch)BaoPatternSwitch.New(this).sidepattern(sidepattern).colorpattern(colorpattern).radiuspattern(radiuspattern);

        mpacking = new BaoCirclePackingSwitch(null, nodes0, baopattern, 1.0, mQuadTree);

        for (BaoNode node : nodes0) {
            draw(node, Color.yellow());
        }

        startTimer(1000);
    }

    protected void switchSide() {
        Log.v("GLSurfaceBaoSwitch", "switchSide");
        ArrayList<Double> sidepattern = ((BaoPatternSwitch)mpacking.mbaopattern).msidepattern;
        Double pside = sidepattern.get(0);
        ((BaoPatternSwitch)mpacking.mbaopattern).msidepattern.set(0,-1.0*pside);
    }

    protected void OnTouchDown(float x, float y, long currentTime) {
        switchSide();
    }

    protected void OnTouchUp(float x, float y, long currentTime) {
        switchSide();
    }

    protected void OnTimer(long currentTime, long elapsedTime) {
        if (mpacking != null) {
            mpacking.iter(1);
            requestRender();
        }
    }
}

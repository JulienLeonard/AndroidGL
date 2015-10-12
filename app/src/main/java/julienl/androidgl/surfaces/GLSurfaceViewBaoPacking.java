package julienl.androidgl.surfaces;

import android.content.Context;

import java.util.ArrayList;

import julienl.androidgl.bao.BaoCirclePacking;
import julienl.androidgl.bao.BaoNode;
import julienl.androidgl.bao.BaoPattern;
import julienl.androidgl.drawing.Color;
import julienl.androidgl.geometry.Circle;
import julienl.androidgl.geometry.Point2D;
import julienl.androidgl.quadtree.QuadTree;
import julienl.androidgl.utils.Range;

public class GLSurfaceViewBaoPacking extends GLSurfaceViewProto   {

    protected QuadTree mQuadTree;
    private BaoCirclePacking mpacking;

    public GLSurfaceViewBaoPacking(Context context) {
        super(context);
        mQuadTree = new QuadTree();
    }

    protected void OnTouchDown(float x, float y, long currentTime) {
        Circle newcircle = new Circle(new Point2D(x, y), 20.0);
        if (!mQuadTree.isColliding(newcircle)) {
            ArrayList<Color> colorpattern = new ArrayList<>();
            colorpattern.add(Color.white());
            colorpattern.add(Color.red());
            ArrayList<Double> radiuspattern = new ArrayList<>();
            radiuspattern.add(10.0);
            BaoPattern baopattern = new BaoPattern(this).colorpattern(colorpattern).radiuspattern(radiuspattern);
            ArrayList<BaoNode> nodes0 = BaoNode.fromcircle(newcircle);
            mpacking = new BaoCirclePacking(null, nodes0, baopattern, 1.0, mQuadTree);
            for (BaoNode node : nodes0) {
                draw(node, Color.yellow());
            }
        }
        startTimer(10);
    }

    protected void OnTouchUp(float x, float y, long currentTime) {
        stopTimer();
    }

    protected void OnTimer(long currentTime, long elapsedTime) {
        if (mpacking != null) {
            mpacking.iter(10);
            requestRender();
        }
    }
}

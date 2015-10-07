package julienl.androidgl.bao;

import java.util.ArrayList;

import julienl.androidgl.drawing.Color;
import julienl.androidgl.MyGLSurfaceView;
import julienl.androidgl.utils.Utils;

/**
 * Created by JulienL on 10/7/2015.
 */
public class BaoPattern {

	protected ArrayList<Double> mradiuspattern;
	protected ArrayList<Color>  mcolorpattern;
	protected int               mindex;
	protected MyGLSurfaceView mcanvas;
	
	public BaoPattern(MyGLSurfaceView canvas) {
		mcanvas = canvas;

		mradiuspattern = new ArrayList<Double>();
		mradiuspattern.add(1.0);

		mcolorpattern = new ArrayList<Color>();
		mcolorpattern.add(Color.black());
		
		mindex = 0;
	}

	public void draw(BaoNode newnode, int index) {
		draw(newnode,index,color(index));
	}

	// to be overloaded if necessary
	public void draw(BaoNode newnode, int index, Color color) {
		mcanvas.draw(newnode,color);
	}

	public BaoPattern next() {
		mindex += 1;
		return this;
	}

	public BaoPattern radiuspattern(ArrayList<Double> radiuspattern) {
		mradiuspattern = radiuspattern;
		return this;
	}

	public BaoPattern colorpattern(ArrayList<Color> colorpattern) {
		mcolorpattern = colorpattern;
		return this;
	}

	public double radius() {
		return Utils.lcircular(mradiuspattern, mindex);
	}

	public Color color() {
		return color(mindex);
	}

	public Color color(int index) {
		return Utils.lcircular(mcolorpattern,mindex);
	}
}

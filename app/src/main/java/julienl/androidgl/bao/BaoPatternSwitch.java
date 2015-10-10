package julienl.androidgl.bao;

import java.util.ArrayList;

import julienl.androidgl.surfaces.GLSurfaceViewProto;
import julienl.androidgl.utils.Utils;

/**
 * Created by JulienL on 10/8/2015.
 */
public class BaoPatternSwitch extends BaoPattern {

	protected ArrayList<Double> msidepattern;

	public BaoPatternSwitch(GLSurfaceViewProto canvas) {
		super(canvas);
		msidepattern = new ArrayList<Double>();
		msidepattern.add(1.0);
	}

	public static BaoPatternSwitch New(GLSurfaceViewProto canvas) {
		return new BaoPatternSwitch(canvas);
	}

	public double side() {
		return Utils.lcircular(msidepattern, mindex);
	}

	public BaoPatternSwitch sidepattern(ArrayList<Double> sidepattern) {
		msidepattern = sidepattern;
		return this;
	}
}

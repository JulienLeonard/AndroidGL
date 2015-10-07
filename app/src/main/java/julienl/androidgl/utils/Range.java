package julienl.androidgl.utils;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by JulienL on 9/30/2015.
 */
public class Range {

    private double mv1;
    private double mv2;

    public Range(double v1, double v2) {
        mv1 = v1;
        mv2 = v2;
    }

	static public Range New(double v1, double v2) {
		return new Range(v1,v2);
	}

    public double v1() {
        return mv1;
    }

    public double v2() {
        return mv2;
    }

    public double sample(double abscissa) {
        return mv1 + (mv2 - mv1) * abscissa;
    }

    public double trim(double value) {
        if (value < minv()) {
            return minv();
        }
        if (value > maxv()) {
            return maxv();
        }
        return value;
    }

    public double minv() {
        return (mv1 < mv2 ? mv1 : mv2);
    }

    public double maxv() {
        return (mv1 > mv2 ? mv1 : mv2);
    }

    public ArrayList<Double> samples(int niter) {
		ArrayList<Double> result = new ArrayList<Double>();
		for (double abscissa: Range.usamples(niter)) {
			result.add(sample(abscissa));
		}
        return result;
    }

    public double rand(Random rand) {
        return sample(rand.nextDouble());
    }

	public static ArrayList<Double> usamples(int niter) {
		ArrayList<Double> result = new ArrayList<Double>();
		if (niter == 0) {
			return result;
		}
		if (niter == 1) {
			result.add(0.5);
			return result;
		}

        for (int index = 0; index < niter; index++) {
            result.add((double)index/(double)(niter-1));
        }
        return result;
	}

	public Boolean contain(double v) {
		return (mv1 <= mv2) ? (mv1 <= v && v <= mv2) : (mv2 <= v && v <= mv1);
	}
}

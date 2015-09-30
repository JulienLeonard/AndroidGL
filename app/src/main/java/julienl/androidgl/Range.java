package julienl.androidgl;

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

    public double[] samples(short niter) {
        double[] result = new double[niter];
        double incr = (mv2 - mv1)/(double)(niter - 1);
        double v = mv1;
        for (short index = 0; index < niter; index++) {
            result[index] = v;
            v += incr;
        }
        return result;
    }
}

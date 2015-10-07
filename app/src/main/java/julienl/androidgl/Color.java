package julienl.androidgl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Random;

import android.opengl.GLES20;

import julienl.androidgl.MyGLRenderer;

/**
 * A two-dimensional square for use as a drawn object in OpenGL ES 2.0.
 */
public class Color {

    private double mR;
    private double mG;
    private double mB;
    private double mA;
    private double[] mcoords;

    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
     */
    public Color(double r, double g, double b, double a) {
        mcoords = new double[4];
        mcoords[0] = r;
        mcoords[1] = g;
        mcoords[2] = b;
        mcoords[3] = a;
    }

    public double R() {return mcoords[0];}

    public double G() {
        return mcoords[1];
    }

    public double B() {
        return mcoords[2];
    }

    public double A() {
        return mcoords[3];
    }

    public double[] coords() {
        return mcoords;
    }

    public float[] GLcoords() {
        float[] result = new float[4];
        result[0] = (float)R();
        result[1] = (float)G();
        result[2] = (float)B();
        result[3] = (float)A();
        return result;
    }

    public static Color rand(Random rand, double a) {
        return new Color(rand.nextDouble(),rand.nextDouble(),rand.nextDouble(),a);
    }

    public static Color black() {
        return new Color(0.0,0.0,0.0,1.0);
    }
}

package mycompagnycom.myopengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;

import mycompagnycom.myopengl.MyGLRenderer;

/**
 * A two-dimensional square for use as a drawn object in OpenGL ES 2.0.
 */
public class Color {

    private float mR;
    private float mG;
    private float mB;
    private float mA;
    private float[] mcoords;

    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
     */
    public Color(float r, float g, float b, float a) {
        mcoords = new float[4];
        mcoords[0] = r;
        mcoords[1] = g;
        mcoords[2] = b;
        mcoords[3] = a;
    }

    public float R() {
        return mcoords[0];
    }

    public float G() {
        return mcoords[1];
    }

    public float B() {
        return mcoords[2];
    }

    public float A() {
        return mcoords[3];
    }

    public float[] coords() {
        return mcoords;
    }
}

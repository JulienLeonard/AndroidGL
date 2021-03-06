package julienl.androidgl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import java.util.ArrayList;

import julienl.androidgl.drawing.Color;
import julienl.androidgl.drawing.Drawing;
import julienl.androidgl.drawing.PolygonRenderer;
import julienl.androidgl.geometry.Polygon;

/**
 * Provides drawing instructions for a GLSurfaceView object. This class
 * must override the OpenGL ES drawing lifecycle methods:
 * <ul>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceCreated}</li>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onDrawFrame}</li>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceChanged}</li>
 * </ul>
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "MyGLRenderer";
    private volatile ArrayList<Drawing> mDrawings = new ArrayList<>();;
    private PolygonRenderer mPolyRenderer =  new PolygonRenderer();

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix        = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix       = new float[16];
    private final float[] mRotationMatrix   = new float[16];

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        //mPolyRenderer = new PolygonRenderer();
        //mDrawings     = new ArrayList<>();

    }

    @Override
    public void onDrawFrame(GL10 unused) {
        // Log.v("MyGLRenderer", "onDrawFrame");

        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Draw circles
        // for (Drawing drawing: mDrawings) {
        //     mPolyRenderer.draw(drawing.polygon(), drawing.color(), mMVPMatrix);
        // }

        if (mDrawings.size() > 0) {
            Polygon[] polygons = new Polygon[mDrawings.size()];
            Color[]   colors   = new Color[mDrawings.size()];
            int index = 0;
            for (Drawing drawing : mDrawings) {
                polygons[index] = drawing.polygon();
                colors[index]   = drawing.color();
                index += 1;
            }

            // Log.v("MyGLRenderer", "onDrawFrame " + polygons.length);

            // mPolyRenderer.draw(polygons[polygons.length-1], colors[polygons.length-1], mMVPMatrix);
            // mDrawings.clear();
            mPolyRenderer.draws(polygons, colors, mMVPMatrix);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // float ratio = (float) width / height;

        // Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
        Matrix.orthoM(mProjectionMatrix, 0, 0f, width, 0.0f, height, 0, 50);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

    }

    /**
     * Utility method for compiling a OpenGL shader.
     *
     * <p><strong>Note:</strong> When developing shaders, use the checkGlError()
     * method to debug shader coding errors.</p>
     *
     * @param type - Vertex or fragment shader type.
     * @param shaderCode - String containing the shader code.
     * @return - Returns an id for the shader.
     */
    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    /**
     * Utility method for debugging OpenGL calls. Provide the name of the call
     * just after making it:
     *
     * <pre>
     * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
     * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
     *
     * If the operation is not successful, the check throws an error.
     *
     * @param glOperation - Name of the OpenGL call to check.
     */
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

    public void draw(Polygon polygon, Color color) {
        // mDrawings.clear();
        mDrawings.add(new Drawing(polygon,color));
    }
}
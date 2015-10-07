package julienl.androidgl.drawing;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import julienl.androidgl.MyGLRenderer;
import julienl.androidgl.drawing.Color;
import julienl.androidgl.geometry.Point2D;
import julienl.androidgl.geometry.Polygon;

/**
 * Created by JulienL on 9/30/2015.
 */
public class PolygonRenderer {

    private final String vertexShaderCode =
		"uniform   mat4 uMVPMatrix;"              +
		"attribute vec4 aPosition;"               +
		"void main() {"                           +
		"  gl_Position = uMVPMatrix * aPosition;" +
		"}";

    private final String fragmentShaderCode =
		"precision mediump float;"  +
		"uniform   vec4    uColor;" +
		"void main() {"             +
		"  gl_FragColor = uColor;"  +
		"}";

    private final String vertexShaderCodeColor =
            "uniform   mat4  uMVPMatrix;"         +
		"attribute vec4  aPosition;"              +
		"attribute vec4  aColor;"                 +
		"varying   vec4  vColor;"                 +
		"void main(void) {"                       +
		"  gl_Position = uMVPMatrix * aPosition;" +
		"  vColor      = aColor;"                 +
		"}";


    private final String fragmentShaderCodeColor =
		"precision mediump float;" +
		"varying vec4 vColor;"     +
		"void main(void) {"        +
		"  gl_FragColor = vColor;" +
		"}";

    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;
    private int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
	private int mAlphaHandle;
    private int mMVPMatrixHandle;


    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX       = 4;
    static final int COORDS_PER_COLOR        = 4;
    static final int COORDS_PER_VERTEX_COLOR = COORDS_PER_VERTEX + COORDS_PER_COLOR;

    static final int NBYTES_PER_FLOAT = 4;
    static final int NBYTES_PER_SHORT = 2;

    private final int VERTEX_STRIDE       = COORDS_PER_VERTEX       * NBYTES_PER_FLOAT; // 4 bytes per vertex
    private final int VERTEX_STRIDE_COLOR = COORDS_PER_VERTEX_COLOR * NBYTES_PER_FLOAT; // 4 bytes per vertex

    public PolygonRenderer() {
        vertexBuffer   = null;
        drawListBuffer = null;
        mProgram       = 0;
    }

    /**
     * Encapsulates the OpenGL ES instructions for drawing this shape.
     *
     * @param mvpMatrix - The Model View Project matrix in which to draw
     * this shape.
     */
    public void draw(Polygon polygon, Color color, float[] mvpMatrix) {
        Point2D[] points = polygon.GLpoints();
        float[] polyCoords = new float[points.length * COORDS_PER_VERTEX];
        for (int i = 0; i < points.length; i++) {
            polyCoords[i * COORDS_PER_VERTEX + 0] = (float)points[i].x();
            polyCoords[i * COORDS_PER_VERTEX + 1] = (float)points[i].y();
            polyCoords[i * COORDS_PER_VERTEX + 2] = (float)0.0;
            polyCoords[i * COORDS_PER_VERTEX + 3] = (float)1.0;
        }
        short[] drawOrder  = polygon.GLOrder();

        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(polyCoords.length * NBYTES_PER_FLOAT);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(polyCoords);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * NBYTES_PER_SHORT);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        // prepare shaders and OpenGL program
        int vertexShader   = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables


        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle  = GLES20.glGetAttribLocation(mProgram,  "aPosition");
        mColorHandle     = GLES20.glGetUniformLocation(mProgram, "uColor");
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        MyGLRenderer.checkGlError("glGetUniformLocation");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        // GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, VERTEX_STRIDE, vertexBuffer);
        // GLES20.glVertexAttribPointer(mPositionHandle, 4, GLES20.GL_FLOAT, false, 4 * 4, 0);

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color.GLcoords(), 0);

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        MyGLRenderer.checkGlError("glUniformMatrix4fv");

        // Draw the polygon
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    /**
     * Encapsulates the OpenGL ES instructions for drawing this shape.
     *
     * @param mvpMatrix - The Model View Project matrix in which to draw
     * this shape.
     */
    public void draws(Polygon[] polygons, Color[] colors, float[] mvpMatrix) {
        int ncoords = 0;
        int norder = 0;
        for (Polygon poly : polygons) {
            ncoords += poly.GLnpoints() * COORDS_PER_VERTEX_COLOR;
            norder  += poly.GLOrderSize();
        }

        float[] polyCoords = new float[ncoords];
        short[] drawOrder  = new short[norder];

        int ncoordoffset = 0;
        int norderoffset = 0;
        int polyindex    = 0;
        int npointoffset = 0;
        for (Polygon poly : polygons) {
            double[] colorcoords = colors[polyindex].coords();

            for (Point2D point: poly.GLpoints()) {
                polyCoords[ncoordoffset + 0] = (float)point.x();
                polyCoords[ncoordoffset + 1] = (float)point.y();
                polyCoords[ncoordoffset + 2] = (float)0.0f;
                polyCoords[ncoordoffset + 3] = (float)1.0f;
                polyCoords[ncoordoffset + 4] = (float)colorcoords[0];
                polyCoords[ncoordoffset + 5] = (float)colorcoords[1];
                polyCoords[ncoordoffset + 6] = (float)colorcoords[2];
                polyCoords[ncoordoffset + 7] = (float)colorcoords[3];
                ncoordoffset += COORDS_PER_VERTEX_COLOR;
            }

            int orderindex = 0;
            for (short order: poly.GLOrder()) {
                drawOrder[norderoffset + orderindex] =  (short)(npointoffset + order);
                orderindex += 1;
            }

            norderoffset += poly.GLOrderSize();
            polyindex    += 1;
            npointoffset += poly.GLnpoints();
        }

        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(polyCoords.length * NBYTES_PER_FLOAT);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(polyCoords);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * NBYTES_PER_SHORT);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        // prepare shaders and OpenGL program
        int vertexShader   = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,  vertexShaderCodeColor);
        MyGLRenderer.checkGlError("loadShader vertex");
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentShaderCodeColor);
        MyGLRenderer.checkGlError("loadShader fragment");

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables
        GLES20.glUseProgram(mProgram);
        MyGLRenderer.checkGlError("glUseProgram");

        // get handles to shaders
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        MyGLRenderer.checkGlError("glGetUniformLocation matrix");
        mPositionHandle  = GLES20.glGetAttribLocation(mProgram,  "aPosition");
        MyGLRenderer.checkGlError("glGetAttribLocation aPosition");
		mColorHandle     = GLES20.glGetAttribLocation(mProgram, "aColor");
        MyGLRenderer.checkGlError("glGetAttribLocation Color");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glEnableVertexAttribArray(mColorHandle);

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        MyGLRenderer.checkGlError("glUniformMatrix4fv");

        // Prepare the triangle coordinate data
        vertexBuffer.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, VERTEX_STRIDE_COLOR, vertexBuffer);
        MyGLRenderer.checkGlError("glVertexAttribPointer vertex");
        vertexBuffer.position(COORDS_PER_VERTEX);
        GLES20.glVertexAttribPointer(mColorHandle,    COORDS_PER_COLOR, GLES20.GL_FLOAT, false, VERTEX_STRIDE_COLOR, vertexBuffer);
        MyGLRenderer.checkGlError("glVertexAttribPointer colorhandle");

		// GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, drawListBuffer);
		

        // Draw the polygons
        // GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, 0);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
        MyGLRenderer.checkGlError("glDrawElements");

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
		GLES20.glDisableVertexAttribArray(mColorHandle);
    }
}

package julienl.androidgl.surfaces;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;


import julienl.androidgl.MyGLRenderer;
import julienl.androidgl.drawing.Color;
import julienl.androidgl.geometry.Circle;
import julienl.androidgl.geometry.Polygon;

/**
 * Created by JulienL on 10/8/2015.
 */
public class GLSurfaceViewProto extends GLSurfaceView {

	private final MyGLRenderer mRenderer;

	long mstartTimeDown  = 0;
	long mlastTimeMove   = 0;
	long mstartTimeTimer = 0;
    long mlastTimeTimer  = 0;
  //  float mtouchx        = 0;
 //   float mtouchy        = 0;

    private Handler mtimerHandler;
    private Runnable mtimerRunnable;


    public GLSurfaceViewProto(Context context) {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        setEGLConfigChooser(8 , 8, 8, 8, 16, 0);

        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new MyGLRenderer();
        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public void draw(final Circle circle, final Color color) {
        // Log.v("Surfaceview", "draw circle");
        queueEvent(new Runnable() {
				// This method will be called on the rendering
				// thread:
				public void run() {
					Polygon polygon = circle.polygon(10);
					mRenderer.draw(polygon, color);
				}
			});
    }

	protected void OnTouchDown(float x, float y, long currentTime) {
		// to be overloaded
	}

	protected void OnTouchMove(float x, float y, long currentTime, long elapsedTime) {
		// to be overloaded
	}
	
	protected void OnTouchUp(float x, float y, long currentTime) {
		// to be overloaded
	}

	@Override
    public boolean onTouchEvent(MotionEvent e) {

        switch (e.getAction()) {
		  case MotionEvent.ACTION_DOWN:
			Log.v("Test log", "event=DOWN");
			mstartTimeDown = System.currentTimeMillis();
			mlastTimeMove  = mstartTimeDown;
			OnTouchDown(e.getX(), getHeight() - e.getY(),mstartTimeDown);
			break;

		  case MotionEvent.ACTION_MOVE:
			long amcurrentTime = System.currentTimeMillis();
			long elapsedTime = amcurrentTime - mlastTimeMove;
			OnTouchMove(e.getX(), getHeight() - e.getY(), amcurrentTime, elapsedTime);
			mlastTimeMove = amcurrentTime;
			break;


		  case MotionEvent.ACTION_UP:
			  Log.v("Test log", "event=UP");
			long aucurrentTime = System.currentTimeMillis();
			OnTouchUp(e.getX(), getHeight() - e.getY(), aucurrentTime);
			break;
        }
        return true;
    }

    public void startTimer(final long period) {
        Log.v("Surfaceview", "startTimer");

		mstartTimeTimer = System.currentTimeMillis();
		mlastTimeTimer  = mstartTimeTimer;

        mtimerHandler  = new Handler();
        mtimerRunnable = new Runnable() {

				@Override
				public void run() {
					long currentTime = System.currentTimeMillis();
					long elapsedTime = currentTime - mlastTimeTimer;
				
					OnTimer(currentTime, elapsedTime);

					mlastTimeTimer = System.currentTimeMillis();
					mtimerHandler.postDelayed(this, period);
				}
			};

        mtimerHandler.postDelayed(mtimerRunnable, period);
    }

    public void stopTimer() {
        mtimerHandler.removeCallbacks(mtimerRunnable);
    }

	protected void OnTimer(long currentTime, long elapsedTime) {
		// to overload
	}
}

package julienl.androidgl;
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;


import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A view container where OpenGL ES graphics can be drawn on screen.
 * This view can also be used to capture touch events, such as a user
 * interacting with drawn objects.
 */
public class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer mRenderer;

    // set loop to check circles
    long startTime = 0;

    private Random mRandgen      = new Random(0);


    //runs without a timer by reposting this handler at the end of the runnable
    /*
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            Log.v("timerRunnable", "adddCircle");

            mRenderer.addCircle();

            timerHandler.postDelayed(this, 50);
        }
    };
    */

    public MyGLSurfaceView(Context context) {
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

    /*
    public void addCircle() {
        mRenderer.addCircle();
        requestRender();
    }
    */

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.


        // Log.v("Test log", "event=" + e.getAction());

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.v("Test log", "event=DOWN");
                startTime = System.currentTimeMillis();
                break;

            case MotionEvent.ACTION_MOVE:
                long millis = System.currentTimeMillis() - startTime;
                if (millis > 10) {
                    Log.v("Test log", "add circle");
                    queueEvent(new Runnable() {
                        // This method will be called on the rendering
                        // thread:
                        public void run() {
                            Polygon polygon = new Circle(new Point2D(mRandgen.nextDouble(), mRandgen.nextDouble()), 0.1).polygon(10);
                            Color color = Color.rand(mRandgen, 0.1);
                            mRenderer.draw(polygon,color);
                        }});
                    requestRender();
                    startTime = System.currentTimeMillis();
                }
                break;


            case MotionEvent.ACTION_UP:
                Log.v("Test log", "event=UP");
                break;
        }
        return true;
    }

}

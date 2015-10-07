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

import java.util.ArrayList;
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
    private QuadTree mQuadTree;
    private BaoCirclePacking mpacking;

    // set loop to check circles
    long mstartTime = 0;
    float mtouchx = 0;
    float mtouchy = 0;

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

        mQuadTree = new QuadTree();

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public void draw(final Circle circle, final Color color) {
        Log.v("Surfaceview", "draw circle");
        queueEvent(new Runnable() {
            // This method will be called on the rendering
            // thread:
            public void run() {
                    Polygon polygon = circle.polygon(10);
                    mRenderer.draw(polygon, color);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.


        // Log.v("Test log", "event=" + e.getAction());

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.v("Test log", "event=DOWN");
                mstartTime = System.currentTimeMillis();
                mtouchx = e.getX();
                mtouchy = getHeight() - e.getY();
                Circle newcircle = new Circle(new Point2D(mtouchx + Range.New(-10.0,10.0).rand(mRandgen), mtouchy + Range.New(-10.0, 10.0).rand(mRandgen)), 20.0);
                if (!mQuadTree.isColliding(newcircle)) {
                    ArrayList<Color> colorpattern = new ArrayList<>();
                    colorpattern.add(Color.white());
                    colorpattern.add(Color.red());
                    ArrayList<Double> radiuspattern = new ArrayList<>();
                    radiuspattern.add(10.0);
                    BaoPattern baopattern = new BaoPattern(this).colorpattern(colorpattern).radiuspattern(radiuspattern);
                    ArrayList<BaoNode> nodes0 = BaoNode.fromcircle(newcircle);
                    mpacking = new BaoCirclePacking(null, nodes0, baopattern, 1.0, mQuadTree );
                    for (BaoNode node : nodes0) {
                        draw(node,Color.yellow());
                    }
                }
                //mtouchx = e.getX() / getWidth();
                //mtouchy = e.getY() / getHeight();
                //Log.v("Test log", "mtouchx" + mtouchx);
                break;

            case MotionEvent.ACTION_MOVE:
                long millis = System.currentTimeMillis() - mstartTime;

                if (millis > 10) {
                    if (mpacking != null) {
                        mpacking.iter();
                    }
                    requestRender();
                    mstartTime = System.currentTimeMillis();
                }
                break;


            case MotionEvent.ACTION_UP:
                Log.v("Test log", "event=UP");
                break;
        }
        return true;
    }

}

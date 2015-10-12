package julienl.androidgl;

import android.os.Bundle;


import android.app.Activity;

import julienl.androidgl.surfaces.GLSurfaceBaoSwitch;
import julienl.androidgl.surfaces.GLSurfaceQuadTree;
import julienl.androidgl.surfaces.GLSurfaceViewBaoPacking;
import julienl.androidgl.surfaces.GLSurfaceViewProto;

public class MainActivity extends Activity {

    private GLSurfaceViewProto mGLView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity
        mGLView = new GLSurfaceBaoSwitch(this);
        setContentView(mGLView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        mGLView.onResume();
    }
}
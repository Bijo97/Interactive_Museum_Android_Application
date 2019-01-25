package com.example.xtalker.manprotest;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.RelativeLayout;

/**
 * Created by Bijo97 on 28/11/2017.
 */

public class Denah extends Activity {
    private ScaleGestureDetector mScaleDetector;
    CanvasView cv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.denah);

        RelativeLayout rel = (RelativeLayout) findViewById(R.id.rel);
        cv = new CanvasView(Denah.this);
        rel.addView(cv);

        mScaleDetector = new ScaleGestureDetector(this, new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
            }
            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                return true;
            }
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                Log.d("Scale", "zoom ongoing, scale: " + detector.getScaleFactor());
                float result = detector.getScaleFactor() - 1f;
                cv.scaling += (result*0.04);
                return false;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleDetector.onTouchEvent(event);
        return true;
    }
}

package com.gleason.apahelper;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class GestureHandler implements GestureDetector.OnGestureListener{
	private static final String DEBUG_TAG = "test";
	public boolean isSwipe = false;
	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		Log.d(DEBUG_TAG, "On down");
		return true;
	}

	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		Log.d(DEBUG_TAG, "On Fling");
		return true;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		Log.d(DEBUG_TAG, "On long press");

	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		Log.d(DEBUG_TAG, "On Scroll");
		isSwipe=true;
		return true;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		Log.d(DEBUG_TAG, "On Single Tap");
		return true;
	}
}

package com.gleason.apahelper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnTouchModeChangeListener;
import android.view.ViewGroup;

public class ScoreFragment extends Fragment implements OnTouchListener{
	
	private static final String DEBUG_TAG = "test";
	private GestureDetectorCompat mDetector;
	private float startLocationX;
	private float startLocationY;
	private InningsView inningsView;
	private GestureHandler gh;
	
	public static ScoreFragment newInstance(int position) {
        Log.i("Pager", "ScoreFragment.newInstance()");

        ScoreFragment fragment = new ScoreFragment();

        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);

        return fragment;
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.score, container, false);
		inningsView = (InningsView) rootView.findViewById(R.id.inning);
		inningsView.setOnTouchListener(this);
		gh = new GestureHandler();
		mDetector = new GestureDetectorCompat(getActivity(), gh);
        return rootView;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		int action = MotionEventCompat.getActionMasked(event);

		switch (action) {
		case (MotionEvent.ACTION_DOWN):
			this.startLocationX = event.getAxisValue(MotionEvent.AXIS_X);
			this.startLocationY = event.getAxisValue(MotionEvent.AXIS_Y);
			break;
		case (MotionEvent.ACTION_UP):
			if(gh.isSwipe){
				inningsView.addLine();
				inningsView.invalidate();
			}
			float x = event.getAxisValue(MotionEvent.AXIS_X);
			float y = event.getAxisValue(MotionEvent.AXIS_Y);
			if (x > this.startLocationX) {
				// TODO: went up
				Log.d(DEBUG_TAG, "Went Right from "+startLocationX+ ":"+x);
			} else if (x < this.startLocationX) {
				Log.d(DEBUG_TAG, "Went Left from "+startLocationX+ ":"+x);
			} else {
				Log.d(DEBUG_TAG, "Stayed the same");
			}
			if (y > this.startLocationY) {
				// TODO: went up
				Log.d(DEBUG_TAG, "Went Down from "+startLocationY+ ":"+y);
				
			} else if (y < this.startLocationY) {
				Log.d(DEBUG_TAG, "Went Up ");
			} else {
				Log.d(DEBUG_TAG, "Stayed the same from "+startLocationY + ":"+y);
			}
			break;
		}
		this.mDetector.onTouchEvent(event);
		return true;
	}
}

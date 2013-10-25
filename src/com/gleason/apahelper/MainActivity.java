package com.gleason.apahelper;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.AvoidXfermode;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.Menu;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static final String DEBUG_TAG = "test";
	private GestureDetectorCompat mDetector;
	private float startLocationX;
	private float startLocationY;
	private InningsView inningsView;
	private GestureHandler gh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	    inningsView = (InningsView) findViewById(R.id.inning);
		gh = new GestureHandler();
		mDetector = new GestureDetectorCompat(this, gh);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		int action = MotionEventCompat.getActionMasked(event);

		switch (action) {
		case (MotionEvent.ACTION_DOWN):
			this.startLocationX = event.getAxisValue(MotionEvent.AXIS_X);
			this.startLocationY = event.getAxisValue(MotionEvent.AXIS_Y);
			// Log.d(DEBUG_TAG,"Action was DOWN");
			// return true;
			break;
		// case (MotionEvent.ACTION_MOVE) :
		// Log.d(DEBUG_TAG,"Action was MOVE");
		// return true;
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
		// Be sure to call the superclass implementation
		return super.onTouchEvent(event);
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	

}

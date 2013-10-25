package com.gleason.apahelper;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class InningsView extends View {
	private Paint paint = new Paint();
	private List<Line> points = new ArrayList<Line>();
	//TODO: make x and y
	private static final float OFFSET = 32;
	private static final float TOP_OFFSET = 70;
	private static final float HEIGHT = 50;
	private static final float WIDTH = 10;
	private int lines = 0;
	

    public InningsView(Context context) {
        super(context);
        init();
    }
    public InningsView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }
    public InningsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    
    private void init(){
    	paint.setColor(Color.BLACK);
        paint.setStrokeWidth(WIDTH);
    }
    
    
    float currentWidth;
    
    @Override
    public void onDraw(Canvas canvas) {
    	float[] pointsF = getPoints();
        canvas.drawLines(pointsF, paint);
        currentWidth = canvas.getWidth();
    }
    
    private float[] getPoints(){
    	float[] returnValue = new float[points.size()*4];
    	int i = 0;
    	for(Line l : points){
    		returnValue[i++]=l.getStartingX();
    		returnValue[i++]=l.getStartingY();
    		returnValue[i++]=l.getEndingX();
    		returnValue[i++]=l.getEndingY();
    	}
    	return returnValue;
    }
    
    private int currentLocation = 1;
    public void addLine(){
    	Line l = new Line();
    	if((points.size()+1) % 5 == 0){
    		l.setStartingX(OFFSET*(currentLocation-4));
    		l.setStartingY(OFFSET +TOP_OFFSET*lines);
    		l.setEndingX(OFFSET*(currentLocation -1));
    		l.setEndingY(OFFSET +TOP_OFFSET*lines+HEIGHT);
    		currentLocation++;
    	}
    	else{
    		float horizantalLocation = OFFSET*(currentLocation);
    		float width = currentWidth;
    		if (horizantalLocation > width){
    			lines++;
    			currentLocation = 1;
    			horizantalLocation = OFFSET*(currentLocation);
    		}
    		l.setStartingX(horizantalLocation);
    		l.setStartingY(OFFSET +(TOP_OFFSET*lines));
    		l.setEndingX(horizantalLocation);
    		l.setEndingY(OFFSET +(TOP_OFFSET*lines)+HEIGHT);
    		currentLocation++;
    	}
    	points.add(l);
    }
}

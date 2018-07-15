package com.example.patrickconner.paint_dot_java;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class PaintCanvasView extends View {
	
	public PaintCanvasView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void changeStrokeColor(int color) {
	
	}
	
	public void changeStrokeSize(float size) {
	
	}
	
	public void saveImage() {
	
	}
	
	public void clearCanvas() {
	
	}
	
	public void undo() {
	
	}
	
	public void redo() {
	
	}
	
	public void toggleErase() {
	
	}
	
	public void toggleFill() {
	
	}
	
	public void offsetPath(MotionEvent event) {
	
	}
	
	public void sculpt(MotionEvent event) {
	
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}
	
	private PathPoints getCollidingPath(float x, float y) throws Exception {
		throw new Exception("not implemented yet");
	}
}

package com.example.patrickconner.paint_dot_java;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class PaintCanvasView extends View {
	
	private Stack<Command> undoStack, redoStack;
	private List<PathPoints> paths;
	private PathPoints curPath;
	private float curX, curY;
	private Paint paint;
	private Paint.Style paintStyle;
	private boolean isErasing;
	
	private Bitmap bitmap;
	private Canvas canvas;
	Context context;
	
	private static final int TOLERANCE = 3;
	
	public PaintCanvasView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		
		undoStack = new Stack<>();
		redoStack = new Stack<>();
		paths = new ArrayList<>();
		
		paintStyle = Paint.Style.STROKE;
		
		curPath = new PathPoints();
		
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);
		paint.setStyle(paintStyle);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeWidth(4f);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldW, int oldH) {
		super.onSizeChanged(w, h, oldW, oldH);
		
		bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bitmap);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		canvas.drawPath(curPath, paint);
	}
	
	public void changeStrokeColor(int color) {
	
	}
	
	public void changeStrokeSize(float size) {
		paint.setStrokeWidth(size);
	}
	
	public void saveImage() {
	
	}
	
	public void clearCanvas() {
		curPath.reset();
		invalidate();
	}
	
	public void undo() {
		if (!undoStack.empty()) {
			Command commandUndo = undoStack.pop();
			commandUndo.undo();
			redoStack.push(commandUndo);
		}
	}
	
	public void redo() {
		if (!redoStack.empty()) {
			Command commandRedo = redoStack.pop();
			commandRedo.execute();
			undoStack.push(commandRedo);
		}
	}
	
	public void toggleErase() {
		isErasing = !isErasing;
	}
	
	public void toggleFill() {
		if (paintStyle.compareTo(Paint.Style.STROKE) == 0) {
			paintStyle = Paint.Style.FILL_AND_STROKE;
		} else if (paintStyle.compareTo(Paint.Style.FILL_AND_STROKE) == 0) {
			paintStyle = Paint.Style.STROKE;
		}
		
		paint.setStyle(paintStyle);
	}
	
	public void offsetPath(MotionEvent event) {
	
	}
	
	public void sculpt(MotionEvent event) {
	
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				startTouch(x, y);
				invalidate();
				break;
			
			case MotionEvent.ACTION_MOVE:
				moveTouch(x, y);
				invalidate();
				break;
			
			case MotionEvent.ACTION_UP:
				upTouch();
				invalidate();
				break;
		}
		return true;
	}
	
	private void startTouch(float x, float y) {
		if (!isErasing) {
			curPath = new PathPoints();
			curPath.moveTo(x, y);
			curX = x;
			curY = y;
		}
	}
	
	private void moveTouch(float x, float y) {
		if (!isErasing) {
			float dx = Math.abs(x - curX);
			float dy = Math.abs(y - curY);
			
			if (dx >= TOLERANCE && dy >= TOLERANCE) {
				curPath.quadTo(curX, curY, (x + curX) / 2, (y + curY) / 2);
				curX = x;
				curY = y;
			}
		}
	}
	
	private void upTouch() {
		if (!isErasing) {
			curPath.moveTo(curX, curY);
		}
	}
	
	private PathPoints getCollidingPath(float x, float y) throws Exception {
		throw new Exception("not implemented yet");
	}
}

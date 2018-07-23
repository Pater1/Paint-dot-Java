package com.example.patrickconner.paint_dot_java;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class PaintCanvasView extends View {
	
	private Stack<List<PathPoints>> undoStack, redoStack;
	private List<PathPoints> paths;
	private float curX;
	private float curY;
	private PathPoints curPath;
	private Paint curPaint;
	private Paint.Style paintStyle;
	private DrawMode drawMode;
	
	private Bitmap bitmap;
	private Canvas canvas;
	Context context;
	
	public static final int TOLERANCE = 3;
	
	public PaintCanvasView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		
		undoStack = new Stack<>();
		redoStack = new Stack<>();
		paths = new ArrayList<>();
		
		setCurPaint(Color.BLACK, Paint.Style.STROKE, 10f);
		
		drawMode = DrawMode.Draw;
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldW, int oldH) {
		if (h <= 0) h = w;
		super.onSizeChanged(w, h, oldW, oldH);
		
		bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bitmap);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		canvas.drawARGB(255, 255, 255, 255);
		this.canvas.drawARGB(255, 255, 255, 255);
		
		for (PathPoints p : paths) {
			canvas.drawPath(p, p.getPaint());
			this.canvas.drawPath(p, p.getPaint());
		}
		if (curPath != null) {
			canvas.drawPath(curPath, curPaint);
			this.canvas.drawPath(curPath, curPaint);
		}
	}
	
	public void changeStrokeColor(int color) {
		setCurPaint(color, curPaint.getStyle(), curPaint.getStrokeWidth());
	}
	
	public void clearCanvas() {
		pushToUndo();
		paths.clear();
		invalidate();
	}
	
	public void undo() {
		if (!undoStack.empty()) {
			List<PathPoints> prevPaths = undoStack.pop();
			redoStack.push(paths);
			paths = prevPaths;
			invalidate();
		}
	}
	
	public void redo() {
		if (!redoStack.empty()) {
			List<PathPoints> prevPaths = redoStack.pop();
			undoStack.push(paths);
			paths = prevPaths;
			invalidate();
		}
	}
	
	public void toggleFill() {
		if (paintStyle.compareTo(Paint.Style.STROKE) == 0) {
			paintStyle = Paint.Style.FILL_AND_STROKE;
		} else if (paintStyle.compareTo(Paint.Style.FILL_AND_STROKE) == 0) {
			paintStyle = Paint.Style.STROKE;
		}
		
		curPaint.setStyle(paintStyle);
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
		redoStack.clear();
		pushToUndo();
		
		switch (drawMode) {
			case Draw:
				curPath = new PathPoints(curPaint);
				curPath.moveTo(x, y);
				curX = x;
				curY = y;
				
				paths.add(curPath);
				break;
			
			case Erase:
			case Sculpt:
				curX = x;
				curY = y;
				break;
			
			default:
				break;
		}
	}
	
	private void moveTouch(float x, float y) {
		float dx = Math.abs(x - curX);
		float dy = Math.abs(y - curY);
		
		if (dx >= TOLERANCE && dy >= TOLERANCE) {
			switch (drawMode) {
				case Draw:
					curPath.quadTo(curX, curY, (x + curX) / 2, (y + curY) / 2);
					break;
				
				case Sculpt:
					for (PathPoints p : paths) {
						p.sculpt(curX, curY, (x + curX) / 2, (y + curY) / 2, curPaint.getStrokeWidth() * 10);
					}
					break;
				
				case Erase:
					int pathIndex = getCollidingPathIndex(curX, curY);
					if (pathIndex > -1) {
						paths.remove(pathIndex);
					}
					break;
				
				default:
					break;
			}
			
			curX = x;
			curY = y;
		}
	}
	
	private void upTouch() {
		switch (drawMode) {
			case Draw:
				curPath = null;
				break;
			case Sculpt:
				break;
			case Erase:
				break;
			default:
				break;
		}
	}
	
	private int getCollidingPathIndex(float x, float y) {
		for (int i = 0; i < paths.size(); i++) {
			if (paths.get(i).collides(x, y)) {
				return i;
			}
		}
		
		return -1;
	}
	
	private void pushToUndo() {
		List<PathPoints> pathsCopy = new ArrayList<>(paths.size());
		for (PathPoints p : paths) pathsCopy.add(new PathPoints(p));
		undoStack.push(pathsCopy);
	}
	
	public void setDrawMode(DrawMode drawMode) {
		this.drawMode = drawMode;
	}
	
	public void setBrushSize(float brushSize) {
		setCurPaint(curPaint.getColor(), curPaint.getStyle(), brushSize);
	}
	
	private void setCurPaint(@ColorInt int color, Paint.Style style, float strokeWidth) {
		curPaint = new Paint();
		curPaint.setAntiAlias(true);
		curPaint.setColor(color);
		curPaint.setStyle(style);
		curPaint.setStrokeJoin(Paint.Join.ROUND);
		curPaint.setStrokeWidth(strokeWidth);
	}
	
	public Bitmap getBitmap() {
		return bitmap;
	}
}
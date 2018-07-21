package com.example.patrickconner.paint_dot_java;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
		
		curPaint = new Paint();
		curPaint.setAntiAlias(true);
		curPaint.setColor(Color.WHITE);
		curPaint.setStyle(paintStyle);
		curPaint.setStrokeJoin(Paint.Join.ROUND);
		curPaint.setStrokeWidth(4f);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldW, int oldH) {
		if(h <= 0) h = w;
		super.onSizeChanged(w, h, oldW, oldH);
		
		bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bitmap);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		canvas.drawARGB(255, 0, 0, 0);
		
		for (PathPoints p : paths) {
			canvas.drawPath(p, p.getPaint());
		}
		if (curPath != null) {
			canvas.drawPath(curPath, curPaint);
		}
	}
	
	public void changeStrokeColor(int color) {
	
	}
	
	public void changeStrokeSize(float size) {
		curPaint.setStrokeWidth(size);
	}
	
	public void saveImage(String fileName) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(fileName);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void clearCanvas() {
		for (PathPoints p : paths) {
			p.reset();
		}
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
	
	public void toggleErase() {
		isErasing = !isErasing;
	}
	
	public void toggleFill() {
		if (paintStyle.compareTo(Paint.Style.STROKE) == 0) {
			paintStyle = Paint.Style.FILL_AND_STROKE;
		} else if (paintStyle.compareTo(Paint.Style.FILL_AND_STROKE) == 0) {
			paintStyle = Paint.Style.STROKE;
		}
		
		curPaint.setStyle(paintStyle);
	}
	
	public void offsetPath(MotionEvent event) {
	
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
		
		switch (drawMode) {
			case Draw:
				pushToUndo();
				
				curPath = new PathPoints(curPaint);
				curPath.moveTo(x, y);
				curX = x;
				curY = y;
				
				paths.add(curPath);
				break;
			case Sculpt:
				curX = x;
				curY = y;
				break;
			case Erase:
				break;
			default:
				break;
		}
	}
	
	private void moveTouch(float x, float y) {
		float dx = Math.abs(x - curX);
		float dy = Math.abs(y - curY);

		if (dx >= TOLERANCE && dy >= TOLERANCE) {
			switch (drawMode){
				case Draw:
					curPath.quadTo(curX, curY, (x + curX) / 2, (y + curY) / 2);
					break;
				case Sculpt:
					for(PathPoints p : paths){
						p.sculpt(curX, curY, (x + curX) / 2, (y + curY) / 2, curPaint.getStrokeWidth()*10);
					}
					break;
				case Erase:
					break;
				default:
					break;
			}

			curX = x;
			curY = y;
		}
	}
	
	private void upTouch() {
		switch (drawMode){
			case Draw:
				curPath = null;
				List<PathPoints> pathsCopy = new ArrayList<>(paths);
				undoStack.push(pathsCopy);
				break;
			case Sculpt:
				break;
			case Erase:
				break;
			default:
				break;
		}
	}
	
	private PathPoints getCollidingPath(float x, float y) {
		throw new UnsupportedOperationException("not implemented yet");
	}
	
	public PathPoints getCurPath() {
		return curPath;
	}
	
	public void setCurPath(PathPoints curPath) {
		this.curPath = curPath;
	}
	
	public Paint getCurPaint() {
		return curPaint;
	}
}

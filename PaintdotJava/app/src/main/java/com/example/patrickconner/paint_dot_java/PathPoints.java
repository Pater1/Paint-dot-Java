package com.example.patrickconner.paint_dot_java;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

public class PathPoints extends Path {
	
	private Paint paint;
	private List<Point> points;
	
	public PathPoints(Paint paint) {
		super();
		this.paint = paint;
		points = new ArrayList<>();
	}
	
	@Override
	public void moveTo(float x, float y) {
		super.moveTo(x, y);
		points.add(new Point((int) x, (int) y));
	}
	
	@Override
	public void quadTo(float x1, float y1, float x2, float y2) {
		super.quadTo(x1, y1, x2, y2);
		points.add(new Point((int) x2, (int) y2));
	}
	
	public void sculpt(MotionEvent event) {
	
	}
	
	public List<Point> getPoints() {
		return points;
	}
	
	public Paint getPaint() {
		return paint;
	}
}

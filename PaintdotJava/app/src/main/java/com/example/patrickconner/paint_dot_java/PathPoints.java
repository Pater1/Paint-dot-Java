package com.example.patrickconner.paint_dot_java;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

public class PathPoints extends Path {
	
	private List<Point> points;
	private Paint paint;
	
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
	
	public List<Point> getPoints() {
		return points;
	}
	
	public Paint getPaint() {
		return paint;
	}
}

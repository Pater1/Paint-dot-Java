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
		commonCtor(paint);
	}
	
	public PathPoints(PathPoints p) {
		super();
		commonCtor(p.getPaint());
		
		for (Point point : p.getPoints()) {
			points.add(new Point(point.x, point.y));
		}
		
		redraw();
	}
	
	private void commonCtor(Paint paint) {
		this.paint = paint;
		points = new ArrayList<>();
	}
	
	@Override
	public void reset() {
		super.reset();
//		points.clear();
	}
	
	@Override
	public void moveTo(float x, float y) {
		this.moveTo(x, y, true);
	}
	@Override
	public void quadTo(float x1, float y1, float x2, float y2) {
		this.quadTo(x1, y1, x2, y2, true);
	}

	public void moveTo(float x, float y, boolean add) {
		super.moveTo(x, y);
		if(add) points.add(new Point((int) x, (int) y));
	}
	public void quadTo(float x1, float y1, float x2, float y2, boolean add) {
		super.quadTo(x1, y1, x2, y2);
		if(add) points.add(new Point((int) x2, (int) y2));
	}

	public void sculpt(float x1, float y1, float x2, float y2, float range){
		List<Point> nPoints = new ArrayList<Point>();

		float dx = x2-x1;
		float dy = y2-y1;

		for(Point p : points){
			Point np = new Point();
			float distX = x2-p.x;
			float distY = y2-p.y;
			float dist = (float)Math.sqrt((distX*distX)+(distY*distY));
			if(dist < 0) dist = 0;
			if(dist > range) dist = range;
			float fac = (-1/range)*dist + 1;

			np.x = (int)(p.x + (dx * fac));
			np.y = (int)(p.y + (dy * fac));

			nPoints.add(np);
		}

		points = nPoints;
		redraw();
	}
	@Override
	public void offset(float dx, float dy){
		List<Point> nPoints = new ArrayList<Point>();

		for(Point p : points){
			Point np = new Point();

			np.x = (int)(p.x + dx);
			np.y = (int)(p.y + dy);

			nPoints.add(np);
		}

		points = nPoints;
		super.offset(dx, dy);
	}
	
	public void redraw(){
		this.reset();
		for(int i = 0; i < points.size(); i++){
			if(i == 0){
				moveTo(points.get(i).x, points.get(i).y, false);
			}else{
				quadTo(points.get(i-1).x, points.get(i-1).y, points.get(i).x, points.get(i).y, false);
			}
		}
	}
	
	public boolean collides(float x, float y) {
		for (Point p : points) {
			float dx = Math.abs(x - p.x);
			float dy = Math.abs(y - p.y);
			
			float tolerance = Math.max(paint.getStrokeWidth(), 30);
			
			if (dx <= tolerance && dy <= tolerance) {
				return true;
			}
		}
		
		return false;
	}
	
	public List<Point> getPoints() {
		return points;
	}
	
	public Paint getPaint() {
		return paint;
	}
}

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

	public void sculpt(float x1, float y1, float x2, float y2, float range){
		List<Point> nPoints = new ArrayList<Point>();

		float dx = x2-x1;
		float dy = y2-y1;

		for(Point p : points){
			Point np = new Point();
			float distX = x2-p.x;
			float distY = y2-p.y;
			float fac = (float)Math.sqrt((distX*distX)+(distY*distY)) / range;

			np.x = (int)(p.x + (dx * fac));
			np.y = (int)(p.y + (dy * fac));

			nPoints.add(np);
		}

		points = nPoints;
		redraw();
	}
	public void redraw(){
		this.reset();
		for(int i = 0; i < points.size(); i++){
			if(i == 0){
				moveTo(points.get(i).x, points.get(i).y);
			}else{
				quadTo(points.get(i-1).x, points.get(i-1).y, points.get(i).x, points.get(i).y);
			}
		}
	}
	
	public List<Point> getPoints() {
		return points;
	}
	
	public Paint getPaint() {
		return paint;
	}
}

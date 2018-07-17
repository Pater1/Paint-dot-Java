package com.example.patrickconner.paint_dot_java;

public class DrawPathCommand implements Command {
	
	private PathPoints pathDrawn;
	
	public DrawPathCommand(PathPoints pathDrawn) {
		this.pathDrawn = pathDrawn;
	}
	
	@Override
	public void execute(PaintCanvasView canvas) {
	
	}
	
	@Override
	public void undo(PaintCanvasView canvas) {
	
	}
}
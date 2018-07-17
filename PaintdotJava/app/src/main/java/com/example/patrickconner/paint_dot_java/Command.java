package com.example.patrickconner.paint_dot_java;

public interface Command {
	
	void execute(PaintCanvasView canvas);
	void undo(PaintCanvasView canvas);
}

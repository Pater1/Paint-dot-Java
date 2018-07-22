package com.example.patrickconner.paint_dot_java;

import android.Manifest;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
	
	LinearLayout colorBG;
	SeekBar seekColorH, seekColorS, seekColorV;
	TextView seekLabelH, seekLabelS, seekLabelV;
	Button colorConfirm;
	float[] colorCom = new float[3];
	
	LinearLayout brushSizeBG;
	SeekBar seekBrushSize;
	Integer brushSize = 10;
	
	PaintCanvasView canvas;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		canvas = findViewById(R.id.paintCanvasView);
		colorBG = (LinearLayout) findViewById(R.id.ColorSubmenu);
		seekColorH = (SeekBar) findViewById(R.id.ColorH);
		seekColorH.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				colorCom[0] = (progress / (float) seekBar.getMax()) * 360;
				ColorUpdate();
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			
			}
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			
			}
		});
		seekColorS = (SeekBar) findViewById(R.id.ColorS);
		seekColorS.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				colorCom[1] = progress / (float) seekBar.getMax();
				ColorUpdate();
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			
			}
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			
			}
		});
		seekColorV = (SeekBar) findViewById(R.id.ColorV);
		seekColorV.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				colorCom[2] = progress / (float) seekBar.getMax();
				ColorUpdate();
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			
			}
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			
			}
		});
		colorConfirm = (Button) findViewById(R.id.ColorConfirm);
		seekLabelH = (TextView) findViewById(R.id.LabelH);
		seekLabelS = (TextView) findViewById(R.id.LabelS);
		seekLabelV = (TextView) findViewById(R.id.LabelV);
		ColorUpdate();
		colorBG.setVisibility(View.GONE);
		
		brushSizeBG = (LinearLayout) findViewById(R.id.BrushSizeSubmenu);
		seekBrushSize = (SeekBar) findViewById(R.id.BrushSize);
		seekBrushSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				brushSize = progress;
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			
			}
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			
			}
		});
		brushSizeBG.setVisibility(View.GONE);
		
		eraseButton = (Button) findViewById(R.id.EraseToggle);
		sculptButton = (Button) findViewById(R.id.SculptToggle);
		
		OnClear = new Action() {
			@Override
			public void Execute() {
				canvas.clearCanvas();
			}
		};
		OnUndo = new Action() {
			@Override
			public void Execute() {
				canvas.undo();
			}
		};
		OnRedo = new Action() {
			@Override
			public void Execute() {
				canvas.redo();
			}
		};
		OnBrushSizeSet = new Action1<Integer>() {
			public void Execute(Integer x) {
				canvas.setBrushSize(x);
			}
		};
		OnDrawModeChange = new Action1<DrawMode>() {
			@Override
			public void Execute(DrawMode var) {
				canvas.setDrawMode(var);
			}
		};
		OnColorChange = new Action1<Integer>() {
			@Override
			public void Execute(Integer var) {
				canvas.changeStrokeColor(var);
			}
		};
		OnSave = new Function<Bitmap>() {
			@Override
			public Bitmap Execute() {
				return canvas.getBitmap();
			}
		};
	}
	
	public Action OnClear;
	
	public void CanvasClear(View view) {
		if (OnClear != null) OnClear.Execute();
	}
	
	public Function<Bitmap> OnSave;
	
	private int requestCode;
	public void CanvasSave(View view) {
		if (OnSave != null) {
			String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				requestPermissions(permissions, requestCode);
			}
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		String filePath = null;
		
		if (grantResults.length > 0) {
			Bitmap toSave = OnSave.Execute();
			FileOutputStream out = null;
			try {
				File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/PaintDotJava");
				if (!dir.exists()) {
					dir.mkdir();
				}
				File file = new File(dir, "picture.png");
				file.createNewFile();
				filePath = file.getAbsolutePath();
				out = new FileOutputStream(file, false);
				toSave.compress(Bitmap.CompressFormat.PNG, 100, out);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (out != null) {
					try {
						out.close();
						ContentValues values = new ContentValues();
						
						values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
						values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
						values.put(MediaStore.MediaColumns.DATA, filePath);
						
						getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
						Toast.makeText(this, "Picture saved", Toast.LENGTH_LONG).show();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			Toast.makeText(this, "Could export the picture.", Toast.LENGTH_LONG).show();
		}
	}
	
	public Action OnUndo;
	
	public void CanvasUndo(View view) {
		if (OnUndo != null) OnUndo.Execute();
	}
	
	public Action OnRedo;
	
	public void CanvasRedo(View view) {
		if (OnRedo != null) OnRedo.Execute();
	}
	
	public Action1<Integer> OnColorChange;
	Integer color = Color.BLACK;
	
	public void ColorSet(View view) {
		colorBG.setVisibility(View.VISIBLE);
	}
	
	public void ColorUpdate() {
		color = Color.HSVToColor(colorCom);
		colorBG.setBackgroundColor(color);
		colorConfirm.setBackgroundColor(color);
		
		float[] invHsv = new float[3];
		invHsv[0] = (colorCom[0] + 180) % 360;
		invHsv[1] = 1 - colorCom[1];
		invHsv[2] = 1 - colorCom[2];
		int colorInv = Color.HSVToColor(invHsv);
		
		seekColorH.getProgressDrawable().setColorFilter(colorInv, PorterDuff.Mode.SRC_ATOP);
		seekColorS.getProgressDrawable().setColorFilter(colorInv, PorterDuff.Mode.SRC_ATOP);
		seekColorV.getProgressDrawable().setColorFilter(colorInv, PorterDuff.Mode.SRC_ATOP);
		seekLabelH.setTextColor(colorInv);
		seekLabelS.setTextColor(colorInv);
		seekLabelV.setTextColor(colorInv);
		colorConfirm.setTextColor(colorInv);
		
	}
	
	public void ColorPush(View view) {
		if (OnColorChange != null) OnColorChange.Execute(color);
		colorBG.setVisibility(View.GONE);
	}
	
	public Action1<Integer> OnBrushSizeSet;
	
	public void BrushSizeSet(View view) {
		brushSizeBG.setVisibility(View.VISIBLE);
	}
	
	public void BrushSizePush(View view) {
		if (OnBrushSizeSet != null) OnBrushSizeSet.Execute(brushSize);
		brushSizeBG.setVisibility(View.GONE);
	}
	
	private Button eraseButton, sculptButton;
	private DrawMode drawMode = DrawMode.Draw;
	public Action1<DrawMode> OnDrawModeChange;
	
	public void setDrawMode(DrawMode setTo) {
		drawMode = setTo;
		switch (drawMode) {
			case Erase:
				eraseButton.setBackgroundColor(Color.YELLOW);
				sculptButton.setBackgroundColor(Color.WHITE);
				break;
			case Sculpt:
				sculptButton.setBackgroundColor(Color.YELLOW);
				eraseButton.setBackgroundColor(Color.WHITE);
				break;
			default:
				sculptButton.setBackgroundColor(Color.WHITE);
				eraseButton.setBackgroundColor(Color.WHITE);
				break;
		}
		
		if (OnDrawModeChange != null) OnDrawModeChange.Execute(setTo);
	}
	
	public void ToggleErase(View view) {
		if (drawMode == DrawMode.Erase) {
			setDrawMode(DrawMode.Draw);
		} else {
			setDrawMode(DrawMode.Erase);
		}
	}
	
	public void ToggleSculpt(View view) {
		if (drawMode == DrawMode.Sculpt) {
			setDrawMode(DrawMode.Draw);
		} else {
			setDrawMode(DrawMode.Sculpt);
		}
	}
}
package com.example.patrickconner.paint_dot_java;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout colorSet;
    LinearLayout colorBG;
    SeekBar seekColorH, seekColorS, seekColorV;
    Button colorConfirm;
    float[] colorCom = new float[3];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        colorSet = (ConstraintLayout)findViewById(R.id.ColorSupermenu);
        colorBG = (LinearLayout)findViewById(R.id.ColorSubmenu);
        seekColorH = (SeekBar)findViewById(R.id.ColorH);
        seekColorH.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                colorCom[0] = progress/ (float)seekBar.getMax();
                ColorUpdate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekColorS = (SeekBar)findViewById(R.id.ColorS);
        seekColorS.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                colorCom[1] = progress/ (float)seekBar.getMax();
                ColorUpdate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekColorV = (SeekBar)findViewById(R.id.ColorV);
        seekColorV.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                colorCom[2] = progress/ (float)seekBar.getMax();
                ColorUpdate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        colorConfirm = (Button)findViewById(R.id.ColorConfirm);
        colorSet.setVisibility(View.GONE);
    }

    public Action OnClear;
    public void CanvasClear(View view){
        if(OnClear != null) OnClear.Execute();
    }

    public Function<Bitmap> OnSave;
    public void CanvasSave(View view){
        if(OnSave != null) {
            Bitmap toSave = OnSave.Execute();
        }
    }

    public Action OnUndo;
    public void CanvasUndo(View view){
        if(OnUndo != null) OnUndo.Execute();
    }
    public Action OnRedo;
    public void CanvasRedo(View view){
        if(OnRedo != null) OnRedo.Execute();
    }

    public Action1<Integer> OnColorChange;
    Integer color = Color.BLACK;
    public void ColorSet(View view){
        colorSet.setVisibility(View.VISIBLE);
    }
    public void ColorUpdate(){
        color = Color.HSVToColor(colorCom);
        colorBG.setBackgroundColor(color);
        colorConfirm.setBackgroundColor(color);

        int r, g, b;
        float[] invHsv = new float[3];
        Color.colorToHSV(color, invHsv);
        r = 255-Color.red(color);
        invHsv[1] = invHsv[1] > 0.5f? 0.0f: 1.0f;
        invHsv[2] = invHsv[2] > 0.5f? 0.0f: 1.0f;

        int colorInv = Color.rgb(r,g,b);
        seekColorH.setBackgroundColor(colorInv);
        seekColorS.setBackgroundColor(colorInv);
        seekColorV.setBackgroundColor(colorInv);
        colorConfirm.setTextColor(colorInv);

    }
    public void ColorPush(View view){
        if(OnColorChange != null) OnColorChange.Execute(color);
        colorSet.setVisibility(View.GONE);
    }

    public Action1<Integer> OnBrushSizeSet;
    public void BrushSizeSet(View view){
        //submenu up
    }
    public void BrushSizePush(View view){
        //submenu down
        Integer radius = 0;
        if(OnBrushSizeSet != null) OnBrushSizeSet.Execute(radius);
    }
}

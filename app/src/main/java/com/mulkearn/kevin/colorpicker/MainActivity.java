package com.mulkearn.kevin.colorpicker;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout myLayout = (RelativeLayout) findViewById(R.id.mainView);
        myLayout.setBackgroundColor(Color.WHITE);

        SeekBar redSeeker = (SeekBar) findViewById(R.id.redSeeker);
        SeekBar greenSeeker = (SeekBar) findViewById(R.id.greenSeeker);
        SeekBar blueSeeker = (SeekBar) findViewById(R.id.blueSeeker);

        final int red_value = redSeeker.getProgress();
        final int green_value = greenSeeker.getProgress();
        final int blue_value = blueSeeker.getProgress();

        final TextView redValue = (TextView) findViewById(R.id.redValue);
        redValue.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "255")});
        final TextView greenValue = (TextView) findViewById(R.id.greenValue);
        greenValue.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "255")});
        final TextView blueValue = (TextView) findViewById(R.id.blueValue);
        blueValue.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "255")});

        //redValue.setText(Integer.toString(red_value));
        //greenValue.setText(Integer.toString(green_value));
        //blueValue.setText(Integer.toString(blue_value));

        redSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                redValue.setText(Integer.toString(progressChangedValue));
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
                //redValue.setText(Integer.toString(progressChangedValue));
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
                //redValue.setText(Integer.toString(progressChangedValue));
            }
        });

        greenSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                greenValue.setText(Integer.toString(progressChangedValue));
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
                //redValue.setText(Integer.toString(progressChangedValue));
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
                //redValue.setText(Integer.toString(progressChangedValue));
            }
        });

        blueSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                blueValue.setText(Integer.toString(progressChangedValue));
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
                //redValue.setText(Integer.toString(progressChangedValue));
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
                //redValue.setText(Integer.toString(progressChangedValue));
            }
        });

    }

    public static String getBackColor(int red, int green, int blue){

        int num = Color.rgb(red,green,blue);
        String color = Integer.toString(num);

        return color;
    }



}

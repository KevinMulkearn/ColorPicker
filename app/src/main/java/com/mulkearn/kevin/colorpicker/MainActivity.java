package com.mulkearn.kevin.colorpicker;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

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
        final TextView greenValue = (TextView) findViewById(R.id.greenValue);
        final TextView blueValue = (TextView) findViewById(R.id.blueValue);

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



}

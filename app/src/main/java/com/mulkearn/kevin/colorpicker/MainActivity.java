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

    RelativeLayout myLayout;
    SeekBar redSeeker, greenSeeker, blueSeeker;
    TextView redValue, greenValue,  blueValue, hexValue, hueValue, satValue, valValue;
    private int red_value, green_value, blue_value;
    private String hex;
    private float hue, sat, val;
    private float[] hsv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myLayout = (RelativeLayout) findViewById(R.id.mainView);
        myLayout.setBackgroundColor(Color.WHITE);

        redSeeker = (SeekBar) findViewById(R.id.redSeeker);
        greenSeeker = (SeekBar) findViewById(R.id.greenSeeker);
        blueSeeker = (SeekBar) findViewById(R.id.blueSeeker);

        red_value = redSeeker.getProgress();
        green_value = greenSeeker.getProgress();
        blue_value = blueSeeker.getProgress();

        redValue = (TextView) findViewById(R.id.redValue);
        redValue.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "255")});
        greenValue = (TextView) findViewById(R.id.greenValue);
        greenValue.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "255")});
        blueValue = (TextView) findViewById(R.id.blueValue);
        blueValue.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "255")});

        hexValue = (TextView) findViewById(R.id.hexValue);

        hueValue = (TextView) findViewById(R.id.hueValue);
        satValue = (TextView) findViewById(R.id.satValue);
        valValue = (TextView) findViewById(R.id.valValue);


        redSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                redValue.setText(Integer.toString(progressChangedValue));
                getBackColor();
                getHexValue();
                getHSVValue();

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
                getBackColor();
                getHexValue();
                getHSVValue();

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
                getBackColor();
                getHexValue();
                getHSVValue();

            }
            public void onStartTrackingTouch(SeekBar seekBar) {
                //redValue.setText(Integer.toString(progressChangedValue));
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
                //redValue.setText(Integer.toString(progressChangedValue));
            }
        });

    }

    public void getBackColor(){
        red_value = redSeeker.getProgress();
        green_value = greenSeeker.getProgress();
        blue_value = blueSeeker.getProgress();
        myLayout.setBackgroundColor( 0xff000000 + red_value * 0x10000 + green_value * 0x100 + blue_value);

    }

    public void getHexValue(){
        hex = String.format("#%02X%02X%02X", red_value, green_value, blue_value);
        hexValue.setText(hex);
        hexValue.setTextColor(Color.parseColor(hex));
    }

    public void getHSVValue(){
        hsv = new float[3];
        red_value = redSeeker.getProgress();
        green_value = greenSeeker.getProgress();
        blue_value = blueSeeker.getProgress();

        Color.RGBToHSV(red_value, green_value, blue_value, hsv);

        float hue = hsv[0];
        float sat = hsv[1] * 100;
        float val = hsv[2] * 100;

        hueValue.setText(Integer.toString((int) hue) + "\u00b0");
        satValue.setText(Integer.toString((int) sat) + "%");
        valValue.setText(Integer.toString((int) val) + "%");

        hueValue.setTextColor(Color.parseColor(hex));
        satValue.setTextColor(Color.parseColor(hex));
        valValue.setTextColor(Color.parseColor(hex));

    }



}

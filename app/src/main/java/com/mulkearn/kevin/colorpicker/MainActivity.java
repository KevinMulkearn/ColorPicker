package com.mulkearn.kevin.colorpicker;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    RelativeLayout myLayout;
    SeekBar redSeeker, greenSeeker, blueSeeker;
    TextView redValue, greenValue,  blueValue, hexValue, hueValue, satValue, valValue;
    private int red_value, green_value, blue_value;
    private String hex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Main view
        myLayout = (RelativeLayout) findViewById(R.id.mainView);

        //Seek Bars
        redSeeker = (SeekBar) findViewById(R.id.redSeeker);
        greenSeeker = (SeekBar) findViewById(R.id.greenSeeker);
        blueSeeker = (SeekBar) findViewById(R.id.blueSeeker);

        //Text Views
        redValue = (TextView) findViewById(R.id.redValue);
        greenValue = (TextView) findViewById(R.id.greenValue);
        blueValue = (TextView) findViewById(R.id.blueValue);

        hexValue = (TextView) findViewById(R.id.hexValue);

        hueValue = (TextView) findViewById(R.id.hueValue);
        satValue = (TextView) findViewById(R.id.satValue);
        valValue = (TextView) findViewById(R.id.valValue);


        redSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                redValue.setText("R: " + Integer.toString(progress));
                getBackColor();
                getHexValue();
                getHSVValue();
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Empty
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Empty
            }
        });

        greenSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                greenValue.setText("G: " + Integer.toString(progress));
                getBackColor();
                getHexValue();
                getHSVValue();
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Empty
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Empty
            }
        });

        blueSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                blueValue.setText("B: " + Integer.toString(progress));
                getBackColor();
                getHexValue();
                getHSVValue();
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Empty
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Empty
            }
        });

    }

    public void getBackColor(){
        red_value = redSeeker.getProgress();
        green_value = greenSeeker.getProgress();
        blue_value = blueSeeker.getProgress();

        myLayout.setBackgroundColor( 0xff000000 + red_value * 0x10000 + green_value * 0x100 + blue_value);
        redValue.setBackgroundColor(0xff000000 + red_value * 0x10000);
        greenValue.setBackgroundColor(0xff000000 + green_value * 0x100);
        blueValue.setBackgroundColor(0xff000000 + blue_value);
    }

    public void getHexValue(){
        red_value = redSeeker.getProgress();
        green_value = greenSeeker.getProgress();
        blue_value = blueSeeker.getProgress();

        hex = String.format("%02X%02X%02X",red_value, green_value, blue_value);
        hexValue.setText("Hex: " + hex);
    }

    public void getHSVValue(){
        float[] hsv = new float[3];
        red_value = redSeeker.getProgress();
        green_value = greenSeeker.getProgress();
        blue_value = blueSeeker.getProgress();

        Color.RGBToHSV(red_value, green_value, blue_value, hsv);

        float hue = hsv[0];
        float sat = hsv[1] * 100;
        float val = hsv[2] * 100;

        hueValue.setText("H: " + Integer.toString((int) hue) + "\u00b0");
        satValue.setText("S: " + Integer.toString((int) sat) + "%");
        valValue.setText("V: " + Integer.toString((int) val) + "%");
    }



}

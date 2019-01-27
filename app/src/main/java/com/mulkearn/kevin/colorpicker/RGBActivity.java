package com.mulkearn.kevin.colorpicker;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class RGBActivity extends AppCompatActivity {

    RelativeLayout myLayout;
    SeekBar redSeeker, greenSeeker, blueSeeker;
    TextView redValue, greenValue,  blueValue, hexValue, hueValue, satValue, valValue;
    DatabaseHelper mDatabaseHelper;

    float hue = 0, sat = 0, val = 0;
    int red_value = 0 , green_value = 0, blue_value = 0;
    String hex = "#000000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rgb);

        myLayout = findViewById(R.id.mainView);
        redSeeker = findViewById(R.id.redSeeker);
        greenSeeker = findViewById(R.id.greenSeeker);
        blueSeeker = findViewById(R.id.blueSeeker);
        redValue = findViewById(R.id.redValue);
        greenValue = findViewById(R.id.greenValue);
        blueValue = findViewById(R.id.blueValue);
        hexValue = findViewById(R.id.hexValue);
        hueValue = findViewById(R.id.hueValue);
        satValue = findViewById(R.id.satValue);
        valValue = findViewById(R.id.valValue);

        mDatabaseHelper = new DatabaseHelper(this);

        // Set SeekBar background
        int[] redGradValues = {Color.rgb(0,0,0), Color.rgb(255,0,0)};  // Start color to end color
        GradientDrawable redGrad = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, redGradValues);
        redGrad.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        redSeeker.setBackground(redGrad);
        int[] greenGradValues = {Color.rgb(0,0,0), Color.rgb(0,255,0)}; // Start color to end color
        GradientDrawable greenGrad = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, greenGradValues);
        greenGrad.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        greenSeeker.setBackground(greenGrad);
        int[] blueGradValues = {Color.rgb(0,0,0), Color.rgb(0,0,255)}; // Start color to end color
        GradientDrawable blueGrad = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, blueGradValues);
        blueGrad.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        blueSeeker.setBackground(blueGrad);

        // Color data from hsv activity
        red_value = getIntent().getIntExtra("red", 0);
        green_value = getIntent().getIntExtra("green", 0);
        blue_value = getIntent().getIntExtra("blue", 0);
        redValue.setText("R: " + red_value);
        greenValue.setText("G: " + green_value);
        blueValue.setText("B: " + blue_value);
        redSeeker.setProgress(red_value);
        greenSeeker.setProgress(green_value);
        blueSeeker.setProgress(blue_value);
        getBackColor();
        getHexValue();
        getHSVValue();

        redSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                red_value = redSeeker.getProgress();
                green_value = greenSeeker.getProgress();
                blue_value = blueSeeker.getProgress();
                redValue.setText("R: " + progress);
                getBackColor();
                getHexValue();
                getHSVValue();
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        greenSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                red_value = redSeeker.getProgress();
                green_value = greenSeeker.getProgress();
                blue_value = blueSeeker.getProgress();
                greenValue.setText("G: " + progress);
                getBackColor();
                getHexValue();
                getHSVValue();
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        blueSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                red_value = redSeeker.getProgress();
                green_value = greenSeeker.getProgress();
                blue_value = blueSeeker.getProgress();
                blueValue.setText("B: " + progress);
                getBackColor();
                getHexValue();
                getHSVValue();
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            case R.id.saveHex:
                AddData(hex);
                return true;
            case R.id.random:
                Random r_red = new Random();
                Random r_green = new Random();
                Random r_blue = new Random();
                int randRed = r_red.nextInt(256);
                int randGreen = r_green.nextInt(256);
                int randBlue = r_blue.nextInt(256);
                redSeeker.setProgress(randRed);
                greenSeeker.setProgress(randGreen);
                blueSeeker.setProgress(randBlue);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void AddData(String newEntry) {
        boolean insertData = mDatabaseHelper.addColor(newEntry);
        if (insertData) {
            Toast.makeText(RGBActivity.this, newEntry + " " + getString(R.string.saved), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(RGBActivity.this, "Error Saving Data!", Toast.LENGTH_SHORT).show();
        }
    }

    public void getBackColor(){
        myLayout.setBackgroundColor( 0xff000000 + red_value * 0x10000 + green_value * 0x100 + blue_value);
        redValue.setBackgroundColor(0xff000000 + red_value * 0x10000);
        greenValue.setBackgroundColor(0xff000000 + green_value * 0x100);
        blueValue.setBackgroundColor(0xff000000 + blue_value);
    }

    public void getHexValue(){
        hex = String.format("#%02X%02X%02X",red_value, green_value, blue_value);
        hexValue.setText("Hex: " + hex);
    }

    public void getHSVValue(){
        float[] hsv = new float[3];
        Color.RGBToHSV(red_value, green_value, blue_value, hsv);
        hue = hsv[0];
        sat = hsv[1] * 100;
        val = hsv[2] * 100;
        hueValue.setText("H: " + Math.round(hue) + "\u00b0");
        satValue.setText("S: " + Math.round(sat)  + "%");
        valValue.setText("V: " + Math.round(val)  + "%");
    }

    public void hsvNavClick(View view) {
        Intent i_hsv = new Intent(this, hsvActivity.class);
        i_hsv.putExtra("hue", hue);
        i_hsv.putExtra("sat", sat);
        i_hsv.putExtra("val", val);
        startActivity(i_hsv);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void hexNavClick(View view) {
        Intent i_hex = new Intent(this, HexActivity.class);
        i_hex.putExtra("red", red_value);
        i_hex.putExtra("green", green_value);
        i_hex.putExtra("blue", blue_value);
        startActivity(i_hex);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void redMinus(View view) {
        redSeeker.setProgress(red_value-1);
    }

    public void redPlus(View view) {
        redSeeker.setProgress(red_value+1);
    }

    public void greenMinus(View view) {
        greenSeeker.setProgress(green_value-1);
    }

    public void greenPlus(View view) {
        greenSeeker.setProgress(green_value+1);
    }

    public void blueMinus(View view) {
        blueSeeker.setProgress(blue_value-1);
    }

    public void bluePlus(View view) {
        blueSeeker.setProgress(blue_value+1);
    }
}

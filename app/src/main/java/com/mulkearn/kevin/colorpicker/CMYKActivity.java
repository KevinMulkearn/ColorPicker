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

public class CMYKActivity extends AppCompatActivity {

    RelativeLayout myLayout;
    SeekBar cyanSeeker, magentaSeeker, yellowSeeker, blackSeeker;
    TextView redValue, greenValue,  blueValue, hexValue, hueValue, satValue, valValue, cyanValue, magentaValue, yellowValue, blackValue;
    DatabaseHelper mDatabaseHelper;

    float hue = 0, sat = 0, val = 0;
    int red_value = 0 , green_value = 0, blue_value = 0;
    int cyan_value = 0 , magenta_value = 0, yellow_value = 0, black_value = 0;
    String hex = "#000000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cmyk);

        myLayout = findViewById(R.id.mainView);
        cyanSeeker = findViewById(R.id.cyanSeeker);
        magentaSeeker = findViewById(R.id.magentaSeeker);
        yellowSeeker = findViewById(R.id.yellowSeeker);
        blackSeeker = findViewById(R.id.blackSeeker);
        redValue = findViewById(R.id.redValue);
        greenValue = findViewById(R.id.greenValue);
        blueValue = findViewById(R.id.blueValue);
        hexValue = findViewById(R.id.hexValue);
        hueValue = findViewById(R.id.hueValue);
        satValue = findViewById(R.id.satValue);
        valValue = findViewById(R.id.valValue);
        cyanValue = findViewById(R.id.cyanValue);
        magentaValue = findViewById(R.id.magentaValue);
        yellowValue = findViewById(R.id.yellowValue);
        blackValue = findViewById(R.id.blackValue);

        mDatabaseHelper = new DatabaseHelper(this);

        // Set SeekBar background
        int[] cyanGradValues = {Color.rgb(255,255,255), Color.rgb(0,255,255)};  // Start color to end color
        GradientDrawable cyanGrad = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, cyanGradValues);
        cyanGrad.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        cyanSeeker.setBackground(cyanGrad);
        int[] magentaGradValues = {Color.rgb(255,255,255), Color.rgb(255,0,255)}; // Start color to end color
        GradientDrawable magentaGrad = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, magentaGradValues);
        magentaGrad.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        magentaSeeker.setBackground(magentaGrad);
        int[] yellowGradValues = {Color.rgb(255,255,255), Color.rgb(255,255,0)}; // Start color to end color
        GradientDrawable yellowGrad = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, yellowGradValues);
        yellowGrad.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        yellowSeeker.setBackground(yellowGrad);
        int[] blackGradValues = {Color.rgb(255,255,255), Color.rgb(0,0,0)}; // Start color to end color
        GradientDrawable blackGrad = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, blackGradValues);
        blackGrad.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        blackSeeker.setBackground(blackGrad);

        // Color data from hsv activity
        red_value = getIntent().getIntExtra("red", 0);
        green_value = getIntent().getIntExtra("green", 0);
        blue_value = getIntent().getIntExtra("blue", 0);
        getCMYKValue();
        cyanSeeker.setProgress(cyan_value);
        magentaSeeker.setProgress(magenta_value);
        yellowSeeker.setProgress(yellow_value);
        blackSeeker.setProgress(black_value);
        getRGBValue();
        getBackColor();
        getHexValue();
        getHSVValue();

        cyanSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cyan_value = cyanSeeker.getProgress();
                magenta_value = magentaSeeker.getProgress();
                yellow_value = yellowSeeker.getProgress();
                black_value = blackSeeker.getProgress();
                cyanValue.setText("C: " + progress);
                getRGBValue();
                getBackColor();
                getHexValue();
                getHSVValue();
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        magentaSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cyan_value = cyanSeeker.getProgress();
                magenta_value = magentaSeeker.getProgress();
                yellow_value = yellowSeeker.getProgress();
                black_value = blackSeeker.getProgress();
                magentaValue.setText("M: " + progress);
                getRGBValue();
                getBackColor();
                getHexValue();
                getHSVValue();
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        yellowSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cyan_value = cyanSeeker.getProgress();
                magenta_value = magentaSeeker.getProgress();
                yellow_value = yellowSeeker.getProgress();
                black_value = blackSeeker.getProgress();
                yellowValue.setText("Y: " + progress);
                getRGBValue();
                getBackColor();
                getHexValue();
                getHSVValue();
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        blackSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cyan_value = cyanSeeker.getProgress();
                magenta_value = magentaSeeker.getProgress();
                yellow_value = yellowSeeker.getProgress();
                black_value = blackSeeker.getProgress();
                blackValue.setText("Y: " + progress);
                getRGBValue();
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
                Random r_cyan = new Random();
                Random r_magenta = new Random();
                Random r_yellow = new Random();
                Random r_black = new Random();
                int randCyan = r_cyan.nextInt(256);
                int randMagenta = r_magenta.nextInt(256);
                int randYellow = r_yellow.nextInt(256);
                int randBlack = r_black.nextInt(256);
                cyanSeeker.setProgress(randCyan);
                magentaSeeker.setProgress(randMagenta);
                yellowSeeker.setProgress(randYellow);
                blackSeeker.setProgress(randBlack);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void AddData(String newEntry) {
        boolean insertData = mDatabaseHelper.addColor(newEntry);
        if (insertData) {
            Toast.makeText(CMYKActivity.this, newEntry + " " + getString(R.string.saved), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(CMYKActivity.this, "Error Saving Data!", Toast.LENGTH_SHORT).show();
        }
    }

    public void getCMYKValue(){
        float r = (float)red_value/255f;
        float g = (float)green_value/255f;
        float b = (float)blue_value/255f;

        black_value = Math.round(1f - Math.max(Math.max(r,g),b));
        cyan_value = Math.round((1f- r - black_value) / (1f - black_value));
        magenta_value = Math.round((1f- g - black_value) / (1f - black_value));
        yellow_value = Math.round((1f - b - black_value) / (1 - black_value));
    }

    public void getBackColor(){
        myLayout.setBackgroundColor( 0xff000000 + red_value * 0x10000 + green_value * 0x100 + blue_value);
    }

    public void getRGBValue(){
        float f_cyan_value = (float)cyan_value/100f;
        float f_magenta_value = (float)magenta_value/100f;
        float f_yellow_value = (float)yellow_value/100f;
        float f_black_value = (float)black_value/100f;

        red_value = Math.round(255f * (1f-f_cyan_value) * (1f-f_black_value));
        green_value = Math.round(255f * (1f-f_magenta_value) * (1f-f_black_value));
        blue_value = Math.round(255f * (1f-f_yellow_value) * (1f-f_black_value));

        redValue.setText("R: " + red_value);
        greenValue.setText("G: " + green_value);
        blueValue.setText("B: " + blue_value);
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
        Intent i_hsv = new Intent(this, HSVActivity.class);
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

    public void cyanMinus(View view) {
        cyanSeeker.setProgress(cyan_value-1);
    }

    public void cyanPlus(View view) {
        cyanSeeker.setProgress(cyan_value+1);
    }

    public void magentaMinus(View view) {
        magentaSeeker.setProgress(magenta_value-1);
    }

    public void magentaPlus(View view) {
        magentaSeeker.setProgress(magenta_value+1);
    }

    public void yellowMinus(View view) {
        yellowSeeker.setProgress(yellow_value-1);
    }

    public void yellowPlus(View view) {
        yellowSeeker.setProgress(yellow_value+1);
    }

    public void blackMinus(View view) {
        blackSeeker.setProgress(black_value-1);
    }

    public void blackPlus(View view) {
        blackSeeker.setProgress(black_value+1);
    }
}